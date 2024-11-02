package com.devpipe.GestionBiblioteca.servicio;

import com.devpipe.GestionBiblioteca.modelo.Devolucion;

import java.util.List;

public interface IDevolucioneServicio {

    public List<Devolucion> listarDevoluciones();

    public Devolucion buscarDevolucionesPorId(Integer IdDevolucion);

    public void guardarDevolucion(Devolucion devolucion);

    public void eliminarDevolucion(Devolucion devolucion);
}
