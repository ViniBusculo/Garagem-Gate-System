package com.exemplo.garage.controller;

import com.exemplo.garage.model.PlacaAutorizada;
import com.exemplo.garage.service.PlacaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/placas")
public class PlacaController {

    private final PlacaService placaService;

    public PlacaController(PlacaService placaService) {
        this.placaService = placaService;
    }

    @GetMapping
    public String listarPlacas(Model model) {
        model.addAttribute("placas", placaService.listarTodas());
        return "placas"; // Vai renderizar placas.html
    }

    @PostMapping
    public String salvarPlaca(@RequestParam String placa, @RequestParam String dono) {
        PlacaAutorizada nova = new PlacaAutorizada();
        nova.setPlaca(placa.toUpperCase());
        nova.setDono(dono);
        placaService.salvar(nova);
        return "redirect:/placas";
    }
}
