package pipoo.spaceinvaders;
import pipoo.core.ElementoGrafico;
import pipoo.core.Movible;
import pipoo.core.recursos.Borde;

public class Proyectil extends Movible{
    public Proyectil(double x, double y) {
        super(x, y, 4, 15);
        //velocidades
    }

    @Override
    public void mover(double delta) {
        this.y += this.velocidadY * delta;
    }

    @Override
    public void reaccionarAColision(ElementoGrafico c) {
        // el proyectil se destruye/desaparece
        if (c instanceof NaveHeroe){
            this.visible = false;}
        if (c instanceof NaveNodriza){
            this.visible = false;}
        if (c instanceof Borde){
            this.visible = false;}
        if (c instanceof Escudo){
            this.visible = false;}
        if (c instanceof Enemigo){
            this.visible = false;}

        // falta colision con otro proyectil
    }

    @Override
    public boolean colisionaCon(ElementoGrafico otro) {
        return false;
    }
}
