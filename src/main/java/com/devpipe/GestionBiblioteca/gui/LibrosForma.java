package com.devpipe.GestionBiblioteca.gui;

import com.devpipe.GestionBiblioteca.modelo.Libro;
import com.devpipe.GestionBiblioteca.servicio.ILibroServicio;
import com.devpipe.GestionBiblioteca.servicio.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


@Component
public class LibrosForma extends JFrame{
    private JPanel librosPrincipal;
    private JTable librosTabla;
    private JTextField isbnTexto;
    private JTextField tituloTexto;
    private JTextField autorTexto;
    private JTextField generoTexto;
    private JTextField editorialTexto;
    private JTextField anoTexto;
    private JTextField numeroPaginasTexto;
    private JButton guardarButton;
    private JButton buscarButton;
    private JButton eliminarButton;
    private JButton limpiarButton;
    private JButton menuPrincipalButton;
    private JButton mostrarTodosButton;
    private JTextField cantidadTexto;
    ILibroServicio libroServicio;
    private DefaultTableModel tablaModeloLibros;
    private Integer idLibro;

    @Autowired
    public LibrosForma(LibroServicio libroServicio){
        this.libroServicio = libroServicio;
        iniciarForma();
        guardarButton.addActionListener(e -> guardarLibro());
        librosTabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarLibroSeleccionado();
            }
        });
        buscarButton.addActionListener(e -> buscarLibroPorId());
        limpiarButton.addActionListener(e -> limpiarFormulario());
        eliminarButton.addActionListener(e -> eliminarLibro());
        mostrarTodosButton.addActionListener(e -> mostrarTodos());
        menuPrincipalButton.addActionListener(e -> menuPrincipal());
    }

    private void iniciarForma(){
        setContentPane(librosPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300,900);
        setLocationRelativeTo(null);//centra ventana
    }

    private void createUIComponents() {
            this.tablaModeloLibros = new DefaultTableModel(0, 9) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cabeceros = {"id", "Titulo", "Autor", "Genero", "Editorial", "Año Publicacion", "# Paginas", "ISBN", "Reserva Disponible", "Total Libros"};
        this.tablaModeloLibros.setColumnIdentifiers(cabeceros);
        this.librosTabla = new JTable(tablaModeloLibros);
        //Restringimos la seleccion de la tabla a un solo registro
        this.librosTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Cargar listado de libros
        listarLibros();
    }

    private void listarLibros(){
        this.tablaModeloLibros.setRowCount(0);
        var libros = this.libroServicio.listarLibros();
        libros.forEach(libro -> {
            Object[] renglonLibro = {
                    libro.getIdLibro(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getGenero(),
                    libro.getEditorial(),
                    libro.getAnoPublicacion(),
                    libro.getNumeroPaginas(),
                    libro.getIsbn(),
                    libro.getDisponibilidadReserva(),
                    libro.getCantidad(),
            };
            this.tablaModeloLibros.addRow(renglonLibro);
        });
    }

    private void guardarLibro() {

        if (isbnTexto.getText().equals("")) {
            mostrarMensaje("Proporciona un ISBN");
            isbnTexto.requestFocusInWindow();
            return;
        }
        if (tituloTexto.getText().equals("")) {
            mostrarMensaje("Proporciona un Titulo");
            tituloTexto.requestFocusInWindow();
            return;
        }
         //Recuperar los valores del formulario

        var titulo = tituloTexto.getText();
        var autor = autorTexto.getText();
        var genero = generoTexto.getText();
        var editorial = editorialTexto.getText();
        var anoPublicacion = anoTexto.getText();
        var paginas = Integer.parseInt(numeroPaginasTexto.getText());
        var isbn = isbnTexto.getText();

        var cantidad = Integer.parseInt(cantidadTexto.getText());
        if (cantidad > 0){
            Integer disponibilidadReserva = 0;
            var libro = new Libro(this.idLibro, titulo, autor, genero, editorial, anoPublicacion, paginas, isbn, disponibilidadReserva, cantidad);
            this.libroServicio.guardarLibro(libro);
        }else
            mostrarMensaje("Cantidad debe ser mayor a 0");
      if  (this.idLibro == null)
            mostrarMensaje("Se agrego el nuevo libro ");
       else
            mostrarMensaje("Se actualizo el libro");
      limpiarFormulario();
      listarLibros();
    }

    private void cargarLibroSeleccionado(){
        var renglon = librosTabla.getSelectedRow();
        if (renglon != -1); // significa que no se selecciono ningun registro
        var id = librosTabla.getModel().getValueAt(renglon, 0).toString();
        this.idLibro = Integer.parseInt(id);
        var titulo = librosTabla.getModel().getValueAt(renglon, 1).toString();
        this.tituloTexto.setText(titulo);
        var autor = librosTabla.getModel().getValueAt(renglon, 2).toString();
        this.autorTexto.setText(autor);
        var genero = librosTabla.getModel().getValueAt(renglon, 3).toString();
        this.generoTexto.setText(genero);
        var editorial = librosTabla.getModel().getValueAt(renglon, 4).toString();
        this.editorialTexto.setText(editorial);
        var anoPublicacion = librosTabla.getModel().getValueAt(renglon, 5).toString();
        this.anoTexto.setText(anoPublicacion);
        var numeroPaginas = librosTabla.getModel().getValueAt(renglon, 6).toString();
        this.numeroPaginasTexto.setText(numeroPaginas);
        var isbn = librosTabla.getModel().getValueAt(renglon, 7).toString();
        this.isbnTexto.setText(isbn);
        var cantidad = librosTabla.getModel().getValueAt(renglon, 9).toString();
        this.cantidadTexto.setText(cantidad);

    }
    private void eliminarLibro(){
        var renglon = librosTabla.getSelectedRow();
        if (renglon != -1 ){
            var idLibroeStr = librosTabla.getModel().getValueAt(renglon,0).toString();
            this.idLibro = Integer.parseInt(idLibroeStr);
            var libro = new Libro();
            libro.setIdLibro(this.idLibro);
            libroServicio.eliminarLibro(libro);
            mostrarMensaje("Libro con id " + this.idLibro + " eliminado");
            limpiarFormulario();
            listarLibros();
        }
        else
            mostrarMensaje("Debe Seleccionar un libro a eliminar");
    }

    private void buscarLibroPorId(){
        this.tablaModeloLibros.setRowCount(0);
        var idLibroEnt = JOptionPane.showInputDialog("Digite el id del libro");
        this.idLibro = Integer.parseInt(idLibroEnt);
        var libro = libroServicio.buscarLibroPorId(this.idLibro);
        if (libro != null) {
            Object[] renglonLibro = {
                    libro.getIdLibro(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getGenero(),
                    libro.getEditorial(),
                    libro.getAnoPublicacion(),
                    libro.getNumeroPaginas(),
                    libro.getIsbn(),
            };
            this.tablaModeloLibros.addRow(renglonLibro);
        }
        else mostrarMensaje("Libro no encontrado");
        limpiarFormulario();
    }

    private void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void limpiarFormulario(){
        tituloTexto.setText("");
        autorTexto.setText("");
        generoTexto.setText("");
        editorialTexto.setText("");
        anoTexto.setText("");
        numeroPaginasTexto.setText("");
        isbnTexto.setText("");
        cantidadTexto.setText("");
        //Limpiamos el id del socio seleccionado
        this.idLibro = null;
        //Desleccionamos el registro seleccionado de la tabla
        this.librosTabla.getSelectionModel().clearSelection();
    }

    private void mostrarTodos(){
    limpiarFormulario();
    listarLibros();
    }

    private void menuPrincipal(){
        dispose();
        MenuForma menuForma = new MenuForma();
        menuForma.iniciarForma();
        menuForma.setVisible(true);

    }
}
