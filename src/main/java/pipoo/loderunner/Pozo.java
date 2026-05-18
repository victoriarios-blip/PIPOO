package pipoo.loderunner;
import pipoo.core.ElementoGrafico;
import java.awt.Graphics2D;

public class Pozo  extends ElementoGrafico {

    private double tiempoRestante;
    private int estado; // Ej: 0 = Abierto, 1 = Cerrándose, 2 = Cerrado

    public Pozo(double x, double y, int ancho, int alto) {
        super(x, y, ancho, alto);
        this.tiempoRestante = 5.0; // 5 segundos según el documento
        this.estado = 0;
    }

    @Override
    public void dibujar(Graphics2D g) {
        // dibuja el hueco negro.
    }


    public void actualizar(double delta) {
        // 1. Descontar el tiempo transcurrido
        if (tiempoRestante > 0) {
            tiempoRestante -= delta; // Resta los milisegundos que pasaron
        }

        // 2. Cambiar el estado del pozo según el tiempo que quede
        if (tiempoRestante <= 0) {
            tiempoRestante = 0;
            estado = 2; // Estado 2 = Completamente cerrado

        } else if (tiempoRestante <= 1.5) {
            // Queda 1.5 segundos o menos: el hueco empieza a cerrarse
            // Las reglas dicen que se puede caminar sobre ellos mientras se están cerrando [4]
            estado = 1; // Estado 1 = Cerrándose

        } else {
            estado = 0; // Estado 0 = Totalmente abierto
        }
    }

    public int getEstado() {
        return estado;
    }



}
