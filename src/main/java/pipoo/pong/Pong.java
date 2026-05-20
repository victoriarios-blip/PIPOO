package pipoo.pong;

import pipoo.core.Juego;
import com.entropyinteractive.Keyboard;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.event.KeyEvent;

public class Pong extends Juego {

    private Pelota pelota;
    private Paleta paleta1, paleta2;
    private Marcador marcador;

    public Pong() {
        super("Retro Pong", 800, 600); // titulo y tamaño de ventana
    }

    @Override
    public void gameStartup() {
        pelota   = new Pelota(400, 300, 8);
        paleta1  = new Paleta(20,  250, 10, 80);   // jugador con W y S
        paleta2  = new Paleta(770, 250, 10, 80);   // jugador con flechitas
        marcador = new Marcador(200, 200, 200,200); //ver estos valores
    }

    @Override
    public void gameUpdate(double delta) {
        Keyboard teclado = this.getKeyboard();

        // Controles jugador 1
        if (teclado.isKeyPressed(KeyEvent.VK_W)) {
            paleta1.moverArriba(delta);
        }
        if (teclado.isKeyPressed(KeyEvent.VK_S)) {
            paleta1.moverAbajo(delta);
        }

        // Controles jugador 2
        if (teclado.isKeyPressed(KeyEvent.VK_UP)) {
            paleta2.moverArriba(delta);
        }
        if (teclado.isKeyPressed(KeyEvent.VK_DOWN)) {
            paleta2.moverAbajo(delta);
        }
        pelota.mover(delta);
        detectarColisiones();
        actualizarPuntaje();
    }

    @Override
    public void gameDraw(Graphics2D g) {
        // Limpiar fondo
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        pelota.dibujar(g);
        paleta1.dibujar(g);
        paleta2.dibujar(g);
        marcador.dibujar(g);
    }

    @Override
    public void gameShutdown() {
        // SistemaDeJuego con ranking:
        // sistemaDeJuego.guardarPuntaje("J1", marcador.getPuntosJ1());
        // sistemaDeJuego.guardarPuntaje("J2", marcador.getPuntosJ2());

        pelota = null;
        paleta1 = null;
        paleta2 = null;
        marcador = null;
    }

    @Override
    protected void detectarColisiones() {
        // Rebotee
        if (pelota.getY() - pelota.getRadio() <= 0 ||
                pelota.getY() + pelota.getRadio() >= getHeight()) {
            pelota.rebotarVertical();
        }

        // Colisión con paletas -> VER EL TEMA DE reaccionarAColisiones, PORQ RECIBE PALETA1 ACA
        if (pelota.colisionaCon(paleta1)) {
            pelota.reaccionarAColision(paleta1);
        }
        if (pelota.colisionaCon(paleta2)) {
            pelota.reaccionarAColision(paleta2);
        }
    }

    @Override
    protected void actualizarPuntaje() {
        // Pelota sale por la izquierda → punto J2
        if (pelota.getX() < 0) {
            marcador.sumarPuntoJ2();
            pelota.reiniciar();
        }
        // Pelota sale por la derecha → punto J1
        if (pelota.getX() > getWidth()) {
            marcador.sumarPuntoJ1();
            pelota.reiniciar();
        }
    }
}




