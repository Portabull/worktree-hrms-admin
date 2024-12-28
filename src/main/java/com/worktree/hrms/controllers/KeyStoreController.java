package com.worktree.hrms.controllers;

import com.worktree.hrms.annotations.Feature;
import com.worktree.hrms.constants.CommonConstants;
import org.apache.pdfbox.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api")
public class KeyStoreController {

    private final static Logger logger = LoggerFactory.getLogger(KeyStoreController.class);

    @Feature
    @PostMapping("/generate/keystore")
    public ResponseEntity<?> generateKeystore(@RequestBody Map<String, Object> request) {

        String tempDir = System.getProperty("java.io.tmpdir");

        if (!tempDir.endsWith(File.separator)) {
            tempDir = tempDir + File.separator;
        }

        String absoluteFilePath = tempDir + UUID.randomUUID() + "keystore.p12";

        // Extract parameters from the map
        String alias = (String) request.get("aliasName");
        String password = (String) request.get("keyStorePassword");
        String keyAlgorithm = (String) request.get("algorithm");
        int keySize = Integer.valueOf(request.get("keySize").toString());
        int validity = Integer.valueOf(request.get("validity").toString());
        String cn = (String) request.get("commonName");
        String ou = (String) request.get("orgUnit");
        String o = (String) request.get("org");
        String l = (String) request.get("locality");
        String st = (String) request.get("state");
        String c = (String) request.get("countryCode");

        String command = String.format(
                "keytool -genkeypair -alias %s -keyalg %s -keysize %d -validity %d -keystore %s -storetype PKCS12 -storepass %s -dname \"CN=%s, OU=%s, O=%s, L=%s, S=%s, C=%s\"",
                alias, keyAlgorithm, keySize, validity, absoluteFilePath, password, cn, ou, o, l, st, c
        );

        try {
            // Execute the command
            Process process = Runtime.getRuntime().exec(command);

            // Capture output and error streams
            String infoMessage = captureStream(process.getInputStream());
            String errorMessage = captureStream(process.getErrorStream());

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                if (errorMessage == null || errorMessage.trim().equals("")) {
                    errorMessage = infoMessage;
                }
                return ResponseEntity.status(500).body(Map.of(CommonConstants.STATUS, CommonConstants.FAILED, CommonConstants.STATUS_CODE,
                        500, CommonConstants.MESSAGE, errorMessage));
            }

            // Stream the file for download and delete afterward
            File keystoreFile = new File(absoluteFilePath);
            if (!keystoreFile.exists()) {
                return ResponseEntity.status(404).body(Map.of(CommonConstants.STATUS, CommonConstants.FAILED, CommonConstants.STATUS_CODE,
                        404, CommonConstants.MESSAGE, CommonConstants.INTERNAL_SERVER_ERROR));
            }
            ResponseEntity<?> response;
            try (InputStream inputStream = new FileInputStream(keystoreFile)) {

                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=keystore.p12");
                headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
                headers.add("commandMessage", errorMessage == null || errorMessage.trim().equals("") ? infoMessage : errorMessage);

                response = ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(keystoreFile.length())
                        .body(new InputStreamResource(new ByteArrayInputStream(IOUtils.toByteArray(inputStream))));
            }

            boolean deleted = keystoreFile.delete();

            if (!deleted) {
                logger.error("Warning: Failed to delete keystore file.");
            }

            return response;
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of(CommonConstants.STATUS, CommonConstants.FAILED, CommonConstants.STATUS_CODE,
                    404, CommonConstants.MESSAGE, CommonConstants.INTERNAL_SERVER_ERROR));
        }
    }

    @Feature
    @PostMapping("/validate/keystore")
    public ResponseEntity<?> validateKeystore(@RequestBody Map<String, Object> request) {

        String tempDir = System.getProperty("java.io.tmpdir");

        if (!tempDir.endsWith(File.separator)) {
            tempDir = tempDir + File.separator;
        }

        String absoluteFilePath = tempDir + UUID.randomUUID() + "keystore.p12";

        // Extract parameters from the map
        String alias = (String) request.get("aliasName");
        String password = (String) request.get("keyStorePassword");
        String keyAlgorithm = (String) request.get("algorithm");
        int keySize = Integer.valueOf(request.get("keySize").toString());
        int validity = Integer.valueOf(request.get("validity").toString());
        String cn = (String) request.get("commonName");
        String ou = (String) request.get("orgUnit");
        String o = (String) request.get("org");
        String l = (String) request.get("locality");
        String st = (String) request.get("state");
        String c = (String) request.get("countryCode");

        String command = String.format(
                "keytool -genkeypair -alias %s -keyalg %s -keysize %d -validity %d -keystore %s -storetype PKCS12 -storepass %s -dname \"CN=%s, OU=%s, O=%s, L=%s, S=%s, C=%s\"",
                alias, keyAlgorithm, keySize, validity, absoluteFilePath, password, cn, ou, o, l, st, c
        );

        try {
            // Execute the command
            Process process = Runtime.getRuntime().exec(command);

            // Capture output and error streams
            String infoMessage = captureStream(process.getInputStream());
            String errorMessage = captureStream(process.getErrorStream());

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                if (errorMessage == null || errorMessage.trim().equals("")) {
                    errorMessage = infoMessage;
                }
                return ResponseEntity.status(500).body(Map.of(CommonConstants.STATUS, CommonConstants.FAILED, CommonConstants.STATUS_CODE,
                        500, CommonConstants.MESSAGE, errorMessage));
            }

            // Stream the file for download and delete afterward
            File keystoreFile = new File(absoluteFilePath);
            if (!keystoreFile.exists()) {
                return ResponseEntity.status(404).body(Map.of(CommonConstants.STATUS, CommonConstants.FAILED, CommonConstants.STATUS_CODE,
                        404, CommonConstants.MESSAGE, CommonConstants.INTERNAL_SERVER_ERROR));
            }

            boolean deleted = keystoreFile.delete();

            if (!deleted) {
                logger.error("Warning: Failed to delete keystore file.");
            }

            return ResponseEntity.status(200).body(CommonConstants.SUCCESS_RESPONSE);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of(CommonConstants.STATUS, CommonConstants.FAILED, CommonConstants.STATUS_CODE,
                    404, CommonConstants.MESSAGE, CommonConstants.INTERNAL_SERVER_ERROR));
        }
    }


    @Feature
    @PostMapping("/link/generated-keystore")
    public ResponseEntity<?> generateKeyStoreLink(@RequestBody Map<String, Object> request) {

        String tempDir = System.getProperty("java.io.tmpdir");

        if (!tempDir.endsWith(File.separator)) {
            tempDir = tempDir + File.separator;
        }

        String absoluteFilePath = tempDir + UUID.randomUUID() + "keystore.p12";

        // Extract parameters from the map
        String alias = (String) request.get("aliasName");
        String password = (String) request.get("keyStorePassword");
        String keyAlgorithm = (String) request.get("algorithm");
        int keySize = Integer.valueOf(request.get("keySize").toString());
        int validity = Integer.valueOf(request.get("validity").toString());
        String cn = (String) request.get("commonName");
        String ou = (String) request.get("orgUnit");
        String o = (String) request.get("org");
        String l = (String) request.get("locality");
        String st = (String) request.get("state");
        String c = (String) request.get("countryCode");

        String command = String.format(
                "keytool -genkeypair -alias %s -keyalg %s -keysize %d -validity %d -keystore %s -storetype PKCS12 -storepass %s -dname \"CN=%s, OU=%s, O=%s, L=%s, S=%s, C=%s\"",
                alias, keyAlgorithm, keySize, validity, absoluteFilePath, password, cn, ou, o, l, st, c
        );

        try {
            // Execute the command
            Process process = Runtime.getRuntime().exec(command);

            // Capture output and error streams
            String infoMessage = captureStream(process.getInputStream());
            String errorMessage = captureStream(process.getErrorStream());

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                if (errorMessage == null || errorMessage.trim().equals("")) {
                    errorMessage = infoMessage;
                }
                return ResponseEntity.status(500).body(Map.of(CommonConstants.STATUS, CommonConstants.FAILED, CommonConstants.STATUS_CODE,
                        500, CommonConstants.MESSAGE, errorMessage));
            }

            // Stream the file for download and delete afterward
            File keystoreFile = new File(absoluteFilePath);
            if (!keystoreFile.exists()) {
                return ResponseEntity.status(404).body(Map.of(CommonConstants.STATUS, CommonConstants.FAILED, CommonConstants.STATUS_CODE,
                        404, CommonConstants.MESSAGE, CommonConstants.INTERNAL_SERVER_ERROR));
            }

            //TODO: keystoreFile has to be linked
//            request.get("certificateName");

            boolean deleted = keystoreFile.delete();

            if (!deleted) {
                logger.error("Warning: Failed to delete keystore file.");
            }

            return ResponseEntity.status(200).body(CommonConstants.SUCCESS_RESPONSE);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of(CommonConstants.STATUS, CommonConstants.FAILED, CommonConstants.STATUS_CODE,
                    404, CommonConstants.MESSAGE, CommonConstants.INTERNAL_SERVER_ERROR));
        }
    }

    private String captureStream(InputStream stream) throws IOException {
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append(System.lineSeparator());
            }
        }
        return result.toString();
    }

}
