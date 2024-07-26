package com.test_task.FileManager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import lombok.experimental.FieldDefaults;
import org.springframework.core.io.InputStreamResource;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO для ответа о файле.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Schema(description = "DTO для ответа о файле")
public class FileResponse extends RepresentationModel<FileResponse> implements Serializable {

    /**
     * Имя файла.
     */
    @Schema(description = "Имя файла", example = "example.txt")
    String filename;

    /**
     * Тип содержимого файла.
     */
    @Schema(description = "Тип содержимого файла", example = "text/plain")
    String contentType;

    /**
     * Размер файла в байтах.
     */
    @Schema(description = "Размер файла в байтах", example = "1024")
    Long fileSize;

    /**
     * Дата создания файла.
     */
    @Schema(description = "Дата создания файла", example = "2024-07-25T10:00:00Z")
    Date createdTime;

    /**
     * Ссылка для скачивания файла.
     */
    @Schema(description = "Ссылка для скачивания файла", example = "http://example.com/download/example.txt")
    String downloadLink;

    /**
     * Зашифрованное имя файла.
     */
    @Schema(description = "Зашифрованное имя файла", example = "encryptedExample123")
    String encryptedFile;

    /**
     * Флаг одноразовой ссылки.
     */
    @Schema(description = "Флаг одноразовой ссылки", example = "true")
    Boolean oneTimeLink;

    /**
     * Время истечения срока действия одноразовой ссылки.
     */
    @Schema(description = "Время истечения срока действия одноразовой ссылки", example = "2024-07-26T10:00:00Z")
    Date expirationTime;

    /**
     * Поток для данных файла.
     * Это поле не включается в JSON-ответ.
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Schema(hidden = true)
    transient InputStreamResource stream;
}

