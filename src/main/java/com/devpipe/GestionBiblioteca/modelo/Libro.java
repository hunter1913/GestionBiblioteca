package com.devpipe.GestionBiblioteca.modelo;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLibro;
    private String titulo;
    private String autor;
    private String genero;
    private String editorial;
    private String anoPublicacion;
    private Integer numeroPaginas;
    private String isbn;



}
