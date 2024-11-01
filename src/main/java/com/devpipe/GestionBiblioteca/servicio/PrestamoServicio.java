package com.devpipe.GestionBiblioteca.servicio;


import com.devpipe.GestionBiblioteca.modelo.Prestamo;
import com.devpipe.GestionBiblioteca.repositorio.PrestamoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrestamoServicio implements IPrestamoServicio{
    @Autowired
    private PrestamoRepositorio prestamoRepositorio;

    @Override
    public List<Prestamo> listarPrestamo() {
        List<Prestamo> prestamos = prestamoRepositorio.findAll();
        return prestamos;
    }

    @Override
    public Prestamo buscarPrestamoPorId(Integer IdPrestamo) {
        Prestamo prestamo = prestamoRepositorio.findById(IdPrestamo).orElse(null);
        return prestamo;
    }

    @Override
    public void guardarPrestamo(Prestamo prestamo) {
    prestamoRepositorio.save(prestamo);
    }

    @Override
    public void eliminarPrestamo(Prestamo prestamo) {
    prestamoRepositorio.delete(prestamo);
    }
}
