package com.desafio_integrador.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.desafio_integrador.entidades.Articulo;
import com.desafio_integrador.entidades.Fabrica;
import com.desafio_integrador.servicios.ArticuloServicios;
import com.desafio_integrador.servicios.FabricaServicios;

@Controller
@RequestMapping("/articulo")
public class ArticuloControlador {
    @Autowired
    private ArticuloServicios articuloServicios;

    @Autowired
    private FabricaServicios fabricaServicios;

    @GetMapping("/guardar")
    public String guardar(ModelMap modelo) {
        modelo.addAttribute("fabricas", fabricaServicios.listarFabricas());
        return "articulo_form.html";
    }

    @PostMapping("/guardar")
    public String registro(String nombreArticulo, String descripcionArticulo, @RequestParam(required = false) String idFabrica, ModelMap modelo) {
        try {
            articuloServicios.guardarArticulo(nombreArticulo, descripcionArticulo, idFabrica);
            System.out.println("Articulo guardado con exito 1");
            modelo.addAttribute("mensaje", "Articulo guardado con exito");
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            modelo.addAttribute("fabricas", fabricaServicios.listarFabricas());
            return "articulo_form.html";
        }
        return "inicio.html";
    }

    @GetMapping("/lista")
    public String lista(ModelMap modelo) {
        List<Articulo> articulos = articuloServicios.listarArticulos();
        modelo.addAttribute("articulos", articulos);
        return "articulo_list.html";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {
        try {

            modelo.put("articulo", articuloServicios.buscarPorIdArticulo(id));
            
            List<Fabrica> fabricas = fabricaServicios.listarFabricas();
            
            modelo.addAttribute("fabricas", fabricas);
            return "articulo_form.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "inicio.html";
        }
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, @RequestParam String nombreArticulo, @RequestParam String descripcionArticulo, @RequestParam String idFabrica, ModelMap modelo) {
        try {
            List<Fabrica> fabricas = fabricaServicios.listarFabricas();
            modelo.addAttribute("fabricas", fabricas);
            articuloServicios.actualizarArticulo(id, nombreArticulo, descripcionArticulo, idFabrica);
            modelo.addAttribute("exito", "Articulo modificado con exito");
            return "redirect:../lista";
        } catch (Exception e) {
            List<Fabrica> fabricas = fabricaServicios.listarFabricas();
            modelo.put("error", "Error al modificar el articulo " + e.getMessage());
            modelo.addAttribute("fabricas", fabricas);
            return "articulo_modificar.html";
        }
    }    
}
