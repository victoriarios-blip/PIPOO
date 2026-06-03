package pipoo.loderunner;
import pipoo.core.ElementoGrafico;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Pozo  extends ElementoGrafico {
    private double tiempoAbierto;
    private int estado;
    private boolean estaCavando = true;
    private double timerCavado = 0;
    private BufferedImage imgAbierta;

    public Pozo(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.tiempoAbierto = 0.0;
        this.estado = 0;
    }

    public void configurarAnimacion(BufferedImage imgFrag, BufferedImage imgAbierta) {
        this.imgAbierta = imgAbierta;
        this.setImagen(imgFrag); // Comienza mostrando el fragmento
    }


    public void actualizar(double delta) {
        tiempoAbierto += delta; // delta es 1.0/60.0 por cada frame
        if (estaCavando) {
            timerCavado += delta;
            if (timerCavado >= 0.25) { // Dura 1/4 de segundo (podés ajustarlo)
                estaCavando = false;
                setImagen(imgAbierta); // Se abre completamente
            }
            return; // Salimos para que el tiempo de cerrado no corra todavía
        }
        // Regla: 5 segundos en total.
        if (tiempoAbierto >= 5.0) {
            estado = 2; // Se cierra por completo
        } else if (tiempoAbierto >= 4.5) {
            estado = 1; // Último medio segundo: se vuelve piso firme
        }
    }

    public int getEstado() {
        if (estaCavando) return -1; // -1 significa "bloque sólido a medio romper"

        return estado;
    }

    @Override
    public void reaccionarAColision(ElementoGrafico otro) {

    }
}
