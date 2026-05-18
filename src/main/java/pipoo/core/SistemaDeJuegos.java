package core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import pipoo.core.Juego;
import pong.Pong;
// import spaceinvaders.SpaceInvaders;
// import loderunner.LodeRunner;

public class SistemaDeJuegos extends JPanel implements ActionListener {
    Juego juegoActual;
    Thread hiloJuego;

    public SistemaDeJuegos() {
        setLayout(new GridLayout(1, 3, 10, 10)); // Botones en fila

        JButton btnPong = new JButton("Jugar Pong");
        btnPong.addActionListener(this);
        add(btnPong);

        // Agregar botones para Lode Runner y Space Invaders aquí...
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Jugar Pong")) {
            juegoActual = new Pong();
            // Inicia el juego en un hilo separado como pide el recurso de la cátedra
            hiloJuego = new Thread(() -> juegoActual.run(1.0 / 60.0));
            hiloJuego.start();
        }
    }

    public static void main(String[] args) {
        JFrame ventanaMenu = new JFrame("Retro Reobot 2026 - Menú Principal");
        ventanaMenu.add(new SistemaDeJuegos());
        ventanaMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaMenu.pack();
        ventanaMenu.setLocationRelativeTo(null);
        ventanaMenu.setVisible(true);
    }
}
