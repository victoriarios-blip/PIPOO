package pipoo.loderunner;
import pipoo.core.ElementoGrafico;

import java.awt.Graphics2D;

public class Lingote extends ElementoGrafico {

    public Lingote(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void dibujar(Graphics2D g) {
        // TODO: Dibuja el oro. Recuerda que si el Héroe lo toca, debe sumar puntos
    }

    @Override
    public boolean colisionaCon(ElementoGrafico otro) {
        return false;
    }

    @Override
    public void reaccionarAColision(ElementoGrafico otro) {

    }
}
