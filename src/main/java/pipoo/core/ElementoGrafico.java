package pipoo.core;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class ElementoGrafico extends java.awt.geom.Rectangle2D.Double implements Colisionable {
    protected BufferedImage imagen = null;
    protected boolean visible = true;

    public ElementoGrafico(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public void dibujar(Graphics2D g) {
        if (visible && imagen != null) {
            g.drawImage(this.imagen, (int)this.x, (int)this.y, null);
        }
    }

    @Override
    public Shape getLimites() {
        return this;
    }

    @Override
    public void reaccionarAColision() {
    }
}
