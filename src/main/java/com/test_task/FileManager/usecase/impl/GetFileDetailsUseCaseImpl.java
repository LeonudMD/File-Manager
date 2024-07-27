package com.test_task.FileManager.usecase.impl;

import com.test_task.FileManager.entity.FileMetadata;
import com.test_task.FileManager.service.FileStorageService;
import com.test_task.FileManager.usecase.GetFileDetailsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Реализация use case для получения деталей файла.
 */
@Service
@RequiredArgsConstructor
public class GetFileDetailsUseCaseImpl implements GetFileDetailsUseCase {

    private final FileStorageService fileStorageService;

    /**
     * Выполняет операцию получения деталей файла.
     *
     * @param fileName имя файла для получения деталей.
     * @return метаданные файла.
     */
    @Override
    public FileMetadata execute(String fileName) {
        return fileStorageService.getFileDetails(fileName);
    }
}
