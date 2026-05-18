package pipoo.pong;

import pipoo.core.Colisionable;
import pipoo.core.Movible;

import java.awt.*;

public class Paleta extends Movible implements Colisionable {
    private double x, y;
    private int ancho, alto;
    private double velocidad;

    // constructor de paleta
    public Paleta(double x, double y, int ancho, int alto) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
    }

    public void mover(double delta) {  }         // de Movible
    //public boolean colisionaCon(Colisionable otro) {  } // de Colisionable
    public void dibujar(Graphics2D g) {  }       // de ElementoGrafico

    public void moverArriba(double delta) {  }   // propios: movimiento controlado
    public void moverAbajo(double delta) {  }
    // getters: getX, getY, getAncho, getAlto
}