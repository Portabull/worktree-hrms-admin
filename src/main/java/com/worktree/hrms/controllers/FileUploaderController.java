package com.worktree.hrms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("/upload")
public class FileUploaderController {


    @Autowired
    private Environment environment;

    @PostMapping
    public ResponseEntity<Map> uploadFile(@RequestParam("file") MultipartFile file) {
        // Check if the file is empty
        if (file.isEmpty()) {
            return new ResponseEntity<>(Map.of(), HttpStatus.BAD_REQUEST);
        }

        // Ensure the directory exists
        File dir = new File(environment.getProperty("upload.dir"));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        System.out.println("Hi uploading..........................");

        // Streaming the file to disk
        try (InputStream inputStream = file.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(new File(environment.getProperty("upload.dir") + file.getOriginalFilename()))) {

            // Buffer to write data in chunks
            byte[] buffer = new byte[1024];
            int bytesRead;

            // Read from InputStream and write to the OutputStream
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return new ResponseEntity<>(Map.of("status", "success"), HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>(Map.of(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
