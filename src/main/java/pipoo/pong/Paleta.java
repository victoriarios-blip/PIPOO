package pipoo.pong;

import pipoo.core.Colisionable;
import pipoo.core.ElementoGrafico;
import pipoo.core.Movible;

import java.awt.*;

public class Paleta extends Movible implements Colisionable {
    private static final double VELOCIDAD_BASE = 300.0;
    private double velocidadActual = VELOCIDAD_BASE;


    // constructor de paleta
    public Paleta(double x, double y, int ancho, int alto) {
        super(x, y, ancho, alto);
    }

    @Override
    public void mover(double delta) {
        this.y += (this.velocidadY * delta);
        // Limitar que la paleta no se salga de la pantalla
        if (this.y < 0) this.y = 0;
        if (this.y + this.height > 600) this.y = 600-this.height;
    }

    @Override
    public void reaccionarAColision(ElementoGrafico otro) { }

    @Override
    public boolean colisionaCon(ElementoGrafico otro) { return this.intersects(otro);} // de Colisionable

    public void moverArriba(double delta) {
        this.velocidadY = -velocidadActual; // Seteamos la velocidad negativa
        this.mover(delta); // Llamamos al movimiento físico
    }

    public void moverAbajo(double delta) {
        this.velocidadY = velocidadActual; // Seteamos la velocidad positiva
        this.mover(delta);
    }

    public void incrementarVelocidad() {
        this.velocidadActual *= 1.10;     }

    public void resetVelocidad() {
        this.velocidadActual = VELOCIDAD_BASE;
        this.velocidadY = 0; // Frenar la paleta al reiniciar
    }

    //getters y setters
    public double getX() { return this.x; }
    public double getY() { return this.y; }
    public int getAlto() { return (int)this.height; }
    public int getAncho() { return (int)this.width; }
    public double getVelocidadDesplazamiento() {
        return velocidadActual;
    }

}