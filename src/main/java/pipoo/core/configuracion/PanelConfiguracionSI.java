package pipoo.core.configuracion;

import javax.swing.*;
import java.awt.*;

public class PanelConfiguracionSI extends JPanel {
    private ConfiguracionSI config;
    private JCheckBox chkSonido, chkPantalla;
    private JComboBox<String> comboSkinsNave, comboSkinsAliens, comboMusica, comboVelocidad;
    private JButton btnGuardar, btnReset, btnVolver;

    public PanelConfiguracionSI(ConfiguracionSI config, JPanel contenedor, CardLayout cl) {
        this.config = config;
        this.setLayout(new GridLayout(0, 2, 10, 10)); // Grilla para etiquetas y componentes

        // Inicializacion de componentes
        chkSonido = new JCheckBox("Sonido Activado", config.isSonidoActivado());
        chkPantalla = new JCheckBox("Pantalla Completa", config.isPantallaCompleta());

        comboSkinsNave = new JComboBox<>(new String[]{"Original", "Galactica", "Retro"});
        comboSkinsAliens = new JComboBox<>(new String[]{"Original", "Pixel Art", "Monstruos"});

        // Velocidad de los invasores
        comboVelocidad = new JComboBox<>(new String[]{"Lenta", "Media", "Rápida"});

        comboMusica = new JComboBox<>(new String[]{"Tema SI Original", "Remix 8-bit", "Synthwave"});

        btnVolver = new JButton("Volver al Menú");

        // Agregamos al panel
        this.add(new JLabel("Ajustes de Audio:")); this.add(chkSonido);
        this.add(new JLabel("Modo de Pantalla:")); this.add(chkPantalla);
        this.add(new JLabel("Skin de Nave:"));    this.add(comboSkinsNave);
        this.add(new JLabel("Skin de Aliens:"));  this.add(comboSkinsAliens);
        this.add(new JLabel("Velocidad Aliens:"));this.add(comboVelocidad);
        this.add(new JLabel("Pista Musical:"));   this.add(comboMusica);
        this.add(btnVolver);

        //controles
        this.add(new JLabel("Disparo:"));
        this.add(new JLabel("Espacio"));
        this.add(new JLabel("Movimiento:"));
        this.add(new JLabel("Flechas Derecha/Izquierda"));

        // Botones
        btnGuardar = new JButton("Guardar");
        btnReset = new JButton("Reset");

        // Lógica de navegación
        btnVolver.addActionListener(e -> cl.show(contenedor, "INICIO"));

        // Lógica de persistencia
        btnGuardar.addActionListener(e -> {
            config.setSonidoActivado(chkSonido.isSelected());
            config.setVelocidadInvasores((String) comboVelocidad.getSelectedItem());
            config.guardar(); // Persistencia en config_si.properties
            JOptionPane.showMessageDialog(this, "Configuración de Space Invaders guardada.");
        });

        this.add(btnGuardar);
        this.add(btnReset);
        this.add(new JLabel("")); // Espacio
        this.add(btnVolver);
    }
}

