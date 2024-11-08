package com.devpipe.GestionBiblioteca.repositorio;

import com.devpipe.GestionBiblioteca.modelo.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepositorio extends JpaRepository<Reserva,Integer> {
}
