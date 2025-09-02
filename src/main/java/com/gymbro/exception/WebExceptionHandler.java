package com.gymbro.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Manipulador de exceções específico para controladores web.
 * Retorna páginas HTML de erro em vez de respostas JSON.
 */
@ControllerAdvice(basePackages = "com.gymbro.controller")
public class WebExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("error/404");
        modelAndView.addObject("error", "Recurso não encontrado");
        modelAndView.addObject("message", ex.getMessage());
        modelAndView.addObject("path", request.getRequestURI());
        return modelAndView;
    }

    @ExceptionHandler(BusinessException.class)
    public ModelAndView handleBusinessException(BusinessException ex, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("error/400");
        modelAndView.addObject("error", "Erro de regra de negócio");
        modelAndView.addObject("message", ex.getMessage());
        modelAndView.addObject("path", request.getRequestURI());
        return modelAndView;
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ModelAndView handleUnauthorizedException(UnauthorizedException ex, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("error/401");
        modelAndView.addObject("error", "Acesso não autorizado");
        modelAndView.addObject("message", ex.getMessage());
        modelAndView.addObject("path", request.getRequestURI());
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGlobalException(Exception ex, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("error/500");
        modelAndView.addObject("error", "Erro interno do servidor");
        modelAndView.addObject("message", "Ocorreu um erro inesperado. Tente novamente mais tarde.");
        modelAndView.addObject("path", request.getRequestURI());
        return modelAndView;
    }
}
