package com.devpipe.GestionBiblioteca.servicio;


import com.devpipe.GestionBiblioteca.modelo.Libro;

import com.devpipe.GestionBiblioteca.repositorio.LibroRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroServicio implements ILibroServicio{
    @Autowired
    private LibroRepositorio libroRepositorio;



    @Override
    public List<Libro> listarLibros() {
        List<Libro> libros  = libroRepositorio.findAll();
        return libros;
    }

    @Override
    public Libro buscarLibroPorId(Integer IdLibro) {
        Libro libro = libroRepositorio.findById(IdLibro).orElse(null);
        return libro;
    }

    @Override
    public void guardarLibro(Libro libro) {
    libroRepositorio.save(libro);
    }

    @Override
    public void eliminarLibro(Libro libro) {
    libroRepositorio.delete(libro);
    }

    @Override
    public List buscarLibroPorDisponibilidad(String disponibilidad) {
        List<Libro> libros = libroRepositorio.buscarLibroPorDisponibilidad(disponibilidad);
        return libros;
    }


}
