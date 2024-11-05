package com.SPRING_CLASEV.springboot_claseV_app.repositorios;

import com.SPRING_CLASEV.springboot_claseV_app.entidades.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioLibro extends JpaRepository<Libro, Long> {
    @Query("SELECT l FROM Libro l WHERE l.titulo = :titulo")
    public Libro buscarPorTitulo(@Param("titulo") String titulo);

    @Query("SELECT l FROM Libro l WHERE l.autor.nombre = :nombreAutor")
    List<Libro> buscarPorAutor(@Param("nombreAutor") String nombreAutor);
}
