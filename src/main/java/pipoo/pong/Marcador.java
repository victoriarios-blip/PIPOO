package pipoo.pong;

import pipoo.core.ElementoGrafico;

import java.awt.*;

public class Marcador extends ElementoGrafico {
    private int puntosJugador1, puntosJugador2;

    public Marcador(double x, double y, int ancho, int alto) {
        super(x, y, ancho, alto);
    }

    public void dibujar(Graphics2D g) { } // muestra el score en pantalla
    public void sumarPuntoJ1(){ }
    public void sumarPuntoJ2(){ } // incrementar puntaje

    @Override
    public boolean colisionaCon(ElementoGrafico otro) {
        return false;
    }

    @Override
    public void reaccionarAColision(ElementoGrafico otro) {

    }
    //public boolean hayGanador() { } // gana el que alcanza a 11 o 15 puntos
}