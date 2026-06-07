package pipoo.spaceinvaders;

import pipoo.core.ElementoGrafico;
import pipoo.core.Movible;

import java.awt.*;

public class NaveNodriza extends Movible {
    private int valorPuntaje;
    private boolean destruida = false;
    private double tiempoMuestraPuntaje = 0;
    private static final double DURACION_PUNTAJE = 1.0; // 1 segundo en pantalla

    public NaveNodriza(double x, double y) {
        super(x, y, 72, 27); // x3 como el resto de sprites
        this.velocidadX = 100;
        this.valorPuntaje = (int)(Math.random() * 251) + 50;
    }

    @Override
    public void mover(double delta) {
        if (destruida) {
            tiempoMuestraPuntaje += delta;
            if (tiempoMuestraPuntaje >= DURACION_PUNTAJE) {
                this.visible = false; // Recién acá desaparece del juego
            }
            return; // Si está destruida, no se desplaza
        }

        // Movimiento normal si está viva
        this.x += this.velocidadX * delta;
        if (this.velocidadX > 0 && this.x > 800) this.visible = false;
    }

    @Override
    public void dibujar(Graphics2D g) {
        if (!visible) return;

        if (destruida) {
            // Podés usar el g.drawString de Java temporalmente para probar:
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            g.drawString(String.valueOf(valorPuntaje), (int)this.x, (int)this.y + 15);
        } else if (bufferImage != null) {
            g.drawImage(bufferImage, (int)this.x, (int)this.y, (int)this.width, (int)this.height, null);
        }
    }

    @Override
    public void reaccionarAColision(ElementoGrafico otro) {
        if (destruida) return;
        if (otro instanceof Proyectil p && p.getOrigen() == Proyectil.Origen.HEROE) {
            this.destruida = true;
            this.velocidadX = 0; // Se frena en el lugar
        }
    }

    public int getValorPuntaje() { return valorPuntaje; }
}