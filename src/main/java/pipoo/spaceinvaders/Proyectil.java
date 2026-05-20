package pipoo.spaceinvaders;

import pipoo.core.Movible;

public class Proyectil extends Movible{
    @Override
    public void mover(double delta) {
        // lógica de movimiento vertical continuo
    }

    @Override
    public void reaccionarAColision() {
        // el proyectil se destruye/desaparece
        this.visible = false;
    }

}
