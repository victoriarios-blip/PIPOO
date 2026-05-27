package pipoo.loderunner;
import pipoo.core.ElementoGrafico;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;


public class Heroe extends Runner{
    private int cantOro;
    private boolean enEscalera;

    //atributos animación
    private BufferedImage imagenDer;
    private BufferedImage imagenIzq;
    private BufferedImage imagenEscalera;
    private BufferedImage imagenColgado;
    private BufferedImage[] imagenesMuriendo = new BufferedImage[1]; // Arreglo para los 7 frames


    // SETTERS ANIMACIONES
    public void setImagenDer(BufferedImage img) { this.imagenDer = img; }
    public void setImagenIzq(BufferedImage img) { this.imagenIzq = img; }
    public void setImagenEscalera(BufferedImage img) { this.imagenEscalera = img; }
    public void setImagenColgado(BufferedImage img) { this.imagenColgado = img; }
    public Heroe(double x, double y, int width, int height) {
        super(x, y, width, height);
        this.cantOro = 0;
        this.enEscalera = false;
    }

    // Método para cargar un frame específico de la muerte
    public void setImagenMuriendo(int indice, BufferedImage img) {
        if(indice >= 0 && indice < 7) {
            this.imagenesMuriendo[indice] = img;
        }
    }

    public void setVelocidadX(double velocidadX) {
        this.velocidadX = velocidadX;
    }

    public void setVelocidadY(double velocidadY) {
        this.velocidadY = velocidadY;
    }



    public void cavar() {
        // La lógica de instanciar el pozo en el ArrayList será controlada desde el juego.
        // TODO a futuro: Cambiar el "frame" de la animación del héroe o hacer sonar un efecto.
    }

    @Override
    public void dibujar(Graphics2D g) {
        // TODO: Dibujar el rectángulo o la imagen del héroe.
    }

    public void recolectarOro() {
        this.cantOro++;
    }


    public int getCantOro() { return cantOro; }




    @Override
    public void mover(double delta) {
        if (estaCayendo) {
            this.velocidadX = 0;
            this.velocidadY = 5;
        } else {
            // Lógica de lectura de teclado para moverse
        }

        // Ahora usamos directamente this.x y this.y heredados de Rectangle2D.Double [2]
        this.x += (this.velocidadX * delta * 60);
        this.y += (this.velocidadY * delta * 60);
        // Lógica de cambio de sprite (animación)
        if (this.velocidadX > 0 && imagenDer != null) {
            super.setImagen(imagenDer); // Mira a la derecha
        } else if (this.velocidadX < 0 && imagenIzq != null) {
            super.setImagen(imagenIzq); // Mira a la izquierda
        } else if (this.velocidadY != 0 && isEnEscalera() && imagenEscalera != null) {
            super.setImagen(imagenEscalera); // Trepando
        }


    }


    @Override
    public void setImagen(BufferedImage nuevaImagen) {

    }

    @Override
    public boolean colosionaCon(ElementoGrafico otro) {
        return false;
    }

    public boolean isEnEscalera() {
        return enEscalera;
    }

    public void setEnEscalera(boolean enEscalera) {
        this.enEscalera = enEscalera;
    }

    @Override
    public boolean colisionaCon(ElementoGrafico otro) {
        return false;
    }

    @Override
    public void reaccionarAColision(ElementoGrafico otro) {}
}

