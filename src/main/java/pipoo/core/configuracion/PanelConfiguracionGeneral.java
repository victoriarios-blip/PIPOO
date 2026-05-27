package pipoo.core.configuracion;

import javax.swing.*;
import java.awt.*;

public class PanelConfiguracionGeneral extends JPanel {
    private ConfiguracionGeneral config;
    private JCheckBox chkSonido, chkPantalla;
    private JButton btnGuardar, btnReset, btnVolver;

    public PanelConfiguracionGeneral(ConfiguracionGeneral config, JPanel contenedor, CardLayout cl) {
        this.config = config;
        this.setLayout(new GridLayout(0, 2, 10, 10));

        // Componentes
        chkSonido = new JCheckBox("Sonido Global Activado", config.isSonidoActivado());
        chkPantalla = new JCheckBox("Modo Pantalla Completa", config.isPantallaCompleta());

        // Agregar a la interfaz
        this.add(new JLabel("Audio del Sistema:")); this.add(chkSonido);
        this.add(new JLabel("Visualización:"));    this.add(chkPantalla);

        btnGuardar = new JButton("Guardar");
        btnReset = new JButton("Reset");
        btnVolver = new JButton("Volver al Inicio");

        // Lógica de los botones
        btnGuardar.addActionListener(e -> {
            config.setSonidoActivado(chkSonido.isSelected());
            config.setPantallaCompleta(chkPantalla.isSelected());
            config.guardar();
            JOptionPane.showMessageDialog(this, "Configuración general guardada.");
        });

        btnReset.addActionListener(e -> {
            config.reset();
            actualizarGUI();
            JOptionPane.showMessageDialog(this, "Valores globales restaurados.");
        });

        btnVolver.addActionListener(e -> cl.show(contenedor, "INICIO"));

        this.add(btnGuardar);
        this.add(btnReset);
        this.add(new JLabel("")); // Espacio
        this.add(btnVolver);
    }

    private void actualizarGUI() {
        chkSonido.setSelected(config.isSonidoActivado());
        chkPantalla.setSelected(config.isPantallaCompleta());
    }
}
