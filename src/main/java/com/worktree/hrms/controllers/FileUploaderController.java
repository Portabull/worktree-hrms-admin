package com.worktree.hrms.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class FileUploaderController {


    @Autowired
    private Environment environment;

    @PostMapping("/upload")
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


    @GetMapping("/videos")
    public ResponseEntity<List<Map<String, String>>> listVideos() {
        File dir = new File(environment.getProperty("upload.dir") );
        List<Map<String, String>> videos = Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .filter(File::isFile)
                .map(file -> {
                    Map<String, String> video = new HashMap<>();
                    video.put("title", file.getName());
                    video.put("url",  file.getName());
                    return video;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(videos);
    }


    @GetMapping("/stream/{videoName}")
    public void streamVideo(@PathVariable String videoName, HttpServletRequest request, HttpServletResponse response) {
        try {
            String decodedVideoName = URLDecoder.decode(videoName, StandardCharsets.UTF_8.name());
            File videoFile = new File(environment.getProperty("upload.dir") + File.separator + decodedVideoName);

            if (!videoFile.exists()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            long fileLength = videoFile.length();
            String range = request.getHeader("Range");
            long start = 0, end = fileLength - 1;

            if (range != null && range.startsWith("bytes=")) {
                String[] ranges = range.substring(6).split("-");
                start = Long.parseLong(ranges[0]);
                if (ranges.length > 1) {
                    end = Long.parseLong(ranges[1]);
                }
            }

            long contentLength = end - start + 1;
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            response.setContentType("video/mp4");
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);
            response.setHeader("Content-Length", String.valueOf(contentLength));

            try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(videoFile));
                 OutputStream outputStream = response.getOutputStream()) {
                inputStream.skip(start);
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1 && contentLength > 0) {
                    outputStream.write(buffer, 0, bytesRead);
                    contentLength -= bytesRead;
                }
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }





}
