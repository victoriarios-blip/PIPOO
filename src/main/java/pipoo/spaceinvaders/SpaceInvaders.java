package pipoo.spaceinvaders;

import com.entropyinteractive.Keyboard;
import pipoo.core.*;
import pipoo.core.configuracion.ConfiguracionSI;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class SpaceInvaders extends Juego {
    // entidades principales
    private NaveHeroe jugador;
    private List<Enemigo> oleada;
    private List<Escudo> escudos;
    private NaveNodriza enemigoFinal;
    private List<Proyectil> proyectilesEnemigos;
    private List<Proyectil> proyectilesHeroe;

    // estado, niveles, marcadores
    private enum Estado { CARGA, JUGANDO, GAMEOVER }
    private Estado estadoActual = Estado.CARGA;
    private String nombreJ1;
    private int puntaje = 0;
    private int nivel = 1;
    private double tiempoJuego = 0;
    private static int contadorDisparosTotales = 0;
    private int cantidadAliensIniciales = 0;
    private double factorVelocidadGlobal = 1.0;

    // skins, pantalla carga, animaciones
    private int skinSeleccionada = 1; // 1 = original, 2 = color, 3 = halloween
    private double acumuladorCarga = 0;
    private static final double DURACION_CARGA = 5.0;
    private double tiempoGameOver = 0;
    private double escalaGameOver = 0.0;
    private java.util.Map<Character, BufferedImage> fuenteArcade;

    // assets
    private BufferedImage imgProyectilHeroe;
    private BufferedImage imgProyectilEnemigo;
    private BufferedImage imgNaveNodriza;
    private BufferedImage imgPantallaCarga;
    private BufferedImage imgGameOver;
    private BufferedImage naveHeroe;
    private BufferedImage naveHeroeExplosion1;
    private BufferedImage naveHeroeExplosion2;
    private BufferedImage pulpo1;
    private BufferedImage pulpo2;
    private BufferedImage cangrejo1;
    private BufferedImage cangrejo2;
    private BufferedImage calamar1;
    private BufferedImage calamar2;
    private BufferedImage escudoIntacto;
    private BufferedImage escudo1daño;
    private BufferedImage escudo2daño;
    private BufferedImage escudo3daño;
    private BufferedImage escudo4daño;
    private BufferedImage muerteEnemigo;

    // audio y controles temporales
    private int pistaMusicalSeleccionada = 1;
    private boolean sonidoActivado = true;
    private double tiempoMarcha = 0;
    private double tiempoNodriza = 0;
    private double tiempoUfoSonido = 0;
    private int pasoMarcha = 1;
    private static final double intervaloNodriza = 20;

    // códigos de teclado
    private int teclaIzq;
    private int teclaDer;
    private int teclaDisparo;

    // constructor
    public SpaceInvaders() {
        super("PIPOO SPACE INVADERS", 800, 600);
    }

    // contador de disparos
    public static int getContadorDisparos() { return contadorDisparosTotales; }

    @Override
    public void gameStartup() {
        // 1. configuraciones y controles
        ConfiguracionSI config = (ConfiguracionSI) this.getConfiguracion();
        double velocidadBaseAliens = 40;
        if (config != null) {
            this.nombreJ1 = config.getNombreJ1();
            this.sonidoActivado = config.isSonidoActivado();
            this.teclaIzq = config.getTeclaIzq();
            this.teclaDer = config.getTeclaDer();
            this.teclaDisparo = config.getTeclaDisparo();

            String velElegida = config.getVelocidadInvasores();
            if ("Lenta".equals(velElegida)) {
                velocidadBaseAliens = 20; // terminan en 60
            } else if ("Rápida".equals(velElegida)) {
                velocidadBaseAliens = 65; //terminan en 195
            }

            String skinElegida = config.getSkinModo();
            if ("Color".equals(skinElegida))        this.skinSeleccionada = 2;
            else if ("Halloween".equals(skinElegida)) this.skinSeleccionada = 3;
            else                                      this.skinSeleccionada = 1;

            String musicaElegida = config.getPistaMusical();
            this.pistaMusicalSeleccionada = "Tema 2 (Alternativo)".equals(musicaElegida) ? 2 : 1;
        } else {
            this.nombreJ1 = this.appProperties.getProperty("nombreJ1", "Invitado");
            this.teclaIzq = 37;      // val x defecto: flechas y espacio
            this.teclaDer = 39;
            this.teclaDisparo = 32;
        }

        // 2. precarga audios y efectos
        gestorAudio.precargarEfecto("playagain", this.getClass().getResource("audio/playagain.wav"));
        gestorAudio.precargarEfecto("shoot", this.getClass().getResource("audio/shoot.wav"));
        gestorAudio.precargarEfecto("explosion", this.getClass().getResource("audio/explosion.wav"));
        gestorAudio.precargarEfecto("spaceinvaderdead", this.getClass().getResource("audio/spaceinvaderdead.wav"));
        gestorAudio.precargarEfecto("ufo_lowpitch", this.getClass().getResource("audio/ufo_lowpitch.wav"));
        gestorAudio.precargarEfecto("fastinvader1", this.getClass().getResource("audio/fastinvader1.wav"));
        gestorAudio.precargarEfecto("fastinvader2", this.getClass().getResource("audio/fastinvader2.wav"));
        gestorAudio.precargarEfecto("fastinvader3", this.getClass().getResource("audio/fastinvader3.wav"));
        gestorAudio.precargarEfecto("fastinvader4", this.getClass().getResource("audio/fastinvader4.wav"));

        if (estadoActual == Estado.CARGA && sonidoActivado) {
            gestorAudio.reproducirMusica(this.getClass().getResource("audio/pantallacarga.wav"));
        }

        System.out.println("Iniciando Space Invaders...");

        // 3. inicializacion listas y entidades
        oleada = new ArrayList<>();
        escudos = new ArrayList<>();
        proyectilesEnemigos = new ArrayList<>();
        proyectilesHeroe = new ArrayList<>();

        jugador = new NaveHeroe(380, 525);

        int[] posicionesEscudosX = {104, 272, 440, 608};
        for (int x : posicionesEscudosX) {
            escudos.add(new Escudo(x, 410));
        }

        int inicioX = (800 - 638) / 2;
        int inicioY = 50;

        for (int fila = 0; fila < 5; fila++) {
            for (int col = 0; col < 11; col++) {
                double x = inicioX + (col * 58);
                double y = inicioY + (fila * 34) + (nivel - 1) * 34;

                if (fila == 0)      oleada.add(new Pulpo(x, y));
                else if (fila < 3)  oleada.add(new Cangrejo(x, y));
                else                oleada.add(new Calamar(x, y));
            }
        }

        // buffering y carga de assets (variación de skins)
        try {
            String sufijo = "";
            if (skinSeleccionada == 2)      sufijo = "Color";
            else if (skinSeleccionada == 3) sufijo = "HW";

            naveHeroe = ImageIO.read(this.getClass().getResource("imagenes/naveHeroeIntacta" + sufijo + ".png"));
            naveHeroeExplosion1 = ImageIO.read(this.getClass().getResource("imagenes/naveHeroeExplosion1" + sufijo + ".png"));
            naveHeroeExplosion2 = ImageIO.read(this.getClass().getResource("imagenes/naveHeroeExplosion2" + sufijo + ".png"));

            imgNaveNodriza = ImageIO.read(this.getClass().getResource("imagenes/naveNodriza" + sufijo + ".png"));

            pulpo1 = ImageIO.read(this.getClass().getResource("imagenes/pulpo1" + sufijo + ".png"));
            pulpo2 = ImageIO.read(this.getClass().getResource("imagenes/pulpo2" + sufijo + ".png"));

            cangrejo1 = ImageIO.read(this.getClass().getResource("imagenes/cangrejo1" + sufijo + ".png"));
            cangrejo2 = ImageIO.read(this.getClass().getResource("imagenes/cangrejo2" + sufijo + ".png"));

            calamar1 = ImageIO.read(this.getClass().getResource("imagenes/calamar1" + sufijo + ".png"));
            calamar2 = ImageIO.read(this.getClass().getResource("imagenes/calamar2" + sufijo + ".png"));

            imgProyectilHeroe   = ImageIO.read(this.getClass().getResource("imagenes/proyectilHeroe" + sufijo + ".png"));
            imgProyectilEnemigo = ImageIO.read(this.getClass().getResource("imagenes/proyectilEnemigo" + sufijo + ".png"));

            escudoIntacto = ImageIO.read(this.getClass().getResource("imagenes/escudoIntacto" + sufijo + ".png"));
            escudo1daño = ImageIO.read(this.getClass().getResource("imagenes/escudo1daño" + sufijo + ".png"));
            escudo2daño = ImageIO.read(this.getClass().getResource("imagenes/escudo2daño" + sufijo + ".png"));
            escudo3daño = ImageIO.read(this.getClass().getResource("imagenes/escudo3daño" + sufijo + ".png"));
            escudo4daño = ImageIO.read(this.getClass().getResource("imagenes/escudo4daño" + sufijo + ".png"));

            imgPantallaCarga = ImageIO.read(this.getClass().getResource("imagenes/pantalla_carga_SI.png"));
            imgGameOver = ImageIO.read(this.getClass().getResource("imagenes/game_over.png"));
            muerteEnemigo = ImageIO.read(this.getClass().getResource("imagenes/muerte_enemigo" + sufijo + ".png"));

            fuenteArcade = new java.util.HashMap<>();
            tiempoJuego = 0;

            for (int i = 0; i <= 9; i++) {
                char numero = (char) ('0' + i);
                BufferedImage imgNum = ImageIO.read(this.getClass().getResource("imagenes/" + i + ".png"));
                fuenteArcade.put(numero, imgNum);
            }

            for (char c = 'A'; c <= 'Z'; c++) {
                String nombreArchivo = String.valueOf(c).toLowerCase() + ".png";
                BufferedImage imgLetra = ImageIO.read(this.getClass().getResource("imagenes/" + nombreArchivo));
                fuenteArcade.put(c, imgLetra);
            }

            for (Escudo escudo : escudos) {
                escudo.setImgIntacto(escudoIntacto);
                escudo.setEscudo1daño(escudo1daño);
                escudo.setEscudo2daño(escudo2daño);
                escudo.setEscudo3daño(escudo3daño);
                escudo.setEscudo4daño(escudo4daño);
                escudo.setImagen(escudoIntacto);
            }

            jugador.setImagenIntacta(naveHeroe);
            jugador.setImagenExplosion1(naveHeroeExplosion1);
            jugador.setImagenExplosion2(naveHeroeExplosion2);

            for (Enemigo e : oleada) {
                if (e instanceof Pulpo)         e.setImagenes(pulpo1, pulpo2);
                else if (e instanceof Cangrejo) e.setImagenes(cangrejo1, cangrejo2);
                else if (e instanceof Calamar)  e.setImagenes(calamar1, calamar2);
                e.setImagenMuerte(muerteEnemigo);
            }

        } catch (Exception e) {
            System.out.println("Error cargando los assets: " + e.getMessage());
        }

        for (Enemigo e : oleada) {
            e.setVelocidadX(velocidadBaseAliens);
        }
        cantidadAliensIniciales = oleada.size();
    }

    @Override
    public void gameUpdate(double delta) {
        // pantalla de carga
        if (estadoActual == Estado.CARGA) {
            acumuladorCarga += delta;
            if (acumuladorCarga >= DURACION_CARGA) {
                estadoActual = Estado.JUGANDO;
                if (sonidoActivado) {
                    gestorAudio.reproducirMusica(getUrlMusicaSeleccionada());
                }
            }
            return;
        }

        // game over
        if (estadoActual == Estado.GAMEOVER) {
            tiempoGameOver += delta;
            if (escalaGameOver < 1.0) {
                escalaGameOver += delta * 2.0;
                if (escalaGameOver > 1.0) escalaGameOver = 1.0;
            }
            Keyboard teclado = this.getKeyboard();
            if (teclado.isKeyPressed(KeyEvent.VK_ESCAPE)) {
                gestorAudio.detenerMusica(); // apaga cualquier rastro de sonido
                this.stop();                 // detiene el bucle, cierra ventana actual
                return;
            }
            if (teclado.isKeyPressed(KeyEvent.VK_ENTER) || teclado.isKeyPressed(KeyEvent.VK_SPACE)) {
                reproducirEfecto("playagain");
                reiniciarJuego();
            }

            return;
        }

        // gameplay activo
        Keyboard teclado = this.getKeyboard();

        if (!jugador.isMuriendo()) {
            // escucha teclas que ingresa el jugador
            if (teclado.isKeyPressed(teclaIzq))       jugador.moverIzquierda();
            else if (teclado.isKeyPressed(teclaDer))  jugador.moverDerecha();
            else                                      jugador.detener();

            if (teclado.isKeyPressed(teclaDisparo) && proyectilesHeroe.isEmpty()) {
                Proyectil p = jugador.disparar();
                if (p != null) {
                    p.setImagen(imgProyectilHeroe);
                    proyectilesHeroe.add(p);
                    contadorDisparosTotales++;
                    reproducirEfecto("shoot");
                }
            }
        } else {
            jugador.detener();
        }

        jugador.mover(delta);

        // aceleración movimiento aliens
        if (!oleada.isEmpty()) {
            double porcentajeDestruido = 1.0 - ((double) oleada.size() / cantidadAliensIniciales);
            factorVelocidadGlobal = 1.0 + (porcentajeDestruido * 2.0);
        }

        for (Enemigo enemigo : oleada) {
            enemigo.mover(delta * factorVelocidadGlobal);
            enemigo.actualizarFrame(delta * factorVelocidadGlobal);
        }

        // paso rítmico aliens
        tiempoMarcha += delta * factorVelocidadGlobal;
        if (tiempoMarcha >= 0.8) {
            reproducirEfecto("fastinvader" + pasoMarcha);
            pasoMarcha++;
            if (pasoMarcha > 4) pasoMarcha = 1;
            tiempoMarcha = 0;
        }

        boolean tocoBorde = false;
        for (Enemigo e : oleada) {
            if (e.x <= 0 || e.x + e.width >= 800) {
                tocoBorde = true;
                break;
            }
        }

        if (tocoBorde) {
            for (Enemigo e : oleada) { e.bajarFila(20); }
        }

        for (Enemigo enemigo : oleada) {
            Proyectil p = enemigo.disparar();
            if (p != null) {
                p.setImagen(imgProyectilEnemigo);
                proyectilesEnemigos.add(p);
            }
        }

        for (Proyectil p : proyectilesEnemigos) { p.mover(delta); }
        for (Proyectil p : proyectilesHeroe)    { p.mover(delta); }

        for (Escudo escudo : escudos) { escudo.resetFrame(); }

        detectarColisiones();
        actualizarPuntaje();
        limpiarNoVisibles();

        // derrota por destrucción de la naveHeroe
        if (!jugador.isVisible() && jugador.getVidas() <= 0) {
            System.out.println("GAME OVER - Te quedaste sin vidas");
            gestorAudio.detenerMusica();
            reproducirEfecto("explosion");
            estadoActual = Estado.GAMEOVER;
            tiempoGameOver = 0;
            escalaGameOver = 0.0;
            return;
        }

        // derrota por invasión
        for (Enemigo e : oleada) {
            if (e.y + e.height >= 500) {
                System.out.println("GAME OVER - Los enemigos llegaron al límite");
                gestorAudio.detenerMusica();
                reproducirEfecto("explosion");
                estadoActual = Estado.GAMEOVER;
                tiempoGameOver = 0;
                escalaGameOver = 0.0;
                return;
            }
        }

        if (oleada.isEmpty()) {
            System.out.println("¡Nivel " + nivel + " completado!");
            avanzarDeNivel();
        }

        // nave nodriza
        tiempoNodriza += delta;
        if (tiempoNodriza >= intervaloNodriza) {
            enemigoFinal = new NaveNodriza(-60, 45);
            enemigoFinal.setImagen(imgNaveNodriza);
            tiempoNodriza = 0;
        }
        if (enemigoFinal != null && enemigoFinal.isVisible()) {
            enemigoFinal.mover(delta);

            tiempoUfoSonido += delta;
            if (tiempoUfoSonido >= 0.25) {
                reproducirEfecto("ufo_lowpitch");
                tiempoUfoSonido = 0;
            }
        }

        jugador.actualizar(delta);
        tiempoJuego += delta;
    }

    private void avanzarDeNivel() {
        nivel++;
        factorVelocidadGlobal = 1.0;

        proyectilesHeroe.clear();
        proyectilesEnemigos.clear();
        enemigoFinal = null;

        jugador.setX(380);
        jugador.setY(525);
        jugador.setVisible(true);

        oleada.clear();
        escudos.clear();

        gameStartup();
    }

    void limpiarNoVisibles() {
        proyectilesEnemigos.removeIf(p -> !p.isVisible() || p.y > 600);
        proyectilesHeroe.removeIf(p -> !p.isVisible() || p.y < 0);
        oleada.removeIf(e -> !e.isVisible());
    }

    @Override
    public void gameDraw(Graphics2D g) {
        if (estadoActual == Estado.CARGA) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            if (imgPantallaCarga != null) {
                g.drawImage(imgPantallaCarga, 0, 0, getWidth(), getHeight(), null);
            }
            return;
        }

        if (estadoActual == Estado.GAMEOVER) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            java.awt.geom.AffineTransform transformOriginal = g.getTransform();
            java.awt.Composite compositeOriginal = g.getComposite();

            g.translate(400, 300);
            g.scale(escalaGameOver, escalaGameOver);
            g.translate(-400, -300);

            float alphaPalpitar = (float) (0.65f + 0.35f * Math.sin(tiempoGameOver * 5.0));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaPalpitar));

            if (imgGameOver != null) {
                g.drawImage(imgGameOver, 250, 110, 300, 180, null);
            }

            g.setComposite(compositeOriginal);
            if ((int)(tiempoGameOver * 2.5) % 2 == 0) {
                dibujarTextoRetro(g, "PRESS ENTER TO PLAY AGAIN", 176, 340);
                dibujarTextoRetro(g, "PRESS ESC TO EXIT TO MENU", 176, 380);
            }

            g.setTransform(transformOriginal);
            return;
        }

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (Enemigo e : oleada)          { e.dibujar(g); }
        for (Escudo escudo : escudos)     { escudo.dibujar(g); }
        for (Proyectil p : proyectilesHeroe)   { p.dibujar(g); }
        for (Proyectil p : proyectilesEnemigos){ p.dibujar(g); }

        jugador.dibujar(g);

        if (enemigoFinal != null && enemigoFinal.isVisible()) {
            enemigoFinal.dibujar(g);
        }

        // render hud
        g.setColor(Color.GREEN);
        g.fillRect(0, 555, 800, 4);

        String strPuntaje = "SCORE " + String.format("%04d", puntaje);
        String strTiempo  = "TIME "  + String.format("%03d", (int)tiempoJuego);
        String strVidas   = "LIVES " + jugador.getVidas();

        dibujarTextoRetro(g, strPuntaje, 40, 570);
        dibujarTextoRetro(g, strTiempo, 330, 570);
        dibujarTextoRetro(g, strVidas, 620, 570);
    }

    private void dibujarTextoRetro(Graphics2D g, String texto, int x, int y) {
        texto = texto.toUpperCase();
        int anchoCaracter = 16;
        int altoCaracter = 16;
        int espaciado = 2;

        for (int i = 0; i < texto.length(); i++) {
            char caracter = texto.charAt(i);
            if (caracter != ' ') {
                BufferedImage img = fuenteArcade.get(caracter);
                if (img != null) {
                    g.drawImage(img, x, y, anchoCaracter, altoCaracter, null);
                }
            }
            x += anchoCaracter + espaciado;
        }
    }

    private java.net.URL getUrlMusicaSeleccionada() {
        String archivo = (pistaMusicalSeleccionada == 2) ? "audio/theme2.wav" : "audio/theme1.wav";
        return this.getClass().getResource(archivo);
    }

    private void reproducirEfecto(String nombre) {
        if (sonidoActivado) {
            gestorAudio.reproducirEfecto(nombre);
        }
    }

    @Override
    public void gameShutdown() {
        // 1. instanciar y cargar archivo ranking
        Ranking manager = new Ranking("ranking_si.dat");
        manager.cargarRanking();

        // 2. fecha actual de sistema
        String fecha = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());

        // 3. registra con constructor de 5 parámetros
        // Nombre, Nivel, Puntaje, Fecha y Modo de juego
        manager.agregarEntrada(new RankingEntry(this.nombreJ1, this.nivel, this.puntaje, fecha, "SPACE"));
        manager.guardarRanking();

        // 4. presenta el ranking en la interfaz al finalizar la partida
        // textarea flotante (fondo negro, texto blanco)
        JTextArea areaRanking = new JTextArea(manager.toStrOrdenado("PARTIDA FINALIZADA - TOP 10 GLOBAL", "SPACE"));
        areaRanking.setFont(new Font("Monospaced", Font.PLAIN, 14)); // columnas alineadas
        areaRanking.setBackground(Color.BLACK);
        areaRanking.setForeground(Color.WHITE);
        areaRanking.setEditable(false);

        // se muestra el ranking antes de cerrar la ventana
        JOptionPane.showMessageDialog(null,
                new JScrollPane(areaRanking),
                "PIPOO ARCADE - RANKING",
                JOptionPane.INFORMATION_MESSAGE);

        // 5. cerrar hilos de sonido
        gestorAudio.detenerMusica();
    }

    @Override
    protected void detectarColisiones() {
        for (Proyectil proyectil : proyectilesHeroe) {
            if (!proyectil.isVisible()) continue;
            for (Enemigo enemigo : oleada) {
                if (!enemigo.isVisible()) continue;
                if (proyectil.colisionaCon(enemigo)) {
                    proyectil.reaccionarAColision(enemigo);
                    reproducirEfecto("spaceinvaderdead");
                    enemigo.reaccionarAColision(proyectil);
                    puntaje += enemigo.getValorPuntaje();
                }
            }
        }

        for (Proyectil proyectil : proyectilesHeroe) {
            if (!proyectil.isVisible()) continue;
            for (Escudo escudo : escudos) {
                if (!escudo.isVisible()) continue;
                if (proyectil.colisionaCon(escudo)) {
                    proyectil.reaccionarAColision(escudo);
                    escudo.reaccionarAColision(proyectil);
                }
            }
        }

        for (Proyectil proyectil : proyectilesEnemigos) {
            if (!proyectil.isVisible()) continue;
            if (proyectil.colisionaCon(jugador)) {
                proyectil.reaccionarAColision(jugador);
                jugador.reaccionarAColision(proyectil);
            }
        }

        for (Proyectil proyectil : proyectilesEnemigos) {
            if (!proyectil.isVisible()) continue;
            for (Escudo escudo : escudos) {
                if (!escudo.isVisible()) continue;
                if (proyectil.colisionaCon(escudo)) {
                    proyectil.reaccionarAColision(escudo);
                    escudo.reaccionarAColision(proyectil);
                }
            }
        }

        for (Proyectil ph : proyectilesHeroe) {
            if (!ph.isVisible()) continue;
            for (Proyectil pe : proyectilesEnemigos) {
                if (!pe.isVisible()) continue;
                if (ph.colisionaCon(pe)) {
                    ph.reaccionarAColision(pe);
                    pe.reaccionarAColision(ph);
                }
            }
        }

        if (enemigoFinal != null && enemigoFinal.isVisible()) {
            for (Proyectil proyectil : proyectilesHeroe) {
                if (!proyectil.isVisible()) continue;
                if (proyectil.colisionaCon(enemigoFinal)) {
                    proyectil.reaccionarAColision(enemigoFinal);
                    enemigoFinal.reaccionarAColision(proyectil);
                    puntaje += enemigoFinal.getValorPuntaje();
                }
            }
        }
    }

    @Override
    protected void actualizarPuntaje() { }

    private void reiniciarJuego() {
        oleada.clear();
        escudos.clear();
        proyectilesHeroe.clear();
        proyectilesEnemigos.clear();
        enemigoFinal = null;
        tiempoMarcha = 0;
        pasoMarcha = 1;
        tiempoUfoSonido = 0;

        puntaje = 0;
        tiempoJuego = 0;
        tiempoGameOver = 0;
        escalaGameOver = 0.0;

        nivel = 1;
        contadorDisparosTotales = 0;
        factorVelocidadGlobal = 1.0;

        gameStartup();
        estadoActual = Estado.JUGANDO;
    }
}