package com.SPRING_CLASEV.springboot_claseV_app.controladores;

import com.SPRING_CLASEV.springboot_claseV_app.excepciones.MiException;
import com.SPRING_CLASEV.springboot_claseV_app.servicios.AutorServicio;
import com.SPRING_CLASEV.springboot_claseV_app.servicios.EditorialServicio;
import com.SPRING_CLASEV.springboot_claseV_app.entidades.Autor;
import com.SPRING_CLASEV.springboot_claseV_app.entidades.Editorial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;

@Controller
@RequestMapping("/editorial")
public class EditorialControlador {
    @Autowired
    private EditorialServicio editorialServicio;
    @Autowired
    private AutorServicio autorServicio;

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

    @GetMapping("/lista")
    public String listar(Model model) {
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        model.addAttribute( "editoriales", editoriales);
        return "editorial_list.html";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable UUID id, ModelMap modelo) {
        modelo.put("editorial", editorialServicio.getOne(id));
        return "editorial_modificar.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable UUID id, String nombre, ModelMap modelo) {
        try {
            editorialServicio.modificarEditorial(nombre, id);
            modelo.addAttribute("exito", "Editorial actualizada exitosamente");
            return "redirect:../lista";
        } catch (MiException e) {
            modelo.put("error", e.getMessage());
            return "editorial_modificar.html";
        }
    }
}
