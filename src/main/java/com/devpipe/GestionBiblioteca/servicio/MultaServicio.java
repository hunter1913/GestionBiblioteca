package com.devpipe.GestionBiblioteca.servicio;

import com.devpipe.GestionBiblioteca.modelo.Multa;
import com.devpipe.GestionBiblioteca.modelo.Socio;
import com.devpipe.GestionBiblioteca.repositorio.MultaRepositorio;
import com.devpipe.GestionBiblioteca.repositorio.SocioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MultaServicio implements IMultaServicio{

    @Autowired
    private MultaRepositorio multaRepositorio;

    @Override
    public List<Multa> listarMultas() {
        List<Multa> multas  = multaRepositorio.findAll();
        return multas;
    }

    @Override
    public Multa buscarMultaPorId(Integer IdMulta) {
        Multa multa = multaRepositorio.findById(IdMulta).orElse(null);
        return multa;
    }

    @Override
    public void guardarMulta(Multa multa) {
        multaRepositorio.save(multa);

    }

    @Override
    public void eliminarMulta(Multa multa) {
    multaRepositorio.delete(multa);
    }
}
