package com.test_task.FileManager.decorator;

import com.test_task.FileManager.helper.MediaTypeInfo;
import com.test_task.FileManager.mapper.FileResponseMapper;
import com.test_task.FileManager.dto.FileResponse;
import com.test_task.FileManager.util.FileResponseLink;
import io.minio.StatObjectResponse;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Декоратор для маппера FileResponseMapper.
 * Добавляет дополнительные ссылки в зависимости от текущего медиа типа.
 */
public abstract class FileResponseDecorator implements FileResponseMapper {

    @Setter(onMethod = @__({@Autowired}))
    private FileResponseLink linkUtils;

    /**
     * Преобразует StatObjectResponse в FileResponse для добавления файла с дополнительными ссылками.
     *
     * @param objectStat объект StatObjectResponse.
     * @return объект FileResponse с дополнительными ссылками, если медиа тип "hal".
     */
    @Override
    public FileResponse fileAddResponse(StatObjectResponse objectStat) {
        FileResponse response = buildFileResponse(objectStat);
        String mediaType = MediaTypeInfo.getCurrentMediaType();
        return mediaType != null && mediaType.equals("hal")
                ? linkUtils.addOperationWithLink(response)
                : response;
    }

    /**
     * Преобразует StatObjectResponse в FileResponse для получения файла с дополнительными ссылками.
     *
     * @param objectStat объект StatObjectResponse.
     * @return объект FileResponse с дополнительными ссылками, если медиа тип "hal".
     */
    @Override
    public FileResponse fileGetResponse(StatObjectResponse objectStat) {
        FileResponse response = buildFileResponse(objectStat);
        String mediaType = MediaTypeInfo.getCurrentMediaType();
        return mediaType != null && mediaType.equals("hal")
                ? linkUtils.getOperationWithLink(response)
                : response;
    }

    /**
     * Создает FileResponse из StatObjectResponse.
     *
     * @param objectStat объект StatObjectResponse.
     * @return объект FileResponse.
     */
    private FileResponse buildFileResponse(StatObjectResponse objectStat) {
        return FileResponse.builder()
                .filename(objectStat.object())
                .fileSize(objectStat.size())
                .contentType(objectStat.contentType())
                .createdTime(Date.from(objectStat.lastModified().toInstant()))
                .build();
    }
}

