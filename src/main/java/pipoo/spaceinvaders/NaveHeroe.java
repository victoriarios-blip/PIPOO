package pipoo.spaceinvaders;

import pipoo.core.ElementoGrafico;
import pipoo.core.Movible;

public class NaveHeroe extends Movible implements Disparador {
    public NaveHeroe(double x, double y) {
        super(x, y, 4, 15);
        //velocidades
    }

    @Override
    public void mover(double delta) {
        // lógica de movimiento (horizontal)
    }

    @Override
    public void disparar() {
        // new Proyectil()
    }

    @Override
    public void reaccionarAColision() {
        // restar vida
    }

    @Override
    public boolean colisionaCon(ElementoGrafico otro) {
        return false;
    }

    @Override
    public void reaccionarAColision(ElementoGrafico otro) {

    }
}
