package pipoo.core.recursos;

import pipoo.core.ElementoGrafico;

public class Borde extends ElementoGrafico {
    public Borde(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void reaccionarAColision(ElementoGrafico otro) {

    }

    @Override
    public boolean colisionaCon(ElementoGrafico otro) {
        return false;
    }
}
