package com.devpipe.GestionBiblioteca.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer idPrestamo;
    private Date fechaPrestamo ;
    private Date fechaDevolucion;

    private Integer id_socio;
    private Integer libroIdLibro;

    @ManyToOne
//    @JoinColumn(name = "id_socio")
////    @OneToMany(targetEntity = Socio.class,cascade = CascadeType.ALL)
////    @JoinColumn(name = "id_prestamo")
    private Socio socio;

}
