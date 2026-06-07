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
import java.net.URL;

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
    private String pistaMusical;

    //imagenes
    private BufferedImage[] imgNumeros;
    private Rectangle2D.Double bordeSuperior;
    private Rectangle2D.Double bordeInferior;
    private String skinCancha;
    private String skinPelota;
    private String skinPaleta;
    private String rutaCancha, rutaPaleta, rutaPaleta2, rutaPelota, rutaDivisor;

    //sonido
    private GestorAudio gestorAudio = new GestorAudio();

    //atributos para gestionar el tiempo de pantalla de carga
    private BufferedImage imgLoading;
    private double acumTiempo = 0.0;
    private boolean enCarga = true;

    public Pong() {
        super("PIPOO PONG", 800, 600); // titulo y tamaño de ventana
        System.out.println("Propiedades en memoria: " + this.appProperties.stringPropertyNames());

    }

    @Override
    public void gameStartup() {
        try {
            //pantalla de carga
            this.imgLoading = ImageIO.read(getClass().getResource("/pipoo/pong/imagenes/pantalla_carga.png"));

            ConfiguracionPong config = (ConfiguracionPong) this.getConfiguracion();
            pistaMusical = config.getPistaMusical(); //pueden ser ninguna, jeff the bat o keyboard cat

            skinPelota = appProperties.getProperty("skinPelota", "Original");
            skinPaleta = appProperties.getProperty("skinPaletas", "Original");
            skinCancha = appProperties.getProperty("skinCancha", "Original");

            //ranking
            this.nombreJ1 = this.appProperties.getProperty("nombreJ1", "Invitado");

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
            bordeInferior = new Rectangle2D.Double(0, getHeight() - grosorBorde, getWidth(), grosorBorde);


            pelota = new Pelota(400, 300, 15, 15);
            paleta1 = new Paleta(20, (double) getHeight() / 2 - 40, 15, 80);
            paleta2 = new Paleta(getWidth() - 35, (double) getHeight() / 2 - 40, 15, 80);


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
                String seleccion = config.getPistaMusical();
                if (seleccion != null && !"Ninguna".equals(seleccion)) {
                    String nombreArchivo = "";
                    if ("Jeff The Bat".equals(seleccion)) {
                        nombreArchivo = "sonidos/jeffthebat.wav";
                    } else if ("Keyboard Cat".equals(seleccion)) {
                        nombreArchivo = "sonidos/keyboard_cat.wav";
                    } else if ("Koopa Troopa Beach".equals(seleccion)) {
                        nombreArchivo = "sonidos/koopa_troopa_beach.wav";
                    } else if ("Under The Sea".equals(seleccion)) {
                        nombreArchivo = "sonidos/under_the_sea.wav";
                    } else if ("Yoshi Island".equals(seleccion)) {
                        nombreArchivo = "sonidos/yoshi_island.wav";
                    }
                    if (!nombreArchivo.isEmpty()) {
                        URL urlMusica = getClass().getResource(nombreArchivo);
                        if (urlMusica != null) {
                            gestorAudio.reproducirMusica(urlMusica);
                        } else {
                            System.err.println("No se encontró el archivo: " + nombreArchivo);
                        }
                    }
                }

                // Efectos cortos
                gestorAudio.precargarEfecto("rebote_borde", getClass().getResource("sonidos/pelota_rebota_borde.wav"));
                gestorAudio.precargarEfecto("rebote_paleta", getClass().getResource("sonidos/pelota_rebota_paleta.wav"));
                gestorAudio.precargarEfecto("punto", getClass().getResource("sonidos/anotacion.wav"));
            }


            //cargamos los assets (DEFAULT)
            // --- LOGICA PARA LA PELOTA ---
            rutaPelota = "/pipoo/pong/imagenes/pelota_default.png";
            if (skinPelota.equalsIgnoreCase("Shpong")) {
                pelota = new Pelota(400, 300, 32, 32);
                rutaPelota = "/pipoo/pong/shpong/saturno.png";
            } else if (skinPelota.equals("Japong")) {
                pelota = new Pelota(400, 300, 32, 32);
                rutaPelota = "/pipoo/pong/japong/pelota_japong.png";
            }
            BufferedImage imgPelota = ImageIO.read(getClass().getResource(rutaPelota));
            pelota.setImagen(imgPelota);

            // --- LOGICA PARA LAS PALETAS ---
            rutaPaleta = "/pipoo/pong/imagenes/paleta_default.png";
            rutaPaleta2 = null;
            if (skinPaleta.equalsIgnoreCase("Shpong")) {
                rutaPaleta = "/pipoo/pong/shpong/nave_azul.png";
                rutaPaleta2 = "/pipoo/pong/shpong/nave_azul_derecha.png";
                paleta1 = new Paleta(20, (double) getHeight() / 2 - 40, 40, 80);
                paleta2 = new Paleta(getWidth() - 45, (double) getHeight() / 2 - 40, 40, 80);
            } else if (skinPaleta.equalsIgnoreCase("Japong")) {
                rutaPaleta = "/pipoo/pong/japong/paleta_japong.png";
            }
            BufferedImage imgPaleta = ImageIO.read(getClass().getResource(rutaPaleta));
            BufferedImage imgPaleta2 = (rutaPaleta2 != null) ? ImageIO.read(getClass().getResource(rutaPaleta2)) : imgPaleta;
            paleta2.setImagen(imgPaleta);
            paleta1.setImagen(imgPaleta2);

            // --- LÓGICA PARA LA CANCHA ---
            rutaCancha = "/pipoo/pong/imagenes/cancha_default.png"; // Default
            rutaDivisor = "/pipoo/pong/imagenes/barra_del_medio.png";
            this.imgNumeros = new BufferedImage[16];
            if (skinCancha.equalsIgnoreCase("Shpong")) {
                rutaCancha = "/pipoo/pong/shpong/cancha_shpong.png";
                this.imgCancha = ImageIO.read(getClass().getResource(rutaCancha));
                this.imgGameOver = ImageIO.read(getClass().getResource("/pipoo/pong/shpong/game_over_shpong.png"));
                for (int i = 0; i <= 15; i++) {
                    this.imgNumeros[i] = ImageIO.read(getClass().getResource("/pipoo/pong/shpong/" + i + ".png"));
                }
            } else if (skinCancha.equalsIgnoreCase("Japong")) {
                rutaCancha = "/pipoo/pong/japong/cancha_japong.png";
                this.imgCancha = ImageIO.read(getClass().getResource(rutaCancha));
                for (int i = 0; i <= 15; i++) {
                    this.imgNumeros[i] = ImageIO.read(getClass().getResource("/pipoo/pong/japong/" + i + ".png"));
                }
            } else {
                // ELEMENTOS COMUNES (Divisor y Game Over)
                this.imgDivisor = ImageIO.read(getClass().getResource(rutaDivisor));
                this.imgGameOver = ImageIO.read(getClass().getResource("/pipoo/pong/imagenes/game_over.png"));
                for (int i = 0; i <= 15; i++) {
                    this.imgNumeros[i] = ImageIO.read(getClass().getResource("/pipoo/pong/imagenes/" + i + ".png"));
                }
            }
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
        if (enCarga) {
            // --- ESTADO DE CARGA ---
            acumTiempo += delta;
            if (acumTiempo >= 3.0) {
                enCarga = false;
            }
        } else {
            Keyboard teclado = this.getKeyboard();

            // Movimiento Jugador 1
            if (teclado.isKeyPressed(KeyEvent.VK_W)) {
                paleta1.moverArriba(delta);
            }
            if (teclado.isKeyPressed(KeyEvent.VK_S)) {
                paleta1.moverAbajo(delta);
            }

            // Lógica del Jugador 2 o BOT
            if (this.jugador2 instanceof Bot) {
                this.actualizarBOT(delta);
            } else {
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
    }

    @Override
    public void gameDraw(Graphics2D g) {
        if (enCarga) {
            // pantalla de carga...
            if (imgLoading != null) {
                g.drawImage(imgLoading, 0, 0, getWidth(), getHeight(), null);
            }
        } else {
            //si la carga es falsa, dibujamos todo
            // fondo de la cancha
            if (imgCancha != null) {
                g.drawImage(imgCancha, 0, 0, getWidth(), getHeight(), null);
            }

            // divisor de cancha
            if (imgDivisor != null) {
                int posX = (getWidth() / 2) - (imgDivisor.getWidth() / 2);
                g.drawImage(imgDivisor, posX, 1, imgDivisor.getWidth(), getHeight(), null);
            }

            // game over
            if (juegoFinalizado) {
                g.drawImage(imgGameOver, 0, 0, getWidth(), getHeight(), null);
            }

            // bordes
            if (skinCancha.equalsIgnoreCase("Shpong")) {
                GradientPaint gradiente = new GradientPaint(0, 0, new Color(60, 20, 160), getWidth() / 2, 0, new Color(20, 100, 220), true);
                g.setPaint(gradiente);
            } else {
                g.setColor(Color.WHITE);
            }

            if (bordeSuperior != null) g.fill(bordeSuperior);
            if (bordeInferior != null) g.fill(bordeInferior);

            // marcador
            if (imgNumeros[puntosJ1] != null) {
                g.drawImage(imgNumeros[puntosJ1], getWidth() / 4, 90, null);
            }
            if (imgNumeros[puntosJ2] != null) {
                g.drawImage(imgNumeros[puntosJ2], (getWidth() / 4) * 3, 90, null);
            }

            pelota.dibujar(g);
            paleta1.dibujar(g);
            paleta2.dibujar(g);

            // nombre de jugadores
            g.setFont(new Font("Consolas", Font.BOLD, 20));
            g.setColor(Color.WHITE);
            if (this.nombreJ1 != null) g.drawString(this.nombreJ1, (getWidth() / 4) - 20, 75);
            if (this.nombreJ2 != null) g.drawString(this.nombreJ2, (getWidth() / 4) * 3 - 45, 75);
        }
    }

    @Override
    public void gameShutdown() {
        // SistemaDeJuego con ranking:
        Ranking manager = new Ranking("ranking_pong.dat");
        manager.cargarRanking();
        String fecha = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());

        //entrada para jugador 1
        manager.agregarEntrada(new RankingEntry(this.nombreJ1, 1, this.puntosJ1, fecha));

        // entrada jugador 2 (Solo si no es un BOT)
        if (!(this.jugador2 instanceof Bot)) {
            manager.agregarEntrada(new RankingEntry(this.nombreJ2, 1, this.puntosJ2, fecha));
        }

        manager.guardarRanking();

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




