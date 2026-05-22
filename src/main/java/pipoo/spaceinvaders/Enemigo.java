package pipoo.spaceinvaders;
import pipoo.core.Movible;

public abstract class Enemigo extends Movible implements Disparador{
    protected int valorPuntaje;

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
    public void disparar() { /* lógica de disparo */ }

    @Override
    public void reaccionarAColision() {
        // si lo toca el proyectil del héroe: muere y suma puntaje al sistema
    }
}
