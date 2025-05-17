package com.worktree.hrms.controllers;

import com.worktree.hrms.constants.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("api")
public class PaymentsController {

    @GetMapping("payment-history")
    public ResponseEntity<Map<String, Object>> paymentHistory(@RequestParam String tenantId) {

        Map<String, Object> response = new HashMap<>();

        List<Map<String, Object>> datas = new ArrayList<>();

        Map<String, Object> data1 = new HashMap<>();
        data1.put("paymentId", "12345");
        data1.put("paymentAmount", "$500");
        data1.put("billStartDate", "2024-01-01");
        data1.put("billEndDate", "2024-12-31");
        data1.put("usersCount", "50");
        data1.put("licenceType", "Enterprise");


        data1.put("paymentScreenshots", Arrays.asList(
                paymentScreenShots(getBase64String("static/image/temp/1.webp", "image/webp"), "1.webp"),
                paymentScreenShots(getBase64String("static/image/temp/1689263849595.pdf", "application/pdf"), "1689263849595.pdf")

                , paymentScreenShots(getBase64String("static/image/temp/2.webp", "image/webp"), "2.webp")));


        datas.add(data1);

        Map<String, Object> data2 = new HashMap<>();
        data2.put("paymentId", "54321");
        data2.put("paymentAmount", "$750");
        data2.put("billStartDate", "2024-02-01");
        data2.put("billEndDate", "2025-01-31");
        data2.put("usersCount", "100");
        data2.put("licenceType", "Professional");


        data2.put("paymentScreenshots", Arrays.asList(
                paymentScreenShots(getBase64String("static/image/temp/InvestmentsBook (4).xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"), "InvestmentsBook (4).xlsx"),
                paymentScreenShots(getBase64String("static/image/temp/4.webp", "image/webp"), "4.webp")));

        datas.add(data2);

        response.put("payments", datas);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private String getBase64String(String fileName, String mimeType) {
        try (InputStream inputStream = PaymentsController.class.getClassLoader().getResourceAsStream(fileName)) {

            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                // Read from the InputStream and write to the ByteArrayOutputStream
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }
                // Convert the byte array to Base64
                byte[] bytes = byteArrayOutputStream.toByteArray();
                return "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(bytes);
            } catch (IOException e) {
                log.error(CommonConstants.EXCEPTION_OCCURRED, e);
                return null;
            }

        } catch (Exception e) {
            log.error(CommonConstants.EXCEPTION_OCCURRED, e);
            return null;
        }
    }


    public Map<String, Object> paymentScreenShots(String base64, String fileName) {
        Map<String, Object> paymentScreenshots1 = new HashMap<>();
        paymentScreenshots1.put("fileName", fileName);
        paymentScreenshots1.put("fileContent", base64);
        return paymentScreenshots1;

    }


}
