package com.devpipe.GestionBiblioteca.gui;

import com.devpipe.GestionBiblioteca.modelo.Multa;
import com.devpipe.GestionBiblioteca.servicio.IMultaServicio;
import com.devpipe.GestionBiblioteca.servicio.MultaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


@Component
public class MultasForma extends JFrame{
    private JPanel multasPrincipal;
    private JTable multasTabla;
    private JButton buscarButton;
    private JButton mostraTodosButton;
    private JButton pagarMultaButton;
    private JButton menuPrincipalButton;
    private JButton multasPagadasButton;
    IMultaServicio multaServicio;
    private DefaultTableModel tablaModeloMultas;
    private PrestamosForma prestamosForma;
    private Integer idMulta;
    private Integer idSocio;
    private Integer idLibro;
    private Integer idDevolucion;
    private Integer diasMora;
    private Integer monto;
    private String estado;

    @Autowired
    public MultasForma(MultaServicio multaServicio, PrestamosForma prestamosForma) {
        this.multaServicio = multaServicio;
        this.prestamosForma = prestamosForma;
        iniciarForma();
        menuPrincipalButton.addActionListener(e -> menuPrincipal());
        pagarMultaButton.addActionListener(e -> pagarMulta());
        multasTabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarMultaSeleccionada();
            }
        });
        mostraTodosButton.addActionListener(e -> mostrarTodos());
        multasPagadasButton.addActionListener(e -> listarMultasPagadas());
        buscarButton.addActionListener(e -> buscarMulta());
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
        listarMultasPendientes();
    }

    private void listarMultasPendientes(){
        this.tablaModeloMultas.setRowCount(0);
        var multas = this.multaServicio.listarMultas();
        multas.forEach(multa -> {
            if(multa.getEstado().equals("Pendiente")) {
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
            }
        });
    }


    private void cargarMultaSeleccionada() {
        var renglon = multasTabla.getSelectedRow();
        if (renglon != -1) ; // significa que no se selecciono ningun registro
        var id = multasTabla.getModel().getValueAt(renglon, 0).toString();
        this.idMulta = Integer.parseInt(id);
        var multa = multaServicio.buscarMultaPorId(idMulta);
        this.idSocio = multa.getIdSocio();
        this.idLibro = multa.getIdLibro();
        var idDevolucionEnt = multasTabla.getModel().getValueAt(renglon,3).toString();
        this.idDevolucion = Integer.parseInt(idDevolucionEnt);
        var diasMoraEnt = multasTabla.getModel().getValueAt(renglon, 4).toString();
        this.diasMora = Integer.parseInt(diasMoraEnt);
        var montoEnt = multasTabla.getModel().getValueAt(renglon, 5).toString();
        this.monto = Integer.parseInt(montoEnt);
        var estadoEnt = multasTabla.getModel().getValueAt(renglon, 6).toString();
        this.estado = estadoEnt;
    }

    private void pagarMulta(){
        Multa multa = new Multa();
        multa = multaServicio.buscarMultaPorId(this.idMulta);
        multa.setEstado("Pagado");
        multaServicio.guardarMulta(multa);
        mostrarMensaje("Se aprobo la transaccion multa pagada satisfactoriamente");
        listarMultasPendientes();
        limpiarFormulario();
    }

    private void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void limpiarFormulario(){
        //Limpiamos los ids seleccionados
        this.idMulta = null;
        this.idSocio = null;
        this.idLibro = null;
       this.idDevolucion = null;
       this.diasMora = null;
       this.monto =  null;
       this.estado="";
        //Desleccionamos el registro seleccionado de la tabla
        this.multasTabla.clearSelection();
    }

    private void mostrarTodos(){
        listarMultasPendientes();
        limpiarFormulario();
    }

    private void listarMultasPagadas(){
        this.tablaModeloMultas.setRowCount(0);
        var multas = this.multaServicio.listarMultas();
        multas.forEach(multa -> {
            if(multa.getEstado().equals("Pagado")) {
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
            }
        });
    }

    private void buscarMulta(){
        this.tablaModeloMultas.setRowCount(0);
        var idMultaEntrada = JOptionPane.showInputDialog("Digite el numero de la multa");
        this.idMulta = Integer.parseInt(idMultaEntrada);
        var multa = multaServicio.buscarMultaPorId(this.idMulta);
        if (multa != null) {
            if(multa.getEstado().equals("Pendiente")) {
                Object[] renglonLibro = {
                        multa.getIdMulta(),
                        prestamosForma.cambiarIdPorNombreSocio(multa.getIdSocio()),
                        prestamosForma.cambiarIdPorNombreLibro(multa.getIdLibro()),
                        multa.getIdDevolucion(),
                        multa.getDias_mora(),
                        multa.getMonto(),
                        multa.getEstado(),
                };
                this.tablaModeloMultas.addRow(renglonLibro);
            }else
                mostrarMensaje("La multa ya se encuentra pagada");
        }
        else mostrarMensaje("Multa no encontrada");
        limpiarFormulario();

    }

    }
