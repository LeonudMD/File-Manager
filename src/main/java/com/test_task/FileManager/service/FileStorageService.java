package com.test_task.FileManager.service;

import com.test_task.FileManager.entity.FileMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

/**
 * Интерфейс для сервиса хранения файлов.
 */
public interface FileStorageService {
    FileMetadata addFile(MultipartFile multipartFile, Boolean oneTimeLink, long duration, TimeUnit timeUnit);
    void deleteFile(String fileName);
    FileMetadata getFile(String fileName);
    FileMetadata getFileDetails(String fileName);
}
