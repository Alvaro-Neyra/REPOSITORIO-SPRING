package com.springboot_claseVII_app.controladores;

import com.springboot_claseVII_app.entidades.Autor;
import com.springboot_claseVII_app.entidades.Editorial;
import com.springboot_claseVII_app.entidades.Libro;
import com.springboot_claseVII_app.excepciones.MiException;
import com.springboot_claseVII_app.servicios.AutorServicio;
import com.springboot_claseVII_app.servicios.EditorialServicio;
import com.springboot_claseVII_app.servicios.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
        return "inicio.html";
    }

    @GetMapping("/lista")
    public String listar(Model model) {
        List<Libro> libros = libroServicio.listarLibros();
        model.addAttribute( "libros", libros);
        return "libro_list.html";
    }

    @GetMapping("/modificar/{isbn}")
    public String modificar(@PathVariable Long isbn, ModelMap modelo) {
        modelo.put("libro", libroServicio.getOne(isbn));

        List<Autor> autores = autorServicio.listarAutores();
        List<Editorial> editoriales = editorialServicio.listarEditoriales();

        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);
        return "libro_modificar.html";
    }

    @PostMapping("/modificar/{isbn}")
    public String modificar(@PathVariable Long isbn, String titulo, Integer ejemplares, UUID idAutor, UUID idEditorial, ModelMap modelo, Model model) {
        try {
            List<Autor> autores = autorServicio.listarAutores();
            List<Editorial> editoriales = editorialServicio.listarEditoriales();

            modelo.addAttribute("autores", autores);
            modelo.addAttribute("editoriales", editoriales);

            libroServicio.modificarLibro(titulo, ejemplares, idAutor, idEditorial, isbn);
            modelo.addAttribute("exito", "Libro modificado exitosamente");
            return "redirect:../lista";
        } catch (MiException e) {
            List<Autor> autores = autorServicio.listarAutores();
            List<Editorial> editoriales = editorialServicio.listarEditoriales();

            modelo.put("error", e.getMessage());

            modelo.addAttribute("autores", autores);
            modelo.addAttribute("editoriales", editoriales);
            return "libro_modificar.html";
        }
    }
}
