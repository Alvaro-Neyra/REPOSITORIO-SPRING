package com.springboot_claseX_app.controladores;

import com.springboot_claseX_app.entidades.Autor;
import com.springboot_claseX_app.excepciones.MiException;
import com.springboot_claseX_app.servicios.AutorServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
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
        return "inicio.html";
    }

    @GetMapping("/lista")
    public String listar(Model model) {
        List<Autor> autores = autorServicio.listarAutores();
        model.addAttribute( "autores", autores);
        return "autor_list.html";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable UUID id, ModelMap modelo) {
        modelo.put("autor", autorServicio.getOne(id));
        return "autor_modificar.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable UUID id, String nombre, ModelMap modelo) {
        try {
            autorServicio.modificarAutor(nombre, id);
            modelo.addAttribute("exito", "Autor actualizado exitosamente");
            return "redirect:../lista";
        } catch (MiException e) {
            modelo.put("error", e.getMessage());
            return "autor_modificar.html";
        }
    }
}
