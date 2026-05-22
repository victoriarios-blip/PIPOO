package pipoo.loderunner;
import pipoo.core.ElementoGrafico;

import java.awt.Graphics2D;


public class Heroe extends Runner{
    private int cantOro;
    private boolean enEscalera;


    public Heroe(double x, double y, int width, int height) {
        super(x, y, width, height);
        this.cantOro = 0;
        this.enEscalera = false;
    }

    public void setVelocidadX(double velocidadX) {
        this.velocidadX = velocidadX;
    }

    public void setVelocidadY(double velocidadY) {
        this.velocidadY = velocidadY;
    }

    public void cavar() {
        // La lógica de instanciar el pozo en el ArrayList será controlada desde el juego.
        // TODO a futuro: Cambiar el "frame" de la animación del héroe o hacer sonar un efecto.
    }

    @Override
    public void dibujar(Graphics2D g) {
        // TODO: Dibujar el rectángulo o la imagen del héroe.
    }

    public void recolectarOro() {
        this.cantOro++;
    }

    @Override
    public void mover(double delta) {
        if (estaCayendo) {
            this.velocidadX = 0;
            this.velocidadY = 5;
        } else {
            // Lógica de lectura de teclado para moverse
        }

        // Ahora usamos directamente this.x y this.y heredados de Rectangle2D.Double [2]
        this.x += (this.velocidadX * delta * 60);
        this.y += (this.velocidadY * delta * 60);
    }

    public boolean isEnEscalera() {
        return enEscalera;
    }

    public void setEnEscalera(boolean enEscalera) {
        this.enEscalera = enEscalera;
    }

    @Override
    public boolean colisionaCon(ElementoGrafico otro) {
        return false;
    }

    @Override
    public void reaccionarAColision(ElementoGrafico otro) {

    }
}

