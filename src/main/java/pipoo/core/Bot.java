package pipoo.core;

public class Bot extends Jugador{
    private double margenError;

    public Bot(String nombre, double margenError) {
        super(nombre);
        this.margenError = margenError;
    }

    // Constructor por defecto para el oponente de la CPU
    public Bot() {
        super("CPU");
        this.margenError = 10.0; // Valor por defecto para la dificultad
    }

    public double getMargenError() {
        return margenError;
    }

    public void setMargenError(double margenError) {
        this.margenError = margenError;
    }
}
