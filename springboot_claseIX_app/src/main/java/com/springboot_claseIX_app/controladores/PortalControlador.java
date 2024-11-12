package com.springboot_claseIX_app.controladores;

import com.springboot_claseIX_app.entidades.Usuario;
import com.springboot_claseIX_app.enumeraciones.Rol;
import com.springboot_claseIX_app.excepciones.MiException;
import com.springboot_claseIX_app.servicios.UsuarioServicios;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

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
        Usuario usuario = usuarioServicio.getUsuarioAutenticado();
        session.setAttribute("usuario", usuario);
        System.out.println("Usuario autenticado:" + usuario.toString());
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
                           ModelMap modelo, MultipartFile archivo) {
        try {
            usuarioServicio.registrar(nombre, email, password, password2, archivo);
            modelo.put("exito", "¡Usuario registrado exitosamente!");
            return "login.html";
        } catch (MiException e) {
            System.out.println("Error al registrar un nuevo usuario: " + e.getMessage());
            modelo.put("error", e.getMessage());
            return "registrar.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session){
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            if (usuario != null) {
                System.out.println("Usuario obtenido por session!");
            } else {
                System.out.println("Usuario no encontrado!");
            }
            modelo.put("usuario", usuario);
            return "usuario_modificar_user.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            System.out.println("Error al obtener el usuario: " + e.getMessage());
        }
        return "inicio.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/perfil/{id}")
    public String actualizar(MultipartFile archivo, @PathVariable String id, @RequestParam String nombre, @RequestParam String email, @RequestParam String password,
                             @RequestParam String password2, ModelMap modelo, HttpSession session) {
        try {
            Usuario usuarioActual = usuarioServicio.getOne(id);
            usuarioServicio.modificarUsuario(id, nombre, email, password, password2, usuarioActual.getRol(), archivo);
            modelo.put("exito", "Usuario actualizado exitosamente!");

            session.setAttribute("usuariosession", usuarioServicio.getOne(id));
            return "inicio.html";
        } catch (MiException e) {
            modelo.put("error", e.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "usuario_modificar_user.html";
        }
    }
}