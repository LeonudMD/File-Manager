package com.test_task.FileManager.mapper;

import com.test_task.FileManager.decorator.FileResponseDecorator;
import com.test_task.FileManager.dto.FileResponse;
import io.minio.StatObjectResponse;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер для преобразования StatObjectResponse в FileResponse.
 */
@Mapper
@DecoratedWith(FileResponseDecorator.class)
public interface FileResponseMapper {

    /**
     * Преобразует StatObjectResponse в FileResponse для добавления файла.
     *
     * @param statObject объект StatObjectResponse.
     * @return объект FileResponse.
     */
    @Mapping(target = "stream", ignore = true)
    @Mapping(target = "filename", source = "object")
    @Mapping(target = "fileSize", source = "size")
    @Mapping(target = "createdTime", expression = "java(new java.util.Date(statObject.lastModified().toEpochMilli()))")
    @Mapping(target = "contentType", source = "contentType")
    FileResponse fileAddResponse(StatObjectResponse statObject);

    /**
     * Преобразует StatObjectResponse в FileResponse для получения файла.
     *
     * @param statObject объект StatObjectResponse.
     * @return объект FileResponse.
     */
    @Mapping(target = "stream", ignore = true)
    @Mapping(target = "filename", source = "object")
    @Mapping(target = "fileSize", source = "size")
    @Mapping(target = "createdTime", expression = "java(new java.util.Date(statObject.lastModified().toEpochMilli()))")
    @Mapping(target = "contentType", source = "contentType")
    FileResponse fileGetResponse(StatObjectResponse statObject);
}
