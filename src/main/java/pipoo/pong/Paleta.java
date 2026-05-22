package pipoo.pong;

import pipoo.core.Colisionable;
import pipoo.core.ElementoGrafico;
import pipoo.core.Movible;

import java.awt.*;

public class Paleta extends Movible implements Colisionable {
    private double x, y;
    private int ancho, alto;
    private double velocidadPaleta;

    // constructor de paleta
    public Paleta(double x, double y, int ancho, int alto) {
        super(x, y, ancho, alto);
    }

    @Override
    public void mover(double delta) {  }         // de Movible

    @Override
    public void reaccionarAColision(ElementoGrafico otro) { }

    @Override
    public boolean colisionaCon(ElementoGrafico otro) { return this.intersects(otro);} // de Colisionable

    @Override
    public void dibujar(Graphics2D g) {  }       // de ElementoGrafico

    public void moverArriba(double delta) {
        this.y = this.y-(this.velocidadY*delta);
    }   // origen(0,0) arriba a la izq
    public void moverAbajo(double delta) {
        this.y = this.y+(this.velocidadY*delta);
    }

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