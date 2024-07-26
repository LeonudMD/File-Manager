package com.test_task.FileManager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.experimental.FieldDefaults;

import java.io.InputStream;
import java.util.Date;

/**
 * Сущность для хранения метаданных файла.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class FileMetadata {

    /**
     * Уникальный идентификатор для метаданных файла.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    Long id;

    /**
     * Имя файла.
     */
    @Column(name = "filename", nullable = false)
    String filename;

    /**
     * Тип содержимого файла.
     */
    @Column(name = "content_type", nullable = false)
    String contentType;

    /**
     * Размер файла в байтах.
     */
    @Column(name = "file_size", nullable = false)
    Long fileSize;

    /**
     * Время создания файла.
     */
    @Column(name = "created_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    Date createdTime;

    /**
     * Ссылка для загрузки файла.
     */
    @Column(name = "download_link", nullable = false)
    String downloadLink;

    /**
     * Зашифрованные данные файла.
     */
    @Column(name = "encrypted_file", nullable = false)
    String encryptedFile;

    /**
     * Флаг, указывающий, является ли ссылка одноразовой.
     */
    @Column(name = "one_time_link", nullable = false)
    Boolean oneTimeLink;

    /**
     * Время истечения срока действия ссылки для загрузки.
     */
    @Column(name = "expiration_time")
    @Temporal(TemporalType.TIMESTAMP)
    Date expirationTime;

    /**
     * Поток для данных файла.
     * не сохраняется в базе данных.
     */
    @Transient
    InputStream stream;
}
