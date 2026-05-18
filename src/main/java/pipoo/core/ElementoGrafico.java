package pipoo.core;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class ElementoGrafico extends Rectangle2D.Double implements Colisionable {
    protected double posicionX;
    protected double posicionY;
    protected double ancho;
    protected double alto;
    protected BufferedImage imagen = null;
    protected boolean visible = true;

    public ElementoGrafico(double x, double y, double ancho, double alto) {
        this.posicionX = x;
        this.posicionY = y;
        this.ancho = ancho;
        this.alto = alto;
    }

    public void dibujar(Graphics2D g) {
        if (visible && imagen != null) {
            g.drawImage(this.imagen, (int)posicionX, (int)posicionY, null);
        }
    }

    @Override
    public java.awt.Shape getLimites() {
        return new Rectangle2D.Double(posicionX, posicionY, ancho, alto);
    }

    @Override
    public void reaccionarAColision() {
    }
}
