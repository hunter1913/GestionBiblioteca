package com.devpipe.GestionBiblioteca.gui;

import com.devpipe.GestionBiblioteca.servicio.DevolucionServicio;
import com.devpipe.GestionBiblioteca.servicio.IDevolucionServicio;
import com.devpipe.GestionBiblioteca.servicio.IPrestamoServicio;
import com.devpipe.GestionBiblioteca.servicio.PrestamoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Component
public class DevolucionesForma extends JFrame{
    private JTable prestamosDevolucionesTabla;
    private JPanel devolucionesPrincipal;
    private JButton buscarButton;
    private JButton menuPrinciapalButton;
    private DefaultTableModel tablaModeloDevoluciones;
    private IDevolucionServicio devolucionServicio;
    private IPrestamoServicio prestamoServicio;
    private Integer idDevolucion;
    private PrestamosForma prestamosForma;


    @Autowired
    public DevolucionesForma(DevolucionServicio devolucionServicio, PrestamoServicio prestamoServicio, PrestamosForma prestamosForma){
        this.devolucionServicio = devolucionServicio;
        this.prestamoServicio = prestamoServicio;
        this.prestamosForma = prestamosForma;
        iniciarForma();
        menuPrinciapalButton.addActionListener(e -> menuPrincipal());
        buscarButton.addActionListener(e -> buscarDevolucionPorId());
    }


    private void createUIComponents() {
        this.tablaModeloDevoluciones = new DefaultTableModel(0, 3) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cabeceros = {"id Prestamo", "id Libro", "Socio"};
        this.tablaModeloDevoluciones.setColumnIdentifiers(cabeceros);
        this.prestamosDevolucionesTabla = new JTable(tablaModeloDevoluciones);
        //Restringimos la seleccion de la tabla a un solo registro
        this.prestamosDevolucionesTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Cargar listado de libros
        listarPrestamos();
    }

    private void iniciarForma(){
        setContentPane(devolucionesPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900,700);
        setLocationRelativeTo(null);//centra ventana
    }

    private void listarPrestamos(){
        this.tablaModeloDevoluciones.setRowCount(0);
        var prestamos = this.prestamoServicio.listarPrestamo();
        prestamos.forEach(prestamo -> {
            Object[] renglonDevolucion = {
                prestamo.getIdPrestamo(),
                prestamosForma.cambiarIdPorNombreLibro(prestamo.getLibroIdLibro()),
                prestamosForma.cambiarIdPorNombreSocio(prestamo.getId_socio()),

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

    private void buscarDevolucionPorId(){
        this.tablaModeloDevoluciones.setRowCount(0);
        var idDevolucionEntrada = JOptionPane.showInputDialog("Digite el id de la devolucion");
        this.idDevolucion = Integer.parseInt(idDevolucionEntrada);
        var devolucion = devolucionServicio.buscarDevolucionesPorId(this.idDevolucion);
        if (devolucion != null) {
            Object[] renglonLibro = {
                    devolucion.getIdDevolucion(),
                    devolucion.getIdPrestamo(),
                    devolucion.getIdLibro(),
            };
            this.tablaModeloDevoluciones.addRow(renglonLibro);
        }
        else mostrarMensaje("Prestamo no encontrado");
        limpiarFormulario();
    }

    private void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void limpiarFormulario(){
        //Limpiamos el id del socio seleccionado
        this.idDevolucion = null;
        //Desleccionamos el registro seleccionado de la tabla
        this.prestamosDevolucionesTabla.getSelectionModel().clearSelection();
    }


}
