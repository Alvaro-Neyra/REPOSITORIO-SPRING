package com.springboot_claseX_app.controladores;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleNotFound(NoHandlerFoundException ex) {
        ModelAndView errorPage = new ModelAndView("error");
        errorPage.addObject("codigo", 404);
        errorPage.addObject("mensaje", "El recurso solicitado no fue encontrado.");
        return errorPage;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleForbidden(AccessDeniedException ex) {
        ModelAndView errorPage = new ModelAndView("error");
        errorPage.addObject("codigo", 403);
        errorPage.addObject("mensaje", "No tiene permisos para acceder al recurso.");
        return errorPage;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleBadRequest(IllegalArgumentException ex) {
        ModelAndView errorPage = new ModelAndView("error");
        errorPage.addObject("codigo", 400);
        errorPage.addObject("mensaje", "El recurso solicitado no existe.");
        return errorPage;
    }

    @ExceptionHandler(SecurityException.class)
    public ModelAndView handleUnauthorized(SecurityException ex) {
        ModelAndView errorPage = new ModelAndView("error");
        errorPage.addObject("codigo", 401);
        errorPage.addObject("mensaje", "No se encuentra autorizado.");
        return errorPage;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneralError(Exception ex) {
        ModelAndView errorPage = new ModelAndView("error");
        errorPage.addObject("codigo", 500);
        errorPage.addObject("mensaje", "Error interno del servidor.");
        return errorPage;
    }
}