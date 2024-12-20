package com.devpipe.GestionBiblioteca.gui;

import com.devpipe.GestionBiblioteca.modelo.Devolucion;
import com.devpipe.GestionBiblioteca.modelo.Multa;
import com.devpipe.GestionBiblioteca.modelo.Reserva;
import com.devpipe.GestionBiblioteca.servicio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class DevolucionesForma extends JFrame{
    private JTable prestamosDevolucionesTabla;
    private JPanel devolucionesPrincipal;
    private JButton buscarButton;
    private JButton menuPrinciapalButton;
    private JButton actualizarButton;
    private JButton devolverLibroButton;
    private JTextField fechaDevolucionTexto;
    //    private JTextField fechaDevolucionTexto;
    private DefaultTableModel tablaModeloDevoluciones;
    private IDevolucionServicio devolucionServicio;
    private IPrestamoServicio prestamoServicio;
    private ILibroServicio libroServicio;
    private IMultaServicio multaServicio;
    private Integer idDevolucion;
    private Integer idPrestamo;
    private Integer idLibro;
    private Integer idSocio;
    private PrestamosForma prestamosForma;
    private Date fechaDevolucionPrevista;
    private Date fechaDevolucionReal;
    private IReservaServicio reservaServicio;



    @Autowired
    public DevolucionesForma(DevolucionServicio devolucionServicio, PrestamoServicio prestamoServicio, PrestamosForma prestamosForma, LibroServicio libroServicio, MultaServicio multasServicio, ReservaServicio reservaServicio){
        this.devolucionServicio = devolucionServicio;
        this.prestamoServicio = prestamoServicio;
        this.prestamosForma = prestamosForma;
        this.libroServicio = libroServicio;
        this.multaServicio = multasServicio;
        this.reservaServicio = reservaServicio;
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
            if(prestamo.getEstado().equals("Pendiente")) {
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

    private void devolverLibro()  {

            var prestamo = prestamoServicio.buscarPrestamoPorId(this.idPrestamo);
            this.idLibro = prestamo.getLibroIdLibro();
            if (prestamo.getEstado().equals("Pendiente")) {
                //Se obtienen las fechas y se formatea
                SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    this.fechaDevolucionReal = date.parse(this.fechaDevolucionTexto.getText());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                int milisecondsByDay = 86400000;
                int dias = (int)((fechaDevolucionReal.getTime()-fechaDevolucionPrevista.getTime())/milisecondsByDay);
                //Cambiar estado del prestamo
                prestamo.setEstado("Devuelto");
                prestamoServicio.guardarPrestamo(prestamo);

                //Verificamos si existe alguna reserva para este libro

                List<Reserva> listaReservas;
                listaReservas =  reservaServicio.listarReservas();


                List<Reserva> librosReservados = new ArrayList<>();
                listaReservas.forEach(reserva -> {
                    if(reserva.getIdLibro().equals(this.idLibro)  && reserva.getEstadoReserva().equals("Pendiente")){
                        librosReservados.add(reserva);
                    }
                });
                if (librosReservados.isEmpty()){
                    sumarUnidadInventarioLibro();
                }else {
                    sumarDisponibilidadReserva();
                    cambiarEstadoReserva(librosReservados);
                }
                Devolucion devolucion = new Devolucion();
                devolucion.setIdDevolucion(this.idDevolucion);
                devolucion.setIdPrestamo(this.idPrestamo);
                devolucion.setIdLibro(this.idLibro);
                devolucion.setFechaDevolucionReal(fechaDevolucionReal);
                this.devolucionServicio.guardarDevolucion(devolucion);
                if  (this.idDevolucion == null)
                    mostrarMensaje("Devolucion exitosa");
                else
                    mostrarMensaje("Se actualizo la devolucion ");

                if (dias > 0) {
                    //Guardamos la multa
                    Multa multa = new Multa();
                    Integer monto = dias * 500;
                    mostrarMensaje("Se genero una multa de: " + monto + " por " + dias + " dias de mora");
                    var estado = "Pendiente";
                    multa.setEstado(estado);
                    multa.setMonto(monto);
                    multa.setIdSocio(this.idSocio);
                    multa.setIdLibro(this.idLibro);
                    multa.setIdDevolucion(devolucion.getIdDevolucion());
                    multa.setDias_mora(dias);
                    multaServicio.guardarMulta(multa);
                }else
                    mostrarMensaje("No se han generado multas");
                mostrarTodos();
            } else
                mostrarMensaje("El prestamo no esta activo");
    }

    private void cargarPrestamoSeleccionado()  {
        var renglon = prestamosDevolucionesTabla.getSelectedRow();
        if (renglon != -1); // significa que no se selecciono ningun registro
        var id = prestamosDevolucionesTabla.getModel().getValueAt(renglon, 0).toString();
        this.idPrestamo = Integer.parseInt(id);
        var prestamo = prestamoServicio.buscarPrestamoPorId(idPrestamo);
        this.idLibro = prestamo.getLibroIdLibro();
        this.idSocio = prestamo.getId_socio();
        var fechaDevPrevista = prestamosDevolucionesTabla.getModel().getValueAt(renglon,4).toString();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.fechaDevolucionPrevista = date.parse(fechaDevPrevista);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    private void sumarUnidadInventarioLibro(){
        var libro = this.libroServicio.buscarLibroPorId(this.idLibro);
        var cantidad = libro.getCantidad();
        cantidad = cantidad + 1;
        libro.setCantidad(cantidad);
        this.libroServicio.guardarLibro(libro);
    }

    private void sumarDisponibilidadReserva(){
        var libro = this.libroServicio.buscarLibroPorId(this.idLibro);
        var cantidad = libro.getDisponibilidadReserva();
        if (cantidad == null){
            cantidad = 0;
        }
        cantidad = cantidad + 1;
        libro.setDisponibilidadReserva(cantidad);
        this.libroServicio.guardarLibro(libro);
    }

    private void cambiarEstadoReserva(List<Reserva> librosReservados){

        var libro = libroServicio.buscarLibroPorId(this.idLibro);

        librosReservados.forEach(reserva -> {
         Integer disponibilidad = libro.getDisponibilidadReserva();
         if(reserva.getIdLibro().equals(this.idLibro) && disponibilidad > 0 ){
             reserva.setEstadoReserva("Listo para prestamo");
             reservaServicio.guardarReserva(reserva);
             disponibilidad = disponibilidad -1;
             libro.setDisponibilidadReserva(disponibilidad);
             libroServicio.guardarLibro(libro);
         }
        });


    }




}
