package pipoo.pong;

import pipoo.core.Colisionable;
import pipoo.core.ElementoGrafico;
import pipoo.core.Movible;

public class Pelota extends Movible implements Colisionable {
    private final int radio;
    private static final double VELOCIDAD_INICIAL = 350.0;
    // constructor de pelota
    public Pelota(double x, double y, int ancho, int alto) {
        super(x, y, ancho, alto);
        this.radio = ancho/2;
        this.velocidadX = VELOCIDAD_INICIAL;
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
            this.velocidadX = -this.velocidadX * 1.15; // Invertir y acelerar

            double centroPaleta = otro.y + (otro.height / 2);
            double centroPelota = this.y + (this.height / 2);
            double impactoRelativo = (centroPelota - centroPaleta)/(otro.height / 2);
            double velocidadMaximaY = 400.0;
            this.velocidadY = impactoRelativo * velocidadMaximaY;

            if (this.velocidadX > 0) {
                this.x = otro.x + otro.width + 1;
            } else {
                this.x = otro.x - this.width - 1;
            }
        }
    }

    //rebotar pelota
    public void rebotarVertical(double limiteY, boolean esTecho) {
        this.velocidadY = -this.velocidadY;  // ella misma maneja su estado
        if (esTecho) {
            this.y = limiteY + 1; // La posiciona justo debajo del borde superior
        } else {
            this.y = limiteY - this.height - 1; // Justo arriba del borde inferior
        }
    }


    //direccionSaque = -1 izquierda, 1 derecha
    public void reiniciar(int direccionSaque) {
        // reinicia la pelota en el centro
        this.velocidadX = VELOCIDAD_INICIAL * direccionSaque;
        this.x = 400;
        this.y = 300;
        this.velocidadY = (Math.random() * 100) - 50; //angulo random
    }


    //getters
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }





    public int getRadio() {
        return radio;
    }
}