package com.devpipe.GestionBiblioteca.repositorio;

import com.devpipe.GestionBiblioteca.modelo.Socio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocioRepositorio extends JpaRepository<Socio,Integer> {
}
