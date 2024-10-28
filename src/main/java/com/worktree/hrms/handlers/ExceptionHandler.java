package com.worktree.hrms.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ExceptionHandler {


    @org.springframework.web.bind.annotation.ExceptionHandler(NoResourceFoundException.class)
    public ModelAndView handleNotFoundError(NoResourceFoundException ex) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("notfound"); // This is the name of the static page (HTML file)
        mav.setStatus(HttpStatus.OK);
        return mav;
    }


}
