package pipoo.spaceinvaders;
import pipoo.core.ElementoGrafico;
import pipoo.core.Movible;
import pipoo.core.recursos.Borde;

import java.awt.*;

public class Proyectil extends Movible {
    public enum Origen {HEROE, ENEMIGO}

    private Origen origen;
    private static final double VELOCIDAD = 300;

    public Proyectil(double x, double y, Origen origen) {
        super(x, y, 4, 15);
        this.origen = origen;
        this.velocidadY = (origen == Origen.HEROE) ? -VELOCIDAD : VELOCIDAD;
    }

    @Override
    public void mover(double delta) {
        this.y += this.velocidadY * delta;
    }

    @Override
    public boolean colisionaCon(ElementoGrafico otro) {
        return this.intersects((java.awt.geom.Rectangle2D) otro);
    }

    @Override
    public void reaccionarAColision(ElementoGrafico c) {
        if (c instanceof NaveHeroe && origen == Origen.ENEMIGO) {
            this.visible = false;
        }
        if (c instanceof NaveNodriza && origen == Origen.HEROE) {
            this.visible = false;
        }
        if (c instanceof Borde) {
            this.visible = false;
        }
        if (c instanceof Escudo) {
            this.visible = false;
        }
        if (c instanceof Enemigo && origen == Origen.HEROE) {
            this.visible = false;
        }
        if (c instanceof Proyectil) {
            this.visible = false;
        }
    }

    public Origen getOrigen() {
        return origen;
    }

    @Override
    public void dibujar(Graphics2D g) {
        if (visible && bufferImage != null) {
            g.drawImage(bufferImage, (int) this.x, (int) this.y, (int) this.width, (int) this.height, null);
        }
    }
}
