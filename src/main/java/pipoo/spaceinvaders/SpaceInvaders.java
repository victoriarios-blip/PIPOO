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

public class SpaceInvaders extends Juego {
    private NaveHeroe jugador;
    private List<Enemigo> oleada;
    private List<Escudo> escudos;
    private NaveNodriza enemigoFinal;
    private String nombreJ1;

    // proyectiles
    private List<Proyectil> proyectilesEnemigos;
    private List<Proyectil> proyectilesHeroe;
    private static int contadorDisparosTotales = 0;

    public static int getContadorDisparos() { return contadorDisparosTotales; }

    // marcador
    private double tiempoJuego = 0; // Acumulador de segundos
    private java.util.Map<Character, java.awt.image.BufferedImage> fuenteArcade;

    // niveles
    private int nivel = 1; // Arranca en el nivel 1
    private int cantidadAliensIniciales = 0; // Para saber cuántos se destruyeron
    private double factorVelocidadGlobal = 1.0; // 1.0 es velocidad normal, irá subiendo

    //aparicion nave nodriza
    private double tiempoNodriza = 0;
    private static final double intervaloNodriza = 20; // aparece cada 20 segundos

    // puntaje
    private int puntaje = 0;

    // marcha enemigos
    private int pasoMarcha = 1;

    private BufferedImage imgProyectilHeroe;
    private BufferedImage imgProyectilEnemigo;
    private BufferedImage imgNaveNodriza;

    // pantalla de carga
    private enum Estado { CARGA, JUGANDO, GAMEOVER }
    private Estado estadoActual = Estado.CARGA; // Arranca en modo carga
    private double acumuladorCarga = 0;
    private static final double DURACION_CARGA = 3.0; // Duración en segundos (ej: 3 segundos)
    private BufferedImage imgPantallaCarga;

    //skins
    private int skinSeleccionada = 1;

    // pantalla game over
    private BufferedImage imgGameOver;
    private double tiempoGameOver = 0;
    private double escalaGameOver = 0.0;

    // audio (Configuración y relojes)
    private int pistaMusicalSeleccionada = 1; // 1 = Original, 2 = Alternativa
    private boolean sonidoActivado = true;    // Para mutear/desmuteas desde la configuración
    private double tiempoMarcha = 0;
    private double tiempoUfoSonido = 0;

    public SpaceInvaders() {
        super("PIPOO SPACE INVADERS", 800, 600);
    }

    @Override
    public void gameStartup() {
        // === 1. LEER LA CONFIGURACIÓN INMEDIATAMENTE (MUDADO AL PRINCIPIO) ===
        ConfiguracionSI config = (ConfiguracionSI) this.getConfiguracion();
        if (config != null) {
            this.nombreJ1 = config.getNombreJ1();
            this.sonidoActivado = config.isSonidoActivado();

            // Mapeo de la Skin elegida al flag numérico de tu juego
            String skinElegida = config.getSkinModo();
            if (skinElegida != null && skinElegida.equals("Color")) {
                this.skinSeleccionada = 2;
            } else if (skinElegida != null && skinElegida.equals("Halloween")) {
                this.skinSeleccionada = 3;
            } else {
                this.skinSeleccionada = 1; // Default / Original
            }

            // Mapeo de la pista musical elegida
            String musicaElegida = config.getPistaMusical();
            if (musicaElegida != null && musicaElegida.equals("Tema 2 (Alternativo)")) {
                this.pistaMusicalSeleccionada = 2;
            } else {
                this.pistaMusicalSeleccionada = 1;
            }
        } else {
            this.nombreJ1 = this.appProperties.getProperty("nombreJ1", "Invitado");
        }

        // === 2. PRECARGA DE EFECTOS EN EL GESTOR COMPARTIDO ===
        gestorAudio.precargarEfecto("playagain", this.getClass().getResource("audio/playagain.wav"));
        gestorAudio.precargarEfecto("shoot", this.getClass().getResource("audio/shoot.wav"));
        gestorAudio.precargarEfecto("explosion", this.getClass().getResource("audio/explosion.wav"));
        gestorAudio.precargarEfecto("spaceinvaderdead", this.getClass().getResource("audio/spaceinvaderdead.wav"));
        gestorAudio.precargarEfecto("ufo_lowpitch", this.getClass().getResource("audio/ufo_lowpitch.wav"));
        gestorAudio.precargarEfecto("fastinvader1", this.getClass().getResource("audio/fastinvader1.wav"));
        gestorAudio.precargarEfecto("fastinvader2", this.getClass().getResource("audio/fastinvader2.wav"));
        gestorAudio.precargarEfecto("fastinvader3", this.getClass().getResource("audio/fastinvader3.wav"));
        gestorAudio.precargarEfecto("fastinvader4", this.getClass().getResource("audio/fastinvader4.wav"));

        // === 3. REPRODUCCIÓN DE MÚSICA DE CARGA ===
        if (estadoActual == Estado.CARGA && sonidoActivado) {
            gestorAudio.reproducirMusica(this.getClass().getResource("audio/pantallacarga.wav"));
        }

        java.net.URL test = this.getClass().getResource("/pipoo/spaceinvaders/imagenes/pulpo1.png");
        System.out.println("PATH TEST: " + test);

        System.out.println("Iniciando Space Invaders...");

        oleada = new ArrayList<>();
        escudos = new ArrayList<>();
        proyectilesEnemigos = new ArrayList<>();
        proyectilesHeroe = new ArrayList<>();

        jugador = new NaveHeroe(380, 525);

        int[] posicionesEscudosX = {104, 272, 440, 608};
        for (int x : posicionesEscudosX) {
            escudos.add(new Escudo(x, 410));
        }

        // oleada enemiga (5 filas x 11 columnas)
        int inicioX = (800 - 638) / 2;
        int inicioY = 50;

        for (int fila = 0; fila < 5; fila++) {
            for (int col = 0; col < 11; col++) {
                double x = inicioX + (col * 58);
                double y = inicioY + (fila * 34) + (nivel - 1) * 34;

                if (fila == 0) oleada.add(new Pulpo(x, y));
                else if (fila < 3) oleada.add(new Cangrejo(x, y));
                else oleada.add(new Calamar(x, y));
            }
        }

        // === 4. CARGA DE ASSETS CON SUFIJO DE SKIN DINÁMICO ===
        try {
            String sufijo = "";
            if (skinSeleccionada == 2) {
                sufijo = "Color";
            } else if (skinSeleccionada == 3) {
                sufijo = "HW";
            }

            // Carga de imágenes usando el sufijo de la skin elegida
            BufferedImage naveHeroe = ImageIO.read(this.getClass().getResource("imagenes/naveHeroeIntacta" + sufijo + ".png"));
            BufferedImage naveHeroeExplosion1 = ImageIO.read(this.getClass().getResource("imagenes/naveHeroeExplosion1" + sufijo + ".png"));
            BufferedImage naveHeroeExplosion2 = ImageIO.read(this.getClass().getResource("imagenes/naveHeroeExplosion2" + sufijo + ".png"));

            imgNaveNodriza = ImageIO.read(this.getClass().getResource("imagenes/naveNodriza" + sufijo + ".png"));

            BufferedImage pulpo1 = ImageIO.read(this.getClass().getResource("imagenes/pulpo1" + sufijo + ".png"));
            BufferedImage pulpo2 = ImageIO.read(this.getClass().getResource("imagenes/pulpo2" + sufijo + ".png"));

            BufferedImage cangrejo1 = ImageIO.read(this.getClass().getResource("imagenes/cangrejo1" + sufijo + ".png"));
            BufferedImage cangrejo2 = ImageIO.read(this.getClass().getResource("imagenes/cangrejo2" + sufijo + ".png"));

            BufferedImage calamar1 = ImageIO.read(this.getClass().getResource("imagenes/calamar1" + sufijo + ".png"));
            BufferedImage calamar2 = ImageIO.read(this.getClass().getResource("imagenes/calamar2" + sufijo + ".png"));

            imgProyectilHeroe   = ImageIO.read(this.getClass().getResource("imagenes/proyectilHeroe" + sufijo + ".png"));
            imgProyectilEnemigo = ImageIO.read(this.getClass().getResource("imagenes/proyectilEnemigo" + sufijo + ".png"));

            BufferedImage escudoIntacto = ImageIO.read(this.getClass().getResource("imagenes/escudoIntacto" + sufijo + ".png"));
            BufferedImage escudo1daño = ImageIO.read(this.getClass().getResource("imagenes/escudo1daño" + sufijo + ".png"));
            BufferedImage escudo2daño = ImageIO.read(this.getClass().getResource("imagenes/escudo2daño" + sufijo + ".png"));
            BufferedImage escudo3daño = ImageIO.read(this.getClass().getResource("imagenes/escudo3daño" + sufijo + ".png"));
            BufferedImage escudo4daño = ImageIO.read(this.getClass().getResource("imagenes/escudo4daño" + sufijo + ".png"));

            imgPantallaCarga = ImageIO.read(this.getClass().getResource("imagenes/pantalla_carga_SI.png"));
            imgGameOver = ImageIO.read(this.getClass().getResource("imagenes/game_over.png"));
            BufferedImage muerteEnemigo = ImageIO.read(this.getClass().getResource("imagenes/muerte_enemigo" + sufijo + ".png"));

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
                if (e instanceof Pulpo) {
                    e.setImagenes(pulpo1, pulpo2);
                } else if (e instanceof Cangrejo) {
                    e.setImagenes(cangrejo1, cangrejo2);
                } else if (e instanceof Calamar) {
                    e.setImagenes(calamar1, calamar2);
                }
                e.setImagenMuerte(muerteEnemigo);
            }

        } catch (Exception e) {
            System.out.println("Error cargando los assets: " + e.getMessage());
        }

        for (Enemigo e : oleada) {
            e.setVelocidadX(40);
        }
        cantidadAliensIniciales = oleada.size();
    }

    @Override
    public void gameUpdate(double delta) {
        // === CONTROL DE TIEMPO DE LA PANTALLA DE CARGA ===
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

        // === CONTROL DE LA PANTALLA DE GAME OVER ===
        if (estadoActual == Estado.GAMEOVER) {
            tiempoGameOver += delta;

            if (escalaGameOver < 1.0) {
                escalaGameOver += delta * 2.0;
                if (escalaGameOver > 1.0) escalaGameOver = 1.0;
            }

            Keyboard teclado = this.getKeyboard();
            if (teclado.isKeyPressed(KeyEvent.VK_ENTER) || teclado.isKeyPressed(KeyEvent.VK_SPACE)) {
                reproducirEfecto("playagain");
                reiniciarJuego();
            }
            return;
        }

        // === LÓGICA DEL JUEGO ACTIVO ===
        Keyboard teclado = this.getKeyboard();

        if (!jugador.isMuriendo()) {
            if (teclado.isKeyPressed(KeyEvent.VK_LEFT))       jugador.moverIzquierda();
            else if (teclado.isKeyPressed(KeyEvent.VK_RIGHT)) jugador.moverDerecha();
            else                                              jugador.detener();

            if (teclado.isKeyPressed(KeyEvent.VK_SPACE) && proyectilesHeroe.isEmpty()) {
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

        if (!oleada.isEmpty()) {
            double porcentajeDestruido = 1.0 - ((double) oleada.size() / cantidadAliensIniciales);
            factorVelocidadGlobal = 1.0 + (porcentajeDestruido * 2.0);
        }

        for (Enemigo enemigo : oleada) {
            enemigo.mover(delta * factorVelocidadGlobal);
            enemigo.actualizarFrame(delta * factorVelocidadGlobal);
        }

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
            for (Enemigo e : oleada) {
                e.bajarFila(20);
            }
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

        for (Escudo escudo : escudos) {
            escudo.resetFrame();
        }

        detectarColisiones();
        actualizarPuntaje();
        limpiarNoVisibles();

        // 9. Verificación de GAME OVER (Por quedarse sin vidas)
        if (!jugador.isVisible() && jugador.getVidas() <= 0) {
            System.out.println("GAME OVER - Te quedaste sin vidas");
            gestorAudio.detenerMusica();
            reproducirEfecto("explosion");
            estadoActual = Estado.GAMEOVER;
            tiempoGameOver = 0;
            escalaGameOver = 0.0;
            return;
        }

        // 10. Verificación de GAME OVER (Si los enemigos invaden la Tierra)
        for (Enemigo e : oleada) {
            if (e.y + e.height >= 500) {
                System.out.println("GAME OVER - Los enemigos llegaron a la línea límite");
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

    // gestion audio ft. gestorAudio
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
        Ranking manager = new Ranking("ranking_si.dat");
        manager.cargarRanking();
        String fecha = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());
        manager.agregarEntrada(new RankingEntry(this.nombreJ1, this.nivel, this.puntaje, fecha));
        manager.guardarRanking();

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