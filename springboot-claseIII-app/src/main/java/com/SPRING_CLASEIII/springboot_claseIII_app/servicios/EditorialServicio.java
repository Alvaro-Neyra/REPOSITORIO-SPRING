package com.SPRING_CLASEIII.springboot_claseIII_app.servicios;

import com.SPRING_CLASEIII.springboot_claseIII_app.entidades.Editorial;
import com.SPRING_CLASEIII.springboot_claseIII_app.excepciones.MiException;
import com.SPRING_CLASEIII.springboot_claseIII_app.repositorios.EditorialRepositorio;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
