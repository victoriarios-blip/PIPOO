package pipoo.spaceinvaders;

import pipoo.core.ElementoGrafico;

public class Calamar extends Enemigo{
    public Calamar(double x, double y) {
        super(x, y, 30, 30);
        this.valorPuntaje = 10;
        //velocidades
    }

    @Override
    public boolean colisionaCon(ElementoGrafico otro) {
        return false;
    }

    @Override
    public void reaccionarAColision(ElementoGrafico otro) {

    }

    @Override
    public boolean colosionaCon(ElementoGrafico otro) {
        return false;
    }
}
