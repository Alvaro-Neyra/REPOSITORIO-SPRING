package com.SPRING_CLASEV.springboot_claseV_app.servicios;

import com.SPRING_CLASEV.springboot_claseV_app.entidades.Autor;
import com.SPRING_CLASEV.springboot_claseV_app.entidades.Editorial;
import com.SPRING_CLASEV.springboot_claseV_app.entidades.Libro;
import com.SPRING_CLASEV.springboot_claseV_app.excepciones.MiException;
import com.SPRING_CLASEV.springboot_claseV_app.repositorios.AutorRepositorio;
import com.SPRING_CLASEV.springboot_claseV_app.repositorios.EditorialRepositorio;
import com.SPRING_CLASEV.springboot_claseV_app.repositorios.RepositorioLibro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LibroServicio {
    @Autowired
    private RepositorioLibro repositorioLibro;
    @Autowired
    private AutorRepositorio autorRepositorio;
    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Transactional
    public void crearLibro(String titulo, Integer ejemplares, UUID idAutor, UUID idEditorial) throws MiException {
        validar(titulo);
        validar(ejemplares);
        validar(idAutor);
        validar(idEditorial);

        Optional<Autor> autor = autorRepositorio.findById(idAutor);
        Optional<Editorial> editorial = editorialRepositorio.findById(idEditorial);

        if (autor.isPresent() && editorial.isPresent()) {

            Libro libro = new Libro();
            libro.setTitulo(titulo);
            libro.setEjemplares(ejemplares);
            libro.setAlta(new Date(System.currentTimeMillis()));
            libro.setAutor(autor.get());
            libro.setEditorial(editorial.get());

            repositorioLibro.save(libro);
        }
    }

    @Transactional(readOnly = true)
    public List<Libro> listarLibros() {
        List<Libro> libros = new ArrayList<>();

        libros = repositorioLibro.findAll();
        return libros;
    }

    @Transactional
    public void modificarLibro(String titulo, Integer ejemplares, UUID idAutor, UUID idEditorial, Long isbn) throws MiException {
        validar(titulo);
        validar(ejemplares);
        validar(idAutor);
        validar(idEditorial);
        validar(isbn);
        Optional<Libro> respuesta = repositorioLibro.findById(isbn);
        Optional<Autor> respuestaAutor = autorRepositorio.findById(idAutor);
        Optional<Editorial> respuestaEditorial = editorialRepositorio.findById(idEditorial);

        Autor autor = new Autor();
        Editorial editorial = new Editorial();

        if (respuestaAutor.isPresent()) {
            autor = respuestaAutor.get();
        }

        if (respuestaEditorial.isPresent()) {
            editorial = respuestaEditorial.get();
        }

        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setTitulo(titulo);
            libro.setEjemplares(ejemplares);
            libro.setAutor(autor);
            libro.setEditorial(editorial);
        }
    }

    @Transactional(readOnly = true)
    public Libro getOne(Long id) {
        return repositorioLibro.getReferenceById(id);
    }

    private void validar(Long id) throws MiException {
        if (id == null) {
            throw new MiException("El id no puede ser nulo");
        }
    }

    private void validar(UUID numeroUUID) throws MiException{
        if (numeroUUID == null) {
            throw new MiException("El UUID no puede ser nulo");
        }
    }

    private void validar(String nombre) throws MiException {
        if (nombre == null || nombre.isEmpty()) {
            throw new MiException("El nombre no puede ser nulo");
        }
    }

    private void validar(Integer numero) throws MiException {
        if (numero <= 0 || numero == null) {
            throw new MiException("El numero no puede ser negativo o igual a cero (o nulo)");
        }
    }

    private void validar(Libro libro) throws MiException {
        if (libro.getTitulo() == null || libro.getTitulo().trim().equals("")) {
            throw new MiException("El titulo no puede estar vacio");
        }
        if (libro.getEjemplares() == null || libro.getEjemplares() <= 0) {
            throw new MiException("El ejemplares no puede estar vacio");
        }

        if (libro.getAlta() == null) {
            throw new MiException("El alto no puede estar vacio");
        }

        if (libro.getAutor() == null) {
            throw new MiException("El autor no puede estar vacio");
        }

        if (libro.getEditorial() == null) {
            throw new MiException("El editorial no puede estar vacio");
        }
    }
}
