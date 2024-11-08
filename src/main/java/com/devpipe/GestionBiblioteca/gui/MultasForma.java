package com.devpipe.GestionBiblioteca.gui;

import com.devpipe.GestionBiblioteca.servicio.IMultaServicio;
import com.devpipe.GestionBiblioteca.servicio.MultaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


@Component
public class MultasForma extends JFrame{
    private JPanel multasPrincipal;
    private JTable multasTabla;
    private JButton buscarButton;
    private JButton mostraTodosButton;
    private JButton pagarMultaButton;
    private JButton menuPrincipalButton;
    IMultaServicio multaServicio;
    private DefaultTableModel tablaModeloMultas;
    private PrestamosForma prestamosForma;

    @Autowired
    public MultasForma(MultaServicio multaServicio, PrestamosForma prestamosForma) {
        this.multaServicio = multaServicio;
        this.prestamosForma = prestamosForma;
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

    private void createUIComponents() {
        this.tablaModeloMultas = new DefaultTableModel(0, 6) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String[] cabeceros = {"# Multa", "Socio", "Libro","# Devolucion", "Dias de Mora","Monto", "Estado"};
        this.tablaModeloMultas.setColumnIdentifiers(cabeceros);
        this.multasTabla = new JTable(tablaModeloMultas);
        //Restringimos la seleccion de la tabla a un solo registro
        this.multasTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Cargar listado de clientes
        listarMultas();
    }

    private void listarMultas(){
        this.tablaModeloMultas.setRowCount(0);
        var multas = this.multaServicio.listarMultas();
        multas.forEach(multa -> {
            Object[] renglonSocio = {
                    multa.getIdMulta(),
                    prestamosForma.cambiarIdPorNombreSocio(multa.getIdSocio()),
                    prestamosForma.cambiarIdPorNombreLibro(multa.getIdLibro()),
                    multa.getIdDevolucion(),
                    multa.getDias_mora(),
                    multa.getMonto(),
                    multa.getEstado(),
            };
            this.tablaModeloMultas.addRow(renglonSocio);
        });
    }




}
