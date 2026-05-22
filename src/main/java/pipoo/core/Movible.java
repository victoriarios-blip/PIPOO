package pipoo.core;

import pipoo.core.ElementoGrafico;

public abstract class Movible extends ElementoGrafico {
        protected double velocidadX;
        protected double velocidadY;

        public Movible(double x, double y, double width, double height) {
            super(x, y, width, height);

            this.velocidadX = 0;
            this.velocidadY = 0;
        }

        public double getVelocidadX() {
            return velocidadX;
        }

        public void setVelocidadX(double velocidadX) {
            this.velocidadX = velocidadX;
        }

        public double getVelocidadY() {
            return velocidadY;
        }

        public void setVelocidadY(double velocidadY) {
            this.velocidadY = velocidadY;
        }

        public abstract void mover(double delta);
}


