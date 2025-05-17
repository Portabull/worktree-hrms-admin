package com.worktree.hrms.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.worktree.hrms.service.UserProfileSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class UserProfileSettingsController {

    private final UserProfileSettingsService userProfileSettingsService;

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

    private final ObjectMapper objectMapper;

    @GetMapping("settings")
    public ResponseEntity<?> settings() throws JsonProcessingException {
        return new ResponseEntity<>(userProfileSettingsService.settings(), HttpStatus.OK);
//        Map<String, Object> resp = objectMapper.readValue(jsonData, Map.class);
//        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PostMapping("upload/profile")
    public ResponseEntity<?> uploadProfilePhoto(@RequestPart MultipartFile file) throws IOException {

        byte[] fileContent = file.getBytes();

        String contentType = file.getContentType();

        String base64String = Base64.getEncoder().encodeToString(fileContent);

        return new ResponseEntity<>(userProfileSettingsService.uploadProfiePic("data:" + contentType + ";base64," + base64String), HttpStatus.OK);
    }

    @PostMapping("profile/logout")
    public ResponseEntity<?> profileLogout(@RequestParam Long tokenId) {
        return new ResponseEntity<>(userProfileSettingsService.profileLogout(tokenId), HttpStatus.OK);
    }

    @PostMapping("profile")
    public ResponseEntity<?> saveProfile(@RequestBody Map<String, Object> payload) {
        return new ResponseEntity<>(userProfileSettingsService.saveProfile(payload), HttpStatus.OK);
    }

    @GetMapping("users")
    public ResponseEntity<?> getUsers(@RequestParam(required = false) Integer page, @RequestParam(required = false) String searchText) {
        return new ResponseEntity<>(userProfileSettingsService.getUsers(page, searchText), HttpStatus.OK);
    }

    @PostMapping("user")
    public ResponseEntity<?> saveUser(@RequestBody Map<String, Object> payload) {
        return new ResponseEntity<>(userProfileSettingsService.saveUser(payload), HttpStatus.OK);
    }

    @PostMapping("delete/user")
    public ResponseEntity<?> deleteUser(@RequestParam Long userId) {
        return new ResponseEntity<>(userProfileSettingsService.deleteUser(userId), HttpStatus.OK);
    }

}
