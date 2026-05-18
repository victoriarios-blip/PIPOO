package pipoo.loderunner;
import java.awt.Graphics2D;

public class Guardia extends Runner {

    public Guardia(double x, double y, int ancho, int alto) {
        super(x, y, ancho, alto);
    }


    public void mover(double delta) { // de interfaz movible

    }


    public void perseguir() {
        // lógica para calcular la ruta hacia el héroe.
        // Nota: Las reglas dicen que a veces pueden tomar movimientos ilógicos [3].
    }

    public void reaparecer() {
        // mover al guardia a una posición (X) aleatoria en la parte superior (Y = 0) [4].
    }


    @Override
    public void dibujar(Graphics2D g) {
        // dibujar al guardia.
    }
}

