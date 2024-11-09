package com.devpipe.GestionBiblioteca.repositorio;

import com.devpipe.GestionBiblioteca.modelo.Libro;

import org.springframework.data.jpa.repository.JpaRepository;
public interface LibroRepositorio extends JpaRepository<Libro,Integer> {



}
