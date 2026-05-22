package pipoo.spaceinvaders;

import pipoo.core.ElementoGrafico;
import pipoo.core.Movible;

public class NaveNodriza extends Movible {
    public NaveNodriza(double x, double y) {
        super(x, y, 4, 15);
        //velocidades
    }

    @Override
    public void mover(double delta) {

    }

    @Override
    public boolean colisionaCon(ElementoGrafico otro) {
        return false;
    }

    @Override
    public void reaccionarAColision(ElementoGrafico otro) {

    }
}
