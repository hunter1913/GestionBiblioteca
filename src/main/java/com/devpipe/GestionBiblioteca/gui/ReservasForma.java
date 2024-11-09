package com.devpipe.GestionBiblioteca.gui;

import com.devpipe.GestionBiblioteca.modelo.Prestamo;
import com.devpipe.GestionBiblioteca.modelo.Reserva;
import com.devpipe.GestionBiblioteca.servicio.IPrestamoServicio;
import com.devpipe.GestionBiblioteca.servicio.IReservaServicio;
import com.devpipe.GestionBiblioteca.servicio.PrestamoServicio;
import com.devpipe.GestionBiblioteca.servicio.ReservaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ReservasForma extends JFrame{
    private JPanel reservasPrincipal;
    private JButton menuPrincipalButton;
    private JTable reservasTabla;
    private JButton hacerPrestamoButton;
    private JTextField fechaReservaTexto;
    private JTextField libroTexto;
    private JTextField socioTexto;
    private JButton registrarReservaButton;
    IReservaServicio reservaServicio;
    private DefaultTableModel tablaModeloReservas;
    private PrestamosForma prestamosForma;
    private Integer idReserva;
    private String estadoReserva;
    private Integer idLibro;
    private Integer idSocio;
    private Integer librosDisponibles;
    IPrestamoServicio prestamoServicio;


    @Autowired
    public ReservasForma(ReservaServicio reservaServicio, PrestamosForma prestamosForma, PrestamoServicio prestamoServicio) {
        this.reservaServicio = reservaServicio;
        this.prestamosForma = prestamosForma;
        this.prestamoServicio = prestamoServicio;
        iniciarForma();

        menuPrincipalButton.addActionListener(e -> menuPrincipal());
        fechaReservaTexto.setText(prestamosForma.cargarFecha());
        reservasTabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarReservaSeleccionada();
            }
        });
        registrarReservaButton.addActionListener(e -> guardarReserva());
        hacerPrestamoButton.addActionListener(e -> hacerPrestamo());
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
            var estado = reserva.getEstadoReserva();
            if (!estado.equals("En prestamo")) {
                Object[] renglonReserva = {
                        reserva.getIdReserva(),
                        formatoFecha(reserva.getFechaReserva()),
                        prestamosForma.cambiarIdPorNombreLibro(reserva.getIdLibro()),
                        prestamosForma.cambiarIdPorNombreSocio(reserva.getIdSocio()),
                        reserva.getEstadoReserva(),
                };
                this.tablaModeloReservas.addRow(renglonReserva);
            }
        });
    }

    private void cargarReservaSeleccionada() {
        var renglon = reservasTabla.getSelectedRow();
        if (renglon != -1) ; // significa que no se selecciono ningun registro
        var id = reservasTabla.getModel().getValueAt(renglon, 0).toString();
        this.idReserva = Integer.parseInt(id);
        var fechaReserva = reservasTabla.getModel().getValueAt(renglon,1).toString();
        this.fechaReservaTexto.setText(fechaReserva);
        var reserva = reservaServicio.buscarReservaPorId(this.idReserva);
        Integer updateCodigoLibro = reserva.getIdLibro();
        this.idLibro = updateCodigoLibro;
        String codigoLibroCadena = String.valueOf(updateCodigoLibro);
        this.libroTexto.setText(codigoLibroCadena);
        Integer updateCodigoSocio = reserva.getIdSocio();
        this.idSocio = updateCodigoSocio;
        String codigoSocioCadena = String.valueOf(updateCodigoSocio);
        this.socioTexto.setText(codigoSocioCadena);
    }

    private void guardarReserva(){
        //Recuperamos los valores
       var fechaReserva = fechaReservaTexto.getText();
       var idLibro = libroTexto.getText();
       this.idLibro= Integer.parseInt(idLibro);
        var idSocio = socioTexto.getText();
        this.idSocio= Integer.parseInt(idSocio);
        this.estadoReserva = "Pendiente";
        Reserva reserva = new Reserva();
        reserva.setFechaReserva(formatoTipoFecha(fechaReserva));
        reserva.setIdLibro(this.idLibro);
        reserva.setIdSocio(this.idSocio);
        reserva.setEstadoReserva(this.estadoReserva);
        this.reservaServicio.guardarReserva(reserva);
        listarReservas();
        limpiarFormulario();
    }

    private void hacerPrestamo(){
            var fechaPrestamoEntrada = cargarFecha();
            var fechaPrestamo = formatoTipoFecha(fechaPrestamoEntrada);
            var fechaDevolucionEntrada = JOptionPane.showInputDialog("Ingrese Fecha de devolucion yyyy-MM-dd");
            var fechaDevolucion = formatoTipoFecha(fechaDevolucionEntrada);
            Prestamo prestamo = new Prestamo();
            prestamo.setIdPrestamo(null);
            prestamo.setFechaPrestamo(fechaPrestamo);
            prestamo.setFechaDevolucion(fechaDevolucion);
            prestamo.setId_socio(this.idSocio);
            prestamo.setLibroIdLibro(this.idLibro);
            prestamo.setEstado("Activo");
            prestamoServicio.guardarPrestamo(prestamo);
            mostrarMensaje("Se registro el prestamo satisfactoriamente");
        cambiarEstadoReserva();
        listarReservas();
        limpiarFormulario();
        }

    private void cambiarEstadoReserva(){
        var reserva = reservaServicio.buscarReservaPorId(this.idReserva);
        reserva.setEstadoReserva("En prestamo");
        reservaServicio.guardarReserva(reserva);

    }



    private Date formatoTipoFecha(String fecha){
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        var fechatexto = fecha;
        Date fechaFormatiada = null;
        try {
            fechaFormatiada = formatoFecha.parse(fechatexto);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return fechaFormatiada;
    }

    private String formatoFecha(Date fecha){
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        return formatoFecha.format(fecha);
    }

    private void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private String cargarFecha(){
        Date fecha = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        return formatoFecha.format(fecha);
    }

    private void limpiarFormulario(){
        fechaReservaTexto.setText(cargarFecha());
        libroTexto.setText("");
        socioTexto.setText("");

        //Limpiamos el id del socio libro y reseva seleccionado
        this.idReserva = null;
        this.idLibro = null;
        this.idSocio = null;
        //Desleccionamos el registro seleccionado de la tabla
        this.reservasTabla.getSelectionModel().clearSelection();
    }

}
