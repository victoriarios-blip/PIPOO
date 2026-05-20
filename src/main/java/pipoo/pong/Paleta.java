package pipoo.pong;

import pipoo.core.Colisionable;
import pipoo.core.ElementoGrafico;
import pipoo.core.Movible;

import java.awt.*;

public class Paleta extends Movible implements Colisionable {
    private double x, y;
    private int ancho, alto;
    private double velocidad;

    // constructor de paleta
    public Paleta(double x, double y, int ancho, int alto) {
        super(x, y, ancho, alto);
    }

    public void mover(double delta) {  }         // de Movible
    public void reaccionarAColision(ElementoGrafico otro) { }
    public boolean colisionaCon(ElementoGrafico otro) { return true;} // de Colisionable
    public void dibujar(Graphics2D g) {  }       // de ElementoGrafico

    public void moverArriba(double delta) {  }   // propios: movimiento controlado
    public void moverAbajo(double delta) {  }

    //getters
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getAlto() { return this.alto; }

    public int getAncho() {
        return this.ancho;
    }
}