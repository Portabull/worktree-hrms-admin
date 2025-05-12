package com.worktree.hrms.utils;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;

@Component
public class FileUtils {

    private FileUtils() {
    }

    public static File convertToFile(String base64String, String fileName) throws IOException {

        if (base64String.contains(",")) {
            base64String = base64String.split(",")[1];
        }

        // Decode the Base64 string
        byte[] fileData = Base64.getDecoder().decode(base64String);

        File file = new File(getTempPath() + new Date().getTime() + fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(fileData);
        }

        return file;
    }

    public static String getTempPath() {

        String tempDir = System.getProperty("java.io.tmpdir");

        if (!tempDir.endsWith(File.separator)) {
            tempDir = tempDir + File.separator;
        }

        return tempDir;
    }

}
