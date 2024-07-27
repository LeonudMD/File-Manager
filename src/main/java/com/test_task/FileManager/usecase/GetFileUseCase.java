package com.test_task.FileManager.usecase;

import com.test_task.FileManager.entity.FileMetadata;

/**
 * Use case для получения файла.
 */
public interface GetFileUseCase {

    /**
     * Выполняет операцию получения файла.
     *
     * @param fileName имя файла для получения.
     * @return метаданные файла.
     */
    FileMetadata execute(String fileName);
}
