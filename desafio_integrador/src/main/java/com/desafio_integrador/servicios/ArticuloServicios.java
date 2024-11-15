package com.desafio_integrador.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desafio_integrador.entidades.Articulo;
import com.desafio_integrador.entidades.Fabrica;
import com.desafio_integrador.excepciones.MiException;
import com.desafio_integrador.repositorios.ArticuloRepositorio;
import com.desafio_integrador.repositorios.FabricaRepositorio;

import jakarta.annotation.PostConstruct;
@Service
public class ArticuloServicios {
    @Autowired
    private ArticuloRepositorio articuloRepositorio;
    @Autowired
    private FabricaRepositorio fabricaRepositorio;

    
    private AtomicInteger nroArticuloContador;


    @PostConstruct
    private void iniciarContador() {
        Integer maxNroArticulo = articuloRepositorio.findMaxNroArticulo();
        nroArticuloContador = new AtomicInteger((maxNroArticulo != null ? maxNroArticulo : 0) + 1);
    }
    
    @Transactional
    public void guardarArticulo(String nombreArticulo, String descripcionArticulo, String idFabrica) throws MiException{
        validar(nombreArticulo);
        validar(descripcionArticulo);
        validar(idFabrica);
        Optional<Fabrica> fabrica = fabricaRepositorio.findById(idFabrica);

        if (fabrica.isPresent()) {
            Fabrica nuevaFabrica = fabrica.get();
            Articulo articulo = new Articulo();
            articulo.setNombreArticulo(nombreArticulo);
            articulo.setDescripcionArticulo(descripcionArticulo);
            articulo.setNroArticulo(nroArticuloContador.getAndIncrement());
            articulo.setFabrica(nuevaFabrica);
            articuloRepositorio.save(articulo);
        }
    }
    @Transactional
    public Articulo buscarPorIdArticulo(String id) throws MiException {
        validar(id);
        return articuloRepositorio.buscarPorIdArticulo(id);
    }
    @Transactional
    public Articulo buscarPorNombreArticulo(String nombre) throws MiException{
        validar(nombre);
        return articuloRepositorio.buscarPorNombreArticulo(nombre);
    }

    @Transactional
    public void eliminarArticulo(String id) throws MiException {
        validar(id);
        articuloRepositorio.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Articulo> listarArticulos() {
        List<Articulo> articulos = new ArrayList<Articulo>();
        articulos = articuloRepositorio.findAll();
        return articulos;
    }

    @Transactional
    public void actualizarArticulo(String idArticulo, String nombreArticulo, String descripcionArticulo, String idFabrica) throws MiException{
        validar(idArticulo);
        validar(nombreArticulo);
        validar(descripcionArticulo);
        validar(idFabrica);
        Optional<Articulo> articulo = articuloRepositorio.findById(idArticulo);
        Optional<Fabrica> fabrica = fabricaRepositorio.findById(idFabrica);

        Fabrica nuevaFabrica = new Fabrica();


        if (fabrica.isPresent()) {
            nuevaFabrica = fabrica.get();
        }

        if (articulo.isPresent())  {
            Articulo articuloActualizado = articulo.get();
            articuloActualizado.setNombreArticulo(nombreArticulo);
            articuloActualizado.setDescripcionArticulo(descripcionArticulo);
            articuloActualizado.setFabrica(nuevaFabrica);
            articuloRepositorio.save(articuloActualizado);
        }
    }
    
    @Transactional(readOnly = true)
    public Articulo getOne(String longId) {
        return articuloRepositorio.getReferenceById(longId);
    }

    public void validar(String id) throws MiException {
        if (id == null) {
            throw new MiException("La cadena no puede ser nulo");
        }
    }

    public void validar(Integer numero) throws MiException {
        if (numero == null) {
            throw new MiException("El numero no puede ser nulo");
        }
    }
}
