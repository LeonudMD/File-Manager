package com.test_task.FileManager.usecase;

import com.test_task.FileManager.entity.FileMetadata;

/**
 * Use case для получения файла.
 */
public interface GetFileUseCase {
    FileMetadata execute(String fileName);
}
