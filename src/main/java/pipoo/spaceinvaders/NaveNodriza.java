package pipoo.spaceinvaders;

import pipoo.core.ElementoGrafico;
import pipoo.core.Movible;

import java.awt.*;

public class NaveNodriza extends Movible {
    private int valorPuntaje;

    public NaveNodriza(double x, double y) {
        super(x, y, 48, 24); // x3 como el resto de sprites
        this.velocidadX = 100;
        this.valorPuntaje = (int)(Math.random() * 251) + 50;
    }

    @Override
    public void mover(double delta) {
        this.x += this.velocidadX * delta;
        // desaparece cuando sale de la pantalla
        if (this.x > 800 || this.x + this.width < 0) {
            this.visible = false;
        }
    }

    @Override
    public void dibujar(Graphics2D g) {
        if (visible && bufferImage != null) {
            g.drawImage(bufferImage, (int)this.x, (int)this.y, (int)this.width, (int)this.height, null);
        }
    }

    @Override
    public boolean colisionaCon(ElementoGrafico otro) {
        return this.intersects((java.awt.geom.Rectangle2D) otro);
    }

    @Override
    public void reaccionarAColision(ElementoGrafico otro) {
        if (otro instanceof Proyectil p && p.getOrigen() == Proyectil.Origen.HEROE) {
            this.visible = false;
        }
    }

    public int getValorPuntaje() { return valorPuntaje; }
}