package com.test_task.FileManager.service;

import com.test_task.FileManager.entity.FileMetadata;
import com.test_task.FileManager.repository.FileMetadataRepository;
import com.test_task.FileManager.util.EncryptionUtil;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.GetObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Service
@Log4j
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private static final Logger LOGGER = Logger.getLogger(FileStorageServiceImpl.class.getName());

    private final MinioClient minioClient;
    private final FileMetadataRepository fileMetadataRepository;

    @Value("${minio.bucket.name}")
    private String bucketName;

    @Override
    public FileMetadata addFile(MultipartFile file, Boolean oneTimeLink, long duration, TimeUnit timeUnit) {
        Path path = Path.of(file.getOriginalFilename());
        InputStream inputStream = null;

        try {
            inputStream = file.getInputStream();

            // Проверяем и вычисляем время истечения срока действия ссылки
            Date expirationTime = calculateExpirationTime(duration, timeUnit);

            // Загружаем файл в Minio
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path.toString())
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            // Создаем зашифрованную ссылку на скачивание
            String encryptedLink = EncryptionUtil.encrypt(file.getOriginalFilename());

            // Формируем URL для скачивания
            String downloadUrl = "http://localhost:8080/v1/files/download/" + encryptedLink;

            // Сохраняем метаданные файла в БД
            FileMetadata metadata = FileMetadata.builder()
                    .filename(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .fileSize(file.getSize())
                    .createdTime(new Date())
                    .downloadLink(downloadUrl)
                    .encryptedFile(encryptedLink)
                    .oneTimeLink(oneTimeLink)
                    .expirationTime(expirationTime)
                    .build();

            return fileMetadataRepository.save(metadata);

        } catch (Exception ex) {
            LOGGER.severe("Error occurred: " + ex.getMessage());
            log.debug("Возникла ошибка: " + ex.getMessage());
            throw new IllegalStateException("Failed to add file: " + ex.getMessage(), ex);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    log.debug("Не удалось закрыть входной поток: " + e.getMessage());
                    LOGGER.warning("Failed to close input stream: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public FileMetadata getFile(String filename) {
        try {
            InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filename)
                            .build()
            );
            Optional<FileMetadata> metadata = fileMetadataRepository.findByFilename(filename);
            if (metadata.isPresent()) {
                FileMetadata fileMetadata = metadata.get();
                fileMetadata.setStream(inputStream);
                // Если ссылка одноразовая, удаляем файл после скачивания
                if (fileMetadata.getOneTimeLink()) {
                    deleteFile(fileMetadata.getFilename());
                }
                return fileMetadata;
            } else {
                log.debug("Метаданные файла не найдены");
                throw new IllegalStateException("File metadata not found");
            }
        } catch (Exception ex) {
            log.debug("Не удалось получить файл: " + ex.getMessage());
            LOGGER.severe("Failed to get file: " + ex.getMessage());
            throw new IllegalStateException("Failed to get file: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteFile(String filename) {
        try {
            // Удаляем файл из Minio
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filename)
                            .build()
            );

            // Удаляем метаданные из БД
            Optional<FileMetadata> metadata = fileMetadataRepository.findByFilename(filename);
            metadata.ifPresent(fileMetadataRepository::delete);
        } catch (Exception ex) {
            log.debug("Ошибка удаления файла: " + ex.getMessage());
            LOGGER.severe("Failed to delete file: " + ex.getMessage());
            throw new IllegalStateException("Failed to delete file: " + ex.getMessage(), ex);
        }
    }

    private Date calculateExpirationTime(long duration, TimeUnit timeUnit) {
        try {
            long expirationMillis = timeUnit.toMillis(duration);
            long currentTimeMillis = System.currentTimeMillis();

            if (expirationMillis < 0 || currentTimeMillis + expirationMillis < currentTimeMillis) {
                log.debug("Рассчитано неверное время истечения срока действия.");
                throw new IllegalArgumentException("Invalid expiration time calculated.");
            }

            return new Date(currentTimeMillis + expirationMillis);
        } catch (Exception ex) {
            log.debug("Ошибка при расчете срока действия:: " + ex.getMessage());
            LOGGER.severe("Error in calculating expiration time: " + ex.getMessage());
            throw new IllegalArgumentException("Failed to calculate expiration time: " + ex.getMessage(), ex);
        }
    }
}

