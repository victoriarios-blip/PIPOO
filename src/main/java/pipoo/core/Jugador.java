package pipoo.core;

public abstract class Jugador {
    protected String nombre;
    protected int puntaje;
    protected int vidas;

    // Constructor general
    public Jugador(String nombre) {
        this.nombre = nombre;
        this.puntaje = 0;
        this.vidas = 0;
    }


}
