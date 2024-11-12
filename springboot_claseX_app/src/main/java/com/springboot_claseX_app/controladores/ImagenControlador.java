package com.springboot_claseX_app.controladores;

import com.springboot_claseX_app.entidades.Usuario;
import com.springboot_claseX_app.servicios.UsuarioServicios;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/imagen")
public class ImagenControlador {
    @Autowired
    UsuarioServicios usuarioServicio;

    @GetMapping("/perfil/{id}")
    public ResponseEntity<byte[]> imagenUsuario(@PathVariable  UUID id, HttpSession session) {
        Usuario usuario = usuarioServicio.getOne(id.toString());
        byte[] imagen = usuario.getImagen().getContenido();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(usuario.getImagen().getMime()));
        System.err.println("IMAGEN OBTENIDA: " + imagen);
        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }
}
