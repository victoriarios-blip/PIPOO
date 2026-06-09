package pipoo.loderunner;
import pipoo.core.ElementoGrafico;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;


public class Heroe extends Runner{
    private int cantOro;
    private boolean enEscalera;
    private boolean enBarra = false;

    //atributos animación
    private BufferedImage imagenDer;
    private BufferedImage imagenIzq;
    private BufferedImage imagenEscalera;
    private BufferedImage imagenColgado;

    // ATRIBUTOS ANIMACION DE MUERTE
    private BufferedImage[] imagenesMuerte = new BufferedImage[7];
    private boolean estaMuriendo = false;
    private int frameMuerteActual = 0;
    private double tiempoAnimacionMuerte = 0;
    private static final double TIEMPO_POR_FRAME = 0.15;

    // SETTERS ANIMACIONES
    public void setImagenDer(BufferedImage img) { this.imagenDer = img; }
    public void setImagenIzq(BufferedImage img) { this.imagenIzq = img; }
    public void setImagenEscalera(BufferedImage img) { this.imagenEscalera = img; }
    public void setImagenColgado(BufferedImage img) { this.imagenColgado = img; }

    // GETTERS ANIMACIONES
    public BufferedImage getImagenDer() { return this.imagenDer; }
    public BufferedImage getImagenIzq() { return this.imagenIzq; }
    public BufferedImage getImagenEscalera() { return this.imagenEscalera; }
    public BufferedImage getImagenColgado() { return this.imagenColgado; }

    public Heroe(double x, double y, int width, int height) {
        super(x, y, width, height);
        this.cantOro = 0;
        this.enEscalera = false;
    }

    public void setEnBarra(boolean enBarra) {
        this.enBarra = enBarra;
    }
    public boolean isEnBarra() {
        return this.enBarra;
    }

    //animacion muerte
    public void setImagenMuriendo(int indice, BufferedImage img) {
        if(indice >= 0 && indice < 7) {
            this.imagenesMuerte[indice] = img;
        }
    }
    public void iniciarMuerte() {
        estaMuriendo = true;
        frameMuerteActual = 0;
        tiempoAnimacionMuerte = 0;
        setVelocidadX(0);
        setVelocidadY(0);
    }

    public boolean isEstaMuriendo() {
        return estaMuriendo;
    }

    public boolean actualizarAnimacionMuerte(double delta) {
        tiempoAnimacionMuerte += delta;
        if (tiempoAnimacionMuerte >= TIEMPO_POR_FRAME) {
            tiempoAnimacionMuerte = 0;
            frameMuerteActual++;

            if (frameMuerteActual >= imagenesMuerte.length) {
                estaMuriendo = false;
                return true;
            }
        }
        return false;
    }

    @Override
    public void dibujar(Graphics2D g) {
        if (estaMuriendo) {
            if (imagenesMuerte[frameMuerteActual] != null) {
                g.drawImage(imagenesMuerte[frameMuerteActual], (int)x, (int)y, (int)width, (int)height, null);            }
        } else {

            super.dibujar(g);

        }


    }

    public void setVelocidadX(double velocidadX) {
        this.velocidadX = velocidadX;
    }
    public void setVelocidadY(double velocidadY) {
        this.velocidadY = velocidadY;
    }
    public double getVelocidadY() {
        return this.velocidadY;
    }
    public double getVelocidadX() {
        return this.velocidadX;
    }

    public void cavar() {
        System.out.println("Héroe cavando...");
    }

    public void recolectarOro() {
        this.cantOro++;
    }


    @Override
    public void mover(double delta) {
        if (estaCayendo) {
            this.velocidadX = 0;
            this.velocidadY = 5;
        }

        this.x += (this.velocidadX * delta * 60);
        this.y += (this.velocidadY * delta * 60);

        // cambio de sprite
        if (this.isEnBarra() && imagenColgado != null) {
            super.setImagen(imagenColgado); // colgado
        } else if (this.velocidadX > 0 && imagenDer != null) {
            super.setImagen(imagenDer); // mira a la derecha
        } else if (this.velocidadX < 0 && imagenIzq != null) {
            super.setImagen(imagenIzq); // mira a la izquierda
        } else if (this.velocidadY != 0 && isEnEscalera() && imagenEscalera != null) {
            super.setImagen(imagenEscalera); // trepando
        }
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

