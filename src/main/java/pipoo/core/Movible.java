package pipoo.core;

import pipoo.core.ElementoGrafico;

import java.awt.*;

public abstract class Movible extends ElementoGrafico {
        protected double velocidadX;
        protected double velocidadY;

        public Movible(double x, double y, double width, double height) {
            super(x, y, width, height);
            this.velocidadX = 0;
            this.velocidadY = 0;
        }

    // setters
    public void setVelocidadX(double vx) { this.velocidadX = vx; }
    public void setVelocidadY(double vy) { this.velocidadY = vy; }

    public double getVelocidadX() {return velocidadX;}
    public double getVelocidadY() {return velocidadY;}

    public abstract void mover(double delta);

    public abstract void dibujar(Graphics2D g);
}


