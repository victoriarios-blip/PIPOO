package pipoo.core.recursos;

import pipoo.core.ElementoGrafico;

import java.awt.*;

public class Marcador extends ElementoGrafico {

    public Marcador(double x, double y, int ancho, int alto) {
        super(x, y, ancho, alto);
    }

    public void dibujar(Graphics2D g) { } // muestra el score en pantalla

    @Override
    public boolean colisionaCon(ElementoGrafico otro) {
        return false;
    }

    @Override
    public void reaccionarAColision(ElementoGrafico otro) {
    }
}