package pipoo.core;

import pipoo.core.ElementoGrafico;

public abstract class Movible extends ElementoGrafico {
        // Atributos propios que añade esta clase
        protected double velocidadX;
        protected double velocidadY;

        public Movible(double x, double y, int ancho, int alto) {
            // 1. Envía los datos espaciales al constructor de ElementoGrafico
            super(x, y, ancho, alto);

            // 2. Inicializa las velocidades en 0 por defecto
            this.velocidadX = 0;
            this.velocidadY = 0;
        }

        // método abstracto obligatorio para las clases hijas
        public abstract void mover(double delta);
}


