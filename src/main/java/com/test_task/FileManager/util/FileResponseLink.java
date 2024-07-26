package com.test_task.FileManager.util;

import com.test_task.FileManager.controller.FileController;
import com.test_task.FileManager.dto.FileResponse;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Утилитный класс для добавления ссылок к ответам FileResponse.
 */
@Component
public class FileResponseLink {

    /**
     * Добавляет ссылки для операций над файлом в ответ FileResponse.
     *
     * @param response объект FileResponse.
     * @return объект FileResponse с добавленными ссылками.
     */
    public FileResponse addOperationWithLink(FileResponse response) {
        Link[] links = new Link[]{
                linkTo(methodOn(FileController.class).getFileDetail(response.getFilename()))
                        .withRel("file-detail")
                        .withType("GET")
                        .withDeprecation("File Detail"),
                linkTo(methodOn(FileController.class).viewFile(response.getFilename()))
                        .withRel("view-file")
                        .withType("GET")
                        .withDeprecation("View File"),
                linkTo(methodOn(FileController.class).downloadFile(response.getFilename()))
                        .withRel("download-file")
                        .withType("GET")
                        .withDeprecation("Download File"),
                linkTo(methodOn(FileController.class).removeFile(response.getFilename()))
                        .withRel("delete-file")
                        .withType("DELETE")
                        .withDeprecation("Delete File")
        };
        return response.add(links);
    }

    /**
     * Добавляет ссылки для получения и операций над файлом в ответ FileResponse.
     *
     * @param response объект FileResponse.
     * @return объект FileResponse с добавленными ссылками.
     */
    public FileResponse getOperationWithLink(FileResponse response) {
        Link[] links = new Link[]{
                linkTo(methodOn(FileController.class).fileUpload(null, null, 0, null))
                        .withRel("add-file")
                        .withType("POST")
                        .withDeprecation("Add File"),
                linkTo(methodOn(FileController.class).viewFile(response.getFilename()))
                        .withRel("view-file")
                        .withType("GET")
                        .withDeprecation("View File"),
                linkTo(methodOn(FileController.class).downloadFile(response.getFilename()))
                        .withRel("download-file")
                        .withType("GET")
                        .withDeprecation("Download File"),
                linkTo(methodOn(FileController.class).removeFile(response.getFilename()))
                        .withRel("delete-file")
                        .withType("DELETE")
                        .withDeprecation("Delete File")
        };
        return response.add(links);
    }
}

