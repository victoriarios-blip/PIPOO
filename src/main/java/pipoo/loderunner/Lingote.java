package pipoo.loderunner;
import pipoo.core.ElementoGrafico;

import java.awt.Graphics2D;

public class Lingote extends ElementoGrafico {

    public Lingote(double x, double y, int ancho, int alto) {
        super(x, y, ancho, alto);
    }

    @Override
    public void dibujar(Graphics2D g) {
        // TODO: Dibuja el oro. Recuerda que si el Héroe lo toca, debe sumar puntos
    }
}
