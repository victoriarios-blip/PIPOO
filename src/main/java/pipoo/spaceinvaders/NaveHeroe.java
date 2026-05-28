package pipoo.spaceinvaders;

import pipoo.core.ElementoGrafico;
import pipoo.core.Movible;

public class NaveHeroe extends Movible implements Disparador {
    public NaveHeroe(double x, double y) {
        super(x, y, 40, 20);
        //velocidades
    }

    @Override
    public void mover(double delta) {
        this.x += this.velocidadX * delta;
    }

    @Override
    public Proyectil disparar() {
        //instanciar un new Proyectil(...) hacia arriba

        return null;
    }

    @Override
    public boolean colisionaCon(ElementoGrafico otro) {
        return false;
    }

    @Override
    public void reaccionarAColision(ElementoGrafico otro) {

    }
}
