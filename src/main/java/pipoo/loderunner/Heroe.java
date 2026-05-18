package pipoo.loderunner;
import java.awt.Graphics2D;


public class Heroe extends Runner{
    private int cantOro;

    public Heroe(double x, double y, int ancho, int alto) {
        super(x, y, ancho, alto);
        this.cantOro = 0;
    }

    public void cavar() {
        // ógica para crear un pozo a los lados (no directamente debajo) [4].
        // Aquí deberías avisarle al juego que cree un nuevo objeto Pozo.
    }

    @Override
    public void dibujar(Graphics2D g) {
        // TODO: Dibujar el rectángulo o la imagen del héroe.
    }

    public void recolectarOro() {
        this.cantOro++;
    }

    @Override
    public void mover(double delta) {
        // TODO: Leer las entradas del teclado para actualizar velocidadX e Y.
        // Importante: Si estaCayendo es true, ignorar el movimiento horizontal [5].
    }
}
