package com.devpipe.GestionBiblioteca.modelo;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode


public class Socio {
    @Id
//    @Column(name = "ID_SOCIO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSocio;
    private String nombre;
    private String apellido;
    private String direccion;
    private String email;
    private String telefono;

//
//    @OneToMany
//    @JoinColumn(name = "ID_SOCIO", referencedColumnName = "ID_SOCIO")
//    private List<Prestamo> prestamos;
}
