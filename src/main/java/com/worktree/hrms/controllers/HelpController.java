package com.worktree.hrms.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.worktree.hrms.utils.DateUtils;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api")
public class HelpController {

//    [
//    {
//        "id": "internet",
//            "title": "Check Internet Connection",
//            "description": "Ensure you have a stable internet connection. Test by opening other websites to confirm connectivity.",
//            "steps": [
//        "Restart your router or modem.",
//                "Check your device's network settings.",
//                "Contact your Internet Service Provider if the issue persists."
//        ]
//    },
//    {
//        "id": "credentials",
//            "title": "Verify Credentials",
//            "description": "Ensure your username and password are correct. If you’ve forgotten your password, click 'Forgot password?' to reset it.",
//            "steps": [
//        "Double-check your username or email address.",
//                "Reset your password using the 'Forgot password?' link.",
//                "Contact support if your credentials are locked."
//        ]
//    },
//    {
//        "id": "location",
//            "title": "Enable Location Services",
//            "description": "Location services may be required to verify your login. Follow these steps to enable location:",
//            "steps": [
//        "On Chrome (Windows/Mac): Go to Settings > Privacy and Security > Site Settings > Location. Enable 'Ask before accessing'.",
//                "On Firefox (Windows/Mac): Go to Preferences > Privacy & Security > Permissions > Location. Enable location access for your site.",
//                "On Safari (Mac/iPhone): Go to Preferences > Websites > Location. Select 'Allow' for your website.",
//                "On Android: Open Settings > Location. Ensure location services are turned on.",
//                "On iOS: Go to Settings > Privacy > Location Services. Enable location services and set your browser to 'While Using the App'."
//        ]
//    },
//    {
//        "id": "cache",
//            "title": "Clear Browser Cache",
//            "description": "Clearing the cache can resolve login issues. Check your browser settings to clear cache and cookies.",
//            "steps": [
//        "On Chrome: Go to Settings > Privacy and Security > Clear Browsing Data.",
//                "On Firefox: Go to Options > Privacy & Security > Cookies and Site Data > Clear Data.",
//                "On Safari: Go to Preferences > Privacy > Manage Website Data > Remove All."
//        ]
//    },
//    {
//        "id": "compatibility",
//            "title": "Browser Compatibility",
//            "description": "Ensure you are using a supported browser (e.g., Chrome, Firefox, Edge). Update your browser to the latest version if needed.",
//            "steps": [
//        "Use a modern browser like Chrome, Firefox, or Edge.",
//                "Update your browser to the latest version.",
//                "Disable outdated plugins or extensions that may interfere."
//        ]
//    },
//    {
//        "id": "extensions",
//            "title": "Disable Extensions",
//            "description": "Browser extensions may interfere with the login process. Disable them temporarily and try again.",
//            "steps": [
//        "Open your browser's extensions or add-ons page.",
//                "Disable any extensions related to ad-blocking or script blocking.",
//                "Restart your browser and try logging in again."
//        ]
//    },
//    {
//        "id": "account",
//            "title": "Ensure Your Account is Active",
//            "description": "If your account is deactivated, contact your administrator to reactivate it.",
//            "steps": [
//        "Verify your account status in your profile settings.",
//                "Contact your administrator for reactivation if needed."
//        ]
//    },
//    {
//        "id": "support",
//            "title": "Contact Support",
//            "description": "If the above steps don’t resolve the issue, contact our support team.",
//            "steps": [
//        "Email our support team at support@example.com.",
//                "Provide details about the issue you are facing.",
//                "Wait for a response within 24-48 hours."
//        ]
//    }
//]

    String a = "[\n" +
            "    {\n" +
            "        \"id\": \"internet\",\n" +
            "        \"title\": \"Check Internet Connection\",\n" +
            "        \"description\": \"Ensure you have a stable internet connection. Test by opening other websites to confirm connectivity.\",\n" +
            "        \"steps\": [\n" +
            "            \"Restart your router or modem.\",\n" +
            "            \"Check your device's network settings.\",\n" +
            "            \"Contact your Internet Service Provider if the issue persists.\"\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"credentials\",\n" +
            "        \"title\": \"Verify Credentials\",\n" +
            "        \"description\": \"Ensure your username and password are correct. If you’ve forgotten your password, click 'Forgot password?' to reset it.\",\n" +
            "        \"steps\": [\n" +
            "            \"Double-check your username or email address.\",\n" +
            "            \"Reset your password using the 'Forgot password?' link.\",\n" +
            "            \"Contact support if your credentials are locked.\"\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"location\",\n" +
            "        \"title\": \"Enable Location Services\",\n" +
            "        \"description\": \"Location services may be required to verify your login. Follow these steps to enable location:\",\n" +
            "        \"steps\": [\n" +
            "            \"On Chrome (Windows/Mac): Go to Settings > Privacy and Security > Site Settings > Location. Enable 'Ask before accessing'.\",\n" +
            "            \"On Firefox (Windows/Mac): Go to Preferences > Privacy & Security > Permissions > Location. Enable location access for your site.\",\n" +
            "            \"On Safari (Mac/iPhone): Go to Preferences > Websites > Location. Select 'Allow' for your website.\",\n" +
            "            \"On Android: Open Settings > Location. Ensure location services are turned on.\",\n" +
            "            \"On iOS: Go to Settings > Privacy > Location Services. Enable location services and set your browser to 'While Using the App.'\"\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"cache\",\n" +
            "        \"title\": \"Clear Browser Cache\",\n" +
            "        \"description\": \"Clearing the cache can resolve login issues. Check your browser settings to clear cache and cookies.\",\n" +
            "        \"steps\": [\n" +
            "            \"On Chrome: Go to Settings > Privacy and Security > Clear Browsing Data.\",\n" +
            "            \"On Firefox: Go to Options > Privacy & Security > Cookies and Site Data > Clear Data.\",\n" +
            "            \"On Safari: Go to Preferences > Privacy > Manage Website Data > Remove All.\"\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"compatibility\",\n" +
            "        \"title\": \"Browser Compatibility\",\n" +
            "        \"description\": \"Ensure you are using a supported browser (e.g., Chrome, Firefox, Edge). Update your browser to the latest version if needed.\",\n" +
            "        \"steps\": [\n" +
            "            \"Use a modern browser like Chrome, Firefox, or Edge.\",\n" +
            "            \"Update your browser to the latest version.\",\n" +
            "            \"Disable outdated plugins or extensions that may interfere.\"\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"extensions\",\n" +
            "        \"title\": \"Disable Extensions\",\n" +
            "        \"description\": \"Browser extensions may interfere with the login process. Disable them temporarily and try again.\",\n" +
            "        \"steps\": [\n" +
            "            \"Open your browser's extensions or add-ons page.\",\n" +
            "            \"Disable any extensions related to ad-blocking or script blocking.\",\n" +
            "            \"Restart your browser and try logging in again.\"\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"account\",\n" +
            "        \"title\": \"Ensure Your Account is Active\",\n" +
            "        \"description\": \"If your account is deactivated, contact your administrator to reactivate it.\",\n" +
            "        \"steps\": [\n" +
            "            \"Verify your account status in your profile settings.\",\n" +
            "            \"Contact your administrator for reactivation if needed.\"\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"support\",\n" +
            "        \"title\": \"Contact Support\",\n" +
            "        \"description\": \"If the above steps don’t resolve the issue, contact our support team.\",\n" +
            "        \"steps\": [\n" +
            "            \"Email our support team at support@example.com.\",\n" +
            "            \"Provide details about the issue you are facing.\",\n" +
            "            \"Wait for a response within 24-48 hours.\"\n" +
            "        ]\n" +
            "    }\n" +
            "]";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DateUtils dateUtils;

    long lastModified;

    @PostConstruct
    public void loadDate() {
        lastModified = dateUtils.getCurrentDate().getTime();
    }


    @GetMapping("help-docs")
    public ResponseEntity<Map<String, Object>> getHelpDocs(@RequestParam String type) throws JsonProcessingException {
        Map<String, Object> response = new HashMap<>();
        response.put("sideBarHeader", "Login Troubleshoot");
        response.put("mainBarHeader", "Troubleshooting Login Issues");
        response.put("mainArea", objectMapper.readValue(a, List.class));

        // Generate ETag
//        String etag = Integer.toHexString(response.hashCode());
        HttpHeaders headers = new HttpHeaders();
//        headers.setETag("\"" + etag + "\"");
//
//        // Set Cache-Control and Last-Modified
//        headers.setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES).cachePublic());
//
//        headers.setLastModified(lastModified);
//
//        // Check If-None-Match
//        String ifNoneMatch = request.getHeader("If-None-Match");
//        if (ifNoneMatch != null && ifNoneMatch.equals("\"" + etag + "\"")) {
//            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).headers(headers).build();
//        }

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

}
