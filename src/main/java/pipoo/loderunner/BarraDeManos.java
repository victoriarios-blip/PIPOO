package pipoo.loderunner;
import pipoo.core.ElementoGrafico;

import java.awt.Graphics2D;


public class BarraDeManos extends ElementoGrafico {
    public BarraDeManos(double x, double y, int ancho, int alto) {
        super(x, y, ancho, alto);
    }

    @Override
    public void dibujar(Graphics2D g) {
        // TODO: Dibuja la barra por la cual se cuelgan los personajes
    }

}
