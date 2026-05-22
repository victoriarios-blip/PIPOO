package pipoo.core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import pipoo.loderunner.LodeRunner;
import pipoo.pong.Pong;
import pipoo.spaceinvaders.SpaceInvaders;

public class SistemaDeJuegos extends JPanel implements ActionListener {
    private Juego juegoActual;
    Thread hiloJuego;

    private JButton btnPong, btnConfigP, btnRankingP;
    private JButton btnLodeRunner, btnConfigLR, btnRankingLR;
    private JButton btnSpaceInvaders, btnConfigSI, btnRankingSI;
    private JButton btnConfigGeneral;

    private CardLayout cardLayout;
    private JPanel panelInicio, panelJuegos,
            panelConfigGeneral, panelConfigP, panelConfigLR, panelConfigSI,
            panelRankingP, panelRankingLR, panelRankingSI,
            panelPong, panelLodeRunner, panelSpaceInvaders;

    public SistemaDeJuegos() {
        setLayout(new GridLayout(1, 3, 10, 10));

        cardLayout = new CardLayout();
        this.setLayout(cardLayout);

        panelInicio = new JPanel();
        panelInicio.setLayout(new BorderLayout());

        // panel con titulo y configuracion general
        JPanel panelNorte = new JPanel();
        JLabel lblTitulo = new JLabel("PIPOO ARCADE", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        btnConfigGeneral = new JButton("Configuración General");
        btnConfigGeneral.addActionListener(this);

        panelNorte.add(lblTitulo);
        panelNorte.add(btnConfigGeneral);
        panelInicio.add(panelNorte, BorderLayout.NORTH);

        // panel con los 3 juegos
        panelJuegos = new JPanel();
        panelJuegos.setLayout(new GridLayout(1, 3, 15, 0));

        // panel pong
        panelPong = crearPanelJuego("PONG");
        btnPong = new JButton("¡Jugar!");
        btnConfigP = new JButton("Configuración");
        btnRankingP = new JButton("Ranking");
        registrarBotones(panelPong, btnPong, btnConfigP, btnRankingP);

        // panel lode runner
        panelLodeRunner = crearPanelJuego("LODE RUNNER");
        btnLodeRunner = new JButton("¡Jugar!");
        btnConfigLR = new JButton("Configuración");
        btnRankingLR = new JButton("Ranking");
        registrarBotones(panelLodeRunner, btnLodeRunner, btnConfigLR, btnRankingLR);

        // panel space invaders
        panelSpaceInvaders = crearPanelJuego("SPACE INVADERS");
        btnSpaceInvaders = new JButton("¡Jugar!");
        btnConfigSI = new JButton("Configuración");
        btnRankingSI = new JButton("Ranking");
        registrarBotones(panelSpaceInvaders, btnSpaceInvaders, btnConfigSI, btnRankingSI);

        //agregar cada panel de juego al panel de juegos
        panelJuegos.add(panelPong);
        panelJuegos.add(panelSpaceInvaders);
        panelJuegos.add(panelLodeRunner);

        panelInicio.add(panelJuegos, BorderLayout.CENTER);

        this.add(panelInicio, "INICIO");

        this.add(panelConfigGeneral, "CONFIG_GENERAL");
        this.add(panelConfigP, "CONFIG_PONG");
        this.add(panelConfigSI, "CONFIG_SPACE");
        this.add(panelConfigLR, "CONFIG_LODE");

        this.add(panelRankingP, "RANKING_PONG");
        this.add(panelRankingSI, "RANKING_SPACE_INV");
        this.add(panelRankingLR, "RANKING_LODE_R");
    }

    //metodos auxiliares para creacion de paneles
    private JPanel crearPanelJuego(String titulo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(titulo));
        return panel;
    }

    private void registrarBotones(JPanel panel, JButton btnJugar, JButton btnConfig, JButton btnRanking) {
        btnJugar.addActionListener(this);
        btnConfig.addActionListener(this);
        btnRanking.addActionListener(this);

        panel.add(Box.createVerticalStrut(20)); // espacio en blanco
        panel.add(btnJugar);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnConfig);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnRanking);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object origen = e.getSource();
        if (origen == btnPong) {
            juegoActual = new Pong();
        } if (origen == btnLodeRunner) {
            juegoActual = new LodeRunner();
        } if (origen == btnSpaceInvaders) {
            juegoActual = new SpaceInvaders();
        }
        else if (origen == btnConfigGeneral) {
            cardLayout.show(this, "CONFIG_GENERAL"); // Cambia la pantalla usando CardLayout
        } else if (origen == btnConfigP) {
            cardLayout.show(this, "CONFIG_PONG");
        } else if (origen == btnConfigSI) {
            cardLayout.show(this, "CONFIG_SPACE");
        } else if (origen == btnConfigLR) {
            cardLayout.show(this, "CONFIG_LODE");
        }
        else if (origen == btnRankingP) {
            cardLayout.show(this, "RANKING_PONG");
        } else if (origen == btnRankingSI) {
            cardLayout.show(this, "RANKING_SPACE_INV");
        } else if (origen == btnRankingLR) {
            cardLayout.show(this, "RANKING_LODE_R");
        }

        hiloJuego = new Thread(() -> juegoActual.run(1.0 / 60.0));
        hiloJuego.start();
    }

    public static void main(String[] args) {
        JFrame ventanaMenu = new JFrame("PIPOO Menú Principal");
        ventanaMenu.add(new SistemaDeJuegos());
        ventanaMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaMenu.pack();
        ventanaMenu.setLocationRelativeTo(null);
        ventanaMenu.setVisible(true);
    }
}
