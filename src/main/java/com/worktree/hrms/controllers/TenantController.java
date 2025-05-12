package com.worktree.hrms.controllers;

import com.worktree.hrms.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("api")
@RestController
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @GetMapping("tenants")
    public ResponseEntity<Map<String, Object>> tenants() {
        return new ResponseEntity<>(tenantService.getTenants(), HttpStatus.OK);
    }

}
