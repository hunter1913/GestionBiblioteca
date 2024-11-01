package com.devpipe.GestionBiblioteca.servicio;

import com.devpipe.GestionBiblioteca.modelo.Prestamo;


import java.util.List;

public interface IPrestamoServicio {
    public List<Prestamo> listarPrestamo();

    public Prestamo buscarPrestamoPorId(Integer IdPrestamo);

    public void guardarPrestamo(Prestamo prestamo);

    public void eliminarPrestamo(Prestamo prestamo);
}
