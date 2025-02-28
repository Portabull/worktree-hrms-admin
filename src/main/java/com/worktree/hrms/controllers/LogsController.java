package com.worktree.hrms.controllers;

import com.worktree.hrms.annotations.Feature;
import com.worktree.hrms.constants.CommonConstants;
import com.worktree.hrms.service.LogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class LogsController {

    @Autowired
    private LogsService logsService;

    @Feature(feature = CommonConstants.Features.KEYSTORE_SETTINGS)
    @GetMapping("/download/logs")
    public ResponseEntity<?> downloadLogs(@RequestParam Optional<Integer> lines) throws IOException {
        ResponseEntity<?> response;

        byte[] bytes = logsService.downloadLogs(lines);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=logs.log");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        response = ResponseEntity.ok()
                .headers(headers)
                .contentLength(bytes.length)
                .body(new InputStreamResource(new ByteArrayInputStream(bytes)));

        return response;
    }
}
