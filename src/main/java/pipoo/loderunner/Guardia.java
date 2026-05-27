package pipoo.loderunner;
import pipoo.core.ElementoGrafico;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Guardia extends Runner {
    private Heroe heroe;
    private boolean tieneOro;
    private Random random;


    public Guardia(double x, double y, int ancho, int alto, Heroe heroe) {
        super(x, y, ancho, alto);
        this.heroe = this.heroe;
        this.tieneOro = false;
        this.random = new Random();
    }

    public void perseguir() {
        if (heroe != null) {
            // persecución, busca la coordenada X del heroe
            if (this.x < heroe.y) {
                this.velocidadX = 2; // se mueve a la derecha
            } else if (this.x > heroe.x) {
                this.velocidadX = -2; // se mueve a la izquierda
            } else {
                this.velocidadX = 0; // alineado
            }

            // a veces se mueven de forma aparentemente ilógica
            // hay un 5% de probabilidades en cada actualización de que el guardia vaya al reves
            if (random.nextInt(100) < 5) {
                this.velocidadX = this.velocidadX * -1;
            }
        }
    }

    public void reaparecer() {
        // reaparece en la parte superior del nivel en una posición random
        this.y = 0; // parte superior de la pantalla

        this.x = random.nextInt((int) (800 - this.width));

        // si reaparece en el aire, empieza a caer
        this.estaCayendo = true;
    }

    @Override
    public void mover(double delta) {
        // cuando se estan cayendo no se pueden desplazar horizontalmente
        if (estaCayendo) {
            this.velocidadX = 0;
            this.velocidadY = 5; // velocidad de caída hacia abajo
        } else {
            this.velocidadY = 0;
            perseguir(); // solo persigue si esta pisando plataforma o escalera
        }

        // velocidad final heredadas de ElementoGrafico
        this.x += this.velocidadX;
        this.y += this.velocidadY;
    }

    @Override
    public void setImagen(BufferedImage nuevaImagen) {

    }

    @Override
    public boolean colosionaCon(ElementoGrafico otro) {
        return false;
    }

    // getter y setter para ver si el guardia agarro o solto oro
    public boolean isTieneOro() {
        return tieneOro;
    }

    public void setTieneOro(boolean tieneOro) {
        this.tieneOro = tieneOro;
    }

    @Override
    public void dibujar(Graphics2D g) {
        // dibujar al guardia.
    }

    @Override
    public boolean colisionaCon(ElementoGrafico otro) {
        return false;
    }

    @Override
    public void reaccionarAColision(ElementoGrafico otro) {

    }
}




