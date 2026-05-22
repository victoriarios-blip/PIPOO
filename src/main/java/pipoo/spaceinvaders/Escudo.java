package pipoo.spaceinvaders;

import pipoo.core.ElementoGrafico;

public class Escudo extends ElementoGrafico {
    private int resistencia;

    public Escudo(double x, double y) {
        super(x, y, 4, 15);
        this.resistencia = 100;
        //velocidades
    }

    public void recibirDanio() {
        // degradar visualmente el escudo
    }

    @Override
    public void reaccionarAColision() {
        recibirDanio();
    }

    @Override
    public boolean colisionaCon(ElementoGrafico otro) {
        return false;
    }

    @Override
    public void reaccionarAColision(ElementoGrafico otro) {

    }
}
