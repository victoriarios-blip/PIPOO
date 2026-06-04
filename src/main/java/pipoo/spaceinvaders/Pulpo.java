package pipoo.spaceinvaders;

import pipoo.core.ElementoGrafico;

public class Pulpo extends Enemigo{
    public Pulpo(double x, double y) {
        super(x, y, 48, 24);
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
    public Proyectil disparar() {

        return null;
    }
    public int getValorPuntaje(){
        return valorPuntaje;
    }
}
