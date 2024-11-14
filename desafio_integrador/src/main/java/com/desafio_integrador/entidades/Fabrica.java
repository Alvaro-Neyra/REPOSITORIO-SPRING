package com.desafio_integrador.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Fabrica {
    
    @Id
    @GeneratedValue(generator = "uuid")
    private String idFabrica;
    private String nombreFabrica;

    public Fabrica() {

    }
    
    public String getIdFabrica() {
        return idFabrica;
    }
    public void setIdFabrica(String idFabrica) {
        this.idFabrica = idFabrica;
    }
    public String getNombreFabrica() {
        return nombreFabrica;
    }
    public void setNombreFabrica(String nombreFabrica) {
        this.nombreFabrica = nombreFabrica;
    }

    @Override
    public String toString() {
        return "Fabrica [idFabrica=" + idFabrica + ", nombreFabrica=" + nombreFabrica + "]";
    }
}
