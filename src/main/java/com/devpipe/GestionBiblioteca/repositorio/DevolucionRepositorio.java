package com.devpipe.GestionBiblioteca.repositorio;

import com.devpipe.GestionBiblioteca.modelo.Devolucion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DevolucionRepositorio extends JpaRepository<Devolucion,Integer> {
}
