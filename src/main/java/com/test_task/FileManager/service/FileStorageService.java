package com.test_task.FileManager.service;

import com.test_task.FileManager.entity.FileMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

/**
 * Интерфейс для сервиса хранения файлов.
 */
public interface FileStorageService {

    /**
     * Добавляет файл в хранилище и сохраняет его метаданные.
     *
     * @param multipartFile файл для загрузки.
     * @param oneTimeLink   флаг одноразовой ссылки.
     * @param duration      продолжительность жизни ссылки.
     * @param timeUnit      единица времени для продолжительности.
     * @return метаданные загруженного файла.
     */
    FileMetadata addFile(MultipartFile multipartFile, Boolean oneTimeLink, long duration, TimeUnit timeUnit);

    /**
     * Удаляет файл из хранилища и его метаданные.
     *
     * @param fileName имя файла для удаления.
     */
    void deleteFile(String fileName);

    /**
     * Получает файл из хранилища.
     *
     * @param fileName имя файла для получения.
     * @return метаданные файла.
     */
    FileMetadata getFile(String fileName);

    /**
     * Получает метаданные файла.
     *
     * @param fileName имя файла для получения метаданных.
     * @return метаданные файла.
     */
    FileMetadata getFileDetails(String fileName);
}
