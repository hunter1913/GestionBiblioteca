package com.devpipe.GestionBiblioteca.servicio;


import com.devpipe.GestionBiblioteca.modelo.Reserva;

import java.util.List;

public interface IReservaServicio {
    public List<Reserva> listarReservas();

    public Reserva buscarReservaPorId(Integer IdReserva);

    public void guardarReserva(Reserva reserva);

    public void eliminarReserva(Reserva reserva);
}
