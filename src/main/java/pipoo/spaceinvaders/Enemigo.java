package pipoo.spaceinvaders;
import pipoo.core.Movible;

public abstract class Enemigo extends Movible implements Disparador{
    protected int valorPuntaje;

    public Enemigo(double x, double y) {
        super(x, y, 4, 15);
        //velocidades
    }

    @Override
    public void mover(double delta) { /* movimiento de oleada */ }
    @Override
    public void disparar() { /* lógica de disparo */ }
    public Enemigo(double x, double y, double width, double height) {
        super(x, y, width, height);
    }
    @Override
    public void reaccionarAColision() {
        // si lo toca el proyectil del héroe: muere y suma puntaje al sistema
    }
}
