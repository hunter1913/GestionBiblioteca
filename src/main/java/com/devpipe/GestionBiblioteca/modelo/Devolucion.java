package com.devpipe.GestionBiblioteca.modelo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Devolucion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDevolucion;
    private Integer idPrestamo;
    private Integer idLibro;
//    @ManyToOne
//    private Prestamo prestamo;
//    @ManyToOne
//    private Libro libro;

}
