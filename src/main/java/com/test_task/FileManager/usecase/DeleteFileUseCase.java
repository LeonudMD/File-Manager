package com.test_task.FileManager.usecase;

/**
 * Use case для удаления файла.
 */
public interface DeleteFileUseCase {

    /**
     * Выполняет операцию удаления файла.
     *
     * @param fileName имя файла для удаления.
     */
    void execute(String fileName);
}
