package com.devpipe.GestionBiblioteca.repositorio;

import com.devpipe.GestionBiblioteca.modelo.Libro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LibroRepositorio extends JpaRepository<Libro,Integer> {

//    @Query("SELECT u FROM Libro u WHERE u.disponibilidad = :disponibilidad")
//    List<Libro> buscarLibroPorDisponibilidad(@Param("disponibilidad")String disponibilidad);

}
