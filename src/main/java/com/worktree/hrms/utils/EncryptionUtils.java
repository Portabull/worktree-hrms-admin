package com.worktree.hrms.utils;

import com.worktree.hrms.exceptions.BadRequestException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class EncryptionUtils {

    private static String SECRET_KEY = "NWIYRFIYF%@&#$)ABCDEFGHIJKLMNOP";

    static {
        // Ensure the secretKey is exactly 32 characters for AES-256
        if (SECRET_KEY.length() < 32) {
            SECRET_KEY = String.format("%-32s", SECRET_KEY).replace(' ', '0'); // Pad with '0' if less than 32 chars
        } else if (SECRET_KEY.length() > 32) {
            SECRET_KEY = SECRET_KEY.substring(0, 32); // Truncate if more than 32 chars
        }
    }

    public static String encrypt(String data) {
        try {


            byte[] keyBytes = SECRET_KEY.getBytes("UTF-8");
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

            // Generate a random IV
            byte[] ivBytes = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(ivBytes);
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            // Initialize Cipher
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);

            // Encrypt the data
            byte[] encryptedBytes = cipher.doFinal(data.getBytes("UTF-8"));

            // Convert IV and encrypted data to hex and concatenate with "::"
            return bytesToHex(ivBytes) + "::" + bytesToHex(encryptedBytes);

        } catch (Exception e) {
            throw new RuntimeException("Encryption error", e);
        }
    }

    public static String decrypt(String encodedData) {
        try {
            String encodedArray[] = encodedData.split("::");

            String iv = encodedArray[0];
            String encryptedData = encodedArray[1];

            byte[] keyBytes = SECRET_KEY.getBytes("UTF-8");
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

    // Helper method to convert byte array to hex string
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

}
