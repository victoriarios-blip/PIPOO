package pipoo.loderunner;

import com.entropyinteractive.Keyboard;
import pipoo.core.GestorAudio;
import pipoo.core.Juego;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

public class LodeRunner extends Juego {

    // MÁQUINA DE ESTADOS
    public enum EstadoJuego { MENU, SELECCION, TRANSICION, JUGANDO, GAMEOVER}
    private EstadoJuego estadoActual = EstadoJuego.MENU;

    // Variables del Menú
    private int opcionMenu = 0; // 0 = Play Arcade, 1 = Select Stage
    private int nivelSeleccionado = 1;
    private double timerTransicion = 0;
    private boolean keyUp = false, keyDown = false, keyLeft = false, keyRight = false, keyEnter = false;


    // ── VARIABLES DEL JUEGO ─────────────────────────────────────
    private Heroe heroe;
    private int ultimaDireccion = 30; // 30 es derecha, -30 es izquierda
    private ArrayList<Pozo> pozos;
    private ArrayList<Guardia> guardias;
    private ArrayList<Lingote> lingotes;
    private ArrayList<BarraDeManos> barras;
    private ArrayList<Plataforma> plataformas;
    private ArrayList<Escalera> escaleras;

    private double cameraX = 0;
    private static final int ANCHO_PANTALLA = 800;
    private static final int ANCHO_MUNDO = 1200;


    private double tiempoRestante = 150.0;
    private boolean escaleraSalidaCreada = false;
    private boolean juegoTerminado = false;
    private Escalera escaleraDeSalida = null;
    private BufferedImage imgEscaleraSalida;

    private double ultimoOroX = 0;
    private double ultimoOroY = 0;

    private boolean pantallaVictoria = false;
    private int highscore = 10000; // Arranca con un puntaje retro por defecto
    private int vidasFinales = 0;
    private int tiempoFinal = 0;

    // ATRIBUTOS DEL HUD
    private BufferedImage imgScore, imgLevel,imgTimeBonus, imgTitulo, imgLives,imgLevelCompleted, imgOro, imgPozo, imgFragmentoPozo, imgBloque, imgLadrilloInferior, imgParedLimite,imgGameOver, imgBloqueRegenerandose;
    private BufferedImage[] numeros;

    // ATRIBUTOS ENTIDADES
    private BufferedImage imgHeroeDer, imgHeroeIzq, imgHeroeEscalera, imgHeroeColgado;
    private BufferedImage imgGuardiaDer, imgGuardiaIzq, imgGuardiaEscalera, imgGuardiaColgado, imgGuardiaAtrapado;
    private BufferedImage imgEscCorta, imgEscMediana, imgEscLarga, imgBarra;
    private BufferedImage[] framesMuerteHeroe;

    private int score = 0;
    private int vidas = 5;
    private int nivel = 1;

    private int orosRecolectadosNivel = 0;
    private int guardiasAtrapadosNivel = 0;
    private double animacionVictoriaY = 0;

    // AUDIO
    private GestorAudio audio = new GestorAudio();
    private double timerPasos = 0.15;
    private double timerEscalera = 0.18;

    public LodeRunner() {
        super("PIPOO LODE RUNNER", 800, 630);
    }

    @Override
    protected void actualizarPuntaje() {
    }

    //carga de recursos (AHORA BLINDADA Y SEPARADA)
    @Override
    public void gameStartup() {
        System.out.println("Iniciando Lode Runner y cargando recursos...");

        pozos = new ArrayList<>();
        guardias = new ArrayList<>();
        lingotes = new ArrayList<>();
        plataformas = new ArrayList<>();
        escaleras = new ArrayList<>();
        barras = new ArrayList<>();


        //CARGAR IMÁGENES
        try {
            // HUD
            imgScore = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/score.png"));
            imgLevel = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/level.png"));
            imgTitulo = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/loderunner_menu.png"));
            imgLives = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/lives.png"));
            imgGameOver = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/game_over.png"));

            numeros = new BufferedImage[10];
            for (int i = 0; i <= 9; i++) {
                numeros[i] = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/" + i + ".png"));
            }

            // Assets nuevos (con manejo seguro por si falta alguno)
            try { imgTimeBonus = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/time_bonus.png")); } catch(Exception e){}
            try { imgLevelCompleted = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/level_completed.png")); } catch(Exception e){}
            try { imgBloqueRegenerandose = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/bloque_regenerandose.png")); } catch(Exception e){}

            // heroe
            imgHeroeDer = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/heroe_mirando_der.png"));
            imgHeroeIzq = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/heroe_mirando_izq.png"));
            imgHeroeEscalera = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/heroe_subiendo_escalera.png"));
            imgHeroeColgado = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/heroe_colgado_barramanos.png"));

            framesMuerteHeroe = new BufferedImage[7];
            for (int i = 1; i <= 7; i++) {
                framesMuerteHeroe[i-1] = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/heroe_muriendo_" + i + ".png"));
            }

            // guardia
            imgGuardiaDer = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/guardia_derecha_1.png"));
            imgGuardiaIzq = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/guardia_izquierda_1.png"));
            imgGuardiaEscalera = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/guardia_en_escalera.png"));
            imgGuardiaColgado = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/guardia_izq_colgado.png"));
            imgGuardiaAtrapado = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/guardia_atrapado_pozo.png"));

            // escenario
            imgEscCorta = ImageIO.read(getClass().getResource("/pipoo/loderunner/imagenes/escalera corta.png"));
            imgEscMediana = ImageIO.read(getClass().getResource("/pipoo/loderunner/imagenes/escalera mediana.png"));
            imgEscLarga = ImageIO.read(getClass().getResource("/pipoo/loderunner/imagenes/escalera_larga.png"));
            imgEscaleraSalida = ImageIO.read(getClass().getResource("/pipoo/loderunner/imagenes/escalera_larga.png"));
            imgBarra = ImageIO.read(getClass().getResource("/pipoo/loderunner/imagenes/barramanos.png"));
            imgOro = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/oro.png"));
            imgBloque = ImageIO.read(getClass().getResource("/pipoo/loderunner/imagenes/bloque.png"));
            imgPozo = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/pozo.png"));
            imgFragmentoPozo = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/fragmentos_pozo.png"));
            imgLadrilloInferior = ImageIO.read(getClass().getResource("/pipoo/loderunner/imagenes/ladrillo_plat_inferior.png"));
            imgParedLimite = ImageIO.read(getClass().getResource("/pipoo/loderunner/imagenes/ladrillo.png"));

        } catch (Exception e) {
            System.out.println("Error cargando imagenes: " + e.getMessage());
        }

        // 3. CARGAR AUDIOS (En un try separado para que no se rompa si falla una imagen)
        try {
            audio.precargarEfecto("pasos", this.getClass().getResource("/pipoo/loderunner/audio/pasos.wav"));
            audio.precargarEfecto("escalera", this.getClass().getResource("/pipoo/loderunner/audio/escalera.wav"));
            audio.precargarEfecto("oro", this.getClass().getResource("/pipoo/loderunner/audio/oro.wav"));
            audio.precargarEfecto("miss", this.getClass().getResource("/pipoo/loderunner/audio/miss.wav"));
            audio.precargarEfecto("game_over", this.getClass().getResource("/pipoo/loderunner/audio/game_over.wav"));

            java.net.URL urlMusica = this.getClass().getResource("/pipoo/loderunner/audio/main_bgm.wav");
            if (urlMusica != null) {
                audio.reproducirMusica(urlMusica);
            }
        } catch (Exception e) {
            System.out.println("Error cargando audios: " + e.getMessage());
        }

        // Arrancamos el juego en la pantalla de menú
        estadoActual = EstadoJuego.MENU;

    }

    // ── MÉTODO PARA CONSTRUIR EL MAPA CUANDO SE ELIGE UN NIVEL ──
    private void cargarNivel(int nivelACargar) {
        pozos.clear();
        guardias.clear();
        lingotes.clear();
        plataformas.clear();
        escaleras.clear();
        barras.clear();

        tiempoRestante = 150.0;
        escaleraSalidaCreada = false;
        escaleraDeSalida = null;
        pantallaVictoria = false;
        juegoTerminado = false;

        orosRecolectadosNivel = 0;
        guardiasAtrapadosNivel = 0;

        // PISO BASE (siempre en todos los mapas)
        agregarFila(0, 500, 39, imgBloque);
        for (int i = 0; i < 41; i++) {
            Plataforma ladrillo = new Plataforma(i * 30, 530, 30, 30);
            if (imgLadrilloInferior != null) ladrillo.setImagen(imgLadrilloInferior);
            plataformas.add(ladrillo);
        }

        //lo que compartarten todos los niveles
        int heroeStartX=430;
        int heroeStartY=510;
        ArrayList<Point> posGuardias = new ArrayList<>();
        int nivelFisico = ((nivelACargar - 1) % 3) + 1;

        //avanzar en niveles
        switch (nivelFisico) {
            case 1:
                agregarFila(0, 80, 13, imgBloque);
                agregarFila(421, 80, 10, imgBloque);
                agregarFila(10, 210, 5, imgBloque);
                agregarFila(185, 210, 9, imgBloque);
                agregarFila(805, 150, 9, imgBloque);
                agregarFila(1105, 150, 3, imgBloque);
                agregarFila(600, 210, 9, imgBloque);
                agregarFila(895, 210, 10, imgBloque);
                agregarFila(600, 180, 3, imgBloque);
                agregarFila(600, 150, 3, imgBloque);
                agregarFila(0, 280, 17, imgBloque);
                agregarFila(540, 280, 11, imgBloque);
                agregarFila(300, 420, 8, imgBloque);
                agregarFila(865, 420, 9, imgBloque);

                // Escaleras
                Escalera e6 = new Escalera(390, 81, 30, 130, false); e6.setImagen(imgEscMediana); escaleras.add(e6);
                Escalera e5 = new Escalera(691, 150, 30, 60, false); e5.setImagen(imgEscCorta); escaleras.add(e5);
                Escalera e51 = new Escalera(1075, 150, 30, 60, false); e51.setImagen(imgEscCorta); escaleras.add(e51);
                Escalera e4 = new Escalera(155, 205, 30, 78, false); e4.setImagen(imgEscCorta); escaleras.add(e4);
                Escalera e41 = new Escalera(865, 210, 30, 209, false); e41.setImagen(imgEscMediana); escaleras.add(e41);
                Escalera e3 = new Escalera(510, 282, 30, 138, false); e3.setImagen(imgEscMediana); escaleras.add(e3);
                Escalera e2 = new Escalera(268, 420, 30, 80, false); e2.setImagen(imgEscCorta); escaleras.add(e2);
                Escalera e11 = new Escalera(1135, 420, 30, 80, false); e11.setImagen(imgEscCorta); escaleras.add(e11);

                // Barras
                BarraDeManos b1 = new BarraDeManos(420, 115, 385, 15); b1.setImagen(imgBarra); barras.add(b1);
                BarraDeManos b2 = new BarraDeManos(538, 370, 324, 15); b2.setImagen(imgBarra); barras.add(b2);

                // Oro
                Lingote o1 = new Lingote(230, 50, 30, 30); o1.setImagen(imgOro); lingotes.add(o1);
                Lingote o2 = new Lingote(1000, 120, 30, 30); o2.setImagen(imgOro); lingotes.add(o2);
                Lingote o3 = new Lingote(960, 180, 30, 30); o3.setImagen(imgOro); lingotes.add(o3);
                Lingote o4 = new Lingote(400, 390, 30, 30); o4.setImagen(imgOro); lingotes.add(o4);
                Lingote o5 = new Lingote(1040, 390, 30, 30); o5.setImagen(imgOro); lingotes.add(o5);
                Lingote o6 = new Lingote(760, 470, 30, 30); o6.setImagen(imgOro); lingotes.add(o6);

                // Setup personajes Nivel 1
                heroeStartX = 450;
                heroeStartY = 510;
                posGuardias.add(new Point(230, 180));
                posGuardias.add(new Point(1000, 180));
                posGuardias.add(new Point(691, 250));
                break;

            case 2:
                // NIVEL 2
                // plataformas
                //PISO 4
                agregarFila(0, 440, 10, imgBloque);
                agregarFila(340, 440, 9, imgBloque);
                agregarFila(900, 440, 9, imgBloque);
                //PISO 3
                agregarFila(0, 350, 15, imgBloque);
                agregarFila(820, 350, 8, imgBloque);
                agregarFila(1100, 350, 3, imgBloque);
                //bloques sueltos
                agregarFila(0, 380, 6, imgBloque);
                agregarFila(0, 410, 2, imgBloque);
                agregarFila(150, 410, 1, imgBloque);
                //PISO 2
                agregarFila(715, 280, 14, imgBloque);
                agregarFila(490, 310, 6, imgBloque);
                agregarFila(75, 200, 6, imgBloque);
                agregarFila(890, 170, 8, imgBloque);
                //PISO 1
                agregarFila(75, 130, 6, imgBloque);
                agregarFila(490, 130, 12, imgBloque);

                //escaleras
                Escalera n2e1= new Escalera(300, 435, 40, 67, false);n2e1.setImagen(imgEscCorta); escaleras.add(n2e1);
                Escalera n2e2= new Escalera(788, 351, 30, 150, false);n2e2.setImagen(imgEscMediana); escaleras.add(n2e2);
                Escalera n2e3= new Escalera(452, 130, 35, 310, false);n2e3.setImagen(imgEscLarga); escaleras.add(n2e3);
                Escalera n2e4= new Escalera(1063, 347, 35, 93, false);n2e4.setImagen(imgEscMediana); escaleras.add(n2e4);
                Escalera n2e5= new Escalera(673, 280, 40, 64, false);n2e5.setImagen(imgEscCorta); escaleras.add(n2e5);
                Escalera n2e6= new Escalera(257, 130, 30, 220, false);n2e6.setImagen(imgEscMediana); escaleras.add(n2e6);
                Escalera n2e7= new Escalera(43, 130, 30, 220, false);n2e7.setImagen(imgEscMediana); escaleras.add(n2e7);
                Escalera n2e8= new Escalera(855, 130, 30, 148, false);n2e8.setImagen(imgEscMediana); escaleras.add(n2e8);
                Escalera n2e9= new Escalera(1133, 280, 38, 70, false);n2e9.setImagen(imgEscCorta); escaleras.add(n2e9);
                Escalera n2e10= new Escalera(1133, 62, 35, 140, false);n2e10.setImagen(imgEscMediana); escaleras.add(n2e10);

                // barraDeManos
                BarraDeManos n2b1 = new BarraDeManos(540, 400, 250, 15); n2b1.setImagen(imgBarra); barras.add(n2b1);
                BarraDeManos n2b2 = new BarraDeManos(287, 250, 167, 15); n2b2.setImagen(imgBarra); barras.add(n2b2);
                BarraDeManos n2b3 = new BarraDeManos(490, 250, 225, 15); n2b3.setImagen(imgBarra); barras.add(n2b3);

                //lingotes
                Lingote n2o1 = new Lingote(65, 410,30,30);n2o1.setImagen(imgOro); lingotes.add(n2o1);
                Lingote n2o2 = new Lingote(1120, 410,30,30);n2o2.setImagen(imgOro); lingotes.add(n2o2);
                Lingote n2o3 = new Lingote(320, 320,30,30);n2o3.setImagen(imgOro); lingotes.add(n2o3);
                Lingote n2o4 = new Lingote(510, 280,30,30);n2o4.setImagen(imgOro); lingotes.add(n2o4);
                Lingote n2o5 = new Lingote(100, 170,30,30);n2o5.setImagen(imgOro); lingotes.add(n2o5);
                Lingote n2o6 = new Lingote(140, 100,30,30);n2o6.setImagen(imgOro); lingotes.add(n2o6);
                Lingote n2o7 = new Lingote(720, 100,30,30);n2o7.setImagen(imgOro); lingotes.add(n2o7);
                Lingote n2o8 = new Lingote(950, 140,30,30);n2o8.setImagen(imgOro); lingotes.add(n2o8);

                //heroe
                heroeStartX = 400; // Nace en el medio
                heroeStartY = 470; // Apoyado en el piso base

                //guardias (3)
                posGuardias.add(new Point(190, 320));
                posGuardias.add(new Point(190, 200));
                posGuardias.add(new Point(810, 280));
                break;

            case 3: //hacer tal cual
                // ========================================================
                // NIVEL 3: LA TORRE CENTRAL (5 Guardias)
                // ========================================================
                // Plataformas centrales
                agregarFila(450, 120, 10, imgBloque);
                agregarFila(450, 240, 10, imgBloque);
                agregarFila(450, 360, 10, imgBloque);
                // Plataformas laterales flotantes
                agregarFila(60, 180, 5, imgBloque);
                agregarFila(990, 180, 5, imgBloque);
                agregarFila(60, 300, 5, imgBloque);
                agregarFila(990, 300, 5, imgBloque);

                // Escaleras de la torre
                Escalera t1 = new Escalera(450, 120, 30, 380, false); t1.setImagen(imgEscLarga); escaleras.add(t1);
                Escalera t2 = new Escalera(720, 120, 30, 380, false); t2.setImagen(imgEscLarga); escaleras.add(t2);

                // Barras para alcanzar las plataformas flotantes
                BarraDeManos tb1 = new BarraDeManos(210, 180, 240, 15); tb1.setImagen(imgBarra); barras.add(tb1);
                BarraDeManos tb2 = new BarraDeManos(750, 180, 240, 15); tb2.setImagen(imgBarra); barras.add(tb2);
                BarraDeManos tb3 = new BarraDeManos(210, 300, 240, 15); tb3.setImagen(imgBarra); barras.add(tb3);
                BarraDeManos tb4 = new BarraDeManos(750, 300, 240, 15); tb4.setImagen(imgBarra); barras.add(tb4);

                // Oro arriesgado en los bordes
                Lingote tg1 = new Lingote(90, 150, 30, 30); tg1.setImagen(imgOro); lingotes.add(tg1);
                Lingote tg2 = new Lingote(1080, 150, 30, 30); tg2.setImagen(imgOro); lingotes.add(tg2);
                Lingote tg3 = new Lingote(90, 270, 30, 30); tg3.setImagen(imgOro); lingotes.add(tg3);
                Lingote tg4 = new Lingote(1080, 270, 30, 30); tg4.setImagen(imgOro); lingotes.add(tg4);
                Lingote tg5 = new Lingote(570, 90, 30, 30); tg5.setImagen(imgOro); lingotes.add(tg5);

                heroeStartX = 585;
                heroeStartY = 510;
                posGuardias.add(new Point(540, 90));   // Cima
                posGuardias.add(new Point(540, 210));  // Medio
                posGuardias.add(new Point(540, 330));  // Abajo
                posGuardias.add(new Point(120, 510));  // Base Izq
                posGuardias.add(new Point(1020, 510)); // Base Der
                break;
        }

        // 1. Crear Héroe
        heroe = new Heroe(heroeStartX, heroeStartY, 30, 30);
        heroe.setImagen(imgHeroeDer);
        heroe.setImagenDer(imgHeroeDer);
        heroe.setImagenIzq(imgHeroeIzq);
        heroe.setImagenEscalera(imgHeroeEscalera);
        heroe.setImagenColgado(imgHeroeColgado);

        for (int i = 0; i < 7; i++) {
            if (framesMuerteHeroe != null && framesMuerteHeroe[i] != null) {
                heroe.setImagenMuriendo(i, framesMuerteHeroe[i]);
            }
        }

        // 2. Crear Guardias Dinámicos
        for (Point p : posGuardias) {
            guardias.add(new Guardia(p.x, p.y, 30, 30, heroe));
        }

        for (Guardia guardia : guardias) {
            guardia.setMapa(escaleras, plataformas, barras, pozos);
            guardia.setImagen(imgGuardiaDer);
            guardia.setImgDer(imgGuardiaDer);
            guardia.setImgIzq(imgGuardiaIzq);
            guardia.setImgEscalera(imgGuardiaEscalera);
            guardia.setImgColgado(imgGuardiaColgado);
            guardia.setImgAtrapado(imgGuardiaAtrapado);
        }
        try {
            audio.detenerMusica(); // Por si venía sonando del menú o de la vida anterior
            java.net.URL urlMusica = this.getClass().getResource("/pipoo/loderunner/audio/main_bgm.wav");
            if (urlMusica != null) {
                audio.reproducirMusica(urlMusica);
            }
        } catch (Exception e) {
            System.out.println("Error con la música: " + e.getMessage());
        }
    }

    @Override
    public void gameUpdate(double delta) {
        Keyboard teclado = this.getKeyboard();

        // ── ENTRADAS ANTIRREBOTE PARA MENÚS ──
        boolean actUp = teclado.isKeyPressed(KeyEvent.VK_UP);
        boolean actDown = teclado.isKeyPressed(KeyEvent.VK_DOWN);
        boolean actLeft = teclado.isKeyPressed(KeyEvent.VK_LEFT);
        boolean actRight = teclado.isKeyPressed(KeyEvent.VK_RIGHT);
        boolean actEnter = teclado.isKeyPressed(KeyEvent.VK_ENTER);

        // ── ESTADO: MENÚ PRINCIPAL ──
        if (estadoActual == EstadoJuego.MENU) {
            if (actUp && !keyUp) opcionMenu = 0;
            if (actDown && !keyDown) opcionMenu = 1;

            if (actEnter && !keyEnter) {
                if (opcionMenu == 0) {
                    nivel = 1;
                    score = 0;
                    vidas = 5;
                    cargarNivel(nivel);
                    estadoActual = EstadoJuego.TRANSICION;
                    timerTransicion = 2.5;
                } else {
                    estadoActual = EstadoJuego.SELECCION;
                }
            }
            actualizarTeclas(actUp, actDown, actLeft, actRight, actEnter);
            return;
        }

        // ── ESTADO: SELECCIÓN DE NIVEL ──
        else if (estadoActual == EstadoJuego.SELECCION) {
            if (actLeft && !keyLeft && nivelSeleccionado > 1) nivelSeleccionado--;

            // ACA ESTÁ EL CAMBIO: Cambiamos el 999 por un 3
            if (actRight && !keyRight && nivelSeleccionado < 3) nivelSeleccionado++;

            if (actEnter && !keyEnter) {
                nivel = nivelSeleccionado;
                score = 0;
                vidas = 5;
                cargarNivel(nivel);
                estadoActual = EstadoJuego.TRANSICION;
                timerTransicion = 2.5;
            }
            actualizarTeclas(actUp, actDown, actLeft, actRight, actEnter);
            return;
        }
        // ── ESTADO: TRANSICIÓN ("STAGE 00X") ──
        else if (estadoActual == EstadoJuego.TRANSICION) {
            timerTransicion -= delta;
            if (timerTransicion <= 0 || (actEnter && !keyEnter)) {
                estadoActual = EstadoJuego.JUGANDO;
            }
            actualizarTeclas(actUp, actDown, actLeft, actRight, actEnter);
            return;
        }

        // ── A PARTIR DE ACÁ ES TU CÓDIGO ORIGINAL DEL JUEGO (ESTADO JUGANDO) ──
        actualizarTeclas(actUp, actDown, actLeft, actRight, actEnter); // Limpiar buffer de teclas

        if (juegoTerminado) {
            if (teclado.isKeyPressed(KeyEvent.VK_ENTER)) {
                // START OVER: Volver al menú
                estadoActual = EstadoJuego.MENU;
                juegoTerminado = false;
            } else if (teclado.isKeyPressed(KeyEvent.VK_ESCAPE)) {
                System.exit(0);
            }
            return;
        }

        if (pantallaVictoria) {
            if (teclado.isKeyPressed(KeyEvent.VK_ENTER)) {
                pantallaVictoria = false;
                avanzarSiguienteNivel();
            }
            return;
        }

        if (heroe.isEstaMuriendo()) {
            boolean animacionTerminada = heroe.actualizarAnimacionMuerte(delta);
            if (animacionTerminada) {
                reiniciarPosiciones();
            }
            return;
        }

        // ACTUALIZAR TIEMPO DEL NIVEL
        if (tiempoRestante > 0) {
            tiempoRestante -= delta;
            if (tiempoRestante <= 0) {
                tiempoRestante = 0;
                System.out.println("¡Se acabó el tiempo! El héroe muere.");
                heroe.iniciarMuerte();
                return;
            }
        }

        // DETECTAR SI EL HEROE RECOLECTO TODO EL ORO
        if (!escaleraSalidaCreada) {
            boolean mapaLimpio = lingotes.isEmpty();

            boolean guardiasTienenOro = false;
            for (Guardia g : guardias) {
                if (g.isTieneOro()) {
                    guardiasTienenOro = true;
                    break;
                }
            }
            if (mapaLimpio && !guardiasTienenOro) {
                System.out.println("¡Todo el oro recolectado y asegurado! Aparece la escalera de salida aleatoria.");
                double yPiso = ultimoOroY + 30;

                ArrayList<Plataforma> plataformasValidas = new ArrayList<>();
                for (Plataforma p : plataformas) {
                    if (Math.abs(p.y - yPiso) <= 10) {
                        plataformasValidas.add(p);
                    }
                }
                double escaleraX = ultimoOroX;
                double escaleraAlto = yPiso;

                if (!plataformasValidas.isEmpty()) {
                    int indexRandom = (int) (Math.random() * plataformasValidas.size());
                    Plataforma bloqueElegido = plataformasValidas.get(indexRandom);
                    escaleraX = bloqueElegido.x;
                    escaleraAlto = bloqueElegido.y;
                }
                escaleraDeSalida = new Escalera(escaleraX, 0, 30, (int) escaleraAlto, false);
                if (imgEscaleraSalida != null) {
                    escaleraDeSalida.setImagen(imgEscaleraSalida);
                }
                escaleras.add(escaleraDeSalida);
                escaleraSalidaCreada = true;
                for (Guardia guardia : guardias) {
                    guardia.setMapa(escaleras, plataformas, barras, pozos);
                }
            }
        }

        // CONDICION DE VICTORIA
        if (escaleraSalidaCreada && heroe.isEnEscalera() && heroe.intersects(escaleraDeSalida)) {
            if (heroe.y <= 10) {
                completarNivel();
                return;
            }
        }

        // Cavar pozos
        if (teclado.isKeyPressed(KeyEvent.VK_SPACE)) {
            heroe.cavar();

            // 1. Calculamos en qué "columna" exacta de la grilla (30x30) está el centro del héroe
            int columnaHeroe = (int) ((heroe.x + (heroe.width / 2.0)) / 30);

            // 2. Determinamos la columna objetivo (la de al lado según dónde miramos)
            int columnaObjetivo = columnaHeroe + (ultimaDireccion > 0 ? 1 : -1);

            // 3. Calculamos el punto de impacto forzándolo al CENTRO matemático de ese bloque
            double puntoImpactoX = (columnaObjetivo * 30) + 15;

            // El Y apunta siempre a la mitad del bloque que está debajo de los pies
            double puntoImpactoY = heroe.y + heroe.height + 15;

            if (puntoImpactoY < 530) {
                intentarCavar(puntoImpactoX, puntoImpactoY);
            }
        }

        // Actualización pozos
        Iterator<Pozo> iteradorPozos = pozos.iterator();
        while (iteradorPozos.hasNext()) {
            Pozo pozoActual = iteradorPozos.next();
            pozoActual.actualizar(delta);

            if (pozoActual.getEstado() == 2) {

                if (heroe.intersects(pozoActual) && heroe.y >= pozoActual.y - 10) {
                    System.out.println("¡El héroe fue enterrado vivo!");
                    heroe.iniciarMuerte();
                }

                for (Guardia g : guardias) {
                    if (g.intersects(pozoActual)) {
                        System.out.println("¡Un guardia fue eliminado! +75 pts");
                        score += 75;
                        guardiasAtrapadosNivel++;
                        g.reaparecer();
                    }
                }

                Plataforma bloqueRegenerado = new Plataforma(pozoActual.x, pozoActual.y, 30, 30);
                if (imgBloque != null)
                    bloqueRegenerado.setImagen(imgBloque);
                plataformas.add(bloqueRegenerado);
                iteradorPozos.remove();
            }
        }

        // movimiento heroe
        if (heroe.isEstaCayendo()) {
            heroe.setVelocidadX(0);
        } else {
            if (teclado.isKeyPressed(KeyEvent.VK_RIGHT)) heroe.setVelocidadX(2);
            else if (teclado.isKeyPressed(KeyEvent.VK_LEFT)) heroe.setVelocidadX(-2);
            else heroe.setVelocidadX(0);
        }

        if (heroe.isEnEscalera()) {
            if (teclado.isKeyPressed(KeyEvent.VK_UP)) heroe.setVelocidadY(-2.5);
            else if (teclado.isKeyPressed(KeyEvent.VK_DOWN)) heroe.setVelocidadY(2.5);
            else heroe.setVelocidadY(0);
        } else if (!heroe.isEstaCayendo()) {
            heroe.setVelocidadY(0);
        }

        if (teclado.isKeyPressed(KeyEvent.VK_RIGHT)) {
            heroe.setVelocidadX(2.5);
            ultimaDireccion = 30;
        } else if (teclado.isKeyPressed(KeyEvent.VK_LEFT)) {
            heroe.setVelocidadX(-2.5);
            ultimaDireccion = -30;
        } else {
            heroe.setVelocidadX(0);
        }

        // sonido pasos
        if (heroe.getVelocidadX() != 0 && !heroe.isEstaCayendo() && !heroe.isEnEscalera()) {
            timerPasos += delta;
            if (timerPasos >= 0.25) {
                audio.reproducirEfecto("pasos");
                timerPasos = 0;
            }
        } else {
            timerPasos = 0.3;
        }

        // sonido escalera
        if (heroe.getVelocidadY() != 0 && heroe.isEnEscalera()) {
            timerEscalera += delta;
            if (timerEscalera >= 0.18) {
                audio.reproducirEfecto("escalera");
                timerEscalera = 0;
            }
        } else {
            timerEscalera = 0.35;
        }

        heroe.mover(delta);

        for (Guardia guardia : guardias) {
            guardia.mover(delta);
            guardia.x = Math.max(30, Math.min(1170 - guardia.width, guardia.x));
        }
        heroe.x = Math.max(30, Math.min(1170 - heroe.width, heroe.x));

        detectarColisiones();

        // camara
        cameraX = heroe.x - ANCHO_PANTALLA / 2.0;
        cameraX = Math.max(0, cameraX);
        cameraX = Math.min(ANCHO_MUNDO  - ANCHO_PANTALLA, cameraX);
    }

    // Método auxiliar para el teclado en menú
    private void actualizarTeclas(boolean u, boolean d, boolean l, boolean r, boolean e) {
        keyUp = u; keyDown = d; keyLeft = l; keyRight = r; keyEnter = e;
    }

    // CAVAR
    private void intentarCavar(double impactoX, double impactoY) {
        Iterator<Plataforma> it = plataformas.iterator();
        while (it.hasNext()) {
            Plataforma p = it.next();
            if (p.contains(impactoX, impactoY)) {
                double pozoX = p.x;
                double pozoY = p.y;
                it.remove();

                Pozo nuevoPozo = new Pozo(pozoX, pozoY, 30, 30);
                if (imgFragmentoPozo != null && imgPozo != null)
                    nuevoPozo.configurarAnimacion(imgFragmentoPozo, imgPozo);
                else if (imgPozo != null)
                    nuevoPozo.setImagen(imgPozo);

                if (imgBloqueRegenerandose != null) {
                    nuevoPozo.setImagenCerrandose(imgBloqueRegenerandose);
                }

                pozos.add(nuevoPozo);
                return;
            }
        }
    }


    @Override
    public void gameDraw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 800, 630);

        // PANTALLA: MENÚ PRINCIPAL
        if (estadoActual == EstadoJuego.MENU) {
            // 1. LA IMAGEN DE FONDO OCUPA TODA LA VENTANA
            if (imgTitulo != null) {
                g.drawImage(imgTitulo, 0, 0, this.getWidth(), this.getHeight(), null);
            }

            // Configuramos la fuente estándar para el menú
            Font fuenteMenu = new Font("Consolas", Font.BOLD, 30);
            g.setFont(fuenteMenu);

            // FontMetrics nos permite calcular el ancho exacto del texto para centrarlo
            FontMetrics fm = g.getFontMetrics(fuenteMenu);

            // 2. OPCIÓN 1: CENTRADA PERFECTAMENTE
            String textoArcade = "1. PLAY ARCADE MODE";
            int xArcade = (this.getWidth() - fm.stringWidth(textoArcade)) / 2; // Centro exacto en X

            if (opcionMenu == 0) g.setColor(Color.YELLOW); else g.setColor(Color.WHITE);
            g.drawString(textoArcade, xArcade, 420); // 390 es la altura (Y), ajustala si querés

            // 3. OPCIÓN 2: CENTRADA PERFECTAMENTE
            String textoSelect = "2. SELECT STAGE";
            int xSelect = (this.getWidth() - fm.stringWidth(textoSelect)) / 2; // Centro exacto en X

            if (opcionMenu == 1) g.setColor(Color.YELLOW); else g.setColor(Color.WHITE);
            g.drawString(textoSelect, xSelect, 470); // 450 es la altura (Y)
            return;
        }

        // PANTALLA: SELECCIÓN DE NIVEL
        if (estadoActual == EstadoJuego.SELECCION) {

            // 1. TÍTULO SUPERIOR CENTRADO
            g.setColor(Color.WHITE);
            Font fuenteTitulo = new Font("Consolas", Font.BOLD, 30);
            g.setFont(fuenteTitulo);
            FontMetrics fmTitulo = g.getFontMetrics(fuenteTitulo);
            String textoTitulo = "SELECT YOUR STAGE";
            int xTitulo = (this.getWidth() - fmTitulo.stringWidth(textoTitulo)) / 2;
            g.drawString(textoTitulo, xTitulo, 200);

            int assetY = 260;
            if (imgHeroeDer != null) g.drawImage(imgHeroeDer, 350, assetY, 40, 40, null);
            if (imgGuardiaDer != null) g.drawImage(imgGuardiaDer, 420, assetY, 40, 40, null);

            // 3. NÚMEROS DE SELECCIÓN CENTRADOS (Debajo de los assets)
            g.setColor(Color.RED);
            Font fuenteNumeros = new Font("Consolas", Font.BOLD, 36);
            g.setFont(fuenteNumeros);
            FontMetrics fmNumeros = g.getFontMetrics(fuenteNumeros);
            String textoNumeros = "< " + String.format("%02d", nivelSeleccionado) + " >";
            int xNumeros = (this.getWidth() - fmNumeros.stringWidth(textoNumeros)) / 2;
            g.drawString(textoNumeros, xNumeros, 360);

            // 4. TEXTO INFERIOR CENTRADO
            g.setColor(Color.LIGHT_GRAY);
            Font fuenteEnter = new Font("Consolas", Font.BOLD, 16);
            g.setFont(fuenteEnter);
            FontMetrics fmEnter = g.getFontMetrics(fuenteEnter);
            String textoEnter = "PRESS ENTER TO START";
            int xEnter = (this.getWidth() - fmEnter.stringWidth(textoEnter)) / 2;
            g.drawString(textoEnter, xEnter, 500);

            return;
        }

        // PANTALLA: TRANSICIÓN ("STAGE 00X")
        if (estadoActual == EstadoJuego.TRANSICION) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Consolas", Font.BOLD, 30));
            g.drawString(String.format("%02d STAGE", nivel), 330, 280);

            if (imgHeroeDer != null) g.drawImage(imgHeroeDer, 340, 340, 40, 40, null);
            if (imgGuardiaDer != null) g.drawImage(imgGuardiaDer, 420, 340, 40, 40, null);
            return;
        }

        // ── PANTALLA: JUEGO NORMAL ──
        Graphics2D mundo = (Graphics2D) g.create();
        mundo.translate((int) -cameraX, 0);

        for (Plataforma p : plataformas)
            p.dibujar(mundo);
        for (Escalera e : escaleras)
            e.dibujar(mundo);
        for (BarraDeManos b : barras)
            b.dibujar(mundo);
        for (Lingote oro : lingotes)
            oro.dibujar(mundo);
        for (Pozo pozo : pozos)
            pozo.dibujar(mundo);

        if (imgParedLimite != null) {
            for (int y =0; y <= 510; y += 30) {
                mundo.drawImage(imgParedLimite, 0, y, 30, 30, null);
                mundo.drawImage(imgParedLimite, ANCHO_MUNDO - 29, y, 35, 30, null);            }
        }
        if (heroe != null)
            heroe.dibujar(mundo);
        for (Guardia guardia : guardias) {
            guardia.dibujar(mundo);
        }

        mundo.dispose();

        // HUD
        int hudY = 575;
        double escala = 1.5;

        if (imgScore != null) {
            double escalaScore = 2.0;
            int w = (int) (imgScore.getWidth() * escalaScore);
            int h = (int) (imgScore.getHeight() * escalaScore);
            g.drawImage(imgScore, 20, hudY, w, h, null);
            dibujarNumero(g, String.format("%06d", score), 20 + w + 10, hudY, escalaScore);
        }

        if (imgLives != null) {
            double escalaLives = 2.0;
            int w = 100;
            int h = w * imgLives.getHeight() / imgLives.getWidth();
            g.drawImage(imgLives, 280, 555, w, h, null);
            dibujarNumero(g, String.format("%03d", vidas), 280 + w + 10, 577, escalaLives);
        }

        if (imgLevel != null) {
            double escalaLevel = 2.0;
            int w = (int) (imgLevel.getWidth() * escalaLevel);
            int h = (int) (imgLevel.getHeight() * escalaLevel);
            g.drawImage(imgLevel, 500, hudY, w, h, null);
            dibujarNumero(g, String.format("%03d", nivel), 500 + w + 10, hudY, escalaLevel);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.BOLD, 20));
        g.drawString("TIME:", 700, hudY + 14);

        double escalaTime = 1.2;
        dibujarNumero(g, String.format("%03d", (int) tiempoRestante), 765, hudY +5, escalaTime);

        if (imgLadrilloInferior != null) {
            // Recorremos todo el ancho de la pantalla (de 30 en 30 píxeles)
            for (int x = 0; x < this.getWidth(); x += 30) {
                // Dibujamos en Y = 600 para que ocupe los últimos 30 píxeles de la ventana
                g.drawImage(imgLadrilloInferior, x, 610, 30, 30, null);
            }
        }
        // GAME OVER
        if (juegoTerminado && imgGameOver != null) {
            g.setColor(new Color(0, 0, 0, 195));
            g.fillRect(0, 0, this.getWidth(), this.getHeight());

            int nuevoAncho = 580;
            int nuevoAlto = (nuevoAncho * imgGameOver.getHeight()) / imgGameOver.getWidth();

            int x = (800 - nuevoAncho) / 2;
            int y = (630 - nuevoAlto) / 2 + 20;

            g.drawImage(imgGameOver, x, y, nuevoAncho, nuevoAlto, null);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Consolas", Font.BOLD, 20));
            g.drawString("PRESS ENTER TO MAIN MENU", 230, y + nuevoAlto + 40);

            g.setColor(Color.LIGHT_GRAY);
            g.setFont(new Font("Consolas", Font.BOLD, 20));
            g.drawString("PRESS ESC TO EXIT", 325, y + nuevoAlto + 65);
        }

        // ── PANTALLA DE VICTORIA CLÁSICA Y LIMPIA (SIN FUENTE ARCADE) ──
        if (pantallaVictoria) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());


            // =========================================================
            // 2. EXTREMO INFERIOR IZQ: HÉROE ANIMADO (MÁS GRANDE Y ABAJO)
            // =========================================================
            int basePlataformaX = 10;
            int basePlataformaY = 520;
            int sizeP = 45;

            if (imgBloque != null) {
                // Primeros 2 bloques
                g.drawImage(imgBloque, basePlataformaX, basePlataformaY, sizeP, sizeP, null);
                g.drawImage(imgBloque, basePlataformaX + sizeP, basePlataformaY, sizeP, sizeP, null);

                // Siguientes 3 bloques
                g.drawImage(imgBloque, basePlataformaX + (sizeP * 3), basePlataformaY, sizeP, sizeP, null);
                g.drawImage(imgBloque, basePlataformaX + (sizeP * 4), basePlataformaY, sizeP, sizeP, null);
                g.drawImage(imgBloque, basePlataformaX + (sizeP * 5), basePlataformaY, sizeP, sizeP, null);
            }

            // Ubicamos la escalera exactamente en el hueco vacío
            int escVictoriaX = basePlataformaX + (sizeP * 2);
            if (imgEscCorta != null) {
                g.drawImage(imgEscCorta, escVictoriaX, basePlataformaY, sizeP, 115, null);
            }

            // Animación del héroe adaptada al pozo (baja y sube)
            double ciclo = (System.currentTimeMillis() % 2000) / 2000.0;
            int maxDesplazamiento = 70;
            int desplazamientoY;

            if (ciclo < 0.5) {
                desplazamientoY = (int) (ciclo * 2 * maxDesplazamiento);
            } else {
                desplazamientoY = (int) (maxDesplazamiento - ((ciclo - 0.5) * 2 * maxDesplazamiento));
            }

            if (imgHeroeEscalera != null) {
                g.drawImage(imgHeroeEscalera, escVictoriaX, basePlataformaY + desplazamientoY, sizeP, sizeP, null);
            }

            // =========================================================
            // 3. CENTRO: DESGLOSE MINIMALISTA PERFECCIONADO Y ALINEADO
            // =========================================================
            int assetX = 250;      // Columnas rígidas de alineación horizontal
            int numX = 450;
            int textoX = 260;

            int datosY = 200;      // Altura inicial
            int filaSeparacion = 75;
            double tamañoNumeros = 3.0;

            // Fijamos la fuente Monospaced nativa directamente
            g.setFont(new Font("Consolas", Font.BOLD, 22));
            g.setColor(Color.WHITE);

            // FILA 1: ORO (Ícono + Puntaje Directo)
            int totalPuntosOro = orosRecolectadosNivel * 250;
            if (imgOro != null) {
                g.drawImage(imgOro, assetX, 170, 55, 55, null);
            }
            dibujarNumero(g, String.format("%04d", totalPuntosOro), numX, datosY, tamañoNumeros);

            // FILA 2: GUARDIA (Ícono + Puntaje Directo)
            int totalPuntosGuardias = guardiasAtrapadosNivel * 75;
            int fila2Y = datosY + filaSeparacion;
            if (imgGuardiaAtrapado != null) {
                g.drawImage(imgGuardiaAtrapado, assetX, 260, 55, 55, null);
            }
            dibujarNumero(g, String.format("%04d", totalPuntosGuardias), numX, fila2Y, tamañoNumeros);

            // FILA 3: TIME BONUS
            int fila3Y = fila2Y + filaSeparacion;
            g.setColor(Color.WHITE);
            g.setFont(new Font("Consolas", Font.BOLD, 33)); // Cambiá el 26 por 24 o 28 si lo querés distinto
            g.drawString("TIME BONUS", 245, 342 + 25);
            dibujarNumero(g, String.format("%04d", tiempoFinal * 10), numX, 345, tamañoNumeros);

            // FILA 4: TOTAL SCORE
            int fila4Y = fila3Y + filaSeparacion + 15;
            g.setColor(Color.WHITE);
            g.setFont(new Font("Consolas", Font.BOLD, 41)); // Un poco más grande para el total
            g.drawString("TOTAL SCORE", 180, 468);
            dibujarNumero(g, String.format("%06d", score), numX, fila4Y, 3.5);

            // GUÍA INFERIOR
            g.setColor(Color.LIGHT_GRAY);
            g.setFont(new Font("Consolas", Font.PLAIN, 16));
            g.drawString("PRESS ENTER TO NEXT STAGE", 290, 610);
        }
    }

    // COLISIONES
    @Override
    protected void detectarColisiones() {
        boolean heroeSoportado = false;
        boolean heroeEnEscalera = false;
        boolean heroeEnBarra = false;
        boolean heroeEnPozo = false;
        ArrayList<Lingote> orosSoltados = new ArrayList<>();
        Keyboard teclado = this.getKeyboard();

        // COLISIONES HÉROE

        // Héroe con oro
        Iterator<Lingote> itOro = lingotes.iterator();
        while (itOro.hasNext()) {
            Lingote oro = itOro.next();
            if (heroe.intersects(oro)) {
                // FIX: Guardamos las coordenadas antes de borrarlo
                ultimoOroX = oro.x;
                ultimoOroY = oro.y;

                heroe.recolectarOro();
                itOro.remove();
                score += 250;
                orosRecolectadosNivel++;
                audio.reproducirEfecto("oro");
            }
        }

        // Héroe con escalera
        for (Escalera escalera : escaleras) {
            if (heroe.intersects(escalera)) {
                heroeSoportado = true;
                heroeEnEscalera = true;
            }
        }

        // Héroe con plataforma sólida
        for (Plataforma plataforma : plataformas) {
            if (heroe.intersects(plataforma)) {
                if (heroe.x + heroe.width > plataforma.x + 8 &&
                        heroe.x < plataforma.x + plataforma.width - 8) {
                    if (!heroeEnPozo) {
                        heroeSoportado = true;
                        if (!heroe.isEnEscalera())
                            heroe.y = plataforma.y - heroe.height + 1;
                    }
                }
            }
        }

        // Héroe con barra de manos
        for (BarraDeManos barra : barras) {
            if (heroe.intersects(barra) && !heroeEnPozo) {
                heroeSoportado = true;
                heroeEnBarra = true;
                if (!heroeEnEscalera)
                    heroe.y = barra.y;
            }
        }

        // Héroe con Pozos (Abiertos y Cerrándose)
        for (Pozo pozo : pozos) {
            if (heroe.intersects(pozo)) {
                if (pozo.getEstado() == 0) { // Pozo abierto

                    // Verificamos si hay un bloque real de soporte abajo
                    boolean tieneSueloAbajo = false;
                    for (Plataforma plat : plataformas) {
                        if (Math.abs(plat.x - pozo.x) < 5 && Math.abs(plat.y - (pozo.y + 30)) < 5) {
                            tieneSueloAbajo = true;
                            break;
                        }
                    }

                    if (tieneSueloAbajo && heroe.y >= pozo.y - 10) {
                        heroeEnPozo = true;
                        heroeSoportado = true;
                        heroe.x = pozo.x;
                        heroe.setVelocidadX(0);
                    }

                } else if (pozo.getEstado() == -1) { // Pozo cerrándose (pisable)
                    boolean vieneDesdeArriba = (heroe.y + heroe.height <= pozo.y + 5);

                    if (vieneDesdeArriba && heroe.x + heroe.width > pozo.x + 8 && heroe.x < pozo.x + pozo.width - 8) {
                        heroeSoportado = true;
                        if (!heroe.isEnEscalera())
                            heroe.y = pozo.y - heroe.height + 1;
                    }
                }
            }
        }

        heroe.setEnBarra(heroeEnBarra);
        heroe.setEstaCayendo(!heroeSoportado);
        heroe.setEnEscalera(heroeEnEscalera);

        //COLSIONES GUARDIAS

        for (Guardia guardia : guardias) {
            boolean guardiaSoportado = false;
            boolean guardiaEnEscalera = false;
            boolean guardiaEnBarra = false;
            boolean guardiaEnPozo = false;
            boolean estabaCayendo = (guardia.getEstado() == Guardia.Estado.CAYENDO);

            // --- NUEVO: EL GUARDIA RECOGE ORO (Solo 1 a la vez y en tierra firme) ---
            if (!guardia.isTieneOro() && (guardia.getEstado() == Guardia.Estado.CAMINANDO || guardia.getEstado() == Guardia.Estado.PATRULLANDO)) {
                Iterator<Lingote> itOroGuardia = lingotes.iterator();
                while (itOroGuardia.hasNext()) {
                    Lingote oro = itOroGuardia.next();

                    // Radar central ULTRA ESTRICTO (< 10 px) para que deba pisarlo perfectamente
                    boolean mismoX = Math.abs((guardia.x + 15) - (oro.x + 15)) < 10;
                    boolean mismoY = Math.abs((guardia.y + 15) - (oro.y + 15)) < 10;

                    if (mismoX && mismoY) {
                        guardia.setTieneOro(true);
                        itOroGuardia.remove(); // Lo saca del mapa porque lo lleva encima
                        System.out.println("¡Guardia ID [" + System.identityHashCode(guardia) + "] acaba de robar un lingote!");                        break;
                    }
                }
            }

            // Guardia con escalera
            for (Escalera escalera : escaleras) {
                double pieX = guardia.x + guardia.width / 2.0;
                double pieY = guardia.y + guardia.height;

                boolean alineadoX = Math.abs(pieX - (escalera.x + escalera.width / 2.0)) <= 5;
                boolean dentroY = pieY >= escalera.y - 5 && guardia.y <= (escalera.y + escalera.height) + 5;

                if (alineadoX && dentroY) {
                    guardiaEnEscalera = true;
                    guardiaSoportado = true;

                    boolean subiendo = guardia.getEstado() == Guardia.Estado.SUBIENDO_ESCALERA;
                    boolean bajando = guardia.getEstado() == Guardia.Estado.BAJANDO_ESCALERA;

                    if (subiendo || bajando) {
                        guardia.x = escalera.x;
                    }
                }

                if (guardia.intersects(escalera) && pieY <= escalera.y + 15) {
                    guardiaSoportado = true;
                }
            }

            // guardia con plataforma
            for (Plataforma plataforma : plataformas) {
                if (guardia.intersects(plataforma)) {
                    if (guardia.x + guardia.width > plataforma.x + 8 &&
                            guardia.x < plataforma.x + plataforma.width - 8) {

                        if (guardia.y < plataforma.y + 15) {
                            if (!guardiaEnPozo) {
                                guardiaSoportado = true;
                                if (!guardiaEnEscalera)
                                    guardia.y = plataforma.y - guardia.height + 1;
                            }
                        }
                    }
                }
            }

            // guardia con barra de manos
            for (BarraDeManos barra : barras) {
                if (guardia.intersects(barra) && !guardiaEnPozo) {
                    guardiaSoportado = true;
                    guardiaEnBarra = true;
                    if (!guardiaEnEscalera) {
                        guardia.y = barra.y;

                        if (guardia.getVelocidadX() == 0) {
                            double difX = heroe.x - guardia.x;
                            if (Math.abs(difX) > 2) {
                                guardia.x += (difX > 0) ? 2.0 : -2.0;
                            } else {
                                guardia.x += 2.0;
                            }
                        }
                    }
                }
            }

            // guardia con pozos (Abiertos y Cerrandose)
            boolean guardiaTocandoPozo = false;

            for (Pozo pozo : pozos) {
                if (guardia.intersects(pozo)) {
                    guardiaTocandoPozo = true;

                    // 1. LÓGICA DE ESCAPE
                    if (guardia.getEstado() == Guardia.Estado.ESCAPANDO_POZO) {
                        guardiaEnPozo = true;
                        guardiaSoportado = true;

                        if (guardia.y <= pozo.y - 15) {
                            guardia.y = pozo.y - guardia.height;
                            guardia.notificarSalidaPozo();

                            double nuevaX = guardia.x + ((heroe.x > guardia.x) ? 20 : -20);
                            guardia.x = Math.max(30, Math.min(1170 - guardia.width, nuevaX));
                        }
                    }
                    // 2. LÓGICA DE ATRAPE
                    else if (pozo.getEstado() == 0) {
                        if (guardia.getEstado() == Guardia.Estado.ATRAPADO_POZO) {
                            guardiaEnPozo = true;
                            guardiaSoportado = true;
                            guardia.x = pozo.x;
                        } else {

                            if (guardia.y >= pozo.y - 10) {
                                // Solo sumamos puntos si es la primera vez que cae
                                if (guardia.getEstado() != Guardia.Estado.ATRAPADO_POZO &&
                                        guardia.getEstado() != Guardia.Estado.ESCAPANDO_POZO) {

                                    // --- NUEVO: SI TIENE ORO, LO SUELTA ARRIBA DEL POZO ---
                                    if (guardia.isTieneOro()) {
                                        guardia.setTieneOro(false); // Pierde el oro

                                        // Se coloca exactamente a 30px, alineado a la grilla de tu juego
                                        Lingote oroRecuperado = new Lingote(pozo.x, pozo.y - 30, 30, 30);
                                        if (imgOro != null) {
                                            oroRecuperado.setImagen(imgOro);
                                        }
                                        orosSoltados.add(oroRecuperado);
                                        System.out.println("¡El guardia soltó el oro al caer al pozo!");
                                    }

                                    guardia.notificarEntradaPozo();
                                    score += 75;
                                    guardiasAtrapadosNivel++;
                                    System.out.println("¡Guardia atrapado! +75 pts");
                                }
                                guardiaEnPozo = true;
                                guardiaSoportado = true;
                                guardia.x = pozo.x;
                                guardia.y = pozo.y;
                            }
                        }
                    }
                    // 3. POZO CERRÁNDOSE
                    else if (pozo.getEstado() == -1) {
                        boolean vieneDesdeArriba = (guardia.y + guardia.height <= pozo.y + 5);

                        if (vieneDesdeArriba && guardia.x + guardia.width > pozo.x + 8 &&
                                guardia.x < pozo.x + pozo.width - 8) {
                            guardiaSoportado = true;
                            if (!guardiaEnEscalera)
                                guardia.y = pozo.y - guardia.height + 1;
                        }
                    }
                }
            }

            if (guardia.getEstado() == Guardia.Estado.ESCAPANDO_POZO && !guardiaTocandoPozo) {
                guardia.notificarSalidaPozo();
            }

            guardia.setEstaCayendo(!guardiaSoportado);
            guardia.setEnBarra(guardiaEnBarra);

            if (estabaCayendo && guardiaSoportado && !guardiaEnPozo) {
                guardia.notificarAterrizaje();
            }

            if (guardiaEnEscalera) {
                for (Escalera e : escaleras) {
                    if (guardia.intersects(e)) {
                        if (guardia.getVelocidadY() < 0 && guardia.y + guardia.height <= e.y + 2) {
                            guardia.notificarFinEscalera();
                        }
                        if (guardia.getVelocidadY() > 0 && guardia.y + guardia.height >= e.y + e.height - 2) {
                            guardia.notificarFinEscalera();
                        }
                    }
                }
            }

            if (!guardiaSoportado
                    && guardia.getEstado() != Guardia.Estado.ATRAPADO_POZO
                    && guardia.getEstado() != Guardia.Estado.ESCAPANDO_POZO
                    && guardia.getEstado() != Guardia.Estado.SUBIENDO_ESCALERA
                    && guardia.getEstado() != Guardia.Estado.BAJANDO_ESCALERA) {
                guardia.setEstado(Guardia.Estado.CAYENDO);
            }
        }

        // COLISIÓN HEROE vs GUARDIA

        boolean atrapado = false;
        for (Guardia guardia : guardias) {
            if (heroe.intersects(guardia)) {

                if (guardia.getEstado() == Guardia.Estado.ATRAPADO_POZO) {
                    boolean pisandoCabeza = (heroe.y + heroe.height <= guardia.y + 15);

                    if (pisandoCabeza) {
                        if (teclado.isKeyPressed(KeyEvent.VK_DOWN)) {
                            atrapado = true;
                        } else {
                            heroeSoportado = true;
                            heroe.y = guardia.y - heroe.height + 1; // le piasa la cabeza
                            heroe.setEstaCayendo(false);
                        }
                    }

                } else {
                    atrapado = true;
                }
            }
        }

        if (atrapado) {
            System.out.println("¡El guardia atrapó al héroe! Pierdes una vida.");
            audio.reproducirEfecto("miss");
            heroe.iniciarMuerte();
        }
        lingotes.addAll(orosSoltados);
    }


    private void reiniciarPosiciones() {
        vidas--;

        if (vidas <= 0) {
            System.out.println("¡GAME OVER! Te quedaste sin vidas.");
            audio.detenerMusica();
            audio.reproducirEfecto("game_over");
            juegoTerminado = true;
            vidas = 0;
            estadoActual = EstadoJuego.GAMEOVER;
        } else {
            System.out.println("Vidas restantes: " + vidas);
            cargarNivel(nivel);
            estadoActual = EstadoJuego.TRANSICION;
            timerTransicion = 2.5;
        }
    }

    @Override
    public void gameShutdown() {
        System.out.println("Cerrando Lode Runner...");
    }

    // otros metodos
    private void agregarFila(double x, double y, int cantidadBloques, BufferedImage img) {
        for (int i = 0; i < cantidadBloques; i++) {
            Plataforma bloque = new Plataforma(x + (i * 30), y, 30, 30);
            bloque.setImagen(img);
            plataformas.add(bloque);
        }
    }

    private void dibujarNumero(Graphics2D g, String texto, int x, int y, double escala) {
        int offsetX = 0;
        for (char c : texto.toCharArray()) {
            int digito = Character.getNumericValue(c);
            if (numeros != null && digito >= 0 && digito <= 9 && numeros[digito] != null) {
                int nuevoAncho = (int) (numeros[digito].getWidth() * escala);
                int nuevoAlto = (int) (numeros[digito].getHeight() * escala);
                g.drawImage(numeros[digito], x + offsetX, y, nuevoAncho, nuevoAlto, null);
                offsetX += nuevoAncho + 2;
            }
        }
    }

    private void completarNivel() {
        System.out.println("¡NIVEL COMPLETADO!");
        vidasFinales = vidas;
        tiempoFinal = (int) tiempoRestante;

        // sumamos las bonificaciones al score
        int bonusNivel = 1500;
        score += bonusNivel;

        int puntosTiempo = tiempoFinal * 10;
        score += puntosTiempo;

        // activamos la pantalla intermedia de victoria
        pantallaVictoria = true;
    }

    private void avanzarSiguienteNivel() {
        nivel++;
        vidas++;

        cargarNivel(nivel);
        estadoActual = EstadoJuego.TRANSICION;
        timerTransicion = 2.5;
    }

    private void reiniciarJuegoTotal() {
        // Restauramos las variables al estado inicial de un juego nuevo
        vidas = 5;
        score = 0;
        nivel = 1;

        cargarNivel(nivel);
        audio.reproducirMusica(this.getClass().getResource("/pipoo/loderunner/audio/main_bgm.wav"));

        estadoActual = EstadoJuego.TRANSICION;
        timerTransicion = 2.5;
    }

    // =========================================================
    // MAIN
    // =========================================================
    public static void main(String[] args) {
        LodeRunner juego = new LodeRunner();
        juego.run(1.0 / 60.0);
        System.exit(0);
    }
}