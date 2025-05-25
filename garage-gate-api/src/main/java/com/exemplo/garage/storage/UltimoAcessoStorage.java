package com.exemplo.garage.storage;

import org.springframework.stereotype.Component;

@Component
public class UltimoAcessoStorage {
    private String placa;
    private boolean autorizado;

    public synchronized void atualizar(String placa, boolean autorizado) {
        this.placa = placa;
        this.autorizado = autorizado;
    }

    public synchronized String getPlaca() {
        return placa;
    }

    public synchronized boolean isAutorizado() {
        return autorizado;
    }
}
