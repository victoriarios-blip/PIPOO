package pipoo.core.configuracion;

import javax.swing.*;
import java.awt.*;

public class PanelConfiguracionLR extends JPanel {
    private ConfiguracionLR config;

    // GUI
    private JCheckBox chkSonido, chkPantalla;
    private JComboBox<String> comboSkinsPersonaje, comboMusica;
    private JButton btnGuardar, btnReset, btnVolver;

    public PanelConfiguracionLR(ConfiguracionLR config, JPanel contenedor, CardLayout cl) {
        this.config = config;
        this.setLayout(new BorderLayout(10, 10));

        // PANEL CENTRAL
        JPanel panelFormulario = new JPanel(new GridLayout(0, 2, 10, 10));

        // checkboxes
        chkSonido   = new JCheckBox("Sonido Activado",  config.isSonidoActivado());
        chkPantalla = new JCheckBox("Pantalla Completa", config.isPantallaCompleta());

        // skins: "Original" y "DeGalaRunner"
        comboSkinsPersonaje = new JComboBox<>(new String[]{"Original", "DeGalaRunner"});

        // musica
        comboMusica = new JComboBox<>(new String[]{"Ninguna", "Tema LR Original", "The Mountain Retro"});

        // video y audio
        panelFormulario.add(new JLabel("Modo de Pantalla:"));
        panelFormulario.add(chkPantalla);
        panelFormulario.add(new JLabel("Sonido:"));
        panelFormulario.add(chkSonido);

        // personalizacion
        panelFormulario.add(new JLabel("Skin Personaje:"));
        panelFormulario.add(comboSkinsPersonaje);
        panelFormulario.add(new JLabel("Pista Musical:"));
        panelFormulario.add(comboMusica);

        // controles
        panelFormulario.add(new JLabel("Movimiento:"));
        panelFormulario.add(new JLabel("Flechas direccionales"));
        panelFormulario.add(new JLabel("Cavar Pozo:"));
        panelFormulario.add(new JLabel("Barra Espaciadora"));
        panelFormulario.add(new JLabel("Efectos Sonido ON/OFF"));
        panelFormulario.add(new JLabel("Q"));
        panelFormulario.add(new JLabel("Música de Fondo ON/OFF"));
        panelFormulario.add(new JLabel("W"));
        panelFormulario.add(new JLabel("Iniciar"));
        panelFormulario.add(new JLabel("Enter"));
        this.add(panelFormulario, BorderLayout.CENTER);

        // PANEL DE BOTONES
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnGuardar = new JButton("Guardar");
        btnReset   = new JButton("Reset");
        btnVolver  = new JButton("Volver al Menú");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnReset);
        panelBotones.add(btnVolver);
        this.add(panelBotones, BorderLayout.SOUTH);

        // LOGICA DE BOTONES
        btnGuardar.addActionListener(e -> {
            config.setPantallaCompleta(chkPantalla.isSelected());
            config.setSonidoActivado(chkSonido.isSelected());
            config.setSkinPersonaje((String) comboSkinsPersonaje.getSelectedItem());
            config.setPistaMusical((String) comboMusica.getSelectedItem());
            config.guardar();
            JOptionPane.showMessageDialog(this, "Configuración de Lode Runner guardada.");
        });
        btnReset.addActionListener(e -> {
            config.reset();
            config.guardar();
            actualizarGUI();
            JOptionPane.showMessageDialog(this, "Valores de Lode Runner restaurados por defecto.");
        });
        btnVolver.addActionListener(e -> cl.show(contenedor, "INICIO"));
        actualizarGUI();
    }

    private void actualizarGUI() {
        chkPantalla.setSelected(config.isPantallaCompleta());
        chkSonido.setSelected(config.isSonidoActivado());
        comboSkinsPersonaje.setSelectedItem(config.getSkinPersonaje());
        comboMusica.setSelectedItem(config.getPistaMusical());
    }
}