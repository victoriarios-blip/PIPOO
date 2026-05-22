package pipoo.spaceinvaders;

import pipoo.core.ElementoGrafico;

public class Cangrejo extends Enemigo{

    public Cangrejo(double x, double y) {
        super(x, y, 4, 15);
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

    // override de los metodos mover y disparar? o se hace en Enemigo??
}
