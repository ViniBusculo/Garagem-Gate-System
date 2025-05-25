package com.exemplo.garage.controller;

import com.exemplo.garage.service.LogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/logs")
    public String listarLogs(Model model) {
        model.addAttribute("logs", logService.listarTodos());
        return "logs"; // Você pode criar logs.html para mostrar os registros
    }

    @GetMapping("/limpa-logs")
    public String limparLogs(Model model) {
        logService.limparTodosOsLogs();
        model.addAttribute("mensagem", "Todos os logs de acesso foram apagados.");
        return "index";
    }
}
