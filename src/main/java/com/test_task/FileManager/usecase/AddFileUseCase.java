package com.test_task.FileManager.usecase;

import com.test_task.FileManager.entity.FileMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

public interface AddFileUseCase {
    FileMetadata execute(MultipartFile multipartFile, Boolean oneTimeLink, long duration, TimeUnit timeUnit);
}
