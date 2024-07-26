package com.test_task.FileManager.util;

import jakarta.annotation.PostConstruct;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * Утилитный класс для шифрования и дешифрования строк с использованием соли и Base64.
 */
@Component
public class EncryptionUtil {

    @Value("${encryption.salt}")
    private String salt;

    private static String SALT;

    /**
     * Инициализация статического поля SALT после внедрения зависимости.
     */
    @PostConstruct
    private void init() {
        SALT = this.salt;
    }

    /**
     * Шифрует переданную строку, добавляя к ней соль и кодируя в Base64.
     *
     * @param strToEncrypt строка для шифрования.
     * @return зашифрованная строка.
     */
    public static String encrypt(String strToEncrypt) {
        if (strToEncrypt == null || strToEncrypt.isEmpty()) {
            throw new IllegalArgumentException("Строка для шифрования не может быть пустой или null.");
        }

        String saltedStr = SALT + strToEncrypt;
        return Base64.encodeBase64URLSafeString(saltedStr.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Дешифрует переданную строку, декодируя ее из Base64 и удаляя соль.
     *
     * @param strToDecrypt строка для дешифрования.
     * @return дешифрованная строка.
     */
    public static String decrypt(String strToDecrypt) {
        if (strToDecrypt == null || strToDecrypt.isEmpty()) {
            throw new IllegalArgumentException("Строка для дешифрования не может быть пустой или null.");
        }

        byte[] decodedBytes = Base64.decodeBase64(strToDecrypt);
        String decodedStr = new String(decodedBytes, StandardCharsets.UTF_8);
        return decodedStr.replaceFirst(SALT, "");
    }
}
