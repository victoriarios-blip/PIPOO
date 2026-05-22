package pipoo.loderunner;
import pipoo.core.ElementoGrafico;

import java.awt.Graphics2D;


public class BarraDeManos extends ElementoGrafico {
    public BarraDeManos(double x, double y, double width, double height) {
        super(x, y, width, height);
    }
    @Override
    public void dibujar(Graphics2D g) {
        // TODO: Dibuja la barra por la cual se cuelgan los personajes
    }

    @Override
    public boolean colisionaCon(ElementoGrafico otro) {
        return false;
    }

    @Override
    public void reaccionarAColision(ElementoGrafico otro) {

    }
}
