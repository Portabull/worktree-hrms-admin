package com.worktree.hrms.handlers;

import com.worktree.hrms.constants.CommonConstants;
import com.worktree.hrms.exceptions.BadRequestException;
import com.worktree.hrms.exceptions.ForbiddenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(Throwable.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundError(Throwable ex) {
        log.error("Exception Occurred :: ", ex);
        Map<String, Object> response = new HashMap<>();
        response.put(CommonConstants.STATUS, CommonConstants.FAILED);
        response.put(CommonConstants.STATUS_CODE, 500);
        response.put(CommonConstants.MESSAGE, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(NoResourceFoundException.class)
    public ModelAndView handleNotFoundError(NoResourceFoundException ex) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("notfound"); // This is the name of the static page (HTML file)
        mav.setStatus(HttpStatus.OK);
        return mav;
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Map<String, Object>> handleForbiddenException(ForbiddenException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(CommonConstants.STATUS, CommonConstants.FAILED);
        response.put(CommonConstants.STATUS_CODE, 403);
        response.put(CommonConstants.MESSAGE, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequestException(BadRequestException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(CommonConstants.STATUS, CommonConstants.FAILED);
        response.put(CommonConstants.STATUS_CODE, 400);
        response.put(CommonConstants.MESSAGE, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
