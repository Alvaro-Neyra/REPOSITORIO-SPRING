package com.desafio_integrador.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.desafio_integrador.entidades.Fabrica;

@Repository
public interface FabricaRepositorio extends JpaRepository<Fabrica, String> {
    @Query("SELECT f FROM Fabrica f WHERE f.idFabrica = :id")
    Fabrica buscarPorIdFabrica(@Param("id") String id);
    @Query("SELECT f FROM Fabrica f WHERE f.nombreFabrica = :nombre")
    Fabrica buscarPorNombreFabrica(@Param("nombre") String nombre);
}