package com.test_task.FileManager.repository;

import com.test_task.FileManager.entity.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для управления сущностями FileMetadata.
 */
@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {

    /**
     * Находит метаданные файла по имени файла.
     *
     * @param filename имя файла.
     * @return объект Optional, содержащий метаданные файла, если найден.
     */
    Optional<FileMetadata> findByFilename(String filename);

    /**
     * Находит все метаданные файлов, у которых время истечения срока действия раньше указанной даты.
     *
     * @param date дата для проверки срока действия.
     * @return список метаданных файлов, срок действия которых истек до указанной даты.
     */
    List<FileMetadata> findAllByExpirationTimeBefore(Date date);
}

