package com.empresa.apiatencioncanales.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Atencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.BIGINT)
    public Long id;

    public Long idCliente;

    public LocalDateTime horarioRegistro;

    public LocalDateTime horarioAtencion;

    public LocalDateTime horarioFinalizaAtencion;

    public String duracionAtencion;

    public String usuarioAtencion;

    public Boolean esPrioridad;

    @Column(nullable = true)
    public String comentarioAtencion;

    @PrePersist
    public void establecerHoraRegistroAtencion() {
        this.horarioRegistro = LocalDateTime.now();
    }

}
