package com.worktree.hrms.controllers;

import com.worktree.hrms.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @GetMapping("home")
    public ResponseEntity<Map<String, Object>> home(@RequestParam(required = false) String type) {
        return new ResponseEntity<>(homeService.home(type), HttpStatus.OK);
    }

}
