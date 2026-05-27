package pipoo.core.configuracion;

import javax.swing.*;
import java.awt.*;

public class PanelConfiguracionLR extends JPanel {
    private ConfiguracionLR config;

    //GUI
    private JCheckBox chkSonido, chkPantalla;
    private JComboBox<String> comboSkinsPersonaje, comboMusica;
    private JButton btnGuardar, btnReset, btnVolver;

    public PanelConfiguracionLR(ConfiguracionLR config, JPanel contenedor, CardLayout cl) {
        this.config = config;
        this.setLayout(new GridLayout(0, 2, 10, 10));

        // --- 1. Inicialización de Componentes ---
        chkSonido = new JCheckBox("Sonido General", config.isSonidoActivado());
        chkPantalla = new JCheckBox("Pantalla Completa", config.isPantallaCompleta());

        // REQUERIMIENTO: Selección de skins y música [2]
        String[] skins = {"Original", "Explorador", "Robot"};
        comboSkinsPersonaje = new JComboBox<>(skins);

        String[] pistas = {"Tema LR Original", "Remix Retro", "Aventura"};
        comboMusica = new JComboBox<>(pistas);

        // --- 2. Agregar al Panel ---
        this.add(new JLabel("Audio:"));         this.add(chkSonido);
        this.add(new JLabel("Modo de Pantalla:")); this.add(chkPantalla);
        this.add(new JLabel("Skin Personaje:"));  this.add(comboSkinsPersonaje);
        this.add(new JLabel("Pista Musical:"));    this.add(comboMusica);

        // --- 3. Sección de Teclas (Texto Fijo según consigna [2]) ---
        this.add(new JLabel("CONTROLES (Fijos):")); this.add(new JLabel("Flechas: Mover"));
        this.add(new JLabel("Cavar Pozo:"));        this.add(new JLabel("Barra Espaciadora"));
        this.add(new JLabel("Audio (Efectos/Música):")); this.add(new JLabel("Q / W"));

        // --- 4. Botones de Acción ---
        btnGuardar = new JButton("Guardar");
        btnReset = new JButton("Reset");
        btnVolver = new JButton("Volver al Menú");

        // Lógica Guardar
        btnGuardar.addActionListener(e -> {
            config.setSonidoActivado(chkSonido.isSelected());
            config.setPantallaCompleta(chkPantalla.isSelected());
            config.setSkinPersonaje((String) comboSkinsPersonaje.getSelectedItem());
            config.setPistaMusical((String) comboMusica.getSelectedItem());

            config.guardar(); // Persistencia en config_lr.properties
            JOptionPane.showMessageDialog(this, "Configuración de Lode Runner guardada.");
        });

        // Lógica Reset
        btnReset.addActionListener(e -> {
            config.reset();
            actualizarGUI(); // Sincroniza visualmente
            JOptionPane.showMessageDialog(this, "Valores de Lode Runner restaurados.");
        });

        // Lógica Volver
        btnVolver.addActionListener(e -> cl.show(contenedor, "INICIO"));

        this.add(btnGuardar);
        this.add(btnReset);
        this.add(new JLabel("")); // Espacio
        this.add(btnVolver);

        actualizarGUI();
    }

    private void actualizarGUI() {
        chkSonido.setSelected(config.isSonidoActivado());
        chkPantalla.setSelected(config.isPantallaCompleta());
        comboSkinsPersonaje.setSelectedItem(config.getSkinPersonaje());
        comboMusica.setSelectedItem(config.getPistaMusical());
    }
}
