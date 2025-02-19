package com.worktree.hrms.controllers;

import com.worktree.hrms.annotations.FileScan;
import com.worktree.hrms.service.LicenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("api")
public class LicenceController {

    @Autowired
    private LicenceService licenceService;

    //    10MB file max limit
    @FileScan(size = 10 * 1024 * 1024, message = "Invalid Licence file uploaded please upload a valid file")
    @PostMapping("licence")
    public ResponseEntity<Map> uploadLicence(@RequestPart MultipartFile file) {
        return new ResponseEntity<>(licenceService.uploadLicence(file), HttpStatus.OK);
    }

    @GetMapping("licence")
    public ResponseEntity<Map> getLicence() {
        Map<String, Object> response = licenceService.getLicence();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
