package com.springboot_claseVIII_app.controladores;

import com.springboot_claseVIII_app.entidades.Usuario;
import com.springboot_claseVIII_app.enumeraciones.Rol;
import com.springboot_claseVIII_app.excepciones.MiException;
import com.springboot_claseVIII_app.servicios.UsuarioServicios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private UsuarioServicios usuarioServicio;

    @GetMapping("/dashboard")
    public String panelAdministrativo() {
        return "panel.html";
    }

    @GetMapping("/usuarios")
    public String listar(ModelMap modelo) {
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);
        return "usuarios_list";
    }

    @GetMapping("/modificarRol/{id}")
    public String cambiarRol(@PathVariable String id, ModelMap modelo) {
        usuarioServicio.cambiarRol(id);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/usuario/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {
        modelo.put("usuario", usuarioServicio.getOne(id));
        return "usuario_modificar";
    }

    @PostMapping("/usuario/modificar/{id}")
    public String modificar(@PathVariable String id, String nombre, String email, String password, String password2, Rol rol, ModelMap modelo) {
        try {
            usuarioServicio.modificarUsuario(id, nombre, email, password, password2, rol);
            modelo.addAttribute("exito", "Usuario modificado exitosamente");
            return "redirect:/admin/usuarios";
        } catch (MiException e) {
            modelo.put("error", e.getMessage());
            return "usuario_modificar";
        }
    }
}
