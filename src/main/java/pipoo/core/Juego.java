package pipoo.core;

import com.entropyinteractive.JGame;

import java.awt.*;

public abstract class Juego extends JGame {
    protected int dificultad;

    public Juego(String titulo, int ancho, int alto) {
        super(titulo, ancho, alto);
}

    protected abstract void actualizarPuntaje();
    protected abstract void detectarColisiones();

    @Override
    public void gameStartup() {}

    @Override
    public void gameUpdate(double v) {}

    @Override
    public void gameDraw(Graphics2D graphics2D) {}

    @Override
    public void gameShutdown(){}
}
