package pipoo.core.configuracion;

import javax.swing.*;
import java.awt.*;

public class PanelConfiguracionPong extends JPanel {
    private ConfiguracionPong config;
    private JCheckBox chkSonido, chkPantalla, chkContraBot;
    private JComboBox<String> comboSkinsPaleta, comboSkinsCancha, comboSkinsPelota, comboMusica;
    private JComboBox<Integer> comboPuntos; //entre 11 o 15
    private JTextField txtNombreJ1;
    private JTextField txtNombreJ2;


    private JTextField txtUpJ1, txtDownJ1, txtUpJ2, txtDownJ2;
    private JPanel parent;
    private CardLayout cl;

    public PanelConfiguracionPong(ConfiguracionPong config, JPanel parent, CardLayout cl) {
        this.config = config;
        this.parent = parent;
        this.cl = cl;
        this.setLayout(new BorderLayout(10, 10));

        // Inicializacion de componentes
        txtNombreJ1 = new JTextField(config.getNombreJ1(), 10);
        txtNombreJ2 = new JTextField(config.getNombreJ2(), 10);
        chkSonido = new JCheckBox("Sonido Activado", config.isSonidoActivado());
        chkPantalla = new JCheckBox("Pantalla Completa", config.isPantallaCompleta());
        chkContraBot = new JCheckBox("Jugar contra BOT", config.getContraBot());

        //para que habilite escribir un nombre si no jugamos contra bot
        txtNombreJ2.setEnabled(!chkContraBot.isSelected());

        String[] skins = {"Original", "Neon"};
        comboSkinsPaleta = new JComboBox<>(skins);
        comboSkinsPelota = new JComboBox<>(skins);
        comboSkinsCancha = new JComboBox<>(skins);

        String[] canciones = {"Ninguna", "Jeff The Bat", "Keyboard Cat"};
        comboMusica = new JComboBox<>(canciones);

        //11 o 15 puntos
        Integer[] opcionesPuntos = {11, 15};
        comboPuntos = new JComboBox<>(opcionesPuntos);

        //Panel del centro
        JPanel panelFormulario = new JPanel(new GridLayout(0, 2, 10, 10));

        //jugadores
        panelFormulario.add(chkContraBot);
        panelFormulario.add(new JLabel(""));
        panelFormulario.add(new JLabel("Nombre Jugador 2:"));
        panelFormulario.add(txtNombreJ2);
        this.add(panelFormulario, BorderLayout.CENTER);

        // --- 3. PANEL DE BOTONES (SUR) ---
        // Agrupamos los botones de acción abajo para que no se estiren con el GridLayout [1, 2]
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnReset = new JButton("Reset");
        JButton btnVolver = new JButton("Volver al Menú");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnReset);
        panelBotones.add(btnVolver);
        this.add(panelBotones, BorderLayout.SOUTH);

        // SECCIÓN: Video y Audio
        panelFormulario.add(new JLabel("Modo de Pantalla:"));
        panelFormulario.add(chkPantalla);
        panelFormulario.add(new JLabel("Sonido:"));
        panelFormulario.add(chkSonido);

        // SECCIÓN: Gameplay
        panelFormulario.add(new JLabel("Puntos para ganar:"));
        panelFormulario.add(comboPuntos);
        panelFormulario.add(new JLabel("Pista Musical:"));
        panelFormulario.add(comboMusica);
        panelFormulario.add(new JLabel("Skin Paletas:"));
        panelFormulario.add(comboSkinsPaleta);
        panelFormulario.add(new JLabel("Skin Pelota:"));
        panelFormulario.add(comboSkinsPelota);

        // SECCIÓN: Info de Controles
        panelFormulario.add(new JLabel("CONTROLES J1:"));
        panelFormulario.add(new JLabel("Flechas Arriba / Abajo"));
        panelFormulario.add(new JLabel("CONTROLES J2:"));
        panelFormulario.add(new JLabel("Teclas W / S"));
        this.add(panelFormulario, BorderLayout.CENTER);

        btnVolver.addActionListener(e -> cl.show(parent, "INICIO"));

        //Logica contra bot
        chkContraBot.addActionListener(e -> {
            boolean esBot = chkContraBot.isSelected();
            txtNombreJ2.setEnabled(!esBot);
            if (esBot) {
                txtNombreJ2.setText("PIPOO BOT");
            } else {
                txtNombreJ2.setText(config.getNombreJ2());
            }
        });


        // Lógica Guardar
        btnGuardar.addActionListener(e -> {
            String seleccionPuntos = comboPuntos.getSelectedItem().toString();
            int puntos = Integer.parseInt(seleccionPuntos);

            String pistaSeleccionada = (String) comboMusica.getSelectedItem();
            config.setPistaMusical(pistaSeleccionada);

            config.setNombreJ1(txtNombreJ1.getText());
            config.setNombreJ2(txtNombreJ2.getText());
            config.setContraBot(chkContraBot.isSelected());
            config.setSonidoActivado(chkSonido.isSelected());
            config.setPantallaCompleta(chkPantalla.isSelected());
            config.setSkinPaletas((String) comboSkinsPaleta.getSelectedItem());
            config.setSkinPelota((String) comboSkinsPelota.getSelectedItem());
            config.setPistaMusical((String) comboMusica.getSelectedItem());
            config.setPuntosParaGanar(puntos);

            config.guardar();

            JOptionPane.showMessageDialog(this, "Configuración guardada correctamente");
        });

        // Lógica reset
        btnReset.addActionListener(e -> {
            config.reset();
            actualizarGUI();
            boolean esBot = chkContraBot.isSelected();
            txtNombreJ2.setEnabled(!esBot);
            txtNombreJ2.setText("PIPOO BOT");
            JOptionPane.showMessageDialog(this, "Valores restaurados por defecto.");
        });
        actualizarGUI();
    }




    private void actualizarGUI() {
        txtNombreJ1.setText(config.getNombreJ1());
        txtNombreJ2.setText(config.getNombreJ2());
        chkContraBot.setSelected(config.getContraBot());
        chkSonido.setSelected(config.isSonidoActivado());
        chkPantalla.setSelected(config.isPantallaCompleta());
        comboSkinsPaleta.setSelectedItem(config.getSkinPaletas());
        comboSkinsPelota.setSelectedItem(config.getSkinPelota());
        comboMusica.setSelectedItem(config.getPistaMusical());
        comboPuntos.setSelectedItem(config.getPuntosParaGanar());
    }
}