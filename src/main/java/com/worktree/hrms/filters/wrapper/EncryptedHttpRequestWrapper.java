package com.worktree.hrms.filters.wrapper;

import com.worktree.hrms.utils.EncryptionUtils;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class EncryptedHttpRequestWrapper extends HttpServletRequestWrapper {

    private final String decryptedBody;

    public EncryptedHttpRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.decryptedBody = decryptRequestBody(request);
    }

    private String decryptRequestBody(HttpServletRequest request) throws IOException {
        if (request.getContentType() != null && request.getContentType().startsWith("multipart/form-data")) {
            return ""; // Skip decryption for multipart requests
        }

        String encryptedBody = new BufferedReader(new InputStreamReader(request.getInputStream()))
                .lines().collect(Collectors.joining("\n"));
        try {

            if (StringUtils.isEmpty(encryptedBody)) {
                return "";
            }

            return EncryptionUtils.decrypt(encryptedBody); // Decrypt and store the body
        } catch (Exception e) {
            throw new IOException("Failed to decrypt request body", e);
        }
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(
                new ByteArrayInputStream(decryptedBody.getBytes(StandardCharsets.UTF_8))
        ));
    }

    @Override
    public ServletInputStream getInputStream() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decryptedBody.getBytes(StandardCharsets.UTF_8));

        return new ServletInputStream() {
            @Override
            public int read() {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // No implementation needed
            }
        };
    }
}