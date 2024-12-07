package com.worktree.hrms.filters;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.worktree.hrms.dao.UserDao;
//import com.worktree.hrms.utils.HibernateUtils;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.hibernate.Session;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Component
//public class SecurityFilter extends OncePerRequestFilter {
//
//    @Autowired
//    @Lazy
//    private HibernateUtils hibernateUtils;
//
//    private List<String> skipEndPoints;
//
//    @Value("${skip.filters.endpoint}")
//    public void setSkipEndpoints(String skipEndpoint) {
//        skipEndPoints = Arrays.asList(skipEndpoint.split(","));
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//
//        if (httpServletRequest.getRequestURI().startsWith("/api")) {
//            Integer statusCode = 401;
//            final String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
//            if (skipEndPoints.stream().noneMatch(endPoint -> endPoint.equalsIgnoreCase(httpServletRequest.getRequestURI()))) {
//                try (Session session = hibernateUtils.getSession()) {
//                    Number a = (Number) session.createQuery("SELECT COUNT(*) FROM UserTokenEntity WHERE jwt=:jwt").setParameter("jwt", token).uniqueResult();
//                    if (a != null && a.intValue() != 0)
//                        statusCode = 200;
//                }
//            } else {
//                statusCode = 200;
//            }
//
//            if (statusCode == null || statusCode == 401) {
//                Map<String, Object> errorDetails = new HashMap<>();
//                errorDetails.put("message", "Unauthorized");
//                errorDetails.put("status", "FAILED");
//
//                PrintWriter out = httpServletResponse.getWriter();
//                out.write("");
//                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
//                new ObjectMapper().writeValue(httpServletResponse.getWriter(), errorDetails);
//                return;
//            }
//        }
//
//        filterChain.doFilter(httpServletRequest, httpServletResponse);
//    }
//}

import com.fasterxml.jackson.databind.ObjectMapper;
import com.worktree.hrms.filters.wrapper.EncryptedHttpRequestWrapper;
import com.worktree.hrms.filters.wrapper.EncryptedHttpResponseWrapper;
import com.worktree.hrms.utils.HibernateUtils;
import com.worktree.hrms.utils.TokenFileUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    @Lazy
    private HibernateUtils hibernateUtils;

    @Autowired
    private TokenFileUtils tokenFileUtils;

    private List<String> skipEndPoints;

    @Value("${skip.filters.endpoints}")
    public void setSkipEndpoints(String skipEndpoint) {
        skipEndPoints = Arrays.asList(skipEndpoint.split(","));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().startsWith("/api")) {
            int statusCode = HttpServletResponse.SC_UNAUTHORIZED;
            final String token = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (skipEndPoints.stream().noneMatch(endPoint -> endPoint.equalsIgnoreCase(request.getRequestURI()))) {
                if (token != null && !token.isEmpty()) {
                    // 1. Check token in the file cache (with file lock for reading)
                    if (tokenFileUtils.isTokenInFileCache(token)) {
                        statusCode = HttpServletResponse.SC_OK;
                    } else {
                        // 2. If not found in file, query the database
                        try (Session session = hibernateUtils.getSession()) {
                            Long tokenCount = (Long) session.createQuery("SELECT COUNT(*) FROM UserTokenEntity WHERE jwt=:jwt")
                                    .setParameter("jwt", token)
                                    .uniqueResult();
                            if (tokenCount != null && tokenCount > 0) {
                                statusCode = HttpServletResponse.SC_OK;
                                // 3. Add token to file cache if found in database (synchronized with file lock)
                                tokenFileUtils.addTokenToFileCache(token);
                            }
                        }
                    }
                }
            } else {
                statusCode = HttpServletResponse.SC_OK;
            }

            if (statusCode == HttpServletResponse.SC_UNAUTHORIZED) {
                populateUnAuthorized(response);
                return;
            }

            if (request.getHeader("X-ENCRYPT-ID") == null) {

                if (request.getHeader("X-TOKEN_PACK_ID") == null) {
                    populateUnAuthorized(response);
                    return;
                } else {
                    if (!isWithinOneMinute(Long.valueOf(request.getHeader("X-TOKEN_PACK_ID")))) {
                        populateUnAuthorized(response);
                        return;
                    }
                }

                EncryptedHttpRequestWrapper encryptedRequest = new EncryptedHttpRequestWrapper(request);
                EncryptedHttpResponseWrapper encryptedResponse = new EncryptedHttpResponseWrapper(response);
                filterChain.doFilter(encryptedRequest, encryptedResponse);
                encryptedResponse.handleResponse(!MediaType.APPLICATION_OCTET_STREAM_VALUE.equalsIgnoreCase(response.getHeader("Content-Type")));
                return;
            }
        }


        filterChain.doFilter(request, response);
    }

    private void populateUnAuthorized(HttpServletResponse response) throws IOException {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("message", "Unauthorized");
        errorDetails.put("status", "FAILED");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getWriter(), errorDetails);
    }

    public static boolean isWithinOneMinute(long timestampFromUI) {
        // Get the current time in IST timezone
        ZonedDateTime currentISTTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
        long currentTimeMillis = currentISTTime.toInstant().toEpochMilli();

        // Calculate the difference between the current time and the timestamp
        long differenceMillis = Math.abs(currentTimeMillis - timestampFromUI);

        // Check if the difference is within one minute (60,000 milliseconds)
        return differenceMillis <= 60000;
    }
}
