package com.devpipe.GestionBiblioteca;

import com.devpipe.GestionBiblioteca.gui.LibrosForma;
import com.devpipe.GestionBiblioteca.gui.MenuForma;
import com.devpipe.GestionBiblioteca.gui.PrestamosForma;
import com.devpipe.GestionBiblioteca.gui.SociosForma;
import com.formdev.flatlaf.FlatDarculaLaf;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

@SpringBootApplication
public class GestionBibliotecaSwing {
    public static void main(String[] args) {
        //Configuramos el modo Oscuro
        FlatDarculaLaf.setup();

        // Instanciamos la fabrica de Spring
        ConfigurableApplicationContext contextoSpring = new SpringApplicationBuilder(GestionBibliotecaSwing.class)
                .headless(false)
                .web(WebApplicationType.NONE)
                .run(args);

        // Crear objeto de Swing
//        SwingUtilities.invokeLater(() -> {
//            LibrosForma librosForma = contextoSpring.getBean(LibrosForma.class);
//            librosForma.setVisible(true);
//        });

//        SwingUtilities.invokeLater(() -> {
//          PrestamosForma prestamosForma = contextoSpring.getBean(PrestamosForma.class);
//          prestamosForma.setVisible(true);
//        });

//
//        SwingUtilities.invokeLater(() -> {
//            SociosForma sociosForma = contextoSpring.getBean(SociosForma.class);
//            sociosForma.setVisible(true);
//        });

        SwingUtilities.invokeLater(() -> {
            MenuForma menuForma = contextoSpring.getBean(MenuForma.class);
            menuForma.setVisible(true);
        });
    }
}
