package com.devpipe.GestionBiblioteca.repositorio;
import com.devpipe.GestionBiblioteca.modelo.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrestamoRepositorio extends JpaRepository <Prestamo,Integer>{
}
