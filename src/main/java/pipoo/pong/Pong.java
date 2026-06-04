package pipoo.pong;

import pipoo.core.*;
import com.entropyinteractive.Keyboard;
import pipoo.core.configuracion.ConfiguracionPong;
import pipoo.core.recursos.Marcador;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Pong extends Juego {

    private Pelota pelota;
    private Paleta paleta1, paleta2;
    private BufferedImage imgCancha, imgDivisor, imgGameOver;
    private Marcador marcador;
    private int puntosJ1=0, puntosJ2=0;
    private boolean juegoFinalizado = false;
    private Jugador jugador1, jugador2;
    private int puntosLimite;
    private boolean sonidoActivado;
    private String nombreJ1;
    private String nombreJ2;

    //imagenes
    private BufferedImage[] imgNumeros;
    //private BufferedImage[] imgNumerosNeon;
    private Rectangle2D.Double bordeSuperior;
    private Rectangle2D.Double bordeInferior;

    //sonido
    private GestorAudio gestorAudio = new GestorAudio();

    public Pong() {
        super("PIPOO PONG", 800, 600); // titulo y tamaño de ventana
        System.out.println("Propiedades en memoria: " + this.appProperties.stringPropertyNames());

    }

    @Override
    public void gameStartup() {
        try {
            ConfiguracionPong config = (ConfiguracionPong) this.getConfiguracion();
            String skinPelota = config.getSkinPelota();
            // pelota.setImagen(Cargar imagen según skinPelota);

            //nombres de j1 y el j2/bot en el juego
            this.nombreJ1 = config.getNombreJ1();
            if (config.getContraBot()) {
                this.nombreJ2 = "PIPOO BOT";
            } else {
                this.nombreJ2 = config.getNombreJ2();
            }

            // Configurar Jugador 1
            this.jugador1 = new Humano(config.getNombreJ1());

            if (config.getContraBot()) {
                this.jugador2 = new Bot("PIPOO-Bot", 20.0);
            } else {
                this.jugador2 = new Humano(config.getNombreJ2());
            }

            //bordesss
            int grosorBorde = 25;
            bordeSuperior = new Rectangle2D.Double(0, 0, getWidth(), grosorBorde);
            bordeInferior = new Rectangle2D.Double(0, getHeight()-grosorBorde, getWidth(), grosorBorde);


            pelota = new Pelota(400, 300, 15, 15);
            paleta1 = new Paleta(20, (double)getHeight() / 2 - 40, 15, 80);
            paleta2 = new Paleta(getWidth() - 35, (double)getHeight()/2 - 40, 15, 80);
            //marcador = new Marcador(200, 200, 200,200); //ver estos valores

            //velocidad de la pelota
            pelota.setVelocidadX(350.00);
            pelota.setVelocidadY(350.00);

            //velocidad de las paletas
            paleta1.setVelocidadY(350.0);
            paleta2.setVelocidadY(350.0);

            //obtenemos los puntos y si el sonido esta activado o no para guardar la configuracion
            this.puntosLimite = config.getPuntosParaGanar();
            sonidoActivado = config.isSonidoActivado();
            if (!sonidoActivado) {
                gestorAudio.detenerMusica();
            } else {
                gestorAudio.reproducirMusica(getClass().getResource("sonidos/Bongo Cat.wav"));
                gestorAudio.precargarEfecto("rebote_borde", getClass().getResource("sonidos/pelota_rebota_borde.wav"));
                gestorAudio.precargarEfecto("rebote_paleta", getClass().getResource("sonidos/pelota_rebota_paleta.wav"));
                gestorAudio.precargarEfecto("punto", getClass().getResource("sonidos/anotacion.wav"));
            }



            //cargamos los assets (DEFAULT)

            BufferedImage imgPelota = ImageIO.read(getClass().getResource("/pipoo/pong/imagenes/pelota_default.png"));
            BufferedImage imgPaleta = ImageIO.read(getClass().getResource("/pipoo/pong/imagenes/paleta_default.png"));
            this.imgCancha = ImageIO.read(getClass().getResource("/pipoo/pong/imagenes/cancha_default.png"));
            this.imgDivisor = ImageIO.read(getClass().getResource("/pipoo/pong/imagenes/barra_del_medio.png"));
            this.imgGameOver = ImageIO.read(getClass().getResource("/pipoo/pong/neon/game_over.png"));

            //NEON
/*
            BufferedImage imgPelota = ImageIO.read(getClass().getResource("/pipoo/pong/neon/yellow_ball_neon.png"));
            BufferedImage imgPaleta = ImageIO.read(getClass().getResource("/pipoo/pong/neon/red_bar_neon.png"));
            this.imgCancha = ImageIO.read(getClass().getResource("/pipoo/pong/neon/cancha_neon.png"));
            this.imgDivisor = ImageIO.read(getClass().getResource("/pipoo/pong/neon/barra_del_medio.png"));

 */
            //puntos estilo default
            imgNumeros = new BufferedImage[16];
            for (int i = 0; i <= 15; i++) {
                imgNumeros[i] = ImageIO.read(getClass().getResource("/pipoo/pong/imagenes/" + i + ".png"));
            }

            //puntos estilo neon
            /*
            imgNumerosNeon = new BufferedImage[16];
            for (int i = 0; i<= 15; i++) {
                imgNumerosNeon[i] = ImageIO.read(getClass().getResource("/pipoo/pong/neon/" + i + ".png"));
            }

             */
            pelota.setImagen(imgPelota);
            paleta1.setImagen(imgPaleta);
            paleta2.setImagen(imgPaleta);

        } catch (IOException e){
            System.err.println("Error cargando assets de Pong " +e.getMessage());

        }
        this.getFrame().addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                stop();
            }
        });
    }

    @Override
    public void gameUpdate(double delta) {
        Keyboard teclado = this.getKeyboard();

        // Controles jugador 1
        if (teclado.isKeyPressed(KeyEvent.VK_W)) {
            paleta1.moverArriba(delta);
        }
        if (teclado.isKeyPressed(KeyEvent.VK_S)) {
            paleta1.moverAbajo(delta);
        }

        // Controles jugador 2 / BOT
        if (this.jugador2 instanceof Bot) {
            // Si es un BOT, ejecuta su logica
            this.actualizarBOT(delta);
        } else {
            // Si no es un bot, es un humano
            if (teclado.isKeyPressed(KeyEvent.VK_UP)) {
                paleta2.moverArriba(delta);
            } else if (teclado.isKeyPressed(KeyEvent.VK_DOWN)) {
                paleta2.moverAbajo(delta);
            }
        }
        pelota.mover(delta);
        detectarColisiones();
        actualizarPuntaje();
    }

    @Override
    public void gameDraw(Graphics2D g) {
        //dibujamos el fondo
        if (imgCancha != null) {
            g.drawImage(imgCancha, 0, 0, getWidth(), getHeight(), null);
        }
        if (imgDivisor != null) {
            int posX = (getWidth()/2) - (imgDivisor.getWidth() / 2);
            g.drawImage(imgDivisor, posX, 1, imgDivisor.getWidth(), getHeight(), null);
        }

        if (juegoFinalizado) {
            g.drawImage(imgGameOver, 0,0, getWidth(), getHeight(), null);
        }

        //bordes
        g.setColor(Color.WHITE);
        if (bordeSuperior != null) {
            g.fill(bordeSuperior);
        }

        if (bordeInferior != null){
            g.fill(bordeInferior);
        }

        //usa puntosj1 y puntosj2 como indice
        if (imgNumeros[puntosJ1] != null) {
            g.drawImage(imgNumeros[puntosJ1], getWidth() / 4, 90, null);
        }
        if (imgNumeros[puntosJ2] != null) {
            // Posicionamos la imagen del número actual del J2
            g.drawImage(imgNumeros[puntosJ2], (getWidth() / 4) * 3, 90, null);
        }

        pelota.dibujar(g);
        paleta1.dibujar(g);
        paleta2.dibujar(g);

        //dibujamos los nombres de los jugadores
        g.setFont(new Font("Monospaced", Font.BOLD, 20));
        g.setColor(Color.WHITE);
        if (this.nombreJ1 != null){
            g.drawString(this.nombreJ1, (getWidth()/4)-20, 75);
        }
        if (this.nombreJ2 != null) {
            g.drawString(this.nombreJ2, (getWidth()/4)*3-45, 75);
        }

    }

    @Override
    public void gameShutdown() {
        // SistemaDeJuego con ranking:
        // sistemaDeJuego.guardarPuntaje("J1", marcador.getPuntosJ1());
        // sistemaDeJuego.guardarPuntaje("J2", marcador.getPuntosJ2());
        gestorAudio.detenerMusica();
        pelota = null;
        paleta1 = null;
        paleta2 = null;
        marcador = null;
    }

    @Override
    protected void detectarColisiones() {
        // Reboteeeee con borde superior
        if (pelota.intersects(bordeSuperior)) {
            pelota.rebotarVertical(0, true);
            pelota.y = bordeSuperior.y + bordeSuperior.height + 1;
            paleta1.incrementarVelocidad();
            paleta2.incrementarVelocidad();
            gestorAudio.reproducirEfecto("rebote_borde");
        }
        // Colision con el borde inferior
        if (pelota.intersects(bordeInferior)) {
            pelota.rebotarVertical(0, false);
            pelota.y = bordeInferior.y - pelota.height - 1;
            paleta1.incrementarVelocidad();
            paleta2.incrementarVelocidad();
            gestorAudio.reproducirEfecto("rebote_borde");
        }

        // Colision con paletas
        if (pelota.colisionaCon(paleta1)) {
            pelota.reaccionarAColision(paleta1);
            gestorAudio.reproducirEfecto("rebote_paleta");
        }
        if (pelota.colisionaCon(paleta2)) {
            pelota.reaccionarAColision(paleta2);
            gestorAudio.reproducirEfecto("rebote_paleta");
        }
    }

    @Override
    protected void actualizarPuntaje() {
        // si el juego terminó, no seguimos sumando puntos
        if (juegoFinalizado) { return; }

        // Si la pelota sale por la izquierda (punto J2)
        if (pelota.x + pelota.width < 0) {
            puntosJ2++;
            if (verificarFinDeJuego()) {
                juegoFinalizado = true;
            } else {
                pelota.reiniciar(-1);
                paleta1.resetVelocidad();
                paleta2.resetVelocidad();
            }
            gestorAudio.reproducirEfecto("punto");
        }
        // Si la pelota sale por la derecha (punto J1)
        else if (pelota.x > getWidth()) {
            puntosJ1++;
            if (verificarFinDeJuego()) {
                juegoFinalizado = true;
            } else {
                pelota.reiniciar(1);
                paleta1.resetVelocidad();
                paleta2.resetVelocidad();
            }
            gestorAudio.reproducirEfecto("punto");
        }
    }

    private void actualizarBOT(double delta) {
        double centroPaleta = paleta2.getY() + (paleta2.getHeight() / 2);
        double centroPelota = pelota.getY() + (pelota.getRadio());

        // Usamos el margenError
        double margen = ((Bot) jugador2).getMargenError();
        double vActual = paleta2.getVelocidadDesplazamiento();

        if (centroPelota < centroPaleta - margen) {
            paleta2.setVelocidadY(-vActual);
        } else if (centroPelota > centroPaleta + margen) {
            paleta2.setVelocidadY(vActual);  // Mover hacia abajo
        } else {
            paleta2.setVelocidadY(0);    // Frenar si está alineado
        }
        paleta2.mover(delta);
    }

    //verifica si alguien gano
    private boolean verificarFinDeJuego() {
        boolean fin = false;
        if (puntosJ1 == this.puntosLimite || puntosJ2 == this.puntosLimite) {
            fin=true;
        }
        return fin;
    }

    public static void main(String[] args) {
        Pong p = new Pong();
        p.run(1.0/60.0);
        System.exit(0);
    }


}




