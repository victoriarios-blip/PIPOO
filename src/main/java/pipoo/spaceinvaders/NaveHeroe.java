package pipoo.spaceinvaders;

import pipoo.core.ElementoGrafico;
import pipoo.core.Movible;
import pipoo.core.recursos.Borde;

import java.awt.*;
import java.awt.image.BufferedImage;

public class NaveHeroe extends Movible implements Disparador {
    private BufferedImage imagenIntacta;
    private BufferedImage imagenExplosion1;
    private BufferedImage imagenExplosion2;

    // vidas
    private int vidas;
    public int getVidas() { return vidas; }

    public NaveHeroe(double x, double y) {
        super(x, y, 48, 24);
        this.velocidadX = 0;
        this.vidas = 3;
    }

    public void setImagenIntacta(BufferedImage img)     { this.imagenIntacta = img;     this.bufferImage = img; }
    public void setImagenExplosion1(BufferedImage img)  { this.imagenExplosion1 = img;  }
    public void setImagenExplosion2(BufferedImage img)  { this.imagenExplosion2 = img;  }

    public void moverIzquierda() { this.velocidadX = -200; }
    public void moverDerecha()   { this.velocidadX =  200; }
    public void detener()        { this.velocidadX = 0; }

    @Override
    public void mover(double delta) { this.x += this.velocidadX * delta; }

    @Override
    public void dibujar(Graphics2D g) {
        if (visible && bufferImage != null) {
            g.drawImage(bufferImage, (int)this.x, (int)this.y, (int)this.width, (int)this.height, null);
        }
    }

    @Override
    public boolean colisionaCon(ElementoGrafico otro) {
        return this.intersects((java.awt.geom.Rectangle2D)otro); }

    public void setVisible(boolean v)        { this.visible = v; }
    public void setX(double x)               { this.x = x; }
    public void setY(double y)               { this.y = y; }
    public void resetImagen()                { this.bufferImage = imagenIntacta; }

    @Override
    public Proyectil disparar() {
        return new Proyectil(
                this.x + this.width / 2 - 2,
                this.y - 15,
                Proyectil.Origen.HEROE
        );
    }

    @Override
    public void reaccionarAColision(ElementoGrafico otro) {
        if (otro instanceof Proyectil p && p.getOrigen() == Proyectil.Origen.ENEMIGO) {
            vidas--;
            if (vidas <= 0) {
                this.bufferImage = imagenExplosion1;
                this.visible = false; // game over
            } else {
                // todavía tiene vidas, mostrar explosión brevemente
                this.bufferImage = imagenExplosion1;
                // reaparecer en posición inicial — SpaceInvaders lo maneja
            }
        }
        if (otro instanceof Borde) {
            if (this.velocidadX < 0) this.x = otro.x + otro.width;
            else                      this.x = otro.x - this.width;
            this.velocidadX = 0;
        }}
}