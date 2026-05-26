package pipoo.loderunner;

import pipoo.core.ElementoGrafico;

public class Bonus extends ElementoGrafico {
    public Bonus(double x, double y,double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public boolean colisionaCon(ElementoGrafico otro) {
        return false;
    }

    @Override
    public void reaccionarAColision(ElementoGrafico otro) {

    }
}
