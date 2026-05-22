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

    private JButton btnPong;
    private JButton btnLodeRunner;
    private JButton btnSpaceInvaders;

    private CardLayout cardLayout;
    private JPanel panelInicio, panelConfigP, panelConfigLR, panelConfigSI,
            panelRankingP, panelRankingLR, panelRankingSI,
            panelPong, panelLoadRunner, panelSpaceInvaders;

    public SistemaDeJuegos() {
        setLayout(new GridLayout(1, 3, 10, 10));

        cardLayout = new CardLayout();
        this.setLayout(cardLayout);

        panelInicio = new JPanel();

        panelPong = new JPanel();
        panelLoadRunner = new JPanel();
        panelSpaceInvaders = new JPanel();

        panelConfigP = new JPanel();
        panelConfigLR = new JPanel();
        panelConfigSI = new JPanel();

        panelRankingP = new JPanel();
        panelRankingLR = new JPanel();
        panelRankingSI = new JPanel();

        btnPong = new JButton("¡Jugar!");
        btnLodeRunner = new JButton("¡Jugar!");
        btnSpaceInvaders = new JButton("¡Jugar!");

        btnPong.addActionListener(this);
        btnLodeRunner.addActionListener(this);
        btnSpaceInvaders.addActionListener(this);

        //agregar los botones a los paneles
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnPong) {
            juegoActual = new Pong();
        }
        if (e.getSource() == btnLodeRunner) {
            juegoActual = new LodeRunner();
        }
        if (e.getSource() == btnSpaceInvaders) {
            juegoActual = new SpaceInvaders();
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
