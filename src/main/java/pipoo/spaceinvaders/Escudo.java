package pipoo.spaceinvaders;

import pipoo.core.ElementoGrafico;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Escudo extends ElementoGrafico {
    private int resistencia; // 4 = intacto, 0 = destruido

    // daño
    private boolean recibioDanioEsteFrame = false;

    private BufferedImage escudoIntacto;
    private BufferedImage escudo1daño;
    private BufferedImage escudo2daño;
    private BufferedImage escudo3daño;
    private BufferedImage escudo4daño;

    public Escudo(double x, double y) {
        super(x, y, 72, 48); // dimensiones reales del escudo
        this.resistencia = 4;
    }

    public void setImgIntacto(BufferedImage img) { this.escudoIntacto = img; this.bufferImage = img; }
    public void setEscudo1daño(BufferedImage img)  { this.escudo1daño = img; }
    public void setEscudo2daño(BufferedImage img)  { this.escudo2daño = img; }
    public void setEscudo3daño(BufferedImage img)  { this.escudo3daño = img; }
    public void setEscudo4daño(BufferedImage img)  { this.escudo4daño = img; }

    public void recibirDanio() {
        if (recibioDanioEsteFrame) return;
        recibioDanioEsteFrame = true;
        if (resistencia <= 0) return;
        resistencia--;
        switch (resistencia) {
            case 3 -> this.bufferImage = escudo1daño;
            case 2 -> this.bufferImage = escudo2daño;
            case 1 -> this.bufferImage = escudo3daño;
            case 0 -> { this.bufferImage = escudo4daño; this.visible = false; }
        }
    }

    public void resetFrame() { recibioDanioEsteFrame = false; }

    @Override
    public boolean colisionaCon(ElementoGrafico otro) {
        return this.intersects((java.awt.geom.Rectangle2D) otro);
    }

    @Override
    public void reaccionarAColision(ElementoGrafico otro) {
        if (otro instanceof Proyectil) {
            recibirDanio(); // ambos proyectiles dañan el escudo
        }
    }

    @Override
    public void dibujar(Graphics2D g) {
        if (visible && bufferImage != null) {
            g.drawImage(bufferImage, (int)this.x, (int)this.y, (int)this.width, (int)this.height, null);
        }
    }

    @Override
    public void reaccionarAColision() {
        recibirDanio();
    }
}