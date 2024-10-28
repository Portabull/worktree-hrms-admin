package com.worktree.hrms.utils;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

@Component
public class TokenFileUtils {


    private static final String TOKEN_CACHE_FILE = "C:\\Users\\91850\\Downloads\\tokens\\token_cache.txt";

    // Check if the token exists in the file cache using streams (with shared file lock for reading)
    public boolean isTokenInFileCache(String token) {
        return syncFile(token, "read");
    }

    public void addTokenToFileCache(String token) {
        syncFile(token, "add");

    }

    // Remove a token from the file cache (synchronized operation with exclusive file lock)
    public synchronized void removeTokenFromFileCache(String token) {
        syncFile(token, "remove");
    }

    private synchronized boolean syncFile(String token, String type) {
        if (type.equals("add")) {
            Path filePath = Path.of(TOKEN_CACHE_FILE);

            try (FileChannel fileChannel = FileChannel.open(filePath, StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {
                // Lock the file briefly for appending the new token
                try (java.nio.channels.FileLock lock = fileChannel.lock()) {
                    String tokenWithNewLine = token + System.lineSeparator();
                    ByteBuffer buffer = ByteBuffer.wrap(tokenWithNewLine.getBytes(StandardCharsets.UTF_8));
                    fileChannel.write(buffer);
                }
            } catch (IOException e) {
                e.printStackTrace(); // Log error
            }
        } else if ("remove".equals(type)) {
            Path filePath = Path.of(TOKEN_CACHE_FILE);
            Path tempFilePath = Path.of(TOKEN_CACHE_FILE + ".tmp");

            try (BufferedReader reader = Files.newBufferedReader(filePath);
                 BufferedWriter writer = Files.newBufferedWriter(tempFilePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {

                // Copy all lines except the one matching the token to the temporary file
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().equals(token)) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace(); // Log error
            }

            // Now replace the original file with the updated file, locking only during the move
            try (FileChannel fileChannel = FileChannel.open(filePath, StandardOpenOption.WRITE);
                 java.nio.channels.FileLock lock = fileChannel.lock()) {
                Files.move(tempFilePath, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace(); // Log error
            }
        } else {
            Path filePath = Path.of(TOKEN_CACHE_FILE);
            try (FileChannel fileChannel = FileChannel.open(filePath, StandardOpenOption.READ);
                 BufferedReader reader = Files.newBufferedReader(filePath)) {

                // Acquire a shared lock for reading (allow multiple readers but no writers)
                try (java.nio.channels.FileLock lock = fileChannel.lock(0L, Long.MAX_VALUE, true)) {
                    return reader.lines().anyMatch(line -> line.trim().equals(token));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }
}
