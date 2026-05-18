package pipoo.spaceinvader;
import pipoo.core.Movible;

public abstract class Enemigo extends Movible implements Disparador{
    protected int valorPuntaje;

    @Override
    public void reaccionarAColision() {
        // si lo toca el proyectil del héroe: muere y suma puntaje al sistema
    }
}
