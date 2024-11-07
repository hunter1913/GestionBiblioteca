package com.devpipe.GestionBiblioteca.gui;

import com.devpipe.GestionBiblioteca.modelo.Libro;
import com.devpipe.GestionBiblioteca.modelo.Prestamo;
import com.devpipe.GestionBiblioteca.modelo.Socio;
import com.devpipe.GestionBiblioteca.servicio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Component
public class PrestamosForma extends JFrame {
    private JTable prestamosTabla;
    private JPanel prestamosPrincipal;
    private JTextField fechaPrestamoTexto;
    private JTextField fechaDevoluTexto;
    private JTextField codigoSocioTexto;
    private JTextField codigoLibroTexto;
    private JButton registrarButton;
    private JButton buscarButton;
    private JButton eliminarButton;
    private JButton menuPrincipalButton;
    private JButton limpiarButton;
    IPrestamoServicio prestamoServicio;
    private DefaultTableModel tablaModeloPrestamos;
    private Integer idPrestamo;
    ISocioServicio socioServicio;
    ILibroServicio libroServicio;
    boolean confirmacion = false;

    @Autowired
    public PrestamosForma(PrestamoServicio prestamoServicio, SocioServicio socioServicio, LibroServicio libroservicio) {
        this.prestamoServicio = prestamoServicio;
        this.socioServicio = socioServicio;
        this.libroServicio = libroservicio;
        iniciarForma();
        fechaPrestamoTexto.addContainerListener(new ContainerAdapter() {
        });
        registrarButton.addActionListener(e -> registrarPrestamo());

        prestamosTabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarPrestamoSeleccionado();
            }
        });
        buscarButton.addActionListener(e -> buscarPrestamoPorId());
        limpiarButton.addActionListener(e -> mostrarTodos());
        eliminarButton.addActionListener(e -> eliminarPrestamo());
        menuPrincipalButton.addActionListener(e -> menuPrincipal());
    }

    private void createUIComponents() {
            this.tablaModeloPrestamos = new DefaultTableModel(0, 4) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
            String[] cabeceros = {"id Prestamo", "Fecha Prestamo", "Fecha Devolucion ","Socio", "Libro"};
            this.tablaModeloPrestamos.setColumnIdentifiers(cabeceros);
            this.prestamosTabla = new JTable(tablaModeloPrestamos);
            //Restringimos la seleccion de la tabla a un solo registro
            this.prestamosTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            // Cargar listado de clientes
        listarPrestamos();
        }


    private void listarPrestamos(){
        this.tablaModeloPrestamos.setRowCount(0);
        var prestamos = this.prestamoServicio.listarPrestamo();

        prestamos.forEach(prestamo -> {
            if(prestamo.getEstado().equals("Activo")){
                Object[] renglonPrestamo = {
                            prestamo.getIdPrestamo(),
                            formatoFecha(prestamo.getFechaPrestamo()),
                            formatoFecha(prestamo.getFechaDevolucion()),
                            cambiarIdPorNombreSocio(prestamo.getId_socio()),
                            cambiarIdPorNombreLibro(prestamo.getLibroIdLibro()),
//                            prestamo.getId_socio(),
//                            prestamo.getLibroIdLibro(),
                            };
                    this.tablaModeloPrestamos.addRow(renglonPrestamo);
            }
        });


    }

        private void iniciarForma() {
            setContentPane(prestamosPrincipal);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(900, 800);
            setLocationRelativeTo(null);//centra ventana
            fechaPrestamoTexto.setText(cargarFecha());
        }

        public String cargarFecha(){
            Date fecha = new Date();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
            return formatoFecha.format(fecha);
        }

    public String formatoFecha(Date fecha){
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
        return formatoFecha.format(fecha);
    }

    private void registrarPrestamo(){
        Integer codigoSocio = Integer.parseInt(codigoSocioTexto.getText());
        Integer codigoLibro = Integer.parseInt(codigoLibroTexto.getText());
        Socio socio = socioServicio.buscarSocioPorId(codigoSocio);
        Libro libro = libroServicio.buscarLibroPorId(codigoLibro);

        if (socio != null && libro != null){
            String disponibilidad = "Si";
            List<Libro> librosDisponibles;
            librosDisponibles=libroServicio.buscarLibroPorDisponibilidad(disponibilidad);
            librosDisponibles.forEach(libro1 -> {
                if (libro1.getIdLibro().equals(codigoLibro)) {
                      this.confirmacion = true;
                      guardarPrestamo();
                    limpiarFormulario();
                    listarPrestamos();
                }
            });
            if (this.confirmacion == false){
                mostrarMensaje("Libro no disponible");
            }
        }else
            if (socio == null){
                mostrarMensaje("Socio no existente, ingrese un socio valido");
            }
            if (libro == null){
                 mostrarMensaje("Libro no existente, ingrese un libro valido");
        }
    }

    private void guardarPrestamo(){
        Integer codigoSocio = Integer.parseInt(codigoSocioTexto.getText());
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
        if (fechaPrestamoTexto.getText().equals("")) {
            mostrarMensaje("Proporciona una fecha de prestamo");
            fechaPrestamoTexto.requestFocusInWindow();
            return;
        }
        if (fechaDevoluTexto.getText().equals("")) {
            mostrarMensaje("Proporciona un fecha de devolucion");
            fechaDevoluTexto.requestFocusInWindow();
            return;
        }
        if (codigoSocioTexto.getText().equals("")) {
            mostrarMensaje("Proporciona un codigo de Socio");
            codigoSocioTexto.requestFocusInWindow();
            return;
        }
        if (codigoLibroTexto.getText().equals("")) {
            mostrarMensaje("Proporciona un codigo de Libro");
            codigoLibroTexto.requestFocusInWindow();
            return;
        }
        //Recuperar los valores del formulario
        var fechatexto1 = fechaPrestamoTexto.getText();
        Date fechaPrestamo = null;
        try {
            fechaPrestamo = formatoFecha.parse(fechatexto1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        var fechatexto2 = fechaDevoluTexto.getText();
        Date fechaDevolucion = null;
        try {
            fechaDevolucion = formatoFecha.parse(fechatexto2);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        var codigoLibro = Integer.parseInt(codigoLibroTexto.getText());
        Prestamo prestamo = new Prestamo();
        prestamo.setIdPrestamo(idPrestamo);
        prestamo.setFechaPrestamo(fechaPrestamo);
        prestamo.setFechaDevolucion(fechaDevolucion);
        prestamo.setId_socio(codigoSocio);
        prestamo.setLibroIdLibro(codigoLibro);
        prestamo.setEstado("Activo");
        Libro libro = libroServicio.buscarLibroPorId(codigoLibro);

        this.prestamoServicio.guardarPrestamo(prestamo);
        Integer cantidad = libro.getCantidad();
        cantidad = cantidad -1;
        libro.setCantidad(cantidad);
        if(cantidad <= 0){
            libro.setDisponibilidad("No");
        }
        libroServicio.guardarLibro(libro);
        if  (this.idPrestamo == null)
            mostrarMensaje("Se agrego el nuevo prestamo ");
        else
            mostrarMensaje("Se actualizo el prestamo");
                limpiarFormulario();
                listarPrestamos();
    }

    private void cargarPrestamoSeleccionado(){
        var renglon = prestamosTabla.getSelectedRow();
        if (renglon != -1); // significa que no se selecciono ningun registro
        var id = prestamosTabla.getModel().getValueAt(renglon, 0).toString();
        this.idPrestamo = Integer.parseInt(id);
        var fechaPrestamo = prestamosTabla.getModel().getValueAt(renglon, 1).toString();
        this.fechaPrestamoTexto.setText(fechaPrestamo);
        var fechaDevolucion = prestamosTabla.getModel().getValueAt(renglon, 2).toString();
        this.fechaDevoluTexto.setText(fechaDevolucion);

        Prestamo prestamo = prestamoServicio.buscarPrestamoPorId(idPrestamo);
        Integer updateCodigoSocio = prestamo.getId_socio();
        String codigoSocioCadena = String.valueOf(updateCodigoSocio);

        this.codigoSocioTexto.setText(codigoSocioCadena);
        Integer updateCodigoLibro = prestamo.getLibroIdLibro();
        String codigoLibroCadena = String.valueOf(updateCodigoLibro);
        this.codigoLibroTexto.setText(codigoLibroCadena);
    }

    private void limpiarFormulario(){
        fechaPrestamoTexto.setText(cargarFecha());
        fechaDevoluTexto.setText("");
        codigoSocioTexto.setText("");
        codigoLibroTexto.setText("");
        //Limpiamos el id del socio seleccionado
        this.idPrestamo = null;
        //Desleccionamos el registro seleccionado de la tabla
        this.prestamosTabla.getSelectionModel().clearSelection();
    }

    private void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void buscarPrestamoPorId(){
        this.tablaModeloPrestamos.setRowCount(0);
        var idPrestEnt = JOptionPane.showInputDialog("Digite el id del prestamo");
        this.idPrestamo = Integer.parseInt(idPrestEnt);
        var prestamo = prestamoServicio.buscarPrestamoPorId(this.idPrestamo);
        if (prestamo != null) {
            Object[] renglonLibro = {
                    prestamo.getIdPrestamo(),
                    formatoFecha(prestamo.getFechaPrestamo()),
                    formatoFecha(prestamo.getFechaDevolucion()),
                    prestamo.getId_socio(),
                    prestamo.getLibroIdLibro(),
            };
            this.tablaModeloPrestamos.addRow(renglonLibro);
        }
        else mostrarMensaje("Prestamo no encontrado");
        limpiarFormulario();
    }

    private void eliminarPrestamo(){
        var renglon = prestamosTabla.getSelectedRow();
        if (renglon != -1 ){
            var idPretaeStr = prestamosTabla.getModel().getValueAt(renglon,0).toString();
            this.idPrestamo = Integer.parseInt(idPretaeStr);
            var prestamo = new Prestamo();
            prestamo.setIdPrestamo(this.idPrestamo);
            prestamoServicio.eliminarPrestamo(prestamo);
            mostrarMensaje("Libro con id " + this.idPrestamo + " eliminado");
            limpiarFormulario();
            listarPrestamos();
        }
        else
            mostrarMensaje("Debe Seleccionar un libro a eliminar");
    }
    private void mostrarTodos(){
        limpiarFormulario();
        listarPrestamos();
    }

    private void menuPrincipal(){
        dispose();
        MenuForma menuForma = new MenuForma();
        menuForma.iniciarForma();
        menuForma.setVisible(true);

    }

    public String cambiarIdPorNombreSocio(Integer id){
        Socio socio = socioServicio.buscarSocioPorId(id);
        String nombre = socio.getNombre();
        String apellido = socio.getApellido();
        String nombreCompleto = nombre + " " + apellido;
        return nombreCompleto;
    }

    public String cambiarIdPorNombreLibro(Integer id){
        Libro libro = libroServicio.buscarLibroPorId(id);
        String titulo = libro.getTitulo();
        return  titulo;
    }


}

