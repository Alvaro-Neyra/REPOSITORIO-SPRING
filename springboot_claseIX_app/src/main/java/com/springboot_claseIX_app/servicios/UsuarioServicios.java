package com.springboot_claseIX_app.servicios;

import com.springboot_claseIX_app.entidades.Imagen;
import com.springboot_claseIX_app.entidades.Usuario;
import com.springboot_claseIX_app.enumeraciones.Rol;
import com.springboot_claseIX_app.excepciones.MiException;
import com.springboot_claseIX_app.repositorios.UsuarioRepositorio;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class UsuarioServicios implements UserDetailsService {
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void registrar(String nombre, String email, String password, String password2, MultipartFile archivo) throws MiException {
        validar(nombre, email, password, password2);
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(new BCryptPasswordEncoder().encode(password));
        nuevoUsuario.setRol(Rol.USER);
        Imagen imagen = imagenServicio.guardarImagen(archivo);
        nuevoUsuario.setImagen(imagen);
        usuarioRepositorio.save(nuevoUsuario);
        System.out.println("Usuario creado");
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorEmail(String email) throws MiException{
        validar(email);
        return usuarioRepositorio.buscarPorEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);
        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_"+ usuario.getRol().toString());
            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        }else{
            return (UserDetails) new UsernameNotFoundException("Usuario no encontrado");
        }
    }

    @Transactional
    public List<Usuario> listarUsuarios(){
        List<Usuario> usuarios = usuarioRepositorio.findAll();
        return usuarios;
    }

    @Transactional
    public void modificarUsuario(String id, String nombre, String email, String password, String password2, Rol rol, MultipartFile archivo) throws MiException {
        validar(nombre, email, password, password2);
        validar(rol);
        validar(archivo);
        Optional<Usuario> usuarioEncontrado = usuarioRepositorio.findById(id);
        if (usuarioEncontrado.isPresent()) {
            usuarioEncontrado.get().setNombre(nombre);
            usuarioEncontrado.get().setEmail(email);
            usuarioEncontrado.get().setPassword(new BCryptPasswordEncoder().encode(password));
            usuarioEncontrado.get().setRol(rol);
            Imagen imagen = null;
            UUID idImagen = null;

             if (usuarioEncontrado.get().getImagen() != null) {
                idImagen = usuarioEncontrado.get().getImagen().getId();
                imagen = imagenServicio.actualizar(archivo, idImagen);
            } else {
                imagen = imagenServicio.guardarImagen(archivo);
                idImagen = imagen.getId();
            }


            usuarioEncontrado.get().setImagen(imagen);

            usuarioRepositorio.save(usuarioEncontrado.get());
        }
    }

    @Transactional
    public void cambiarRol(String id) {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();

            if (usuario.getRol().equals(Rol.USER)) {
                usuario.setRol(Rol.ADMIN);
            } else if (usuario.getRol().equals(Rol.ADMIN)) {
                usuario.setRol(Rol.USER);
            }
        }
    }

    public Usuario getUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserDetails) {
            String email = ((UserDetails) auth.getPrincipal()).getUsername();
            return usuarioRepositorio.buscarPorEmail(email);
        }
        return null;
    }

    @Transactional
    public Usuario getOne(String id) {
        return usuarioRepositorio.getReferenceById(id);
    }

    private void validar(MultipartFile archivo) throws MiException {
        if (archivo == null || archivo.isEmpty()) {
            throw new MiException("El archivo de imagen no puede estar vacío");
        }
    }

    private void validar(String email) throws MiException {
        if (email.isEmpty() || email == null) {
            throw new MiException("El email no puede estar vacio");
        }
    }

    private void validar(Rol rol) throws MiException {
        if (rol == null || !EnumSet.allOf(Rol.class).contains(rol)) {
            throw new MiException("El rol es inválido o no pertenece a los valores permitidos");
        }
    }

    private void validar(String nombre, String email, String password, String password2) throws MiException {
        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("el nombre no puede ser nulo o estar vacío");
        }
        if (email.isEmpty() || email == null) {
            throw new MiException("el email no puede ser nulo o estar vacío");
        }
        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiException("La contraseña no puede estar vacía, y debe tener más de 5 dígitos");
        }
        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas ingresadas deben ser iguales");
        }
    }

}
