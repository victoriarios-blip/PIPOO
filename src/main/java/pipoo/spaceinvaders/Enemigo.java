package pipoo.spaceinvaders;

import pipoo.core.ElementoGrafico;
import pipoo.core.Movible;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Enemigo extends Movible implements Disparador{
    protected int valorPuntaje;
    protected BufferedImage frame1;
    protected BufferedImage frame2;
    protected BufferedImage imagenMuerte;
    private boolean frameActual = true;
    private double tiempoFrame = 0;
    private static final double INTERVALO_FRAME = 0.5;
    private boolean muriendo = false;
    private double tiempoMuerte = 0;
    private static final double DURACION_MUERTE = 0.4;

    public void setFrame1(BufferedImage img) {
        this.frame1 = img; this.bufferImage = img;} // frame1 e imagen inicial
    public void setFrame2(BufferedImage img) {
        this.frame2 = img;}
    public void setImagenMuerte(BufferedImage img) {
        this.imagenMuerte = img;}

    public void actualizarFrame(double delta) {
        if (muriendo) {
            tiempoMuerte += delta;
            if (tiempoMuerte >= DURACION_MUERTE) {
                this.visible = false;
            }
            return;
        }
        tiempoFrame += delta;
        if (tiempoFrame >= INTERVALO_FRAME) {
            frameActual = !frameActual;
            tiempoFrame = 0;
            this.bufferImage = frameActual ? frame1 : frame2;
        }
    }

    public void setImagenes(BufferedImage img1, BufferedImage img2) {
        setFrame1(img1);
        setFrame2(img2);
    }

    public Enemigo(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public int getValorPuntaje() {
        return valorPuntaje;
    }

    @Override
    public void mover(double delta) {
        this.x += this.velocidadX * delta;
        this.y += this.velocidadY * delta; // velocidadY solo es != 0 cuando baja una fila
    }

    @Override
    public void dibujar(Graphics2D g) {
        if (visible && bufferImage != null) {
            g.drawImage(bufferImage, (int)this.x, (int)this.y, (int)this.width, (int)this.height, null);
        }
    }

    // llamado por spaceinvaders cuando toca bajar una fila
    public void bajarFila(double distancia) {
        this.y += distancia;
        this.velocidadX = -this.velocidadX; // invierte dirección horizontal
    }

    @Override
    public Proyectil disparar() {
        // dispara con baja probabilidad por frame, sino retorna null
        if (Math.random() < 0.0005) {
            return new Proyectil(
                    this.x + this.width / 2,
                    this.y + this.height,
                    Proyectil.Origen.ENEMIGO
            );
        }
        return null;
    }

    @Override
    public void reaccionarAColision(ElementoGrafico c) {
        if (c instanceof Proyectil p && p.getOrigen() == Proyectil.Origen.HEROE) {
            this.bufferImage = imagenMuerte;
            this.muriendo = true;
        }
    }

    @Override
    public boolean colisionaCon(ElementoGrafico otro) {
        return this.intersects((java.awt.geom.Rectangle2D) otro);
    }
    }

