package com.devpipe.GestionBiblioteca.servicio;

import com.devpipe.GestionBiblioteca.modelo.Libro;
import com.devpipe.GestionBiblioteca.modelo.Socio;

import java.util.List;

public interface ILibroServicio {
    public List<Libro> listarLibros();

    public Libro buscarLibroPorId(Integer IdLibro);

    public void guardarLibro(Libro  libro);

    public void eliminarLibro(Libro libro);

    public List<Libro> buscarLibroPorDisponibilidad(String disponinbilidad);

}
