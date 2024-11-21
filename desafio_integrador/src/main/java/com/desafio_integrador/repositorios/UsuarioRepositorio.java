package com.desafio_integrador.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.desafio_integrador.entidades.Usuario;


@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {
    @Query("SELECT u FROM Usuario u WHERE u.idUsuario = :id")
    Usuario buscarPorId(String id);
    @Query("SELECT u FROM Usuario u WHERE u.username = :username")
    Usuario buscarPorUsername(String username);
}
