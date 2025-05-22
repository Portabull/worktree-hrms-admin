package com.worktree.hrms.scanners;

import com.worktree.hrms.constants.CommonConstants;
import com.worktree.hrms.exceptions.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class FileScanner {

    // Patterns indicative of malicious content
    private static final List<String> MALICIOUS_PATTERNS = Arrays.asList(
            "<script", "eval(", "javascript:", "onload=", "alert(", "<?php", "<?=", "<%",
            "shell_exec(", "system(", "os.system", "cmd.exe", "powershell", "bash", "rm -rf", "wget", "curl", "base64_decode(",
            "chmod 777"
    );

    // Maximum allowed file size in bytes (e.g., 10 MB)
    private static final long MAX_FILE_SIZE = 10l * 1024 * 1024;

    /**
     * Scans a file for malicious content or metadata issues.
     *
     * @param file MultipartFile to scan.
     * @throws BadRequestException if the file is invalid or contains malicious content.
     */
    public void scanFile(MultipartFile file) {
        // Validate file size and metadata
        validateFile(file);

        // Scan file content based on type
        try {
            String extension = getExtension(file.getOriginalFilename());
            if ("pdf".equals(extension)) {
                scanPdfContent(file); // Handle PDFs specifically
            } else {
                scanGenericContent(file); // Handle all other file types
            }
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Exception Occurred :: ", e);
            throw new BadRequestException("Malicious File Uploaded");
        }
    }

    /**
     * Validates file metadata such as size and extension.
     *
     * @param file MultipartFile to validate.
     * @throws BadRequestException if validation fails.
     */
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("File is empty.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File size exceeds the maximum limit of 10 MB.");
        }

        String extension = getExtension(file.getOriginalFilename());
        if (extension.isEmpty()) {
            throw new BadRequestException("File does not have a valid extension.");
        }
    }

    /**
     * Scans generic file content (text, binary) for malicious patterns.
     *
     * @param file MultipartFile to scan.
     * @throws BadRequestException if malicious content is detected.
     */
    private void scanGenericContent(MultipartFile file) throws BadRequestException {
        try (InputStream inputStream = file.getInputStream()) {
            byte[] buffer = new byte[4096]; // 4 KB buffer
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                String contentChunk = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
                for (String pattern : MALICIOUS_PATTERNS) {
                    if (contentChunk.toLowerCase().contains(pattern.toLowerCase())) {
                        throw new BadRequestException("File contains potentially malicious content.");
                    }
                }
            }
        } catch (IOException e) {
            log.error(CommonConstants.EXCEPTION_OCCURRED, e);
            throw new BadRequestException("Invalid file or file may contains malicious content.");
        }
    }

    /**
     * Scans PDF file content by extracting text and looking for malicious patterns.
     *
     * @param file MultipartFile to scan.
     * @throws Exception if malicious content is detected.
     */
    private void scanPdfContent(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream();
             PDDocument pdfDocument = PDDocument.load(inputStream)) {

            PDFTextStripper pdfStripper = new PDFTextStripper();
            String pdfText = pdfStripper.getText(pdfDocument);

            for (String pattern : MALICIOUS_PATTERNS) {
                if (pdfText.toLowerCase().contains(pattern.toLowerCase())) {
                    throw new BadRequestException("PDF contains potentially malicious content.");
                }
            }
        } catch (IOException e) {
            log.error(CommonConstants.EXCEPTION_OCCURRED, e);
            throw new BadRequestException("Invalid pdf file or pdf may contains malicious content.");
        }
    }

    /**
     * Extracts the file extension from a file name.
     *
     * @param filename the name of the file.
     * @return the file extension in lowercase.
     */
    private String getExtension(String filename) {
        if (filename == null) return "";
        int lastIndex = filename.lastIndexOf(".");
        return (lastIndex == -1) ? "" : filename.substring(lastIndex + 1).toLowerCase();
    }
}
