package com.desafio_integrador.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.desafio_integrador.entidades.Fabrica;
import com.desafio_integrador.excepciones.MiException;
import com.desafio_integrador.repositorios.FabricaRepositorio;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FabricaServicios {

    @Autowired
    private FabricaRepositorio fabricaRepositorio;

    @Transactional
    public void crearFabrica(String nombre) throws MiException{
        validar(nombre);
        Fabrica fabrica = new Fabrica();
        fabrica.setNombreFabrica(nombre);
        fabricaRepositorio.save(fabrica);
    }

    @Transactional
    public void eliminarFabrica(String id) throws MiException {
        validar(id);
        fabricaRepositorio.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Fabrica> listarFabricas() {
        List<Fabrica> fabricas = fabricaRepositorio.findAll();
        return fabricas;
    }

    @Transactional
    public void actualizarFabrica(String id, String nombre) throws MiException {
        validar(id);
        validar(nombre);
        Optional<Fabrica> fabricaOptional = fabricaRepositorio.findById(id);
        if (fabricaOptional.isPresent()) {
            Fabrica fabrica = fabricaOptional.get();
            fabrica.setNombreFabrica(nombre);
            fabricaRepositorio.save(fabrica);
        }
    }

    @Transactional
    public Fabrica getOne(String id) {
        return fabricaRepositorio.getReferenceById(id);
    }

    public void validar(String nombre) throws MiException {
        if (nombre == null || nombre.isEmpty()) {
            throw new MiException("El nombre no puede ser nulo o vacio");
        }
    }
}
