package pipoo.spaceinvaders;

import pipoo.core.ElementoGrafico;
import pipoo.core.Movible;

public class NaveNodriza extends Movible {
    private int valorPuntaje;
    public NaveNodriza(double x, double y) {
        super(x, y, 60, 25);
        this.velocidadX = 100;
        this.valorPuntaje = (int)(Math.random() * 251) + 50;
        //el juego original lo calcula por cantidad de disparos
    }

    @Override
    public void mover(double delta) {
        this.x += this.velocidadX * delta;
    }

    @Override
    public boolean colisionaCon(ElementoGrafico otro) {
        return false;
    }

    @Override
    public void reaccionarAColision(ElementoGrafico otro) {
        // las balas restan vida
    }
}
