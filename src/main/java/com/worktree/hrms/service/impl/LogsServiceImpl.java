package com.worktree.hrms.service.impl;

import com.worktree.hrms.service.LogsService;
import org.apache.pdfbox.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Service
public class LogsServiceImpl implements LogsService {

    @Override
    public byte[] downloadLogs(Map<String, Object> payload) throws IOException {
        try (InputStream inputStream = new FileInputStream("C://Users//91850//Documents//logs//portabull.log")) {
            return IOUtils.toByteArray(inputStream);
        }
    }

}
