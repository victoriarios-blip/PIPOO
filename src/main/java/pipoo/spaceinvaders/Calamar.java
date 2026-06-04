package pipoo.spaceinvaders;

import pipoo.core.ElementoGrafico;

public class Calamar extends Enemigo{
    public Calamar(double x, double y) {
        super(x, y, 45, 24);
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
}
