package com.springboot_claseX_app.controladores;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/* NO DETECTA CORRECTAMENTE EL STATUS_CODE DE HTTPSERVLETREQUEST, POR ELLO CREE UN CONTROLADOR PARA CADA UNO DE LAS EXCEPCIONES*/
@Controller
public class ErroresControlador implements ErrorController {
    @RequestMapping(value = "/error", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {
        ModelAndView errorPage = new ModelAndView("error");

        String errorMsg = "";

        int httpErrorCode = getErrorCode(httpRequest);

        switch(httpErrorCode) {
            case 400: {
                errorMsg = "El recurso solicitado no existe.";
                break;
            }
            case 403: {
                errorMsg = "No tiene permisos para acceder al recurso.";
                break;
            }
            case 401: {
                errorMsg = "No se encuentra autorizado.";
                break;
            }
            case 404: {
                errorMsg = "El recurso solicitado no fue encontrado";
                break;
            }
            case 500: {
                errorMsg = "El recurso solicitado no existe.";
            }
            default: {
                errorMsg = "Error desconocido.";
            }
        }
        errorPage.addObject("codigo", httpErrorCode);
        errorPage.addObject("mensaje", errorMsg);
        return errorPage;
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        Integer statusCode = (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
        System.out.println("Codigo de error detectado: " + statusCode);
        return (statusCode != null) ? statusCode : 500;
    }
}
