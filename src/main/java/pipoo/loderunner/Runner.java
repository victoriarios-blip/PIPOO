package pipoo.loderunner;

import pipoo.core.Movible;

public abstract class Runner extends Movible {
    protected boolean estaCayendo;

    public Runner(double x, double y, int ancho, int alto) {
        super(x, y, ancho, alto); // Llama al constructor de Movible
        this.estaCayendo = false;
        // Las variables velocidadX y velocidadY ya existen aquí de forma invisible
    }
}
