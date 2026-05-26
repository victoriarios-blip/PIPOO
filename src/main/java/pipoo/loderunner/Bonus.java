package pipoo.loderunner;

import pipoo.core.ElementoGrafico;

public class Bonus extends ElementoGrafico {
    public Bonus(double x, double y, int ancho, int alto) {
        super(x, y, ancho, alto);
    }

    @Override
    public boolean colisionaCon(ElementoGrafico otro) {
        return false;
    }

    @Override
    public void reaccionarAColision(ElementoGrafico otro) {

    }
}
