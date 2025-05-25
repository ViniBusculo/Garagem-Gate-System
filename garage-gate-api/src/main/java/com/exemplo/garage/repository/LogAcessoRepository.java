package com.exemplo.garage.repository;


import com.exemplo.garage.model.LogAcesso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogAcessoRepository extends JpaRepository<LogAcesso, Long> {
}
