package com.devpipe.GestionBiblioteca.gui;

import com.devpipe.GestionBiblioteca.GestionBibliotecaSwing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import javax.swing.*;



@Component
public class MenuForma extends JFrame{
    private JPanel menuPrincipal;
    private JButton gestionDeSociosButton;
    private JButton librosButton;
    private String[] args = new String[0];

    @Autowired
    public MenuForma() {
        iniciarForma();

        gestionDeSociosButton.addActionListener(e -> gestionSociosForm(args));

        librosButton.addActionListener(e -> gestionLibrosForm(args));
    }
    public void iniciarForma() {
        setContentPane(menuPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 800);
        setLocationRelativeTo(null);//centra ventana
    }

    private void gestionSociosForm(String[] args){
        dispose();
          ConfigurableApplicationContext contextoSpring = new SpringApplicationBuilder(GestionBibliotecaSwing.class)
                .headless(false)
                .web(WebApplicationType.NONE)
                .run(args);
                SwingUtilities.invokeLater(() -> {
            SociosForma sociosForma = contextoSpring.getBean(SociosForma.class);
            sociosForma.setVisible(true);
        });
    }

    private void gestionLibrosForm(String[] args){
        dispose();
        ConfigurableApplicationContext contextoSpring = new SpringApplicationBuilder(GestionBibliotecaSwing.class)
                .headless(false)
                .web(WebApplicationType.NONE)
                .run(args);
        SwingUtilities.invokeLater(() -> {
            LibrosForma librosForma = contextoSpring.getBean(LibrosForma.class);
            librosForma.setVisible(true);
        });
    }
 }

