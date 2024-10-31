package com.egg.biblioteca.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.excepciones.MiException;
import com.egg.biblioteca.repositorios.AutorRepositorio;

@Service
public class AutorServicio {
    @Autowired
    AutorRepositorio autorRepositorio;

    @Transactional
    public void crearAutor(String nombre) throws MiException {
        validar(nombre);
        Autor autor = new Autor();// Instancio un objeto del tipo Autor
        autor.setNombre(nombre);// Seteo el atributo, con el valor recibido como parámetro

        autorRepositorio.save(autor); // Persisto el dato en mi BBDD
    }

    // Metodo para recuperar una "lista de autores"
    @Transactional(readOnly = true)
    public List<Autor> listarAutores() {

        List<Autor> autores = new ArrayList<>();
        autores = autorRepositorio.findAll();
        return autores;
    }

    // Metodo para modificar un autor
    @Transactional
    public void modificarAutor(String nombre, String id) throws MiException {
        validar(nombre);
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) { // Si encuentra el objeto por id
            Autor autor = respuesta.get();           
            autor.setNombre(nombre);//Seteo la informacion con el dato recibido 
            autorRepositorio.save(autor);
        }
    }

    private void validar(String nombre) throws MiException {
        
        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("el nombre no puede ser nulo o estar vacio");
        }
    }
    
}
