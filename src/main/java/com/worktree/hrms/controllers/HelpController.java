package com.worktree.hrms.controllers;

import com.worktree.hrms.service.HelpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api")
public class HelpController {

    @Autowired
    HelpService helpService;


    @GetMapping("help-docs")
    public ResponseEntity<Map<String, Object>> getHelpDocs(@RequestParam String type) {

        // Generate ETag
//        String etag = Integer.toHexString(response.hashCode());
        HttpHeaders headers = new HttpHeaders();
//        headers.setETag("\"" + etag + "\"");
//
//        // Set Cache-Control and Last-Modified
        headers.setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES).cachePublic());
//
//        headers.setLastModified(lastModified);
//
//        // Check If-None-Match
//        String ifNoneMatch = request.getHeader("If-None-Match");
//        if (ifNoneMatch != null && ifNoneMatch.equals("\"" + etag + "\"")) {
//            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).headers(headers).build();
//        }

        return new ResponseEntity<>(helpService.getHelpConfig(type), headers, HttpStatus.OK);
    }


    @GetMapping("help/config")
    public ResponseEntity<List<Map<String, Object>>> getHelpDocs() {
        return new ResponseEntity<>(helpService.getHelpConfig(), HttpStatus.OK);
    }

    @PostMapping("help/config")
    public ResponseEntity<Map<String, Object>> saveHelpConfig(@RequestBody Map<String, Object> payload) {
        return new ResponseEntity<>(helpService.saveHelpConfig(payload), HttpStatus.OK);
    }

    @GetMapping("help/config-docs")
    public ResponseEntity<Map<String, Object>> getHelpDocs(@RequestParam Long hid) {
        return new ResponseEntity<>(helpService.getHelpConfig(hid), HttpStatus.OK);
    }

    @PostMapping("help/reset")
    public ResponseEntity<Map<String, Object>> reset() {
        return new ResponseEntity<>(helpService.reset(), HttpStatus.OK);
    }

}
