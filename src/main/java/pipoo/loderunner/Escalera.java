package pipoo.loderunner;
import pipoo.core.ElementoGrafico;

import java.awt.Graphics2D;

public class Escalera extends ElementoGrafico {
    private boolean salidaFinal;

    public Escalera(double x, double y, int width, int height, boolean esSalidaFinal) {
        super(x, y, width, height);
        this.salidaFinal = esSalidaFinal;

        // si es la escalera de salida final, arranca invisible
        if (this.salidaFinal) {
            this.visible = false;
        }
    }

    public void crecer() {
        // si agarró todo el oro, aparece una escalera de salida
        this.y -= 30; // sube posición Y
        this.height += 30; // aumenta altura
    }


    public boolean isSalidaFinal() {
        return salidaFinal;
    }

    public void setSalidaFinal(boolean salidaFinal) {
        this.salidaFinal = salidaFinal;
    }


    @Override
    public void reaccionarAColision(ElementoGrafico otro) {

    }

    @Override
    public boolean colisionaCon(ElementoGrafico otro) {
        return false;
    }
}
