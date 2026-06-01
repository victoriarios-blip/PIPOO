package pipoo.spaceinvaders;

import pipoo.core.ElementoGrafico;
import pipoo.core.Movible;

import java.awt.image.BufferedImage;

public abstract class Enemigo extends Movible implements Disparador{
    protected int valorPuntaje;
    protected BufferedImage imagenFrame1;
    protected BufferedImage imagenFrame2;
    protected boolean mostrandoFrame1 = true;
    private double tiempoAnimacion = 0;

    public void setImagenes(BufferedImage img1, BufferedImage img2) {
        this.imagenFrame1 = img1;
        this.imagenFrame2 = img2;
        this.bufferImage = img1;
    }

    public Enemigo(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void mover(double delta) {
        this.x += this.velocidadX * delta;
        this.y += this.velocidadY * delta; // velocidadY solo es != 0 al bajar una fila
    }

    // Llamado por FormacionEnemigos cuando toca bajar una fila
    public void bajarFila(double distancia) {
        this.y += distancia;
        this.velocidadX = -this.velocidadX; // invierte dirección horizontal
    }

    @Override
    public Proyectil disparar() {
        // Dispara con baja probabilidad por frame, sino retorna null
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
            this.visible = false;
            // NOTA DE DISEÑO
        }
    }

    public abstract boolean colosionaCon(ElementoGrafico otro);

    @Override
    public boolean colisionaCon(ElementoGrafico otro){
            return this.intersects(otro);
    }
    }

