package pipoo.core;

public class Bot extends Jugador{
    private double margenError;

    public Bot(String nombre) {
        super(nombre);
        this.margenError = margenError;
    }
    public double getMargenError() {
        return margenError;
    }

    public void setMargenError(double margenError) {
        this.margenError = margenError;
    }
}
