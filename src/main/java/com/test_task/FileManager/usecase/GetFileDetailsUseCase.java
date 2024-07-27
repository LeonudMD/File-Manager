package com.test_task.FileManager.usecase;

import com.test_task.FileManager.entity.FileMetadata;

/**
 * Use case для получения деталей файла.
 */
public interface GetFileDetailsUseCase {

    /**
     * Выполняет операцию получения деталей файла.
     *
     * @param fileName имя файла для получения деталей.
     * @return метаданные файла.
     */
    FileMetadata execute(String fileName);
}
