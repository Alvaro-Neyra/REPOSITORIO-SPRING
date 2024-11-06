package com.springboot_claseVII_app.controladores;

import com.springboot_claseVII_app.servicios.UsuarioServicios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class PortalControlador {
    @Autowired
    private UsuarioServicios usuarioServicio;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/registrar")
    public String registrar() {
        return "registrar.html";
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, @RequestParam String email, @RequestParam String password, @RequestParam String password2, ModelMap modelo) {
        try {
            usuarioServicio.registrar(nombre, email, password, password2);
            modelo.addAttribute("exito", "Usuario registrado con exito");
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "registrar.html";
        }
        return "index.html";
    }
}
