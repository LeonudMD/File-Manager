package com.test_task.FileManager.service;

import com.test_task.FileManager.entity.FileMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

/**
 * Интерфейс для сервиса хранения файлов.
 */
public interface FileStorageService {
    FileMetadata addFile(MultipartFile multipartFile, Boolean oneTimeLink, long duration, TimeUnit timeUnit);

    FileMetadata getFile(String fileName);

    void deleteFile(String fileName);
    
}
