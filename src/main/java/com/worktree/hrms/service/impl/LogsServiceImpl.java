package com.worktree.hrms.service.impl;

import com.worktree.hrms.service.LogsService;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.pdfbox.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class LogsServiceImpl implements LogsService {


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
