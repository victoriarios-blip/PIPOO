package pipoo.spaceinvaders;

public class Escudo {
    private int resistencia;

    public Escudo() {
        this.resistencia = 100; // valor de ejemplo
    }

    public void recibirDanio() {
        // degradar visualmente el escudo
    }

    @Override
    public void reaccionarAColision() {
        recibirDanio();
    }
}
