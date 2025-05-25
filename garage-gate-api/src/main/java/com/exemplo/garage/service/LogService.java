package com.exemplo.garage.service;

import com.exemplo.garage.model.LogAcesso;
import com.exemplo.garage.repository.LogAcessoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogService {

    private final LogAcessoRepository logRepository;

    public LogService(LogAcessoRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void registrarAcesso(String placa, boolean autorizado) {
        LogAcesso log = new LogAcesso();
        log.setPlacaDetectada(placa);
        log.setDataHora(LocalDateTime.now());
        log.setAutorizado(autorizado);
        logRepository.save(log);
    }

    public List<LogAcesso> listarTodos() {
        return logRepository.findAll();
    }

     public void limparTodosOsLogs() {
        logRepository.deleteAll();
    }
}
