package pipoo.core.configuracion;

import javax.swing.*;
import java.awt.*;

public class PanelConfiguracionPong extends JPanel {
    private ConfiguracionPong config;
    private JCheckBox chkSonido, chkPantalla;
    private JComboBox<String> comboSkinsPaleta, comboSkinsCancha, comboSkinsPelota, comboMusica;
    private JComboBox<Integer> comboPuntos; //entre 11 o 15
    // Podrías usar campos de texto simples para las teclas (simplificado)
    private JTextField txtUpJ1, txtDownJ1, txtUpJ2, txtDownJ2;
    private JPanel parent;
    private CardLayout cl;

    public PanelConfiguracionPong(ConfiguracionPong config, JPanel parent, CardLayout cl) {
        this.config = config;
        this.parent = parent;
        this.cl = cl;
        this.setLayout(new GridLayout(0, 2, 10, 10)); // Grilla de 2 columnas [4]

        // --- Inicialización de Componentes ---
        chkSonido = new JCheckBox("Sonido Activado", config.isSonidoActivado());
        chkPantalla = new JCheckBox("Pantalla Completa", config.isPantallaCompleta());

        String[] skins = {"Original", "Neon", "Retro"};
        comboSkinsPaleta = new JComboBox<>(skins);
        comboSkinsPelota = new JComboBox<>(skins);
        comboSkinsCancha = new JComboBox<>(skins);

        String[] canciones = {"Pong Arcade", "Electronic", "None"};
        comboMusica = new JComboBox<>(canciones);

        //11 o 15 puntos
        Integer[] opcionesPuntos = {11, 15};
        comboPuntos = new JComboBox<>(opcionesPuntos);

        // boton volver
        JButton btnVolver = new JButton("Volver al Menú");
        btnVolver.addActionListener(e -> {
            // Usa el CardLayout para mostrar la pantalla de inicio [1]
            cl.show(parent, "INICIO");
        });


        // --- Agregar al Panel ---
        this.add(new JLabel("Ajustes de Audio:")); this.add(chkSonido);
        this.add(new JLabel("Modo de Pantalla:")); this.add(chkPantalla);
        this.add(new JLabel("Skin Paletas:")); this.add(comboSkinsPaleta);
        this.add(new JLabel("Skin Pelota:")); this.add(comboSkinsPelota);
        this.add(new JLabel("Puntos para ganar:")); this.add(comboPuntos);
        this.add(new JLabel("Pista Musical:")); this.add(comboMusica);
        this.add(new JLabel("")); // Espacio vacío en la grilla si es necesario
        this.add(btnVolver);

        this.add(new JLabel("CONTROLES J1:"));
        this.add(new JLabel("Flechas Arriba / Abajo"));
        this.add(new JLabel("CONTROLES J2:"));
        this.add(new JLabel("Teclas W / S"));

        // Botones de Acción
        JButton btnGuardar = new JButton("Guardar");
        JButton btnReset = new JButton("Reset");

        // Lógica Guardar
        btnGuardar.addActionListener(e -> {
            config.setSonidoActivado(chkSonido.isSelected());
            config.setPantallaCompleta(chkPantalla.isSelected());
            config.setSkinPaletas((String) comboSkinsPaleta.getSelectedItem());
            config.setSkinPelota((String) comboSkinsPelota.getSelectedItem());
            config.setPistaMusical((String) comboMusica.getSelectedItem());
            config.setPuntosParaGanar((Integer) comboPuntos.getSelectedItem());

            config.guardar(); // Persistencia física discutida antes
            JOptionPane.showMessageDialog(this, "Configuración guardada correctamente");
        });

        // Lógica reset
        btnReset.addActionListener(e -> {
            config.reset();
            actualizarGUI();
            JOptionPane.showMessageDialog(this, "Valores restaurados por defecto.");
        });

        this.add(btnGuardar);
        this.add(btnReset);

        // Sincronización inicial
        actualizarGUI();
    }

    private void actualizarGUI() {
        chkSonido.setSelected(config.isSonidoActivado());
        chkPantalla.setSelected(config.isPantallaCompleta());
        comboSkinsPaleta.setSelectedItem(config.getSkinPaletas());
        comboSkinsPelota.setSelectedItem(config.getSkinPelota());
        comboMusica.setSelectedItem(config.getPistaMusical());
        comboPuntos.setSelectedItem(config.getPuntosParaGanar());
    }
}