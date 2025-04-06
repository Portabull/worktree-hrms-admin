package com.worktree.hrms.controllers;

import com.worktree.hrms.WorktreeHrmsAdminApplication;
import com.worktree.hrms.constants.CommonConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api")
public class AppController {

    @PostMapping("restart")
    public ResponseEntity<Map<String, Object>> restart() {
        WorktreeHrmsAdminApplication.restart();
        return new ResponseEntity<>(CommonConstants.SUCCESS_RESPONSE, HttpStatus.OK);
    }

}
