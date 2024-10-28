package com.worktree.hrms.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.worktree.hrms.service.UserProfileSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api")
public class UserProfileSettingsController {

    @Autowired
    private UserProfileSettingsService userProfileSettingsService;

    // Sample JSON data
    String jsonData = "{"
            + "\"profile\": {"
            + "\"name\": \"John Abhram123\","
            + "\"mobile\": \"98765432100\","
            + "\"email\": \"example123@gmail.com\","
            + "\"location\": \"HYDD\","
            + "\"alternativeMobileNumber\": \"012345678900\","
            + "\"Address\": \"HYDDD, Telangana\""
            + "},"
            + "\"security\": ["
            + "{"
            + "\"loggedInTime\": \"28/10/2024 14:24:00\","
            + "\"loggedInDevice\": \"Logged in Windows 10 - Chrome browser\","
            + "\"loginLocation\": \"https://googlemaps/17.45\","
            + "\"tokenId\": \"1\","
            + "\"currentUser\": true"

            + "},"
            + "{"
            + "\"loggedInTime\": \"27/10/2024 09:15:00\","
            + "\"loggedInDevice\": \"Logged in macOS - Safari browser\","
            + "\"tokenId\": \"1\","
            + "\"loginLocation\": \"https://googlemaps/17.46\""
            + "}"
            + "],"
            + "\"viewAccounts\": false"
            + "}";

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("settings")
    public ResponseEntity<?> settings() throws JsonProcessingException {
        return new ResponseEntity<>(userProfileSettingsService.settings(), HttpStatus.OK);
//        Map<String, Object> resp = objectMapper.readValue(jsonData, Map.class);
//        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PostMapping("upload/profile")
    public ResponseEntity<?> uploadProfilePhoto(@RequestPart MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("profile/logout")
    public ResponseEntity<?> profileLogout(@RequestParam Long tokenId) {
        return new ResponseEntity<>(userProfileSettingsService.profileLogout(tokenId), HttpStatus.OK);
    }

    @PostMapping("profile")
    public ResponseEntity<?> saveProfile(@RequestBody Map<String, Object> payload) {
        return new ResponseEntity<>(userProfileSettingsService.saveProfile(payload), HttpStatus.OK);
    }
}
