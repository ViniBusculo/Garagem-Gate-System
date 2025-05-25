package com.exemplo.garage.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class PlacaAutorizada {

    @Id
    private String placa; // Ex: "ABC1234"
    private String dono;

    // Getters e setters
    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getDono() {
        return dono;
    }

    public void setDono(String dono) {
        this.dono = dono;
    }
}
