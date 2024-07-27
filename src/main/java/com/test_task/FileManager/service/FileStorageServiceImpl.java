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

/**
 * Реализация сервиса для хранения файлов.
 */
@Service
@Log4j
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private static final Logger LOGGER = Logger.getLogger(FileStorageServiceImpl.class.getName());

    private final MinioClient minioClient;
    private final FileMetadataRepository fileMetadataRepository;

    @Value("${minio.bucket.name}")
    private String bucketName;

    /**
     * Добавляет файл в хранилище и сохраняет его метаданные.
     *
     * @param file         файл для загрузки.
     * @param oneTimeLink  флаг одноразовой ссылки.
     * @param duration     продолжительность жизни ссылки.
     * @param timeUnit     единица времени для продолжительности.
     * @return метаданные загруженного файла.
     */
    @Override
    public FileMetadata addFile(MultipartFile file, Boolean oneTimeLink, long duration, TimeUnit timeUnit) {
        Path path = Path.of(file.getOriginalFilename());
        try (InputStream inputStream = file.getInputStream()) {

            Date expirationTime = calculateExpirationTime(duration, timeUnit);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path.toString())
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            String encryptedLink = EncryptionUtil.encrypt(file.getOriginalFilename());
            String downloadUrl = "http://localhost:8080/v1/files/download/" + encryptedLink;

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
        }
    }

    /**
     * Вычисляет время истечения срока действия ссылки.
     *
     * @param duration продолжительность жизни ссылки.
     * @param timeUnit единица времени для продолжительности.
     * @return дата истечения срока действия ссылки.
     */
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
            log.debug("Ошибка при расчете срока действия: " + ex.getMessage());
            LOGGER.severe("Error in calculating expiration time: " + ex.getMessage());
            throw new IllegalArgumentException("Failed to calculate expiration time: " + ex.getMessage(), ex);
        }
    }

    /**
     * Удаляет файл из хранилища и его метаданные.
     *
     * @param filename имя файла для удаления.
     */
    @Override
    public void deleteFile(String filename) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filename)
                            .build()
            );

            Optional<FileMetadata> metadata = fileMetadataRepository.findByFilename(filename);
            metadata.ifPresent(fileMetadataRepository::delete);
        } catch (Exception ex) {
            log.debug("Ошибка удаления файла: " + ex.getMessage());
            LOGGER.severe("Failed to delete file: " + ex.getMessage());
            throw new IllegalStateException("Failed to delete file: " + ex.getMessage(), ex);
        }
    }

    /**
     * Получает файл из хранилища.
     *
     * @param filename имя файла для получения.
     * @return метаданные файла.
     */
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

    /**
     * Получает метаданные файла.
     *
     * @param filename имя файла для получения метаданных.
     * @return метаданные файла.
     */
    @Override
    public FileMetadata getFileDetails(String filename) {
        return fileMetadataRepository.findByFilename(filename)
                .orElseThrow(() -> new IllegalStateException("File metadata not found"));
    }

    /**
     * Удаляет просроченные файлы каждые 60 секунд.
     */
    @Scheduled(fixedRate = 60000)
    public void deleteExpiredFiles() {
        Date now = new Date();
        List<FileMetadata> expiredFiles = fileMetadataRepository.findAllByExpirationTimeBefore(now);
        for (FileMetadata file : expiredFiles) {
            deleteFile(file.getFilename());
        }
    }
}
