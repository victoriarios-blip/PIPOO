package pipoo.core;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class ElementoGrafico implements Colisionable {
    protected double posicionX;
    protected double posicionY;
    protected int ancho;
    protected int alto;
    protected BufferedImage imagen = null;
    protected boolean visible = true;

    public void dibujar(Graphics2D g) {
        if (visible && imagen != null) {
            g.drawImage(this.imagen, (int)posicionX, (int)posicionY, null);
        }
    }

    @Override
    public java.awt.Shape getLimites() {
        return new Rectangle2D.Double(posicionX, posicionY, ancho, alto);
    }

    // Comportamiento vacío por defecto (para el Fondo, etc.)
    @Override
    public void reaccionarAColision() {
    }
}
