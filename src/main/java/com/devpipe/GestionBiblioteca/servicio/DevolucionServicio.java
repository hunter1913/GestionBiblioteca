package com.devpipe.GestionBiblioteca.servicio;

import com.devpipe.GestionBiblioteca.modelo.Devolucion;
import com.devpipe.GestionBiblioteca.repositorio.DevolucionRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DevolucionServicio implements IDevolucioneServicio{
    @Autowired
    private DevolucionRepositorio devolucionRepositorio;


    @Override
    public List<Devolucion> listarDevoluciones() {
        List<Devolucion> devoluciones = devolucionRepositorio.findAll();
        return devoluciones;
    }

    @Override
    public Devolucion buscarDevolucionesPorId(Integer IdDevolucion) {
        Devolucion devolucion = devolucionRepositorio.findById(IdDevolucion).orElse(null);
        return devolucion;
    }

    @Override
    public void guardarDevolucion(Devolucion devolucion) {
        devolucionRepositorio.save(devolucion);
    }

    @Override
    public void eliminarDevolucion(Devolucion devolucion) {
    devolucionRepositorio.delete(devolucion);
    }
}
