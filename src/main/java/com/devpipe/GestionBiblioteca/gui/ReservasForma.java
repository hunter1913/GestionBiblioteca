package com.devpipe.GestionBiblioteca.gui;

import com.devpipe.GestionBiblioteca.servicio.IReservaServicio;
import com.devpipe.GestionBiblioteca.servicio.ReservaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

@Component
public class ReservasForma extends JFrame{
    private JPanel reservasPrincipal;
    private JButton menuPrincipalButton;
    private JTable reservasTabla;
    private JButton hacerPrestamoButton;
    IReservaServicio reservaServicio;
    private DefaultTableModel tablaModeloReservas;

    @Autowired
    public ReservasForma(ReservaServicio reservaServicio) {
        this.reservaServicio = reservaServicio;
        iniciarForma();

        menuPrincipalButton.addActionListener(e -> menuPrincipal());
    }

    private void iniciarForma(){
        setContentPane(reservasPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900,800);
        setLocationRelativeTo(null);//centra ventana
    }

    private void menuPrincipal(){
        dispose();
        MenuForma menuForma = new MenuForma();
        menuForma.iniciarForma();
        menuForma.setVisible(true);
    }


    private void createUIComponents() {
        this.tablaModeloReservas = new DefaultTableModel(0, 4) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String[] cabeceros = {"# Reserva", "Fecha Reserva", "Libro","Socio", "Estado Reserva"};
        this.tablaModeloReservas.setColumnIdentifiers(cabeceros);
        this.reservasTabla = new JTable(tablaModeloReservas);
        //Restringimos la seleccion de la tabla a un solo registro
        this.reservasTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Cargar listado de clientes
        listarReservas();

    }

    private void listarReservas(){
        this.tablaModeloReservas.setRowCount(0);
        var reservas = this.reservaServicio.listarReservas();
        reservas.forEach(reserva -> {
                      Object[] renglonReserva = {
                        reserva.getIdReserva(),
                              reserva.getFechaReserva(),
                              reserva.getIdLibro(),
                              reserva.getIdSocio(),
                              reserva.getEstadoReserva(),
                };
                this.tablaModeloReservas.addRow(renglonReserva);

        });
    }


}
