package com.springboot_claseVII_app.servicios;

import com.springboot_claseVII_app.entidades.Autor;
import com.springboot_claseVII_app.excepciones.MiException;
import com.springboot_claseVII_app.repositorios.AutorRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AutorServicio {
    @Autowired
    private AutorRepositorio autorRepositorio;

    @Transactional
    public void crearAutor(String nombre) throws MiException{
        validar(nombre);
        Autor autor = new Autor();
        autor.setNombre(nombre);
        autorRepositorio.save(autor);
    }

    @Transactional(readOnly = true)
    public List<Autor> listarAutores() {
        List<Autor> autores = new ArrayList<Autor>();
        autores = autorRepositorio.findAll();
        return autores;
    }

    @Transactional
    public void modificarAutor(String nombre, UUID id) throws MiException{
        validar(nombre);
        validar(id);
        Optional<Autor> autorRespuesta = autorRepositorio.findById(id);
        if (autorRespuesta.isPresent()) {
            Autor autor = autorRespuesta.get();
            autor.setNombre(nombre);
            autorRepositorio.save(autor);
        }
    }

    @Transactional(readOnly = true)
    public Autor getOne(UUID id) {
        return autorRepositorio.getReferenceById(id);
    }

    private void validar(String nombre) throws MiException {
        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El nombre no puede estar vacio");
        }
    }

    private void validar(UUID id) throws MiException {
        if (id == null) {
            throw new MiException("El id no puede estar vacio");
        }
    }
}
