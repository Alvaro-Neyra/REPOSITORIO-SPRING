package com.desafio_integrador.controladores;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.desafio_integrador.servicios.FabricaServicios;


@Controller
@RequestMapping("/fabrica")
public class FabricaControlador {
    @Autowired
    private FabricaServicios fabricaServicios;

    @GetMapping("/guardar")
    public String guardar() {
        return "fabrica_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, ModelMap modelo) {
        try {
            fabricaServicios.crearFabrica(nombre);
            modelo.addAttribute("exito", "Fabrica guardada con exito");
        } catch (Exception e) {
            Logger.getLogger(FabricaControlador.class.getName()).log(Level.SEVERE, null, e);
            modelo.addAttribute("error", e.getMessage());
            return "fabrica_form.html";
        }
        return "inicio.html";
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        modelo.addAttribute("fabricas", fabricaServicios.listarFabricas());
        return "fabrica_list.html";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {
        modelo.put("fabrica", fabricaServicios.getOne(id));
        return "fabrica_modificar.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, @RequestParam String nombre, ModelMap modelo) {
        try {
            fabricaServicios.actualizarFabrica(id, nombre);
            modelo.addAttribute("exito", "Fabrica modificada con exito");
            return "redirect:../lista";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "fabrica_modificar.html";
        }
    }
}
