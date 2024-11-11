package com.worktree.hrms.controllers;

import com.worktree.hrms.annotations.Feature;
import com.worktree.hrms.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api")
public class CommonController {

    @Autowired
    private CommonService commonService;

    @Feature
    @GetMapping("coupons")
    public ResponseEntity<Map<String, Object>> getCoupons() {
        return new ResponseEntity<>(commonService.getCoupons(), HttpStatus.OK);
    }

    @Feature
    @PostMapping("coupon")
    public ResponseEntity<Map<String, Object>> saveCoupon(@RequestBody Map<String, Object> payload) {
        return new ResponseEntity<>(commonService.saveCoupon(payload), HttpStatus.OK);
    }

}
