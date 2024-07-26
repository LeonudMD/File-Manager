package com.test_task.FileManager.usecase.impl;

import com.test_task.FileManager.entity.FileMetadata;
import com.test_task.FileManager.service.FileStorageService;
import com.test_task.FileManager.usecase.AddFileUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AddFileUseCaseImpl implements AddFileUseCase {

    private final FileStorageService fileStorageService;

    @Override
    public FileMetadata execute(MultipartFile multipartFile, Boolean oneTimeLink, long duration, TimeUnit timeUnit) {
        return fileStorageService.addFile(multipartFile, oneTimeLink, duration, timeUnit);
    }
}

