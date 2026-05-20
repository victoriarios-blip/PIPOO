package pipoo.spaceinvaders;

import pipoo.core.Movible;

public class NaveHeroe extends Movible implements Disparador {
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
}
