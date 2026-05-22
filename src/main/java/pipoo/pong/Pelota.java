package pipoo.pong;

import pipoo.core.Colisionable;
import pipoo.core.ElementoGrafico;
import pipoo.core.Movible;

import java.awt.*;

public class Pelota extends Movible implements Colisionable {
    private double x, y;
    private final int radio;

    // constructor de pelota
    public Pelota(double x, double y, int radio) {
        super(x, y, radio*2, radio*2);
        this.radio = radio;
        this.velocidadX = 200;
        this.velocidadY = 200;
    }
    @Override
    public void mover(double delta) {
        this.x += this.velocidadX * delta;
        this.y += this.velocidadY * delta;
    }

    @Override// de Movible
    public boolean colisionaCon(ElementoGrafico otro) {
        return this.intersects(otro);
    }

    @Override
    public void reaccionarAColision(ElementoGrafico otro) {
        if (otro instanceof Paleta) {
            this.velocidadX = -this.velocidadX;
        }
    }

    @Override
    public void dibujar(Graphics2D g) {  }      // de ElementoGrafico


    //rebotar pelota
    public void rebotarVertical() {
        velocidadY = -velocidadY;  // ella misma maneja su estado
    }

    public void rebotarHorizontal() {
        velocidadX = -velocidadX;
    }

    public void reiniciar() {
        // reinicia la pelota en el centro
        this.x = 400-(this.width / 2);
        this.y = 300-(this.height / 2);

        this.velocidadX = -this.velocidadX; //saca el q perdio el punto
    }


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