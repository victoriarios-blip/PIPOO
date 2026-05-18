package pipoo.loderunner;
import pipoo.core.ElementoGrafico;

import java.awt.Graphics2D;

public class Escalera extends ElementoGrafico {
    private boolean salidaFinal;

    public Escalera(double x, double y, int ancho, int alto, boolean esSalidaFinal) {
        super(x, y, ancho, alto);
        this.salidaFinal = esSalidaFinal;

        // si es la escalera de salida final, arranca invisible
        if (this.salidaFinal) {
            this.visible = false;
        }
    }

    public void crecer() {
        // lógica para hacerla visible y armar la escalera hacia la salida
    }

    @Override
    public void dibujar(Graphics2D g) {
        if (visible) {
            // dibuja la escalera
        }
    }

}
