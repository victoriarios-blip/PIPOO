package pipoo.pong;

import pipoo.core.Juego;
import com.entropyinteractive.Keyboard;
import java.awt.Graphics2D;
import java.awt.Color;

public class Pong extends Juego{
    //declarar paleta, pelota, etc de clases

    public Pong() {
        super("Retro Pong", 800, 600); // Título y tamaño de ventana
    }

    @Override
    public void gameStartup() {
        // Inicializar posiciones de paletas y pelota [12, 13]
        System.out.println("Iniciando Pong...");
    }

    @Override
    public void gameUpdate(double delta) {
        Keyboard teclado = this.getKeyboard(); // Para leer teclas [14]

        // 1. Mover objetos (ej: pelota.mover(delta))
        // 2. Controlar entradas del jugador
        detectarColisiones();
        actualizarPuntaje();
    }

    @Override
    public void gameDraw(Graphics2D g) {
        // Limpiar fondo
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Invocar el método dibujar(g) de sus elementos (Pelota, Paletas, Fondo)
    }

    @Override
    public void gameShutdown() {
        System.out.println("Cerrando Pong y volviendo al menú...");
        // Guardar puntajes en Ranking [2]
    }

    @Override
    protected void detectarColisiones() {
        // Lógica si la pelota toca la paleta o los bordes
    }

    @Override
    protected void actualizarPuntaje() {
        // Lógica si la pelota sale de la pantalla
    }
}
}



