package com.springboot_claseX_app.repositorios;

import com.springboot_claseX_app.entidades.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ImagenRepositorio extends JpaRepository<Imagen, UUID> {

}
