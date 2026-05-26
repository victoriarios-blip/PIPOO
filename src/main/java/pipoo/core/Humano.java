package pipoo.core;

public class Humano extends Jugador{
    public Humano(String nombre) {
        super(nombre);
    }
    // Constructor por defecto (para cuando no le pides el nombre al usuario)
    public Humano() {
        super("Jugador 1");
    }
}
