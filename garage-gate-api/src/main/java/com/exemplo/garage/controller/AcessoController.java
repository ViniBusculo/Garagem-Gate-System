package com.exemplo.garage.controller;

import com.exemplo.garage.service.LogService;
import com.exemplo.garage.service.PlacaService;
import com.exemplo.garage.storage.UltimoAcessoStorage;
import com.exemplo.garage.dto.AcessoDTO;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
public class AcessoController {

    private final PlacaService placaService;
    private final LogService logService;

    public AcessoController(PlacaService placaService, LogService logService) {
        this.placaService = placaService;
        this.logService = logService;
    }

    @GetMapping("/")
    public String telaInicial() {
        return "index"; // Vai mostrar index.html
    }

    @PostMapping("/verificar")
    public String verificarPlaca(@RequestParam String placa, Model model) {
        boolean autorizado = placaService.placaEstaAutorizada(placa.toUpperCase());

        logService.registrarAcesso(placa, autorizado);

        model.addAttribute("placa", placa);
        model.addAttribute("autorizado", autorizado);
        return "index";
    }

    @RestController
    @RequestMapping("/api/placas")
    public class PlacaApiController {

        @PostMapping("/verificar")
        public ResponseEntity<Map<String, Object>> verificarPlacaViaApi(@RequestBody Map<String, String> payload) {
            String placa = payload.get("placa");
            boolean autorizado = placaService.placaEstaAutorizada(placa.toUpperCase());

            logService.registrarAcesso(placa, autorizado);

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("placa", placa);
            resposta.put("autorizado", autorizado);

            return ResponseEntity.ok(resposta);
        }
    }

    @GetMapping("/acesso/teste-local")
    public String capturarPlacaLocal(Model model) {
        // Usa imagem salva localmente
        String placa = placaService.capturarEReconhecerPlaca(null);

        if (placa == null) {
            model.addAttribute("mensagem", "Não foi possível reconhecer a placa da imagem local.");
            return "index";
        }

        boolean autorizada = placaService.placaEstaAutorizada(placa);
        if (autorizada) {
            logService.registrarAcesso(placa, true);
            model.addAttribute("mensagem", "Acesso liberado para a placa: " + placa);
        } else {
            logService.registrarAcesso(placa, false);
            model.addAttribute("mensagem", "Placa NÃO autorizada (imagem local): " + placa);
        }

        return "index";
    }

    @PostMapping("/limpar-logs")
    public String limparLogs(Model model) {
        logService.limparTodosOsLogs();
        model.addAttribute("mensagem", "Todos os logs de acesso foram apagados.");
        return "index";
    }

    @GetMapping("/api/placa/verificar")
    public ResponseEntity<Map<String, Object>> verificarPlaca(@RequestParam String placa) {
        boolean autorizada = placaService.placaEstaAutorizada(placa);
        Map<String, Object> response = new HashMap<>();
        response.put("autorizada", autorizada);
        return ResponseEntity.ok(response);
    }

    @Autowired
    private UltimoAcessoStorage ultimoAcesso;

   @PostMapping("/api/placa/logar-acesso")
    public ResponseEntity<Void> logarAcesso(@RequestBody AcessoDTO acesso) {
        logService.registrarAcesso(acesso.getPlaca(), acesso.isAutorizado());
        ultimoAcesso.atualizar(acesso.getPlaca(), acesso.isAutorizado());
        return ResponseEntity.ok().build();
    }

}
