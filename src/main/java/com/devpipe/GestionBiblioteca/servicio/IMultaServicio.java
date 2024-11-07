package com.devpipe.GestionBiblioteca.servicio;

import com.devpipe.GestionBiblioteca.modelo.Multa;

import java.util.List;

public interface IMultaServicio {
    public List<Multa> listarMultas();

    public Multa buscarMultaPorId(Integer IdMulta);

    public void guardarMulta(Multa  multa);

    public void eliminarMulta(Multa multa);

}
