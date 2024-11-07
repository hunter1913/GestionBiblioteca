package com.devpipe.GestionBiblioteca.gui;

import com.devpipe.GestionBiblioteca.servicio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class DevolucionesForma extends JFrame{
    private JTable prestamosDevolucionesTabla;
    private JPanel devolucionesPrincipal;
    private JButton buscarButton;
    private JButton menuPrinciapalButton;
    private JButton actualizarButton;
    private JButton devolverLibroButton;
    private JTextField fechaDevolucionTexto;
    private DefaultTableModel tablaModeloDevoluciones;
    private IDevolucionServicio devolucionServicio;
    private IPrestamoServicio prestamoServicio;
    private ILibroServicio libroServicio;
    private Integer idDevolucion;
    private Integer idPrestamo;
    private Integer idLibro;
    private Integer idSocio;
    private PrestamosForma prestamosForma;


    @Autowired
    public DevolucionesForma(DevolucionServicio devolucionServicio, PrestamoServicio prestamoServicio, PrestamosForma prestamosForma, LibroServicio libroServicio){
        this.devolucionServicio = devolucionServicio;
        this.prestamoServicio = prestamoServicio;
        this.prestamosForma = prestamosForma;
        this.libroServicio = libroServicio;
        iniciarForma();
        menuPrinciapalButton.addActionListener(e -> menuPrincipal());
        buscarButton.addActionListener(e -> buscarPrestamoPorId());
        actualizarButton.addActionListener(e -> mostrarTodos());
        devolverLibroButton.addActionListener(e -> devolverLibro());
        prestamosDevolucionesTabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarPrestamoSeleccionado();
            }
        });
        fechaDevolucionTexto.setColumns(10);
        fechaDevolucionTexto.setText(prestamosForma.cargarFecha());
    }

    private void createUIComponents() {
        this.tablaModeloDevoluciones = new DefaultTableModel(0, 5) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cabeceros = {"id Prestamo", "Libro", "Socio", "Fecha Prestamo", "Devolucion Prevista"};
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
        setSize(900,800);
        setLocationRelativeTo(null);//centra ventana
    }

    private void listarPrestamos(){
        this.tablaModeloDevoluciones.setRowCount(0);
        var prestamos = this.prestamoServicio.listarPrestamo();
        prestamos.forEach(prestamo -> {
            if(prestamo.getEstado().equals("Activo")) {
                Object[] renglonDevolucion = {
                        prestamo.getIdPrestamo(),
                        prestamosForma.cambiarIdPorNombreLibro(prestamo.getLibroIdLibro()),
                        prestamosForma.cambiarIdPorNombreSocio(prestamo.getId_socio()),
                        prestamosForma.formatoFecha(prestamo.getFechaPrestamo()),
                        prestamosForma.formatoFecha(prestamo.getFechaDevolucion()),
                };
                this.tablaModeloDevoluciones.addRow(renglonDevolucion);
            }
        });
    }

    private void menuPrincipal(){
        dispose();
        MenuForma menuForma = new MenuForma();
        menuForma.iniciarForma();
        menuForma.setVisible(true);
    }

    private void buscarPrestamoPorId(){
        this.tablaModeloDevoluciones.setRowCount(0);
        var idPrestamoEntrada = JOptionPane.showInputDialog("Digite el id del prestamo");
        this.idPrestamo = Integer.parseInt(idPrestamoEntrada);
        var prestamo = prestamoServicio.buscarPrestamoPorId(this.idPrestamo);
        if (prestamo != null) {
            Object[] renglonLibro = {
                    prestamo.getIdPrestamo(),
                    prestamosForma.cambiarIdPorNombreLibro(prestamo.getLibroIdLibro()),
                    prestamosForma.cambiarIdPorNombreSocio(prestamo.getId_socio()),
                    prestamosForma.formatoFecha(prestamo.getFechaPrestamo()),
                    prestamosForma.formatoFecha(prestamo.getFechaDevolucion()),
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
        this.idPrestamo = null;
        this.idLibro = null;
        this.idSocio = null;
        //Desleccionamos el registro seleccionado de la tabla
        this.prestamosDevolucionesTabla.getSelectionModel().clearSelection();
    }

    private void mostrarTodos(){
        limpiarFormulario();
        listarPrestamos();
    }

    private void devolverLibro() {
            var prestamo = prestamoServicio.buscarPrestamoPorId(this.idPrestamo);
            if (prestamo.getEstado().equals("Activo")) {
                prestamo.setEstado("Inactivo");
                prestamoServicio.guardarPrestamo(prestamo);
                this.idLibro = prestamo.getLibroIdLibro();
                var libro = libroServicio.buscarLibroPorId(idLibro);
                var cantidad = libro.getCantidad();
                cantidad = cantidad + 1;
                libro.setCantidad(cantidad);
                libroServicio.guardarLibro(libro);
                mostrarMensaje("Devolucion exitosa");
                mostrarTodos();

            } else
                mostrarMensaje("El prestamo no esta activo");

    }

    private void cargarPrestamoSeleccionado(){
        var renglon = prestamosDevolucionesTabla.getSelectedRow();
        if (renglon != -1); // significa que no se selecciono ningun registro
        var id = prestamosDevolucionesTabla.getModel().getValueAt(renglon, 0).toString();
        this.idPrestamo = Integer.parseInt(id);
        var prestamo = prestamoServicio.buscarPrestamoPorId(idPrestamo);
        this.idLibro = prestamo.getLibroIdLibro();
        this.idSocio = prestamo.getId_socio();
    }
}
