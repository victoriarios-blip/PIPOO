package pipoo.loderunner;

import pipoo.core.ElementoGrafico;

public class Plataforma extends ElementoGrafico {
    public Plataforma(double x, double y, double width, double height) {
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
