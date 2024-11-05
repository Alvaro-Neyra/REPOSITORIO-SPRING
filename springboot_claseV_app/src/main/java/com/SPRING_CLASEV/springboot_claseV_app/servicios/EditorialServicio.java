package com.SPRING_CLASEV.springboot_claseV_app.servicios;

import com.SPRING_CLASEV.springboot_claseV_app.entidades.Editorial;
import com.SPRING_CLASEV.springboot_claseV_app.excepciones.MiException;
import com.SPRING_CLASEV.springboot_claseV_app.repositorios.EditorialRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EditorialServicio {
    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Transactional
    public void crearEditorial(String nombre) throws MiException {
        validar(nombre);
        Editorial editorial = new Editorial();
        editorial.setNombre(nombre);
        editorialRepositorio.save(editorial);
    }

    @Transactional(readOnly = true)
    public List<Editorial> listarEditoriales() {
        List<Editorial> editorialLista = new ArrayList<>();
        editorialLista = editorialRepositorio.findAll();
        return editorialLista;
    }

    @Transactional
    public void modificarEditorial(String nombre, UUID idEditorial) throws MiException {
        validar(nombre);
        validar(idEditorial);
        Optional<Editorial> editorial = editorialRepositorio.findById(idEditorial);
        if (editorial.isPresent()) {
            Editorial editorialActual = editorial.get();
            editorialActual.setNombre(nombre);
            editorialRepositorio.save(editorialActual);
        }
    }

    @Transactional(readOnly = true)
    public Editorial getOne(UUID idEditorial) {
        return editorialRepositorio.getReferenceById(idEditorial);
    }

    public void validar(UUID idEditorial) throws MiException {
        if (idEditorial == null) {
            throw new MiException("El id del editorial no puede ser nulo");
        }
    }
    public void validar(String nombre) throws MiException {
        if (nombre == null || nombre.isEmpty()) {
            throw new MiException("El nombre es obligatorio");
        }
    }
}
