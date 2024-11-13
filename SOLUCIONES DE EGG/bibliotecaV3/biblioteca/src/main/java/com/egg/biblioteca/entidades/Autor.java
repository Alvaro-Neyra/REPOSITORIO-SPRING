package com.egg.biblioteca.entidades;



import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Autor {
    @Id
    @GeneratedValue(generator = "uuid")
    //Se sigue usando pero ya sugieren reemplazar esta estrategia.
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    //Otra opcion para generar mas reciente
    

    private String nombre;


}
