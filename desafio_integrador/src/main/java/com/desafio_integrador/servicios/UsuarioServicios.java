package com.desafio_integrador.servicios;

import java.util.EnumSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.desafio_integrador.entidades.Usuario;
import com.desafio_integrador.enumeraciones.Rol;
import com.desafio_integrador.excepciones.MiException;
import com.desafio_integrador.repositorios.UsuarioRepositorio;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.servlet.ServletRequestAttributeEvent;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.ArrayList;

@Service
public class UsuarioServicios implements UserDetailsService {
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public void registrar(String nombre, String username, String password, String password2) throws MiException {
        validar(nombre, username, password, password2);
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setUsername(username);
        nuevoUsuario.setPassword(new BCryptPasswordEncoder().encode(password));
        nuevoUsuario.setRol(Rol.USER);
        usuarioRepositorio.save(nuevoUsuario);
        System.out.println("Usuario creado");
    }

    @Transactional
    public Usuario buscarPorUsername(String username) throws MiException {
        validar(username);
        return usuarioRepositorio.buscarPorUsername(username);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {
        List<Usuario> usuarios = new ArrayList<Usuario>();
        usuarios = usuarioRepositorio.findAll();
        return usuarios;
    }

    @Transactional
    public void modificarUsuario(String id, String nombre, String username, String lastName, String password, String password2, Rol rol) throws MiException {
        validar(nombre, username, password, password2);
        validar(rol);
        Optional<Usuario> usuario = usuarioRepositorio.findById(id);
        if (usuario.isPresent()) {
            Usuario usuarioAModificar = usuario.get();
            usuarioAModificar.setNombre(nombre);
            usuarioAModificar.setUsername(username);
            usuarioAModificar.setApellido(lastName);
            usuarioAModificar.setPassword(new BCryptPasswordEncoder().encode(password));
            usuarioAModificar.setRol(rol);
            usuarioRepositorio.save(usuarioAModificar);
        }
    }

    public Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            return usuarioRepositorio.buscarPorUsername(username);
        }
        return null;
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

    @Transactional
    public Usuario getOne(String id) {
        return usuarioRepositorio.getReferenceById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorUsername(username);
        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_"+ usuario.getRol().toString());
            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getUsername(), usuario.getPassword(), permisos);
        }else{
            return (UserDetails) new UsernameNotFoundException("Usuario no encontrado");
        }
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
