package pipoo.pong;

import pipoo.core.Colisionable;
import pipoo.core.Movible;

import java.awt.*;

public class Pelota extends Movible implements Colisionable {
    private double x, y;
    private double velocidadX, velocidadY;
    private int radio;

    // constructor de pelota
    public Pelota(double x, double y, int radio) {
        super(x, y, radio*2, radio*2);
        this.radio = radio;
        this.velocidadX = 200;
        this.velocidadY = 200;
    }

    public void mover(double delta) {  }        // de Movible
    //public boolean colisionaCon(Colisionable otro) {  } // de Colisionable
    public void dibujar(Graphics2D g) {  }      // de ElementoGrafico


    //rebotar pelota
    public void rebotarVertical() {
        velocidadY = -velocidadY;  // ella misma maneja su estado
    }

    public void rebotarHorizontal() {
        velocidadX = -velocidadX;
    }

    public void reiniciar() {  } // volver al centro


    //getters
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getRadio() {
        return radio;
    }
}