package com.desafio_integrador.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.desafio_integrador.entidades.Articulo;

@Repository
public interface ArticuloRepositorio extends JpaRepository<Articulo, String> {
    @Query("SELECT a FROM Articulo a WHERE a.idArticulo = :id")
    Articulo buscarPorIdArticulo(@Param("id") String id);
    @Query("SELECT a FROM Articulo a WHERE a.nombreArticulo = :nombre")
    Articulo buscarPorNombreArticulo(@Param("nombre") String nombreArticulo);
    @Query("SELECT MAX(a.nroArticulo) FROM Articulo a")
    Integer findMaxNroArticulo();
}