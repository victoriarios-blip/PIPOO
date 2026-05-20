package pipoo.core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import pipoo.loderunner.LodeRunner;
import pipoo.pong.Pong;
import pipoo.spaceinvaders.SpaceInvaders;

public class SistemaDeJuegos extends JPanel implements ActionListener {
    Juego juegoActual;
    Thread hiloJuego;

    private CardLayout cardLayout;
    private JPanel panelMenu, panelConfig, panelRanking, panelPong, panelLoadRunner, panelSpaceInvaders;

    public SistemaDeJuegos() {
        setLayout(new GridLayout(1, 3, 10, 10));

        cardLayout = new CardLayout();
        this.setLayout(cardLayout);

        panelMenu = new JPanel();
        panelConfig = new JPanel();
        panelRanking = new JPanel();
        panelPong = new JPanel();
        panelLoadRunner = new JPanel();
        panelSpaceInvaders = new JPanel();

        JButton btnPong = new JButton("Pong");
        btnPong.addActionListener(this);
        add(btnPong);

        JButton btnLodeRunner = new JButton("Lode Runner");
        btnLodeRunner.addActionListener(this);
        add(btnLodeRunner);

        JButton btnSpaceInvaders = new JButton("Space Invaders");
        btnSpaceInvaders.addActionListener(this);
        add(btnSpaceInvaders);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Pong")) {
            juegoActual = new Pong();
        }
        if (e.getActionCommand().equals("Lode Runner")) {
            juegoActual = new LodeRunner();
        }
        if (e.getActionCommand().equals("Space Invaders")) {
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
