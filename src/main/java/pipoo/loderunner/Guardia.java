package pipoo.loderunner;

import pipoo.core.ElementoGrafico;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Guardia extends Runner {

    public enum Estado {
        CAMINANDO, SUBIENDO_ESCALERA, BAJANDO_ESCALERA, EN_BARRA,
        CAYENDO, ATRAPADO_POZO, ESCAPANDO_POZO, PATRULLANDO
    }

    private static final double VELOCIDAD        = 1.0;
    private static final double VELOCIDAD_ESC    = 1.0;
    private static final double VELOCIDAD_ESCAPE = 1.2;
    private static final double GRAVEDAD         = 3.0;
    private static final int    TICKS_EN_POZO    = 150;
    private static final double TOLERANCIA_X     = 6.0;
    private static final double ANCHO_MUNDO      = 1200.0;

    private Estado estado= Estado.CAMINANDO;
    private int ticksEnPozo = 0;
    private Escalera escaleraObjetivo = null;

    private Heroe heroe;
    private ArrayList<Escalera> escaleras;
    private ArrayList<Plataforma> plataformas;
    private ArrayList<BarraDeManos> barras;
    private ArrayList<Pozo> pozos;

    private double  posicionInicialX;
    private double  posicionInicialY;
    private boolean tieneOro;

    private BufferedImage imgDer, imgIzq, imgEscalera, imgColgado, imgAtrapado;

    public Guardia(double x, double y, int width, int height, Heroe heroe) {
        super(x, y, width, height);
        this.heroe= heroe;
        this.tieneOro= false;
        this.posicionInicialX = x;
        this.posicionInicialY = y;
    }

    public void perseguir() {
        switch (estado) {
            case ATRAPADO_POZO:
                velocidadX = 0;
                velocidadY = 0;
                ticksEnPozo++;
                if (ticksEnPozo >= TICKS_EN_POZO) {
                    estado      = Estado.ESCAPANDO_POZO;
                    ticksEnPozo = 0;
                }
                break;
            case ESCAPANDO_POZO:
                velocidadX = 0;
                velocidadY = -VELOCIDAD_ESCAPE;
                break;
            case CAYENDO:
                velocidadX = 0;
                velocidadY = GRAVEDAD;
                break;
            case SUBIENDO_ESCALERA:
                velocidadX = 0;
                velocidadY = -VELOCIDAD_ESC;
                break;
            case BAJANDO_ESCALERA:
                velocidadX = 0;
                velocidadY = +VELOCIDAD_ESC;
                break;
            case EN_BARRA:
            case CAMINANDO:
            case PATRULLANDO:
            default:
                velocidadY = 0;
                decidirMovimientoHorizontal();
                break;
        }
    }

    //  LOGICA HORIZONTAL
    private void decidirMovimientoHorizontal() {
        boolean estabaEnBarra = isEnBarra();
        boolean mismoNivel = Math.abs(this.y - heroe.y) <= 15;

        // mismo nivel: ir directo al heroe sin buscar escaleras
        if (mismoNivel) {
            escaleraObjetivo = null;
            double diffX = heroe.x - this.x;
            if (Math.abs(diffX) <= 2.0) {
                velocidadX = 0;
            } else {
                velocidadX = signo(diffX) * VELOCIDAD;
            }
            estado = estabaEnBarra ? Estado.EN_BARRA : Estado.CAMINANDO;
            return;
        }

        // distinto nivel: evaluar escaleras (subir o bajar)
        boolean necesitaSubir = (heroe.y < this.y);

        if (escaleraObjetivo == null || !esEscaleraValida(escaleraObjetivo, necesitaSubir)) {
            escaleraObjetivo = buscarMejorEscalera(necesitaSubir);
        }

        // moverse hacia la escalera objetivo
        if (escaleraObjetivo != null) {
            double distX = escaleraObjetivo.x - this.x;
            if (Math.abs(distX) <= TOLERANCIA_X) {
                alinearConEscalera(escaleraObjetivo);
                estado     = necesitaSubir ? Estado.SUBIENDO_ESCALERA : Estado.BAJANDO_ESCALERA;
                velocidadX = 0;
            } else {
                estado = estabaEnBarra ? Estado.EN_BARRA : Estado.PATRULLANDO;
                velocidadX = signo(distX) * VELOCIDAD;
            }
        } else {
            // sin escalera vslida: patrullar
            estado = estabaEnBarra ? Estado.EN_BARRA : Estado.PATRULLANDO;
            double diffX = heroe.x - this.x;
            if (Math.abs(diffX) <= 2.0) {
                velocidadX = 0;
            } else {
                velocidadX = signo(diffX) * VELOCIDAD;
                if (this.x <= 0 || this.x + this.width >= ANCHO_MUNDO) {
                    velocidadX = -velocidadX;
                }
            }
        }
    }

    private boolean esEscaleraValida(Escalera e, boolean subir) {
        if (subir) {
            return (e.y < this.y) && (e.y + e.height >= this.y - 40);
        } else {
            return (e.y <= this.y + this.height + 10) && (e.y + e.height > this.y + this.height + 10);
        }
    }

    private Escalera buscarMejorEscalera(boolean subir) {
        if (escaleras == null) return null;
        Escalera mejor = null;
        double mejorCosto = 999999.0;

        for (Escalera e : escaleras) {
            if (esEscaleraValida(e, subir)) {
                double costoPath = Math.abs(e.x - this.x) + Math.abs(e.x - heroe.x);
                if (costoPath < mejorCosto) {
                    mejorCosto = costoPath;
                    mejor = e;
                }
            }
        }
        return mejor;
    }

    public void notificarEntradaPozo() {
        if (estado != Estado.ATRAPADO_POZO && estado != Estado.ESCAPANDO_POZO) {
            estado      = Estado.ATRAPADO_POZO;
            ticksEnPozo = 0;
            velocidadX  = 0;
            velocidadY  = 0;
            escaleraObjetivo = null;
            setEstaCayendo(false);
            if (tieneOro) tieneOro = false;
        }
    }

    public void notificarSalidaPozo() {
        if (estado == Estado.ESCAPANDO_POZO) {
            estado     = Estado.CAMINANDO;
            velocidadY = 0;
        }
    }

    public void notificarAterrizaje() {
        if (estado == Estado.CAYENDO) {
            estado = Estado.CAMINANDO;
            escaleraObjetivo = null;
        }
        setEstaCayendo(false);
    }

    public void notificarFinEscalera() {
        estado     = Estado.CAMINANDO;
        velocidadX = 0;
        velocidadY = 0;
        escaleraObjetivo = null;
    }

    @Override
    public void mover(double delta) {
        perseguir();
        x += velocidadX;
        y += velocidadY;
        actualizarAnimacion();
    }

    private void actualizarAnimacion() {
        if (estado == Estado.ATRAPADO_POZO || estado == Estado.ESCAPANDO_POZO) {
            if (imgAtrapado != null) super.setImagen(imgAtrapado);
        } else if (estado == Estado.SUBIENDO_ESCALERA || estado == Estado.BAJANDO_ESCALERA) {
            if (imgEscalera != null) super.setImagen(imgEscalera);
        } else if (estado == Estado.EN_BARRA) {
            if (imgColgado != null) super.setImagen(imgColgado);
        } else if (velocidadX < 0 && imgIzq != null) {
            super.setImagen(imgIzq);
        } else if (velocidadX > 0 && imgDer != null) {
            super.setImagen(imgDer);
        }
    }

    public void reaparecer() {
        this.x          = posicionInicialX;
        this.y          = posicionInicialY;
        this.estado     = Estado.CAMINANDO;
        this.ticksEnPozo = 0;
        this.tieneOro   = false;
        this.escaleraObjetivo = null;
        setVelocidadX(0);
        setVelocidadY(0);
        setEstaCayendo(false);
    }

    private int signo(double valor) {
        if (valor >  0.5) return  1;
        if (valor < -0.5) return -1;
        return 0;
    }

    private void alinearConEscalera(Escalera e) {
        this.x = e.x;
    }

    public void   setVelocidadX(double vx) { this.velocidadX = vx; }
    public void   setVelocidadY(double vy) { this.velocidadY = vy; }
    public double getVelocidadX()          { return this.velocidadX; }
    public double getVelocidadY()          { return this.velocidadY; }
    public Estado getEstado()              { return estado; }
    public void   setEstado(Estado e)      { this.estado = e; }
    public boolean isEnBarra()             { return estado == Estado.EN_BARRA; }

    //  transicion de escalera a barra)
    public void setEnBarra(boolean v) {
        if (v) {
            if (estado == Estado.ATRAPADO_POZO || estado == Estado.ESCAPANDO_POZO) return;
            if (estado == Estado.SUBIENDO_ESCALERA || estado == Estado.BAJANDO_ESCALERA) {
                if (Math.abs(heroe.y - this.y) <= 15) {
                    estado = Estado.EN_BARRA;
                    escaleraObjetivo = null;
                    velocidadY = 0;
                }
            } else {
                estado = Estado.EN_BARRA;
            }
        } else {
            if (estado == Estado.EN_BARRA) {
                estado = Estado.CAMINANDO;
            }
        }
    }


    public boolean isTieneOro(){ return tieneOro; }
    public void    setTieneOro(boolean v) { this.tieneOro = v; }

    public void setMapa(ArrayList<Escalera> escaleras, ArrayList<Plataforma> plataformas, ArrayList<BarraDeManos> barras, ArrayList<Pozo> pozos) {
        this.escaleras = escaleras; this.plataformas = plataformas;
        this.barras = barras; this.pozos = pozos;
    }

    public void setImgDer(BufferedImage img)      { this.imgDer      = img; }
    public void setImgIzq(BufferedImage img)      { this.imgIzq      = img; }
    public void setImgEscalera(BufferedImage img) { this.imgEscalera = img; }
    public void setImgColgado(BufferedImage img)  { this.imgColgado  = img; }
    public void setImgAtrapado(BufferedImage img) { this.imgAtrapado = img; }


    @Override public boolean colisionaCon(ElementoGrafico otro)     { return false; }
    @Override public void reaccionarAColision(ElementoGrafico otro) { }
}