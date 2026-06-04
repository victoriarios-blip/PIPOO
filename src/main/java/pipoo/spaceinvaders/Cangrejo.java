package pipoo.spaceinvaders;

import pipoo.core.ElementoGrafico;

public class Cangrejo extends Enemigo{

    public Cangrejo(double x, double y) {
        super(x, y, 48, 24);
        this.valorPuntaje = 20;
        //velocidades
    }

    @Override
    public boolean colisionaCon(ElementoGrafico otro) {
        return false;
    }

    @Override
    public void reaccionarAColision(ElementoGrafico otro) {

    }
}
