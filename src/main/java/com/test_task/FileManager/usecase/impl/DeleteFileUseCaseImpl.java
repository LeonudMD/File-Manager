package com.test_task.FileManager.usecase.impl;

import com.test_task.FileManager.service.FileStorageService;
import com.test_task.FileManager.usecase.DeleteFileUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Реализация use case для удаления файла.
 */
@Service
@RequiredArgsConstructor
public class DeleteFileUseCaseImpl implements DeleteFileUseCase {

    private final FileStorageService fileStorageService;

    /**
     * Выполняет операцию удаления файла.
     *
     * @param fileName имя файла для удаления.
     */
    @Override
    public void execute(String fileName) {
        fileStorageService.deleteFile(fileName);
    }
}
