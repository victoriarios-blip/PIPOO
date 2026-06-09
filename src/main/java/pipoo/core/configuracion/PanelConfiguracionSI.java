package pipoo.core.configuracion;

import javax.swing.*;
import java.awt.*;

public class PanelConfiguracionSI extends JPanel {
    private ConfiguracionSI config;
    private JCheckBox chkSonido, chkPantalla;
    private JComboBox<String> comboSkins, comboMusica, comboVelocidad;
    private JTextField txtNombreJ1;
    private JButton btnGuardar, btnReset, btnVolver;

    public PanelConfiguracionSI(ConfiguracionSI config, JPanel contenedor, CardLayout cl) {
        this.config = config;
        this.setLayout(new BorderLayout(10, 10));

        // Formulario central
        JPanel panelFormulario = new JPanel(new GridLayout(0, 2, 10, 10));

        // Inicialización de componentes
        txtNombreJ1 = new JTextField(config.getNombreJ1(), 10);
        chkSonido = new JCheckBox("Sonido Activado", config.isSonidoActivado());
        chkPantalla = new JCheckBox("Pantalla Completa", config.isPantallaCompleta());

        // Tus tres modos de skins unificados
        comboSkins = new JComboBox<>(new String[]{"Original", "Color", "Halloween"});
        comboVelocidad = new JComboBox<>(new String[]{"Lenta", "Media", "Rápida"});
        comboMusica = new JComboBox<>(new String[]{"Tema 1 (Original)", "Tema 2 (Alternativo)"});

        // Agregamos elementos al formulario
        panelFormulario.add(new JLabel("Nombre del Piloto:")); panelFormulario.add(txtNombreJ1);
        panelFormulario.add(new JLabel("Ajustes de Audio:"));     panelFormulario.add(chkSonido);
        panelFormulario.add(new JLabel("Modo de Pantalla:"));   panelFormulario.add(chkPantalla);
        panelFormulario.add(new JLabel("Modo de Skins:"));      panelFormulario.add(comboSkins);
        panelFormulario.add(new JLabel("Velocidad Inicial:"));  panelFormulario.add(comboVelocidad);
        panelFormulario.add(new JLabel("Pista Musical:"));       panelFormulario.add(comboMusica);

        // Información estática de controles
        panelFormulario.add(new JLabel("Mover Izquierda / Derecha:")); panelFormulario.add(new JLabel("[Flechas Izq / Der]"));
        panelFormulario.add(new JLabel("Disparar Cañón Láser:"));       panelFormulario.add(new JLabel("[Barra Espaciadora]"));

        this.add(panelFormulario, BorderLayout.CENTER);

        // Panel de botones inferior (Sur)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnGuardar = new JButton("Guardar");
        btnReset = new JButton("Reset");
        btnVolver = new JButton("Volver al Menú");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnReset);
        panelBotones.add(btnVolver);
        this.add(panelBotones, BorderLayout.SOUTH);

        // --- ACCIONES DE LOS BOTONES ---
        btnVolver.addActionListener(e -> cl.show(contenedor, "INICIO"));

        btnGuardar.addActionListener(e -> {
            config.setNombreJ1(txtNombreJ1.getText());
            config.setSonidoActivado(chkSonido.isSelected());
            config.setPantallaCompleta(chkPantalla.isSelected());
            config.setSkinModo((String) comboSkins.getSelectedItem());
            config.setVelocidadInvasores((String) comboVelocidad.getSelectedItem());
            config.setPistaMusical((String) comboMusica.getSelectedItem());

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
    }
}

