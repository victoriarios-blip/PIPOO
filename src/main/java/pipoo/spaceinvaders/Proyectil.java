package pipoo.spaceinvaders;
import pipoo.core.ElementoGrafico;
import pipoo.core.Movible;
import pipoo.core.recursos.Borde;

public class Proyectil extends Movible{
    public enum Origen { HEROE, ENEMIGO };
    private Origen origen;
    private static final double VELOCIDAD = 300;

    public Proyectil(double x, double y, Origen origen) {
        super(x, y, 4, 15);
        this.origen = origen;
        this.velocidadY = (origen == Origen.HEROE) ? -VELOCIDAD : VELOCIDAD;
    }

    @Override
    public void mover(double delta) {
        this.y += this.velocidadY * delta;
    }

    @Override
    public void reaccionarAColision(ElementoGrafico c) {
        // el proyectil se destruye/desaparece
        if (c instanceof NaveHeroe && origen == Origen.ENEMIGO){
            this.visible = false;}
        if (c instanceof NaveNodriza && origen == Origen.HEROE){
            this.visible = false;}
        if (c instanceof Borde){
            this.visible = false;}
        if (c instanceof Escudo){
            this.visible = false;}
        if (c instanceof Enemigo && origen == Origen.HEROE){
            this.visible = false;}
        if (c instanceof Proyectil){
        this.visible = false;}
    }

    @Override
    public boolean colisionaCon(ElementoGrafico otro) {
        return this.x < otro.getX() + otro.getWidth()
                && this.x + this.width > otro.getX()
                && this.y < otro.getY() + otro.getHeight()
                && this.y + this.height > otro.getY();
    }

    public Origen getOrigen() {
        return origen;
    }
}
