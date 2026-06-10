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
        super(x, y, 72, 27);
        this.velocidadX = 100;
        this.valorPuntaje = 0;
    }

    @Override
    public void mover(double delta) {
        if (destruida) {
            tiempoMuestraPuntaje += delta;
            if (tiempoMuestraPuntaje >= DURACION_PUNTAJE) {
                this.visible = false; // recien acá desaparece del juego
            }
            return; // si está destruida, no se desplaza
        }
        // movimiento normal mientras está viva
        this.x += this.velocidadX * delta;
        if (this.velocidadX > 0 && this.x > 800) this.visible = false;
    }

    @Override
    public void dibujar(Graphics2D g) {
        if (!visible) return;

        if (destruida) {
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
            this.velocidadX = 0; // se frena en el lugar
            int disparosActuales = SpaceInvaders.getContadorDisparos();
            calcularPuntajeMisterioso(disparosActuales);
        }
    }

    private void calcularPuntajeMisterioso(int disparos) {
        // regla: disparo 23, 38, 53, 68...
        // cumplen que (disparos - 23) es divisible por 15
        if (disparos >= 23 && (disparos - 23) % 15 == 0) {
            this.valorPuntaje = 300; // puntaje mayor
        } else {
            // valores del SI original (50, 100 o 150)
            int[] deConsolacion = {50, 100, 150};
            int indice = (int) (Math.random() * deConsolacion.length);
            this.valorPuntaje = deConsolacion[indice];
        }
    }

    public int getValorPuntaje() { return valorPuntaje; }
}