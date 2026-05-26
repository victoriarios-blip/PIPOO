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

    // --- Getters y Setters exigidos por la cátedra para el Encapsulamiento ---
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }


}
