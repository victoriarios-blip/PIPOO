package pipoo.core.configuracion;

import javax.swing.*;
import java.awt.*;

public class PanelConfiguracionSI extends JPanel {
    private ConfiguracionSI config;
    private JCheckBox chkSonido, chkPantalla;
    private JComboBox<String> comboSkins, comboMusica, comboVelocidad, comboControles;
    private JTextField txtNombreJ1;
    private JButton btnGuardar, btnReset, btnVolver;

    public PanelConfiguracionSI(ConfiguracionSI config, JPanel contenedor, CardLayout cl) {
        this.config = config;
        this.setLayout(new BorderLayout(10, 10));

        // 1. Formulario central de configuraciones
        JPanel panelFormulario = new JPanel(new GridLayout(0, 2, 10, 10));

        txtNombreJ1 = new JTextField(config.getNombreJ1(), 10);
        chkSonido = new JCheckBox("Sonido Activado", config.isSonidoActivado());
        chkPantalla = new JCheckBox("Pantalla Completa", config.isPantallaCompleta());

        comboSkins = new JComboBox<>(new String[]{"Original", "Color", "Halloween"});
        comboVelocidad = new JComboBox<>(new String[]{"Lenta", "Media", "Rápida"});
        comboMusica = new JComboBox<>(new String[]{"Tema 1 (Original)", "Tema 2 (Alternativo)"});
        comboControles = new JComboBox<>(new String[]{"Flechas + Espacio", "A, D + K"});

        panelFormulario.add(new JLabel("Nombre del Piloto:"));   panelFormulario.add(txtNombreJ1);
        panelFormulario.add(new JLabel("Ajustes de Audio:"));     panelFormulario.add(chkSonido);
        panelFormulario.add(new JLabel("Modo de Pantalla:"));   panelFormulario.add(chkPantalla);
        panelFormulario.add(new JLabel("Modo de Skins:"));      panelFormulario.add(comboSkins);
        panelFormulario.add(new JLabel("Velocidad Inicial:"));  panelFormulario.add(comboVelocidad);
        panelFormulario.add(new JLabel("Pista Musical:"));       panelFormulario.add(comboMusica);
        panelFormulario.add(new JLabel("Configuración de Teclas:")); panelFormulario.add(comboControles);

        this.add(panelFormulario, BorderLayout.CENTER);

        // 2. Barra de botones inferior (Sur)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnGuardar = new JButton("Guardar");
        btnReset = new JButton("Reset");
        btnVolver = new JButton("Volver al Menú");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnReset);
        panelBotones.add(btnVolver);
        this.add(panelBotones, BorderLayout.SOUTH);

        // --- ASIGNACIÓN DE OYENTES Y LÓGICA ---
        btnVolver.addActionListener(e -> cl.show(contenedor, "INICIO"));

        btnGuardar.addActionListener(e -> {
            config.setNombreJ1(txtNombreJ1.getText());
            config.setSonidoActivado(chkSonido.isSelected());
            config.setPantallaCompleta(chkPantalla.isSelected());
            config.setSkinModo((String) comboSkins.getSelectedItem());
            config.setVelocidadInvasores((String) comboVelocidad.getSelectedItem());
            config.setPistaMusical((String) comboMusica.getSelectedItem());

            // Mapeo lógico de combinaciones de teclas (KeyCodes)
            String seleccionTeclas = (String) comboControles.getSelectedItem();
            config.setMapeoControles(seleccionTeclas);

            if ("A, D + K".equals(seleccionTeclas)) {
                config.setTeclaIzq(65);      // Tecla A
                config.setTeclaDer(68);      // Tecla D
                config.setTeclaDisparo(75);  // Tecla K
            } else {
                config.setTeclaIzq(37);      // Flecha Izquierda
                config.setTeclaDer(39);      // Flecha Derecha
                config.setTeclaDisparo(32);  // Barra Espaciadora
            }

            config.guardar();
            JOptionPane.showMessageDialog(this, "Configuración de Space Invaders guardada correctamente.");
        });

        btnReset.addActionListener(e -> {
            config.reset();
            config.guardar();
            actualizarGUI();
            JOptionPane.showMessageDialog(this, "Valores restaurados por defecto.");
        });

        actualizarGUI();
    }

    private void actualizarGUI() {
        txtNombreJ1.setText(config.getNombreJ1());
        chkSonido.setSelected(config.isSonidoActivado());
        chkPantalla.setSelected(config.isPantallaCompleta());
        comboSkins.setSelectedItem(config.getSkinModo());
        comboVelocidad.setSelectedItem(config.getVelocidadInvasores());
        comboMusica.setSelectedItem(config.getPistaMusical());
        comboControles.setSelectedItem(config.getMapeoControles());
    }
}