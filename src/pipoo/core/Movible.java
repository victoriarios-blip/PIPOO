package pipoo.core;

public abstract class Movible extends ElementoGrafico{
    protected double velocidadX;
    protected double velocidadY;

    public abstract void mover(double delta);
}
