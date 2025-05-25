package com.exemplo.garage.repository;

import com.exemplo.garage.model.PlacaAutorizada;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlacaAutorizadaRepository extends JpaRepository<PlacaAutorizada, String> {
}
