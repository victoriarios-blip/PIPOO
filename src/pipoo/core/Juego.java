package pipoo.core;

import com.entropyinteractive.JGame;
import java.awt.Graphics2D;

public abstract class Juego extends JGame {
    protected int dificultad;

    public Juego(String titulo, int ancho, int alto) {
        super(titulo, ancho, alto);
    }

    // Estos métodos de su UML ahora los forzamos a implementarse en cada juego particular
    protected abstract void actualizarPuntaje();
    protected abstract void detectarColisiones();

    // Los métodos abstractos obligatorios heredados de JGame [8-10] que cada juego definirá:
    // public abstract void gameStartup();
    // public abstract void gameUpdate(double delta);
    // public abstract void gameDraw(Graphics2D g);
    // public abstract void gameShutdown();
}
