package com.worktree.hrms.controllers;

import com.worktree.hrms.annotations.Feature;
import com.worktree.hrms.constants.CommonConstants;
import com.worktree.hrms.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class CommonController {

    private final CommonService commonService;

    @Feature(feature = CommonConstants.Features.COUPON_SETTINGS)
    @GetMapping("coupons")
    public ResponseEntity<Map<String, Object>> getCoupons() {
        return new ResponseEntity<>(commonService.getCoupons(), HttpStatus.OK);
    }

    @Feature(feature = CommonConstants.Features.COUPON_SETTINGS)
    @PostMapping("coupon")
    public ResponseEntity<Map<String, Object>> saveCoupon(@RequestBody Map<String, Object> payload) {
        return new ResponseEntity<>(commonService.saveCoupon(payload), HttpStatus.OK);
    }

}
