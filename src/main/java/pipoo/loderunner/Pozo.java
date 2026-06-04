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
    private BufferedImage imgCerrandose;

    public Pozo(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.tiempoAbierto = 0.0;
        this.estado = 0;
    }

    public void configurarAnimacion(BufferedImage imgFrag, BufferedImage imgAbierta) {
        this.imgAbierta = imgAbierta;
        this.setImagen(imgFrag); // Comienza mostrando el fragmento
    }
    public void setImagenCerrandose(BufferedImage img) {
        this.imgCerrandose = img;
    }


    public void actualizar(double delta) {
        tiempoAbierto += delta;

        if (estaCavando) {
            timerCavado += delta;
            if (timerCavado >= 0.25) {
                estaCavando = false;
                setImagen(imgAbierta);
            }
            return;
        }

        // 5 segundos en total.
        if (tiempoAbierto >= 5.0) {
            estado = 2; // se cierra por completo

        } else if (tiempoAbierto >= 4.5) {
            estado = -1;
            if (imgCerrandose != null) {
                setImagen(imgCerrandose);
            }
        }
    }

    public int getEstado() {
        if (estaCavando) return -1; // -1 significa "bloque sólido a medio romper"

        return estado;
    }

    @Override
    public void reaccionarAColision(ElementoGrafico otro) {

    }

    @Override
    public boolean colisionaCon(ElementoGrafico otro) {
        return false;
    }
}
