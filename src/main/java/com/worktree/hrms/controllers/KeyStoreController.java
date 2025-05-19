package com.worktree.hrms.controllers;

import com.worktree.hrms.annotations.Feature;
import com.worktree.hrms.constants.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.IOUtils;
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

@Slf4j
@RestController
@RequestMapping("api")
public class KeyStoreController {


    private static final String FAILED_TO_DELETE = "Warning: Failed to delete keystore file.";

    private static final String KEYSTORE_FILE = "keystore.p12";
    private static final String ALIAS_NAME = "aliasName";
    private static final String KEYSTORE_PASSWORD = "keyStorePassword";

    private static final String ALGORITHM = "algorithm";
    private static final String KEYSTORE = "keySize";

    private static final String VALIDITY = "validity";
    private static final String COMMON_NAME = "commonName";
    private static final String ORG_UNIT = "orgUnit";
    private static final String ORG = "org";
    private static final String LOCALITY = "locality";
    private static final String STATE = "state";
    private static final String COUNTRY_CODE = "countryCode";
    private static final String COMMAND = "keytool -genkeypair -alias %s -keyalg %s -keysize %d -validity %d -keystore %s -storetype PKCS12 -storepass %s -dname \"CN=%s, OU=%s, O=%s, L=%s, S=%s, C=%s\"";

    @Feature(feature = CommonConstants.Features.KEYSTORE_SETTINGS)
    @PostMapping("/generate/keystore")
    public ResponseEntity<?> generateKeystore(@RequestBody Map<String, Object> request) {

        String tempDir = System.getProperty(CommonConstants.JAVA_TEMP_DIR);

        if (!tempDir.endsWith(File.separator)) {
            tempDir = tempDir + File.separator;
        }

        String absoluteFilePath = tempDir + UUID.randomUUID() + KEYSTORE_FILE;

        // Extract parameters from the map
        String alias = (String) request.get(ALIAS_NAME);
        String password = (String) request.get(KEYSTORE_PASSWORD);
        String keyAlgorithm = (String) request.get(ALGORITHM);
        int keySize = Integer.parseInt(request.get(KEYSTORE).toString());
        int validity = Integer.parseInt(request.get(VALIDITY).toString());

        String cn = (String) request.get(COMMON_NAME);
        String ou = (String) request.get(ORG_UNIT);
        String o = (String) request.get(ORG);
        String l = (String) request.get(LOCALITY);
        String st = (String) request.get(STATE);
        String c = (String) request.get(COUNTRY_CODE);


        String command = String.format(
                COMMAND,
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
                log.error(FAILED_TO_DELETE);
            }

            return response;
        } catch (Exception e) {
            log.error(CommonConstants.EXCEPTION_OCCURRED, e);
            return ResponseEntity.status(404).body(Map.of(CommonConstants.STATUS, CommonConstants.FAILED, CommonConstants.STATUS_CODE,
                    404, CommonConstants.MESSAGE, CommonConstants.INTERNAL_SERVER_ERROR));
        }
    }

    @Feature(feature = CommonConstants.Features.KEYSTORE_SETTINGS)
    @PostMapping("/validate/keystore")
    public ResponseEntity<?> validateKeystore(@RequestBody Map<String, Object> request) {

        String tempDir = System.getProperty(CommonConstants.JAVA_TEMP_DIR);

        if (!tempDir.endsWith(File.separator)) {
            tempDir = tempDir + File.separator;
        }

        String absoluteFilePath = tempDir + UUID.randomUUID() + KEYSTORE_FILE;

        // Extract parameters from the map
        String alias = (String) request.get(ALIAS_NAME);
        String password = (String) request.get(KEYSTORE_PASSWORD);
        String keyAlgorithm = (String) request.get(ALGORITHM);
        int keySize = Integer.parseInt(request.get(KEYSTORE).toString());
        String cn = (String) request.get(COMMON_NAME);
        String ou = (String) request.get(ORG_UNIT);
        String o = (String) request.get(ORG);
        String l = (String) request.get(LOCALITY);
        String st = (String) request.get(STATE);
        String c = (String) request.get(COUNTRY_CODE);
        String validity = (String) request.get(VALIDITY);


        String command = String.format(
                COMMAND,
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
                log.error(FAILED_TO_DELETE);
            }

            return ResponseEntity.status(200).body(CommonConstants.SUCCESS_RESPONSE);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of(CommonConstants.STATUS, CommonConstants.FAILED, CommonConstants.STATUS_CODE,
                    404, CommonConstants.MESSAGE, CommonConstants.INTERNAL_SERVER_ERROR));
        }
    }


    @Feature(feature = CommonConstants.Features.KEYSTORE_SETTINGS)
    @PostMapping("/link/generated-keystore")
    public ResponseEntity<?> generateKeyStoreLink(@RequestBody Map<String, Object> request) {

        String tempDir = System.getProperty(CommonConstants.JAVA_TEMP_DIR);

        if (!tempDir.endsWith(File.separator)) {
            tempDir = tempDir + File.separator;
        }

        String absoluteFilePath = tempDir + UUID.randomUUID() + KEYSTORE_FILE;

        // Extract parameters from the map
        String alias = (String) request.get(ALIAS_NAME);
        String password = (String) request.get(KEYSTORE_PASSWORD);
        String keyAlgorithm = (String) request.get(ALGORITHM);
        int keySize = Integer.parseInt(request.get(KEYSTORE).toString());
        String cn = (String) request.get(COMMON_NAME);
        String ou = (String) request.get(ORG_UNIT);
        String o = (String) request.get(ORG);
        String l = (String) request.get(LOCALITY);
        String st = (String) request.get(STATE);
        String c = (String) request.get(COUNTRY_CODE);
        String validity = (String) request.get(VALIDITY);


        String command = String.format(
                COMMAND,
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
                log.error(FAILED_TO_DELETE);
            }

            return ResponseEntity.status(200).body(CommonConstants.SUCCESS_RESPONSE);
        } catch (Exception e) {
            log.error(CommonConstants.EXCEPTION_OCCURRED, e);
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
