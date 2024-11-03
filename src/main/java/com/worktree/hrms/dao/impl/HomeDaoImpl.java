package com.worktree.hrms.dao.impl;

import com.worktree.hrms.constants.CommonConstants;
import com.worktree.hrms.dao.HomeDao;
import com.worktree.hrms.utils.HibernateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class HomeDaoImpl implements HomeDao {

    @Autowired
    private HibernateUtils hibernateUtils;

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
            case "chart":
                response.put("chart", getChart());
                break;
            case "billing":
                response.put("billing", getBilling());
                break;
            case "errors":
                response.put("errors", getErrors());
                break;
            case "requests":
                response.put("requests", getRequests());
                break;
            default:
                response.put("chart", getChart());
                response.put("billing", getBilling());
                response.put("errors", getErrors());
                response.put("requests", getRequests());
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
        tenantA.put("tenantName", "Tenant " + getRandomText());
        tenantA.put("expiryDate", "2024-10-30");
        tenants.add(tenantA);

        // Tenant B
        Map<String, String> tenantB = new HashMap<>();
        tenantB.put("tenantName", "Tenant " + getRandomText());
        tenantB.put("expiryDate", "2024-10-28");
        tenants.add(tenantB);

        // Tenant C
        Map<String, String> tenantC = new HashMap<>();
        tenantC.put("tenantName", "Tenant " + getRandomText());
        tenantC.put("expiryDate", "2024-10-26");
        tenants.add(tenantC);

        return tenants;
    }

    private String getRandomText() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 5);
    }

    private Object getChart() {
        Map<String, Object> response = new HashMap<>();

//        // 1D
//        List<List<Object>> instances1D = new ArrayList<>();
//        instances1D.add(Arrays.asList(1698051600, 4));
//        instances1D.add(Arrays.asList(1698055200, 5));
//        instances1D.add(Arrays.asList(1698058800, 6));
//        Map<String, Object> data1D = new HashMap<>();
//        data1D.put("instances", instances1D);
//        response.put("1D", data1D);
//
//        // 1W
//        List<List<Object>> instances1W = new ArrayList<>();
//        instances1W.add(Arrays.asList(1697643600, 8));
//        instances1W.add(Arrays.asList(1697730000, 6));
//        instances1W.add(Arrays.asList(1697816400, 7));
//        Map<String, Object> data1W = new HashMap<>();
//        data1W.put("instances", instances1W);
//        response.put("1W", data1W);
//
//        // 1M
//        List<List<Object>> instances1M = new ArrayList<>();
//        instances1M.add(Arrays.asList(1695375600, 3));
//        instances1M.add(Arrays.asList(1695967200, 5));
//        instances1M.add(Arrays.asList(1696558800, 7));
//        Map<String, Object> data1M = new HashMap<>();
//        data1M.put("instances", instances1M);
//        response.put("1M", data1M);
//
//        // 6M
//        List<List<Object>> instances6M = new ArrayList<>();
//        instances6M.add(Arrays.asList(1677577200, 10));
//        instances6M.add(Arrays.asList(1680265200, 12));
//        instances6M.add(Arrays.asList(1682953200, 15));
//        Map<String, Object> data6M = new HashMap<>();
//        data6M.put("instances", instances6M);
//        response.put("6M", data6M);
//
//        // 1Y
//        List<List<Object>> instances1Y = new ArrayList<>();
//        instances1Y.add(Arrays.asList(1666626000, 8));
//        instances1Y.add(Arrays.asList(1671903600, 6));
//        instances1Y.add(Arrays.asList(1677181200, 9));
//        Map<String, Object> data1Y = new HashMap<>();
//        data1Y.put("instances", instances1Y);
//        response.put("1Y", data1Y);


        // Generate data for each time range
        response.put("1D", generateInstances(24, 3600));    // 24 hours in a day, 1-hour intervals
        response.put("1W", generateInstances(336, 1800));  // 336 half-hour intervals in a week
        response.put("1M",  generateInstances(30, 86400));  // 30 days in a month, 1-day intervals
        response.put("6M", generateInstances(180, 86400));     // 180 days in 6 months, 1-day intervals
        response.put("1Y",  generateInstances(365, 86400));     // 365 days in a year, 1-day intervals


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
        Random random = new Random();
        return random.nextInt(20) + 1; // Generate values between 1 and 20
    }

}
