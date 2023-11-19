package com.empresa.apiatencioncanales.repositories;

import com.empresa.apiatencioncanales.models.Atencion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AtencionRepository extends JpaRepository<Atencion, Long> {

    public Atencion findByIdClienteAndHorarioFinalizaAtencionIsNull(Long idCliente);

    public List<Atencion> findAtencionByHorarioFinalizaAtencionIsNullAndUsuarioAtencionNotNull();

    List<Atencion> findByHorarioRegistroBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
