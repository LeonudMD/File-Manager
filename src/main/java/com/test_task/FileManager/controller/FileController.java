package com.test_task.FileManager.controller;

import com.test_task.FileManager.entity.FileMetadata;
import com.test_task.FileManager.usecase.AddFileUseCase;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
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
    private final GetFileUseCase getFileUseCase;

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
}

