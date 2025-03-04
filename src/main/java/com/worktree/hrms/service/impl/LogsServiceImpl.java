package com.worktree.hrms.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.worktree.hrms.handlers.NotificationWebsocketHandler;
import com.worktree.hrms.service.LogsService;
import jakarta.annotation.PostConstruct;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.pdfbox.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class LogsServiceImpl implements LogsService {

    @Autowired
    private NotificationWebsocketHandler notificationWebsocketHandler;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void initLogs() throws IOException {
        String logFileName = System.getProperty("user.dir") + File.separator + "worktree.log";
        monitorLogFile(logFileName);
    }

    public void monitorLogFile(String logFilePath) throws IOException {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {

            File file = new File(logFilePath);

            try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
                long filePointer = file.length(); // Start at the end of the file (existing logs ignored)

                while (true) {
                    long fileLength = file.length();
                    if (fileLength < filePointer) {
                        // Log file might have been rotated (log rotation handling)
                        filePointer = fileLength;
                    }

                    if (filePointer < fileLength) {
                        raf.seek(filePointer); // Move to last read position
                        String line;
                        while ((line = raf.readLine()) != null) {
                            String logMessage = objectMapper.writeValueAsString(Map.of("method", "handleApplicationLogs", "type", "info", "logs", line));
                        notificationWebsocketHandler.sendNotification(logMessage);
                        }
                        filePointer = raf.getFilePointer(); // Save the last read position
                    }

                    Thread.sleep(1000); // Wait before checking again
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

    }



    @Override
    public byte[] downloadLogs(Optional<Integer> lines) throws IOException {

        String logFileName = System.getProperty("user.dir") + File.separator + "worktree.log";
        List<String> result = new ArrayList<>();

        if (lines.isPresent()) {
            try (ReversedLinesFileReader reader = new ReversedLinesFileReader(new File(logFileName))) {
                String line = "";
                while ((line = reader.readLine()) != null && result.size() < lines.get()) {
                    result.add(line);
                }
            }

            // Create a unique log file name with timestamp and random string in the temp folder
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String randomString = UUID.randomUUID().toString().substring(0, 8); // Take only the first 8 chars
            File tempDir = new File(System.getProperty("java.io.tmpdir"));
            String tempFileName = "filtered_logs_" + timestamp + "_" + randomString + ".log";
            File tempFile = new File(tempDir, tempFileName);

            // Write the result lines to the new unique temp file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                for (String line : result) {
                    writer.write(line);
                    writer.newLine(); // Add a newline after each line
                }
            }

            byte[] bytes;
            try (InputStream inputStream = new FileInputStream(tempFile.getAbsoluteFile())) {
                bytes = IOUtils.toByteArray(inputStream);
            }

            tempFile.getAbsoluteFile().delete();

            return bytes;
        }

        // After writing, return the file as a byte array (optional)
        try (InputStream inputStream = new FileInputStream(logFileName)) {
            return IOUtils.toByteArray(inputStream);
        }
    }


}
