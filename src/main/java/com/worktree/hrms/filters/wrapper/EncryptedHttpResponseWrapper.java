package com.worktree.hrms.filters.wrapper;

import com.worktree.hrms.utils.EncryptionUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class EncryptedHttpResponseWrapper extends HttpServletResponseWrapper {

    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private PrintWriter writer = new PrintWriter(byteArrayOutputStream);

    public EncryptedHttpResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return new ServletOutputStream() {
            @Override
            public void write(int b) {
                byteArrayOutputStream.write(b);
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(jakarta.servlet.WriteListener writeListener) {
            }
        };
    }

    @Override
    public PrintWriter getWriter() {
        return writer;
    }

    public void handleResponse(boolean encrypt) throws IOException {
        writer.flush();
        String responseBody = byteArrayOutputStream.toString();
        try {
            if (encrypt) {
                String encryptedBody = EncryptionUtils.encrypt(responseBody);
                getResponse().getOutputStream().write(encryptedBody.getBytes());
            } else {
                getResponse().getOutputStream().write(responseBody.getBytes());
            }

        } catch (Exception e) {
            throw new IOException("Failed to encrypt response body", e);
        }
    }

}
