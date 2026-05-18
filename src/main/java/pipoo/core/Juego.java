package pipoo.core;

import com.entropyinteractive.JGame;

import java.awt.*;

public abstract class Juego extends JGame {
    protected int dificultad;

    public Juego(String titulo, int ancho, int alto) {
        super(titulo, ancho, alto);
}

    // Estos métodos de su UML ahora los forzamos a implementarse en cada juego particular
    protected abstract void actualizarPuntaje();
    protected abstract void detectarColisiones();

    // Los métodos abstractos obligatorios heredados de JGame [8-10] que cada juego definirá:
    @Override
    public void gameStartup() {

    }

    @Override
    public void gameUpdate(double v) {

    }

    @Override
    public void gameDraw(Graphics2D graphics2D) {

    }

    @Override
    public void gameShutdown(){
    }
}
