package com.test_task.FileManager.usecase.impl;

import com.test_task.FileManager.entity.FileMetadata;
import com.test_task.FileManager.service.FileStorageService;
import com.test_task.FileManager.usecase.GetFileUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Реализация use case для получения файла.
 */
@Service
@RequiredArgsConstructor
public class GetFileUseCaseImpl implements GetFileUseCase {

    private final FileStorageService fileStorageService;

    @Override
    public FileMetadata execute(String fileName) {
        return fileStorageService.getFile(fileName);
    }
}
