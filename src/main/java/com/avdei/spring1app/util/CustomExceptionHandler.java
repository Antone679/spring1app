package com.avdei.spring1app.util;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

//@ControllerAdvice
//public class CustomExceptionHandler {
//    @ExceptionHandler(NoHandlerFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
//    public ModelAndView handleNotFound(NoHandlerFoundException ex, Model model) {
//        ModelAndView modelAndView = new ModelAndView("error");
//        model.addAttribute("code", HttpStatus.NOT_FOUND.value());
//        model.addAttribute("message", "страница не найдена");
//        return modelAndView;
//    }
//
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
//    public ModelAndView handleServerError(Exception ex, Model model) {
//        ModelAndView modelAndView = new ModelAndView("error");
//        model.addAttribute("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
//        model.addAttribute("message", "внутренняя ошибка сервера");
//        return modelAndView;
//    }
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
//    public ModelAndView handleBadRequest(MethodArgumentNotValidException ex, Model model) {
//        ModelAndView modelAndView = new ModelAndView("error");
//        model.addAttribute("code", HttpStatus.BAD_REQUEST.value());
//        model.addAttribute("message", "неверный запрос: " + ex.getMessage());
//        return modelAndView;
//    }
//
//}
