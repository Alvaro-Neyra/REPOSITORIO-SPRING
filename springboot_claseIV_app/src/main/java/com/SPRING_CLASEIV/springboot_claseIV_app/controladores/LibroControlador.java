package com.SPRING_CLASEIV.springboot_claseIV_app.controladores;

import com.SPRING_CLASEIV.springboot_claseIV_app.excepciones.MiException;
import com.SPRING_CLASEIV.springboot_claseIV_app.servicios.AutorServicio;
import com.SPRING_CLASEIV.springboot_claseIV_app.servicios.LibroServicio;
import com.SPRING_CLASEIV.springboot_claseIV_app.servicios.EditorialServicio;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/libro")
public class LibroControlador {
    @Autowired
    private LibroServicio libroServicio;
    @Autowired
    private AutorServicio autorServicio;
    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/registrar")
    public String registrar(ModelMap modelo) {
        modelo.addAttribute("autores", autorServicio.listarAutores());
        modelo.addAttribute("editoriales", editorialServicio.listarEditoriales());
        return "libro_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam(required = false) Long isbn, @RequestParam String titulo,
                           @RequestParam(required = false) Integer ejemplares, @RequestParam(required = false) UUID idAutor,
                           @RequestParam(required = false) UUID idEditorial, ModelMap modelo) {
        try {
            libroServicio.crearLibro(titulo, ejemplares, idAutor, idEditorial);
            modelo.addAttribute("exito", "Libro registrado exitosamente");
        } catch (MiException e) {
            modelo.addAttribute("error", "Error al registrar el libro: " + e.getMessage());
            modelo.addAttribute("autores", autorServicio.listarAutores());
            modelo.addAttribute("editoriales", editorialServicio.listarEditoriales());
            return "libro_form.html";
        }
        return "index.html";
    }
}
