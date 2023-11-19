package com.empresa.apiatencioncanales.services.impl;

import com.empresa.apiatencioncanales.dto.AtencionDTO.AtencionCerrarDTO;
import com.empresa.apiatencioncanales.dto.AtencionDTO.AtencionGenerarDTO;
import com.empresa.apiatencioncanales.exceptions.NotFoundException;
import com.empresa.apiatencioncanales.exceptions.PriorityException;
import com.empresa.apiatencioncanales.exceptions.QueueEmptyException;
import com.empresa.apiatencioncanales.models.Atencion;
import com.empresa.apiatencioncanales.repositories.AtencionRepository;
import com.empresa.apiatencioncanales.services.AtencionService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AtencionServiceImpl implements AtencionService {
    private Queue<Atencion> colaAtenciones = new LinkedList<>();

    private final AtencionRepository atencionRepository;

    public AtencionServiceImpl(AtencionRepository atencionRepository) {
        this.atencionRepository = atencionRepository;
    }


    @Override
    public Atencion generarAtencion(AtencionGenerarDTO atencionDTO) {


        //Si se está agregando un mismo número de cliente en la cola
        // Lo busca y si existe retorna el cliente, no lo agrega.
        Atencion clienteExistenteEnCola = buscarPorIdCliente(atencionDTO.idCliente);

        if (clienteExistenteEnCola == null) {
            Atencion atencionBD = Atencion
                    .builder()
                    .idCliente(atencionDTO.getIdCliente())
                    .esPrioridad(atencionDTO.getEsPrioridad())
                    .build();

            if (Boolean.TRUE.equals(atencionDTO.getEsPrioridad())) {

                // Si tiene prioridad, buscar la primera atención sin prioridad y ponerla antes de esa
                Queue<Atencion> nuevaCola = new LinkedList<>();
                boolean insertado = false;

                while (!colaAtenciones.isEmpty()) {
                    Atencion actual = colaAtenciones.poll();

                    if (!insertado && Boolean.TRUE.equals(!actual.getEsPrioridad())) {
                        nuevaCola.add(atencionBD);
                        insertado = true;
                    }

                    nuevaCola.add(actual);
                }

                if (!insertado) {
                    nuevaCola.add(atencionBD);
                }

                colaAtenciones = nuevaCola;
            } else {

                // Si no tiene prioridad, simplemente agregar al final de la cola
                colaAtenciones.add(atencionBD);
            }

            return atencionRepository.save(atencionBD);

        } else {
            return clienteExistenteEnCola;
        }
    }

    @Override
    public Queue<Atencion> obtenerAtencionesVigentes() {
        return colaAtenciones;
    }

    @Override
    public Atencion atenderAtencion(String usuarioAtencion) {

        if (colaAtenciones.isEmpty()) {
            throw new QueueEmptyException("Cola sin atenciones.");
        }

        Atencion atencionBD = obtenerValoresAtencion();

        colaAtenciones.poll();

        atencionBD.setHorarioAtencion(LocalDateTime.now());
        atencionBD.setUsuarioAtencion(usuarioAtencion);
        atencionRepository.save(atencionBD);

        return atencionBD;
    }

    @Override
    public Atencion cerrarAtencion(AtencionCerrarDTO atencionCerrarDTO, Long idAtencion) {
        Atencion atencionBD = atencionRepository.findByIdClienteAndHorarioFinalizaAtencionIsNull(idAtencion);

        if (atencionBD == null) {
            throw new NotFoundException("No hay atención que finalizar con ese idAtención.");
        }

        atencionBD.setComentarioAtencion(atencionCerrarDTO.comentario);
        atencionBD.setHorarioFinalizaAtencion(LocalDateTime.now());
        atencionBD.setDuracionAtencion(formatDuration(Duration.between(atencionBD.horarioAtencion, atencionBD.horarioFinalizaAtencion)));
        atencionRepository.save(atencionBD);

        return atencionBD;
    }

    @Override
    public void cambiarPrioridad(Long idCliente) {
        Atencion atencion = buscarPorIdCliente(idCliente);

        if (atencion != null) {
            boolean prioridadActual = atencion.getEsPrioridad();

            atencion.setEsPrioridad(!prioridadActual);

            atencionRepository.save(atencion);

            actualizarCola();
        } else {
            throw new PriorityException(String.format("ID Cliente: %d", idCliente));
        }
    }

    @Override
    public List<Atencion> buscarAtencionesAbiertas() {
        return atencionRepository.findAtencionByHorarioFinalizaAtencionIsNullAndUsuarioAtencionNotNull();
    }

    @Override
    public List<Atencion> buscarAtencionesEntreFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return atencionRepository.findByHorarioRegistroBetween(fechaInicio, fechaFin);
    }

    public void actualizarCola() {
        Queue<Atencion> listaActual = obtenerAtencionesVigentes();

        Queue<Atencion> listaNueva = new LinkedList<>();

        List<Atencion> prioridad = new ArrayList<>();
        List<Atencion> sinPrioridad = new ArrayList<>();

        for (Atencion atencion : listaActual) {
            if (Boolean.TRUE.equals(atencion.getEsPrioridad())) {
                prioridad.add(atencion);
            } else {
                sinPrioridad.add(atencion);
            }
        }

        sinPrioridad.sort(Comparator.comparing(Atencion::getHorarioRegistro));
        listaNueva.addAll(prioridad);

        listaNueva.addAll(sinPrioridad);

        colaAtenciones = new LinkedList<>(listaNueva);

    }

    public Atencion obtenerValoresAtencion() {
        return colaAtenciones.peek();
    }

    private static String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = (duration.toMinutes() % 60);
        long seconds = (duration.getSeconds() % 60);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public Atencion buscarPorIdCliente(Long idCliente) {
        for (Atencion atencion : colaAtenciones) {
            if (atencion.getIdCliente().equals(idCliente)) {
                return atencion;
            }
        }
        return null;
    }
}
