package com.desafio_integrador.controladores;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.desafio_integrador.entidades.Usuario;
import com.desafio_integrador.enumeraciones.Rol;
import com.desafio_integrador.excepciones.MiException;
import com.desafio_integrador.servicios.UsuarioServicios;

import jakarta.servlet.http.HttpSession;

@Controller
public class PortalControlador {
    @Autowired
    private UsuarioServicios usuarioServicios;


    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session) {
        Usuario usuario = usuarioServicios.getUsuarioAutenticado();
        session.setAttribute("usuario", usuario);
        System.out.println("Usuario autenticado: " + usuario.toString());
        if (usuario.getRol().equals("ADMIN")) {
            System.out.println("redireccionando.....");
            return "redirect:/admin/dashboard";
        } else {
            return "inicio.html";
        }
    }

    @GetMapping("/registrar")
    public String registrar() {
        return "registrar.html";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "Usuario o contraseña incorrectos");
        }
        return "login.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre,
                        @RequestParam String username,
                        @RequestParam String password,
                        @RequestParam String password2,
                        ModelMap modelo) {
        try {
            usuarioServicios.registrar(nombre, username, password, password2);
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
    public String actualizar(@PathVariable String id, @RequestParam String nombre, @RequestParam String username, @RequestParam String password,
                            @RequestParam String password2, ModelMap modelo, HttpSession session) {
        try {
            Usuario usuarioActual = usuarioServicios.getOne(id);
            usuarioServicios.modificarUsuario(id, nombre, username, password, password2, usuarioActual.getRol());
            modelo.put("exito", "Usuario actualizado exitosamente!");

            session.setAttribute("usuariosession", usuarioServicios.getOne(id));
            return "inicio.html";
        } catch (MiException e) {
            modelo.put("error", e.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("username", username);
            return "usuario_modificar_user.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/usuarios")
    public String listarUsuarios(ModelMap modelo) {
        try {
            modelo.put("usuarios", usuarioServicios.listarUsuarios());
            return "usuarios_list.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "inicio.html";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/usuario/modificar/{id}")
    public String modificarUsuarioGet(@PathVariable String id, ModelMap modelo) {
        try {
            List<Rol> roles = Arrays.asList(Rol.values());
            Usuario usuarioAModificar = usuarioServicios.getOne(id);
            modelo.put("usuario", usuarioAModificar);
            modelo.addAttribute("roles", roles);
            return "usuario_modificar.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "usuario_list.html";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/usuario/modificar/{id}")
    public String modificarUsuario(@PathVariable String id, ModelMap modelo) {
        try {
            Usuario usuarioAModificar = usuarioServicios.getOne(id);
            System.out.println("Usuario a modificar: " + usuarioAModificar.toString());
            usuarioServicios.modificarUsuario(id, usuarioAModificar.getNombre(), usuarioAModificar.getUsername(), usuarioAModificar.getApellido(), usuarioAModificar.getPassword(), usuarioAModificar.getPassword(), usuarioAModificar.getRol());
            System.out.println("Usuario a modificar, exitosamente: " + usuarioAModificar.toString());
            modelo.put("usuario", usuarioAModificar);
            modelo.addAttribute("exito", "Usuario modificado exitosamente!");
            return "redirect:/usuarios";
        } catch (MiException e) {
            modelo.put("error", e.getMessage());
            return "usuario_list.html";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/usuario/modificarRol/{id}")
    public String modificarRolGet(@PathVariable String id, ModelMap modelo) {
        try {
            usuarioServicios.cambiarRol(id);
            modelo.put("exito", "Rol modificado exitosamente!");
            modelo.addAttribute("usuarios", usuarioServicios.listarUsuarios());
            return "redirect:/usuarios";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "usuario_list.html";
        }
    }

}
