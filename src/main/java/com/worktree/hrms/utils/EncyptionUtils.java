package com.worktree.hrms.utils;

import com.worktree.hrms.exceptions.BadRequestException;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@Component
public class EncyptionUtils {

    public String decrypt(String encryptedData, String iv, String secretKey) {
        try {
            // Ensure the secretKey is exactly 32 characters for AES-256
            if (secretKey.length() < 32) {
                secretKey = String.format("%-32s", secretKey).replace(' ', '0'); // Pad with '0' if less than 32 chars
            } else if (secretKey.length() > 32) {
                secretKey = secretKey.substring(0, 32); // Truncate if more than 32 chars
            }

            byte[] keyBytes = secretKey.getBytes("UTF-8");
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

            // Convert IV and encrypted data from Hex to byte arrays
            byte[] ivBytes = hexStringToByteArray(iv);
            byte[] encryptedBytes = hexStringToByteArray(encryptedData);

            // Initialize Cipher
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);

            // Decrypt the data
            byte[] originalBytes = cipher.doFinal(encryptedBytes);
            return new String(originalBytes, "UTF-8");

        } catch (Exception e) {
            throw new BadRequestException("Invalid request or decryption error");
        }
    }

    public byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

}
