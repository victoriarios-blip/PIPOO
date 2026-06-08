package pipoo.core;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class ElementoGrafico extends java.awt.geom.Rectangle2D.Double implements Colisionable {
    protected BufferedImage bufferImage = null;
    protected boolean visible = true;

    public ElementoGrafico(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public void dibujar(Graphics2D g) {
        if (visible && bufferImage != null) {
            g.drawImage(this.bufferImage, (int)this.x, (int)this.y, (int)this.width, (int)this.height, null);
        }
    }
    public void setImagen(BufferedImage nuevaImagen) {
        this.bufferImage = nuevaImagen;
    }

    public boolean isVisible() {
        return visible;
    }

    @Override
    public void reaccionarAColision() {
    }

    @Override
    public boolean colisionaCon(ElementoGrafico otro) {
        if (otro == null) return false;
        return this.intersects(otro.getBounds());
    }

    @Override
    public Rectangle2D getLimites() {
        return this.getBounds();
    }
}
