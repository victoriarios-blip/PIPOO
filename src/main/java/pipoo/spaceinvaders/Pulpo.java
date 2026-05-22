package pipoo.spaceinvaders;

import pipoo.core.ElementoGrafico;

public class Pulpo extends Enemigo{
    public Pulpo(double x, double y) {
        super(x, y, 4, 15);
        this.valorPuntaje = 30;
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
    @Override
    public void disparar() {

    }
    // override de los metodos mover y disparar? o se hace en Enemigo??
}
