package com.worktree.hrms.controllers;

import com.worktree.hrms.exceptions.BadRequestException;
import com.worktree.hrms.service.LoginService;
import com.worktree.hrms.utils.RequestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {

        if (StringUtils.isEmpty(RequestHelper.getHeader("latlong")) || !RequestHelper.getHeader("latlong").contains(":")) {
            throw new BadRequestException("Invalid Request");
        }

        Map<String, Object> response = loginService.login(payload);

        if ("FAILED".equalsIgnoreCase(String.valueOf(response.get("status")))) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        if (!Boolean.valueOf(response.get("licenseVerified").toString())) {
            return new ResponseEntity<>(response, HttpStatus.PAYMENT_REQUIRED);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout() {

        Map<String, Object> response = loginService.logout();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
