package com.empresa.apiatencioncanales.dto.AtencionDTO;

import lombok.*;


@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtencionGenerarDTO {

    public Long idCliente;
    public Boolean esPrioridad;
}
