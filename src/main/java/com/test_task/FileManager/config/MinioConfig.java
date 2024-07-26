package com.test_task.FileManager.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.errors.MinioException;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Конфигурация для MinioClient.
 */
@Log4j
@Configuration
public class MinioConfig {

    @Value("${minio.url}")
    private String minioUrl;

    @Value("${minio.access-key}")
    private String minioAccessKey;

    @Value("${minio.secret-key}")
    private String minioSecretKey;

    @Value("${minio.bucket.name}")
    private String bucketName;

    /**
     * Создание и настройка MinioClient.
     *
     * @return MinioClient
     */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(minioAccessKey, minioSecretKey)
                .build();
    }

    /**
     * Проверка существования указанного бакета и создание нового при необходимости.
     *
     * @param minioClient MinioClient
     * @return true, если бакет существует или был успешно создан; false в случае ошибки
     */
    @Bean
    public boolean ensureBucketExists(MinioClient minioClient) {
        try {
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.debug("Бакет создан успешно");
            }
            checkAndUploadTestPhoto(minioClient);
            return true;
        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            log.error("Произошла ошибка: " + e);
            return false;
        }
    }

    /**
     * Проверка существования тестового фото и загрузка при необходимости.
     *
     * @param minioClient MinioClient
     */
    private void checkAndUploadTestPhoto(MinioClient minioClient) {
        String testPhotoName = "PostMan_Test.jpg";
        try {
            minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(testPhotoName).build());
        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            try {
                ClassPathResource imgFile = new ClassPathResource("static/images/" + testPhotoName);
                InputStream testPhotoStream = imgFile.getInputStream();
                long size = imgFile.contentLength();
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(testPhotoName)
                                .stream(testPhotoStream, size, -1)
                                .contentType("image/jpeg")
                                .build()
                );
                log.debug("Тестовый файл умпешно загружен");
            } catch (IOException | MinioException | InvalidKeyException | NoSuchAlgorithmException ex) {
                throw new RuntimeException("Ошибка загрузки тестового файла", ex);
            }
        }
    }
}

