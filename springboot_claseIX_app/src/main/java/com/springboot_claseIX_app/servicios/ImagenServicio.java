package com.springboot_claseIX_app.servicios;

import com.springboot_claseIX_app.entidades.Imagen;
import com.springboot_claseIX_app.excepciones.MiException;
import com.springboot_claseIX_app.repositorios.ImagenRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Service
public class ImagenServicio {
    @Autowired
    private ImagenRepositorio imagenRepositorio;

    public Imagen guardarImagen(MultipartFile archivo) throws MiException {
        if (archivo != null) {
            try {
                Imagen imagen = new Imagen();
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                return imagenRepositorio.save(imagen);
            } catch (Exception e) {
                System.err.println("Error al guardar una imagen: " + e.getMessage());
            }
        }
        return null;
    }

    public Imagen actualizar(MultipartFile archivo, UUID idImagen) {
        if (archivo != null) {
            try {
                Imagen imagen = new Imagen();

                if (idImagen != null) {
                    Optional<Imagen> respuesta = imagenRepositorio.findById(idImagen);

                    if (respuesta.isPresent()) {
                        imagen = respuesta.get();
                    }
                }

                imagen.setMime((archivo.getContentType()));
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                return imagenRepositorio.save(imagen);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }
}
