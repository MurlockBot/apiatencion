package com.empresa.apiatencioncanales.services;

import com.empresa.apiatencioncanales.dto.AtencionDTO.AtencionCerrarDTO;
import com.empresa.apiatencioncanales.dto.AtencionDTO.AtencionGenerarDTO;
import com.empresa.apiatencioncanales.models.Atencion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Queue;


public interface AtencionService {

    public Atencion generarAtencion(AtencionGenerarDTO atencionDTO);

    public Queue<Atencion> obtenerAtencionesVigentes();

    public Atencion atenderAtencion(String usuarioAtencion);

    public Atencion cerrarAtencion(AtencionCerrarDTO atencionCerrarDTO, Long idAtencion);

    public void cambiarPrioridad(Long idCliente);

    public List<Atencion> buscarAtencionesAbiertas();

    public List<Atencion> buscarAtencionesEntreFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
