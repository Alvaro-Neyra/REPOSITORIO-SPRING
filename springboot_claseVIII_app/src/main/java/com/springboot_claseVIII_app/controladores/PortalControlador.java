package com.springboot_claseVIII_app.controladores;

import com.springboot_claseVIII_app.entidades.Usuario;
import com.springboot_claseVIII_app.excepciones.MiException;
import com.springboot_claseVIII_app.servicios.UsuarioServicios;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        System.out.println(usuario.toString());
        if (usuario.getRol().toString().equals("ADMIN")) {
            System.out.println("redireccionando....");
            return "redirect:/admin/dashboard";
        }
        return "inicio.html";
    }

    @GetMapping("/registrar")
    public String registrar() {
        return "registrar.html";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "Usuario o Contraseña inválidos!");
        }
        return "login.html";
    }


    @PostMapping("/registro")
    public String registro(@RequestParam String nombre,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String password2,
                           ModelMap modelo) {
        try {
            usuarioServicio.registrar(nombre, email, password, password2);
            modelo.put("exito", "¡Usuario registrado exitosamente!");
            return "login.html";
        } catch (MiException e) {
            modelo.put("error", e.getMessage());
            return "registrar.html";
        }
    }
}