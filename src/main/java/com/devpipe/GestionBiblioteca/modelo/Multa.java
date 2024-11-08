package com.devpipe.GestionBiblioteca.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Multa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMulta;
    private String estado;
    private Integer monto;
    private Integer idLibro;
    private Integer idSocio;
    private Integer idDevolucion;
    private Integer dias_mora;

}
