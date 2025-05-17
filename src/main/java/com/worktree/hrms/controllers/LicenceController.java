package com.worktree.hrms.controllers;

import com.worktree.hrms.annotations.FileScan;
import com.worktree.hrms.service.LicenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class LicenceController {

    private final LicenceService licenceService;

    //    10MB file max limit
    @FileScan(size = 10 * 1024 * 1024, message = "Invalid Licence file uploaded please upload a valid file")
    @PostMapping("licence")
    public ResponseEntity<Map<String, Object>> uploadLicence(@RequestPart MultipartFile file) {
        licenceService.uploadLicence(file);
        return new ResponseEntity<>(licenceService.getLicence(), HttpStatus.OK);
    }

    @GetMapping("licence")
    public ResponseEntity<Map<String, Object>> getLicence() {
        Map<String, Object> response = licenceService.getLicence();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
