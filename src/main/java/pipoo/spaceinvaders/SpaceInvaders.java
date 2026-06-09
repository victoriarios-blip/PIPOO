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
    // Entidades principales
    private NaveHeroe jugador;
    private List<Enemigo> oleada;
    private List<Escudo> escudos;
    private NaveNodriza enemigoFinal;
    private List<Proyectil> proyectilesEnemigos;
    private List<Proyectil> proyectilesHeroe;

    // Estado, Niveles y Marcadores
    private enum Estado { CARGA, JUGANDO, GAMEOVER }
    private Estado estadoActual = Estado.CARGA;
    private String nombreJ1;
    private int puntaje = 0;
    private int nivel = 1;
    private double tiempoJuego = 0;
    private static int contadorDisparosTotales = 0;
    private int cantidadAliensIniciales = 0;
    private double factorVelocidadGlobal = 1.0;

    // Control Visual y Animaciones
    private int skinSeleccionada = 1; // 1 = Original, 2 = Color, 3 = HW
    private double acumuladorCarga = 0;
    private static final double DURACION_CARGA = 5.0;
    private double tiempoGameOver = 0;
    private double escalaGameOver = 0.0;
    private java.util.Map<Character, BufferedImage> fuenteArcade;

    // Assets gráficos de uso recurrente
    private BufferedImage imgProyectilHeroe;
    private BufferedImage imgProyectilEnemigo;
    private BufferedImage imgNaveNodriza;
    private BufferedImage imgPantallaCarga;
    private BufferedImage imgGameOver;

    // Audio y Controles Dinámicos
    private int pistaMusicalSeleccionada = 1;
    private boolean sonidoActivado = true;
    private double tiempoMarcha = 0;
    private double tiempoNodriza = 0;
    private double tiempoUfoSonido = 0;
    private int pasoMarcha = 1;
    private static final double intervaloNodriza = 20;

    // Códigos de Teclado asignados dinámicamente
    private int teclaIzq;
    private int teclaDer;
    private int teclaDisparo;

    public SpaceInvaders() {
        super("PIPOO SPACE INVADERS", 800, 600);
    }

    public static int getContadorDisparos() { return contadorDisparosTotales; }

    @Override
    public void gameStartup() {
        // === 1. ASIGNACIÓN INMEDIATA DE CONFIGURACIONES Y CONTROLES ===
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
                velocidadBaseAliens = 20; // Bastante pasivos
            } else if ("Rápida".equals(velElegida)) {
                velocidadBaseAliens = 65; // Arrancan picantes
            }

            String skinElegida = config.getSkinModo();
            if ("Color".equals(skinElegida))        this.skinSeleccionada = 2;
            else if ("Halloween".equals(skinElegida)) this.skinSeleccionada = 3;
            else                                      this.skinSeleccionada = 1;

            String musicaElegida = config.getPistaMusical();
            this.pistaMusicalSeleccionada = "Tema 2 (Alternativo)".equals(musicaElegida) ? 2 : 1;
        } else {
            this.nombreJ1 = this.appProperties.getProperty("nombreJ1", "Invitado");
            this.teclaIzq = 37;      // Valores por defecto ante fallas (Flechas)
            this.teclaDer = 39;
            this.teclaDisparo = 32;
        }

        // === 2. PRECARGA E INDEXACIÓN DE EFECTOS SFX ===
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

        // === 3. INICIALIZACIÓN DE LISTAS Y ENTIDADES ===
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

        // === 4. BUFFERING Y CARGA DE ASSETS (CON FILTRO DE SKINS) ===
        try {
            String sufijo = "";
            if (skinSeleccionada == 2)      sufijo = "Color";
            else if (skinSeleccionada == 3) sufijo = "HW";

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
        // === CONTROL: PANTALLA DE CARGA ===
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

        // === CONTROL: GAME OVER ===
        if (estadoActual == Estado.GAMEOVER) {
            tiempoGameOver += delta;
            if (escalaGameOver < 1.0) {
                escalaGameOver += delta * 2.0;
                if (escalaGameOver > 1.0) escalaGameOver = 1.0;
            }
            Keyboard teclado = this.getKeyboard();
            if (teclado.isKeyPressed(KeyEvent.VK_ESCAPE)) {
                gestorAudio.detenerMusica(); // Apagamos cualquier rastro de sonido
                this.stop();                 // Detiene el bucle y cierra la ventana actual
                return;
            }
            if (teclado.isKeyPressed(KeyEvent.VK_ENTER) || teclado.isKeyPressed(KeyEvent.VK_SPACE)) {
                reproducirEfecto("playagain");
                reiniciarJuego();
            }

            return;
        }

        // === CONTROL: GAMEPLAY ACTIVO ===
        Keyboard teclado = this.getKeyboard();

        if (!jugador.isMuriendo()) {
            // Escucha dinámica de teclas configuradas por el usuario
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

        if (!oleada.isEmpty()) {
            double porcentajeDestruido = 1.0 - ((double) oleada.size() / cantidadAliensIniciales);
            factorVelocidadGlobal = 1.0 + (porcentajeDestruido * 2.0);
        }

        for (Enemigo enemigo : oleada) {
            enemigo.mover(delta * factorVelocidadGlobal);
            enemigo.actualizarFrame(delta * factorVelocidadGlobal);
        }

        // Paso rítmico de los aliens
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

        // Verificación de derrota por destrucción
        if (!jugador.isVisible() && jugador.getVidas() <= 0) {
            System.out.println("GAME OVER - Te quedaste sin vidas");
            gestorAudio.detenerMusica();
            reproducirEfecto("explosion");
            estadoActual = Estado.GAMEOVER;
            tiempoGameOver = 0;
            escalaGameOver = 0.0;
            return;
        }

        // Verificación de derrota por invasión territorial
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

        // Control Nave Nodriza
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
                dibujarTextoRetro(g, "PRESS ESC TO EXIT TO MENU", 176, 380); // ← ¡NUEVA LÍNEA!
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

        // Renderizado del HUD
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
        // 1. Instanciamos y cargamos el archivo de récords
        Ranking manager = new Ranking("ranking_si.dat");
        manager.cargarRanking();

        // 2. Formateamos la fecha actual de la compu
        String fecha = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());

        // 3. Grabamos usando el constructor de 5 parámetros seteando el modo "SPACE"
        // Esto guarda: Nombre, Nivel, Puntaje, Fecha y Modo de juego
        manager.agregarEntrada(new RankingEntry(this.nombreJ1, this.nivel, this.puntaje, fecha, "SPACE"));
        manager.guardarRanking();

        // 4. REQUISITO: Presentar el ranking en la interfaz al finalizar la partida
        // Creamos un JTextArea flotante con estética arcade (Fondo negro, texto blanco)
        JTextArea areaRanking = new JTextArea(manager.toStrOrdenado("PARTIDA FINALIZADA - TOP 10 GLOBAL", "SPACE"));
        areaRanking.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Crucial para mantener las columnas alineadas
        areaRanking.setBackground(Color.BLACK);
        areaRanking.setForeground(Color.WHITE);
        areaRanking.setEditable(false);

        // Mostramos el panel al jugador antes de destruir la ventana del juego
        JOptionPane.showMessageDialog(null,
                new JScrollPane(areaRanking),
                "PIPOO ARCADE - RANKING",
                JOptionPane.INFORMATION_MESSAGE);

        // 5. Apagamos los hilos de audio de forma segura
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