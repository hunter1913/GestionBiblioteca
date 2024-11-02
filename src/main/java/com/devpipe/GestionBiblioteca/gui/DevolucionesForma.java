package com.devpipe.GestionBiblioteca.gui;

import com.devpipe.GestionBiblioteca.servicio.DevolucionServicio;
import com.devpipe.GestionBiblioteca.servicio.IDevolucionServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

@Component
public class DevolucionesForma extends JFrame{
    private JTextField idPrestamoText;
    private JTable prestamosDevolucionesTabla;
    private JPanel devolucionesPrincipal;
    private JButton button1;
    private JButton menuPrinciapalButton;
    private DefaultTableModel tablaModeloDevoluciones;
    private IDevolucionServicio devolucionServicio;

    @Autowired
    public DevolucionesForma(DevolucionServicio devolucionServicio){
        this.devolucionServicio = devolucionServicio;
        iniciarForma();
        menuPrinciapalButton.addActionListener(e -> menuPrincipal());
    }


    private void createUIComponents() {
        this.tablaModeloDevoluciones = new DefaultTableModel(0, 3) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cabeceros = {"id Devolucion", "id Prestamo", "id Libro"};
        this.tablaModeloDevoluciones.setColumnIdentifiers(cabeceros);
        this.prestamosDevolucionesTabla = new JTable(tablaModeloDevoluciones);
        //Restringimos la seleccion de la tabla a un solo registro
        this.prestamosDevolucionesTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Cargar listado de libros
        listarDevoluciones();
    }

    private void iniciarForma(){
        setContentPane(devolucionesPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900,700);
        setLocationRelativeTo(null);//centra ventana
    }

    private void listarDevoluciones(){
        this.tablaModeloDevoluciones.setRowCount(0);
        var devoluciones = this.devolucionServicio.listarDevoluciones();
        devoluciones.forEach(devolucion -> {
            Object[] renglonDevolucion = {
                    devolucion.getIdDevolucion(),
                    devolucion.getIdPrestamo(),
                    devolucion.getIdLibro(),
               };
            this.tablaModeloDevoluciones.addRow(renglonDevolucion);
        });
    }

    private void menuPrincipal(){
        dispose();
        MenuForma menuForma = new MenuForma();
        menuForma.iniciarForma();
        menuForma.setVisible(true);

    }


}
