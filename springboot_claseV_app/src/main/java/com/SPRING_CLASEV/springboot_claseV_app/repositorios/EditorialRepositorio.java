package com.SPRING_CLASEV.springboot_claseV_app.repositorios;

import com.SPRING_CLASEV.springboot_claseV_app.entidades.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EditorialRepositorio extends JpaRepository<Editorial, UUID>{
    @Query("SELECT e FROM Editorial e WHERE e.id = :id")
    public Editorial buscarPorId(@Param("id") UUID id);
}
