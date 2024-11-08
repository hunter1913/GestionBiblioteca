package com.devpipe.GestionBiblioteca.servicio;


import com.devpipe.GestionBiblioteca.modelo.Reserva;
import com.devpipe.GestionBiblioteca.repositorio.ReservaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ReservaServicio implements IReservaServicio{

    @Autowired
    private ReservaRepositorio reservaRepositorio;

    @Override
    public List<Reserva> listarReservas() {
        List<Reserva> reservas = reservaRepositorio.findAll();
        return reservas;
    }

    @Override
    public Reserva buscarReservaPorId(Integer IdReserva) {
       Reserva reserva = reservaRepositorio.findById(IdReserva).orElse(null);
        return reserva;
    }

    @Override
    public void guardarReserva(Reserva reserva) {
        reservaRepositorio.save(reserva);
    }

    @Override
    public void eliminarReserva(Reserva reserva) {
    reservaRepositorio.delete(reserva);
    }
}
