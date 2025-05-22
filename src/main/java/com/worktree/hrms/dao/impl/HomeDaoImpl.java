package com.worktree.hrms.dao.impl;

import com.worktree.hrms.constants.CommonConstants;
import com.worktree.hrms.dao.HomeDao;
import com.worktree.hrms.utils.HibernateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.security.SecureRandom;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class HomeDaoImpl implements HomeDao {

    private final HibernateUtils hibernateUtils;

    private static final String CHART = "chart";
    private static final String BILLING = "billing";
    private static final String ERRORS = "errors";
    private static final String REQUESTS = "requests";
    private static final String TENANT_NAME = "tenantName";
    private static final String TENANT = "Tenant ";
    private static final String EXPIRY_DATE = "expiryDate";


    @Override
    public Map<String, Object> home(String type) {
        Map<String, Object> response = new HashMap<>();
        if (type == null) {
            type = "";
        }


        response.put(CommonConstants.STATUS, CommonConstants.SUCCESS);
        response.put(CommonConstants.MESSAGE, "");
        response.put(CommonConstants.STATUS_CODE, 200);

        switch (type) {
            case CHART:
                response.put(CHART, getChart());
                break;
            case BILLING:
                response.put(BILLING, getBilling());
                break;
            case ERRORS:
                response.put(ERRORS, getErrors());
                break;
            case REQUESTS:
                response.put(REQUESTS, getRequests());
                break;
            default:
                response.put(CHART, getChart());
                response.put(BILLING, getBilling());
                response.put(ERRORS, getErrors());
                response.put(REQUESTS, getRequests());
                break;
        }
        return response;
    }

    private Object getRequests() {
        List<Map<String, String>> requests = new ArrayList<>();

        // Request 001
        Map<String, String> request1 = new HashMap<>();
        request1.put("requestId", "REQ001");
        request1.put("message", "Message for Request 001" + getRandomText());
        requests.add(request1);

        // Request 002
        Map<String, String> request2 = new HashMap<>();
        request2.put("requestId", "REQ002");
        request2.put("message", "Message for Request 002" + getRandomText());
        requests.add(request2);

        return requests;
    }

    private Object getErrors() {
        List<Map<String, String>> logs = new ArrayList<>();
        Map<String, String> log1 = new HashMap<>();
        log1.put("logId", "LOG001");
        log1.put("details", "Error in process A" + getRandomText());
        log1.put("date", "2024-10-23");
        logs.add(log1);

        // Log 002
        Map<String, String> log2 = new HashMap<>();
        log2.put("logId", "LOG002");
        log2.put("details", "Timeout in process B" + getRandomText());
        log2.put("date", "2024-10-22");
        logs.add(log2);

        return logs;
    }

    private Object getBilling() {
        List<Map<String, String>> tenants = new ArrayList<>();

        // Tenant A
        Map<String, String> tenantA = new HashMap<>();
        tenantA.put(TENANT_NAME, TENANT + getRandomText());
        tenantA.put(EXPIRY_DATE, "2024-10-30");
        tenants.add(tenantA);

        // Tenant B
        Map<String, String> tenantB = new HashMap<>();
        tenantB.put(TENANT_NAME, TENANT + getRandomText());
        tenantB.put(EXPIRY_DATE, "2024-10-28");
        tenants.add(tenantB);

        // Tenant C
        Map<String, String> tenantC = new HashMap<>();
        tenantC.put(TENANT_NAME, TENANT + getRandomText());
        tenantC.put(EXPIRY_DATE, "2024-10-26");
        tenants.add(tenantC);

        return tenants;
    }

    private String getRandomText() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 5);
    }

    private Object getChart() {
        Map<String, Object> response = new HashMap<>();
        // Generate data for each time range
        response.put("1D", generateInstances(24, 3600));    // 24 hours in a day, 1-hour intervals
        response.put("1W", generateInstances(336, 1800));  // 336 half-hour intervals in a week
        response.put("1M", generateInstances(30, 86400));  // 30 days in a month, 1-day intervals
        response.put("6M", generateInstances(180, 86400));     // 180 days in 6 months, 1-day intervals
        response.put("1Y", generateInstances(365, 86400));     // 365 days in a year, 1-day intervals


        return response;
    }


    private Map<String, Object> generateInstances(int count, int intervalSeconds) {
        List<List<Object>> instances = new ArrayList<>();
        long currentTimestamp = System.currentTimeMillis() / 1000; // Current time in seconds

        for (int i = 0; i < count; i++) {
            instances.add(Arrays.asList(currentTimestamp, generateRandomValue()));
            currentTimestamp -= intervalSeconds; // Move back by the interval in seconds
        }

        Map<String, Object> data = new HashMap<>();
        data.put("instances", instances);
        return data;
    }

    // Generates random values as placeholders, adjust as needed
    private int generateRandomValue() {
        SecureRandom random = new SecureRandom();
        return random.nextInt(20) + 1; // Generate values between 1 and 20
    }

}
