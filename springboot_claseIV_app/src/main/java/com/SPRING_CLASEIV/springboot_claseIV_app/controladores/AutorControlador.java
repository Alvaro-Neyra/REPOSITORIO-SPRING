package com.SPRING_CLASEIV.springboot_claseIV_app.controladores;

import com.SPRING_CLASEIV.springboot_claseIV_app.excepciones.MiException;
import com.SPRING_CLASEIV.springboot_claseIV_app.servicios.AutorServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/autor")
public class AutorControlador {
    @Autowired
    private AutorServicio autorServicio;

    // Obtener formulario autor
    @GetMapping("/registrar")
    public String registrar() {
        return "autor_form.html";
    }

    // Persistir (agregar) autor
    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, Model model) {
        try {
            autorServicio.crearAutor(nombre);
            model.addAttribute("exito", "Autor registrado exitosamente");
        } catch (MiException e) {
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, e);
            model.addAttribute("error", e.getMessage());
            return "autor_form.html";
        }
        return "index.html";
    }
}
