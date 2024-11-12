package com.springboot_claseX_app.repositorios;

import com.springboot_claseX_app.entidades.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AutorRepositorio extends JpaRepository<Autor, UUID> {
    @Query("SELECT a FROM Autor a WHERE a.id = :id")
    Autor buscarPorId(@Param("id") UUID id);
}
