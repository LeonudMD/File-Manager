package com.test_task.FileManager.usecase;

import com.test_task.FileManager.entity.FileMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

/**
 * Use case для добавления файла.
 */
public interface AddFileUseCase {

    /**
     * Выполняет операцию добавления файла.
     *
     * @param multipartFile файл для загрузки.
     * @param oneTimeLink   флаг одноразовой ссылки.
     * @param duration      продолжительность жизни ссылки.
     * @param timeUnit      единица времени для продолжительности.
     * @return метаданные загруженного файла.
     */
    FileMetadata execute(MultipartFile multipartFile, Boolean oneTimeLink, long duration, TimeUnit timeUnit);
}
