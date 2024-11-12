package com.springboot_claseIX_app.entidades;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id_libro")
    private Long isbn;
    @Column(name="titulo")
    private String titulo;
    @Column(name="ejemplares")
    private Integer ejemplares;

    @Temporal(TemporalType.DATE)
    @Column(name="alta")
    private Date alta;

    @ManyToOne
    @JoinColumn(name = "id_autor", referencedColumnName = "id")
    private Autor autor;

    @ManyToOne
    @JoinColumn(name = "id_editorial", referencedColumnName = "id")
    private Editorial editorial;

    public Libro() {

    }

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getEjemplares() {
        return ejemplares;
    }

    public void setEjemplares(Integer ejemplares) {
        this.ejemplares = ejemplares;
    }

    public Date getAlta() {
        return alta;
    }

    public void setAlta(Date alta) {
        this.alta = alta;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Editorial getEditorial() {
        return editorial;
    }

    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }
}
