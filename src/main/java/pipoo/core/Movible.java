package pipoo.core;

import pipoo.core.ElementoGrafico;

public abstract class Movible extends ElementoGrafico {
        protected double velocidadX;
        protected double velocidadY;

        public Movible(double x, double y, int ancho, int alto) {
            super(x, y, ancho, alto);

            this.velocidadX = 0;
            this.velocidadY = 0;
        }

        public abstract void mover(double delta);
}


