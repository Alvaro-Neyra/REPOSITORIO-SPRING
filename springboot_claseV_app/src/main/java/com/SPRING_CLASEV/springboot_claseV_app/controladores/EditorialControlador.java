package com.SPRING_CLASEIV.springboot_claseIV_app.controladores;

import com.SPRING_CLASEIV.springboot_claseIV_app.excepciones.MiException;
import com.SPRING_CLASEIV.springboot_claseIV_app.servicios.EditorialServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/editorial")
public class EditorialControlador {
    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/registrar")
    public String registrar() {
        return "editorial_form.html";
    }
    @PostMapping("/registro")
    public String registrar(@RequestParam String nombre, Model modelo) {
        try {
            editorialServicio.crearEditorial(nombre);
            modelo.addAttribute("exito", "Editorial registrado exitosamente");
        } catch (MiException e) {
            Logger.getLogger(EditorialControlador.class.getName()).log(Level.SEVERE, null, e);
            modelo.addAttribute("error", e.getMessage());
            return "editorial_form.html";
        }
        return "index.html";
    }
}
