package com.test_task.FileManager.helper;


import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Утилитный класс для получения текущего mediaType из запроса.
 */
@UtilityClass
public class MediaTypeInfo {

    /**
     * Возвращает текущий mediaType из запроса.
     *
     * @return текущий mediaType.
     * @throws IllegalArgumentException если requestAttributes равен null.
     */
    public String getCurrentMediaType() {
        var requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new IllegalArgumentException("RequestAttributes равен null. Не удалось получить текущий mediaType.");
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        return request.getParameter("mediaType");
    }
}

