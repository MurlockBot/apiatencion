package com.empresa.apiatencioncanales.controllers;

import com.empresa.apiatencioncanales.dto.AtencionDTO.AtencionCerrarDTO;
import com.empresa.apiatencioncanales.dto.AtencionDTO.AtencionGenerarDTO;
import com.empresa.apiatencioncanales.services.AtencionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("api/v1/atencion")
public class AtencionController {

    public final AtencionService atencionService;

    public AtencionController(AtencionService atencionService) {
        this.atencionService = atencionService;
    }

    @PostMapping()
    public ResponseEntity<?> generarAtencion(@RequestParam("idcliente") Long idCliente, @RequestParam("esPrioridad") Boolean esPrioridad) {

        AtencionGenerarDTO atencionDTO = AtencionGenerarDTO.builder()
                .idCliente(idCliente)
                .esPrioridad(esPrioridad)
                .build();
        return new ResponseEntity<>(atencionService.generarAtencion(atencionDTO), HttpStatus.OK);
    }

    @GetMapping("/consultar/vigentes")
    public ResponseEntity<?> obtenerAtencionesVigentes() {
        return new ResponseEntity<>(atencionService.obtenerAtencionesVigentes(), HttpStatus.OK);
    }

    @GetMapping("/consultar/abiertas")
    public ResponseEntity<?> obtenerAtencionesAbiertas() {
        return new ResponseEntity<>(atencionService.buscarAtencionesAbiertas(), HttpStatus.OK);
    }

    @GetMapping("/consultar/porfecha")
    public ResponseEntity<?> obtenerAtencionesPorFecha(
            @RequestParam("fechaInicio") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fechaFin) {

        // Si fechaFin no está presente, establece la fecha actual como valor por defecto
        if (fechaFin == null) {
            fechaFin = LocalDateTime.now();
        }

        return new ResponseEntity<>(atencionService.buscarAtencionesEntreFechas(fechaInicio, fechaFin), HttpStatus.OK);
    }

    @PatchMapping("/atenderatencion")
    public ResponseEntity<?> atenderAtencionProxima(@RequestParam("usuarioAtencion") String usuarioAtencion) {
        return new ResponseEntity<>(atencionService.atenderAtencion(usuarioAtencion), HttpStatus.OK);
    }

    @PatchMapping("/cambiarprioridad")
    public ResponseEntity<?> cambiarPrioridad(@RequestParam("idCliente") Long idCliente) {
        atencionService.cambiarPrioridad(idCliente);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PatchMapping("/cerraratencion")
    public ResponseEntity<?> cerrarAtencion(@RequestParam("idAtencion") Long idAtencion, @RequestBody AtencionCerrarDTO atencionCerrarDTO) {
        return new ResponseEntity<>(atencionService.cerrarAtencion(atencionCerrarDTO, idAtencion), HttpStatus.OK);
    }


}
