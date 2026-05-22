package pipoo.loderunner;
import pipoo.core.ElementoGrafico;
import java.awt.Graphics2D;

public class Pozo  extends ElementoGrafico {

    private double tiempoRestante;
    private int estado;

    public Pozo(double x, double y, int ancho, int alto) {
        super(x, y, ancho, alto);
        this.tiempoRestante = 5.0;
        this.estado = 0;
    }

    @Override
    public void dibujar(Graphics2D g) {
        // dibuja el hueco negro.
    }


    public void actualizar(double delta) {
        // descontar tiempo transcurrido
        if (tiempoRestante > 0) {
            tiempoRestante -= delta; // resta milisegundos
        }

        // cambiar estado
        if (tiempoRestante <= 0) {
            tiempoRestante = 0;
            estado = 2; // cerrado

        } else if (tiempoRestante <= 1.5) {
            // empieza a cerrarse
            estado = 1; // cerrandose

        } else {
            estado = 0; // abierto
        }
    }

    public int getEstado() {
        return estado;
    }


    @Override
    public boolean colisionaCon(ElementoGrafico otro) {
        return false;
    }

    @Override
    public void reaccionarAColision(ElementoGrafico otro) {

    }
}
