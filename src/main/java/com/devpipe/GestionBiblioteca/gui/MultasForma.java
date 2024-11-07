package com.devpipe.GestionBiblioteca.gui;

import com.devpipe.GestionBiblioteca.servicio.IMultaServicio;
import com.devpipe.GestionBiblioteca.servicio.MultaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


@Component
public class MultasForma extends JFrame{
    private JPanel multasPrincipal;
    private JTable multasTabla;
    private JButton buscarButton;
    private JButton mostraTodosButton;
    private JButton pagarMultaButton;
    private JButton menuPrincipalButton;
    IMultaServicio multaServicio;

    @Autowired
    public MultasForma(MultaServicio multaServicio) {
        this.multaServicio = multaServicio;
        iniciarForma();
        menuPrincipalButton.addActionListener(e -> menuPrincipal());
    }

    private void iniciarForma(){
        setContentPane(multasPrincipal);
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




}
