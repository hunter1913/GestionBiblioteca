package com.devpipe.GestionBiblioteca.gui;

import com.devpipe.GestionBiblioteca.modelo.Socio;
import com.devpipe.GestionBiblioteca.servicio.ISocioServicio;
import com.devpipe.GestionBiblioteca.servicio.SocioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


@Component
public class SociosForma extends JFrame{
    private JPanel sociosPrincipal;
    private JTable sociosTabla;
    private JTextField nombreTexto;
    private JTextField apellidoTexto;
    private JTextField direccionTexto;
    private JTextField emailTexto;
    private JTextField telefonoTexto;
    private JButton guardarButton;
    private JButton eliminarButton;
    private JButton limpiarButton;
    private JButton menuPrincipalButton;
    ISocioServicio socioServicio;
    private DefaultTableModel tablaModeloSocios;
    private Integer idSocio;



    @Autowired
    public SociosForma(SocioServicio socioServicio){
        this.socioServicio = socioServicio;
        iniciarForma();


        sociosTabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarSocioSeleccionado();
            }
        });
        guardarButton.addActionListener(e -> guardarSocio());
        eliminarButton.addActionListener(e -> eliminarSocio());
        limpiarButton.addActionListener(e -> limpiarFormulario());
        menuPrincipalButton.addActionListener(e -> menuPrincipal());
    }



    private void iniciarForma(){
        setContentPane(sociosPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900,800);
        setLocationRelativeTo(null);//centra ventana
    }

    private void createUIComponents() {
            this.tablaModeloSocios = new DefaultTableModel(0, 5) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cabeceros = {"id", "Nombre", "Apellido", "Direccion", "Email", "Telefono"};
        this.tablaModeloSocios.setColumnIdentifiers(cabeceros);
        this.sociosTabla = new JTable(tablaModeloSocios);
        //Restringimos la seleccion de la tabla a un solo registro
        this.sociosTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Cargar listado de clientes
        listarSocios();
    }

    private void listarSocios(){
        this.tablaModeloSocios.setRowCount(0);
        var clientes = this.socioServicio.listarSocios();
        clientes.forEach(socio -> {
            Object[] renglonSocio = {
                    socio.getIdSocio(),
                    socio.getNombre(),
                    socio.getApellido(),
                    socio.getDireccion(),
                    socio.getEmail(),
                    socio.getTelefono(),
            };
            this.tablaModeloSocios.addRow(renglonSocio);
        });
    }


    private void guardarSocio() {
        if (nombreTexto.getText().equals("")) {
            mostrarMensaje("Proporciona un nombre");
            nombreTexto.requestFocusInWindow();
            return;
        }
        if (apellidoTexto.getText().equals("")) {
            mostrarMensaje("Proporciona un apellido");
            apellidoTexto.requestFocusInWindow();
            return;
        }
        //Recuperar los valores del formulario
        var nombre = nombreTexto.getText();
        var apellido = apellidoTexto.getText();
        var direccion = direccionTexto.getText();
        var email = emailTexto.getText();

        var telefono = Integer.parseInt(telefonoTexto.getText());

//        var socio = new Socio(this.idSocio, nombre, apellido, direccion, email, telefono);
        Socio socio = new Socio();
        socio.setIdSocio(this.idSocio);
        socio.setNombre(nombre);
        socio.setApellido(apellido);
        socio.setDireccion(direccion);
        socio.setEmail(email);
        socio.setTelefono(telefono);
        this.socioServicio.guardarSocio(socio);
        if  (this.idSocio == null)
            mostrarMensaje("Se agrego el nuevo socio ");
        else
            mostrarMensaje("Se actualizo el socio");
        limpiarFormulario();
        listarSocios();
    }

    private void cargarSocioSeleccionado(){
        var renglon = sociosTabla.getSelectedRow();
        if (renglon != -1); // significa que no se selecciono ningun registro
        var id = sociosTabla.getModel().getValueAt(renglon, 0).toString();
        this.idSocio = Integer.parseInt(id);
        var nombre = sociosTabla.getModel().getValueAt(renglon, 1).toString();
        this.nombreTexto.setText(nombre);
        var apellido = sociosTabla.getModel().getValueAt(renglon, 2).toString();
        this.apellidoTexto.setText(apellido);
        var direccion = sociosTabla.getModel().getValueAt(renglon, 3).toString();
        this.direccionTexto .setText(direccion);
        var email = sociosTabla.getModel().getValueAt(renglon, 4).toString();
        this.emailTexto .setText(email);
        var telefono = sociosTabla.getModel().getValueAt(renglon, 5).toString();
        this.telefonoTexto .setText(telefono);
    }

    private void eliminarSocio(){
        var renglon = sociosTabla.getSelectedRow();
        if (renglon != -1 ){
            var idSocioeStr = sociosTabla.getModel().getValueAt(renglon,0).toString();
            this.idSocio = Integer.parseInt(idSocioeStr);
            var socio = new Socio();
            socio.setIdSocio(this.idSocio);
            socioServicio.eliminarSocio(socio);
            mostrarMensaje("Socio con id " + this.idSocio + " eliminado");
            limpiarFormulario();
            listarSocios();
        }
        else
            mostrarMensaje("Debe Seleccionar un cliente a eliminar");
    }

    private void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void limpiarFormulario(){
        nombreTexto.setText("");
        apellidoTexto.setText("");
        direccionTexto.setText("");
        emailTexto.setText("");
        telefonoTexto.setText("");
        //Limpiamos el id del socio seleccionado
        this.idSocio = null;
        //Desleccionamos el registro seleccionado de la tabla
        this.sociosTabla.getSelectionModel().clearSelection();
    }


    private void menuPrincipal(){
        dispose();
        MenuForma menuForma = new MenuForma();
        menuForma.iniciarForma();
        menuForma.setVisible(true);

    }

}
