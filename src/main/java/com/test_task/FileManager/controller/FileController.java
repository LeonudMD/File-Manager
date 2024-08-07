package com.test_task.FileManager.controller;

import com.test_task.FileManager.entity.FileMetadata;
import com.test_task.FileManager.usecase.AddFileUseCase;
import com.test_task.FileManager.usecase.DeleteFileUseCase;
import com.test_task.FileManager.usecase.GetFileDetailsUseCase;
import com.test_task.FileManager.usecase.GetFileUseCase;
import com.test_task.FileManager.util.EncryptionUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

/**
 * Контроллер для работы с файлами.
 */
@RestController
@Log4j
@RequestMapping("/v1/files")
@RequiredArgsConstructor
@Tag(name = "Управление файлами", description = "API для управления файлами")
public class FileController {

    private final AddFileUseCase addFileUseCase;
    private final DeleteFileUseCase deleteFileUseCase;
    private final GetFileUseCase getFileUseCase;
    private final GetFileDetailsUseCase getFileDetailsUseCase;

    /**
     * Загрузка файла на сервер.
     *
     * @param file         файл для загрузки.
     * @param oneTimeLink  флаг одноразовой ссылки.
     * @param duration     продолжительность жизни ссылки.
     * @param timeUnit     единица времени для продолжительности.
     * @return метаданные загруженного файла.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Загрузить файл", description = "Загружает файл на сервер")
    public ResponseEntity<?> fileUpload(
            @Parameter(description = "Файл для загрузки", required = true) @RequestPart("file") MultipartFile file,
            @Parameter(description = "Флаг одноразовой ссылки", required = true) @RequestParam("oneTimeLink") Boolean oneTimeLink,
            @Parameter(description = "Продолжительность", required = true) @RequestParam("duration") long duration,
            @Parameter(description = "Единица времени", required = true) @RequestParam("timeUnit") TimeUnit timeUnit) {
        try {
            FileMetadata response = addFileUseCase.execute(file, oneTimeLink, duration, timeUnit);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception ex) {
            log.debug("Ошибка при загрузке файла: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка при загрузке файла: " + ex.getMessage());
        }
    }

    /**
     * Просмотр файла с сервера.
     *
     * @param encryptedFile зашифрованное имя файла для просмотра.
     * @return поток для просмотра файла.
     */
    @GetMapping("/view/{encryptedFile}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Просмотреть файл", description = "Просматривает файл с сервера")
    public ResponseEntity<InputStreamResource> viewFile(
            @Parameter(description = "Зашифрованное имя файла для просмотра", required = true) @PathVariable String encryptedFile) {
        String decryptedFile = EncryptionUtil.decrypt(encryptedFile);
        FileMetadata source = getFileUseCase.execute(decryptedFile);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(source.getContentType()))
                .contentLength(source.getFileSize())
                .header("Content-disposition", "inline; filename=" + source.getFilename())
                .body(new InputStreamResource(source.getStream()));
    }

    /**
     * Скачивание файла с сервера.
     *
     * @param encryptedFile зашифрованное имя файла для скачивания.
     * @return поток для скачивания файла.
     */
    @GetMapping("/download/{encryptedFile}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Скачать файл", description = "Скачивает файл с сервера")
    public ResponseEntity<InputStreamResource> downloadFile(
            @Parameter(description = "Зашифрованное имя файла для скачивания", required = true) @PathVariable String encryptedFile) {
        String decryptedFile = EncryptionUtil.decrypt(encryptedFile);
        FileMetadata source = getFileUseCase.execute(decryptedFile);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(source.getFileSize())
                .header("Content-disposition", "attachment; filename=" + source.getFilename())
                .body(new InputStreamResource(source.getStream()));
    }

    /**
     * Удаление файла с сервера.
     *
     * @param encryptedFile зашифрованное имя файла для удаления.
     * @return ответ без содержания.
     */
    @DeleteMapping("/{encryptedFile}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить файл", description = "Удаляет файл с сервера")
    public ResponseEntity<Void> removeFile(
            @Parameter(description = "Зашифрованное имя файла для удаления", required = true) @PathVariable String encryptedFile) {
        String decryptedFile = EncryptionUtil.decrypt(encryptedFile);
        deleteFileUseCase.execute(decryptedFile);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получение деталей файла с сервера.
     *
     * @param encryptedFile зашифрованное имя файла для получения деталей.
     * @return метаданные файла.
     */
    @GetMapping("/{encryptedFile}/detail")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получить детали файла", description = "Получает детали файла с сервера")
    public ResponseEntity<FileMetadata> getFileDetail(
            @Parameter(description = "Зашифрованное имя файла для получения деталей", required = true) @PathVariable String encryptedFile) {
        String decryptedFile = EncryptionUtil.decrypt(encryptedFile);
        FileMetadata response = getFileDetailsUseCase.execute(decryptedFile);
        return ResponseEntity.ok(response);
    }
}
