package com.exemplo.garage.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class LogAcesso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placaDetectada;
    private LocalDateTime dataHora;
    private boolean autorizado;

    // Getters e setters
    public Long getId() {
        return id;
    }

    public String getPlacaDetectada() {
        return placaDetectada;
    }

    public void setPlacaDetectada(String placaDetectada) {
        this.placaDetectada = placaDetectada;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public boolean isAutorizado() {
        return autorizado;
    }

    public void setAutorizado(boolean autorizado) {
        this.autorizado = autorizado;
    }
}
