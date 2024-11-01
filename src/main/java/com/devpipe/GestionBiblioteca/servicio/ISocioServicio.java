package com.devpipe.GestionBiblioteca.servicio;

import com.devpipe.GestionBiblioteca.modelo.Socio;

import java.util.List;

public interface ISocioServicio {
    public List<Socio> listarSocios();

    public Socio buscarSocioPorId(Integer IdSocio);

    public void guardarSocio(Socio socio);

    public void eliminarSocio(Socio socio);

}
