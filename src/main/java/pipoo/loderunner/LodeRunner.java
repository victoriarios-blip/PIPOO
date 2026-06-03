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

    // --- ATRIBUTOS DE CONDICIÓN DE VICTORIA y TIEMPO ---
    private double tiempoRestante = 150.0; // 150 segundos por nivel, por ejemplo
    private boolean escaleraSalidaCreada = false;
    private Escalera escaleraDeSalida = null;
    private BufferedImage imgEscaleraSalida; // Para guardar la imagen de la escalera secreta

    // --- ATRIBUTOS DEL HUD ---
    private BufferedImage imgScore, imgLevel, imgTitulo, imgLives, imgPozo, imgFragmentoPozo, imgBloque, imgLadrilloInferior, imgParedLimite ;
    private BufferedImage[] numeros;
    private BufferedImage imgHighscore;

    private int score = 0;
    private int vidas = 5;
    private int nivel = 1;

    // --- AUDIO ---
    private GestorAudio audio = new GestorAudio();
    private double timerPasos = 0.15;
    private double timerEscalera = 0.18;

    public LodeRunner() {
        super("Retro Lode Runner", 800, 630);
    }

    @Override
    protected void actualizarPuntaje() {
    }

    // =========================================================
    // STARTUP
    // =========================================================
    @Override
    public void gameStartup() {
        System.out.println("Iniciando Lode Runner...");

        pozos = new ArrayList<>();
        guardias = new ArrayList<>();
        lingotes = new ArrayList<>();
        plataformas = new ArrayList<>();
        escaleras = new ArrayList<>();
        barras = new ArrayList<>();

        try {
            // HUD
            imgScore = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/score.png"));
            imgLevel = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/level.png"));
            imgTitulo = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/titulo lode runner.png"));
            imgLives = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/lives.png"));
            imgHighscore = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/24138.png"));

            numeros = new BufferedImage[10];
            for (int i = 0; i <= 9; i++) {
                numeros[i] = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/" + i + ".png"));
            }

            // Héroe
            BufferedImage imgHeroeDer = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/heroe_mirando_der.png"));
            BufferedImage imgHeroeIzq = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/heroe_mirando_izq.png"));
            BufferedImage imgHeroeEscalera = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/heroe_subiendo_escalera.png"));
            BufferedImage imgHeroeColgado = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/heroe_colgado_barramanos.png"));

            // Guardia
            BufferedImage imgGuardiaDer = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/guardia_derecha_1.png"));
            BufferedImage imgGuardiaIzq = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/guardia_izquierda_1.png"));
            BufferedImage imgGuardiaEscalera = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/guardia_en_escalera.png"));
            BufferedImage imgGuardiaColgado = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/guardia_izq_colgado.png"));
            BufferedImage imgGuardiaAtrapado = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/guardia_atrapado_pozo.png"));

            // Escenario
            BufferedImage imgEscCorta = ImageIO.read(getClass().getResource("/pipoo/loderunner/imagenes/escalera corta.png"));
            BufferedImage imgEscMediana = ImageIO.read(getClass().getResource("/pipoo/loderunner/imagenes/escalera mediana.png"));
            BufferedImage imgEscLarga = ImageIO.read(getClass().getResource("/pipoo/loderunner/imagenes/escalera larga.png"));
            BufferedImage imgBarra = ImageIO.read(getClass().getResource("/pipoo/loderunner/imagenes/barramanos.png"));
            BufferedImage imgOro = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/oro.png"));
            imgBloque = ImageIO.read(getClass().getResource("/pipoo/loderunner/imagenes/bloque.png"));
            imgPozo = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/pozo.png"));
            imgFragmentoPozo = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/fragmentos_pozo.png"));
            imgLadrilloInferior = ImageIO.read(getClass().getResource("/pipoo/loderunner/imagenes/ladrillo_plat_inferior.png"));
            imgParedLimite = ImageIO.read(getClass().getResource("/pipoo/loderunner/imagenes/ladrillo.png")); // CARGAR AQUÍ
            // ── MAPA NIVEL - NO MODIFICAR NADA, QUEDO PERFECTO ──────────

            // PISO TECHO 1
            agregarFila(0, 80, 13, imgBloque);
            // PISO TECHO 2
            agregarFila(421, 80, 10, imgBloque);

            // PISO 2 1
            agregarFila(10, 210, 5, imgBloque);
            // PISO 2 2
            agregarFila(185, 210, 9, imgBloque);

            // PISO intermedio 1
            agregarFila(805, 150, 9, imgBloque);
            // PISO intermedio 2
            agregarFila(1105, 150, 3, imgBloque);

            // PISO 2 plat que sostiene a la plat chiquita 1
            agregarFila(600, 210, 9, imgBloque);
            // PISO 2 plat que sostiene a la plat chiquita 2
            agregarFila(895, 210, 10, imgBloque);
            // plat chiquita 1
            agregarFila(600, 180, 3, imgBloque);
            // plat chiquita 2
            agregarFila(600, 150, 3, imgBloque);

            // PISO 3 1
            agregarFila(0, 280, 17, imgBloque);
            // PISO 3 2
            agregarFila(540, 280, 11, imgBloque);

            // PISO 4 izq
            agregarFila(300, 420, 8, imgBloque);
            // PISO 4 derecha
            agregarFila(865, 420, 9, imgBloque);

            // PISO BASE
            agregarFila(0, 500, 39, imgBloque);
            //Lecho de ladrillos
            for (int i = 0; i < 40; i++) {
                Plataforma ladrillo = new Plataforma(i * 30, 530, 30, 30);
                if (imgLadrilloInferior != null) {
                    ladrillo.setImagen(imgLadrilloInferior);
                }
                plataformas.add(ladrillo);
            }

            // ── ESCALERAS ────────────────────────────────────────────────

            Escalera e6 = new Escalera(390, 81, 30, 130, false);
            e6.setImagen(imgEscMediana);
            escaleras.add(e6);

            Escalera e5 = new Escalera(691, 150, 30, 60, false);
            e5.setImagen(imgEscCorta);
            escaleras.add(e5);

            Escalera e51 = new Escalera(1075, 150, 30, 60, false);
            e51.setImagen(imgEscCorta);
            escaleras.add(e51);

            Escalera e4 = new Escalera(155, 205, 30, 78, false);
            e4.setImagen(imgEscCorta);
            escaleras.add(e4);

            Escalera e41 = new Escalera(865, 210, 30, 209, false);
            e41.setImagen(imgEscMediana);
            escaleras.add(e41);

            Escalera e3 = new Escalera(510, 282, 30, 138, false);
            e3.setImagen(imgEscMediana);
            escaleras.add(e3);

            Escalera e2 = new Escalera(268, 420, 30, 80, false);
            e2.setImagen(imgEscCorta);
            escaleras.add(e2);

            Escalera e11 = new Escalera(1135, 420, 30, 80, false);
            e11.setImagen(imgEscCorta);
            escaleras.add(e11);

            // Adentro del try de gameStartup, debajo de tus otras imágenes:
            imgEscaleraSalida = ImageIO.read(getClass().getResource("/pipoo/loderunner/imagenes/escalera larga.png"));

            // Reiniciar el tiempo al arrancar el juego
            tiempoRestante = 150.0;

            // ── BARRAS DE MANOS ──────────────────────────────────────────

            BarraDeManos b1 = new BarraDeManos(420, 115, 385, 15);
            b1.setImagen(imgBarra);
            barras.add(b1);

            BarraDeManos b2 = new BarraDeManos(538, 370, 324, 15);
            b2.setImagen(imgBarra);
            barras.add(b2);

            // ── LINGOTES ─────────────────────────────────────────────────

            Lingote o1 = new Lingote(230, 50, 30, 30);
            o1.setImagen(imgOro);
            lingotes.add(o1);
            Lingote o2 = new Lingote(1000, 120, 30, 30);
            o2.setImagen(imgOro);
            lingotes.add(o2);
            Lingote o3 = new Lingote(960, 180, 30, 30);
            o3.setImagen(imgOro);
            lingotes.add(o3);
            Lingote o4 = new Lingote(400, 390, 30, 30);
            o4.setImagen(imgOro);
            lingotes.add(o4);
            Lingote o5 = new Lingote(1040, 390, 30, 30);
            o5.setImagen(imgOro);
            lingotes.add(o5);
            Lingote o6 = new Lingote(760, 470, 30, 30);
            o6.setImagen(imgOro);
            lingotes.add(o6);

            // ── HÉROE ────────────────────────────────────────────────────

            heroe = new Heroe(450, 510, 30, 30);
            heroe.setImagen(imgHeroeDer);
            heroe.setImagenDer(imgHeroeDer);
            heroe.setImagenIzq(imgHeroeIzq);
            heroe.setImagenEscalera(imgHeroeEscalera);
            heroe.setImagenColgado(imgHeroeColgado);

            for (int i = 1; i <= 7; i++) {
                BufferedImage frameMuerte = ImageIO.read(
                        this.getClass().getResource("/pipoo/loderunner/imagenes/heroe_muriendo_" + i + ".png"));
                heroe.setImagenMuriendo(i - 1, frameMuerte);
            }

            // ── GUARDIAS ─────────────────────────────────────────────────

            guardias.add(new Guardia(230, 180, 30, 30, heroe)); // G1 — piso 2 izq
            guardias.add(new Guardia(1000, 180, 30, 30, heroe)); // G2 — piso 2 der
            guardias.add(new Guardia(691, 250, 30, 30, heroe)); // G3 — piso 3 centro

            // CAMBIO 1: inyectar mapa + asignar imágenes
            for (Guardia guardia : guardias) {
                guardia.setMapa(escaleras, plataformas, barras, pozos); // ← NUEVO
                guardia.setImagen(imgGuardiaDer);
                guardia.setImgDer(imgGuardiaDer);
                guardia.setImgIzq(imgGuardiaIzq);
                guardia.setImgEscalera(imgGuardiaEscalera);
                guardia.setImgColgado(imgGuardiaColgado);
                guardia.setImgAtrapado(imgGuardiaAtrapado);
            }

            // ── SONIDOS ──────────────────────────────────────────────────

            audio.precargarEfecto("pasos", this.getClass().getResource("/pipoo/loderunner/audio/pasos.wav"));
            audio.precargarEfecto("escalera", this.getClass().getResource("/pipoo/loderunner/audio/escalera.wav"));
            audio.precargarEfecto("oro", this.getClass().getResource("/pipoo/loderunner/audio/oro.wav"));
            audio.precargarEfecto("miss", this.getClass().getResource("/pipoo/loderunner/audio/miss.wav"));
            audio.precargarEfecto("game_over", this.getClass().getResource("/pipoo/loderunner/audio/game_over.wav"));

            java.net.URL urlMusica = this.getClass().getResource("/pipoo/loderunner/audio/main_bgm.wav");
            audio.reproducirMusica(urlMusica);

        } catch (Exception e) {
            System.out.println("Error cargando imagenes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =========================================================
    // UPDATE
    // =========================================================
    @Override
    public void gameUpdate(double delta) {
        Keyboard teclado = this.getKeyboard();

        if (heroe.isEstaMuriendo()) {
            boolean animacionTerminada = heroe.actualizarAnimacionMuerte(delta);
            if (animacionTerminada) {
                reiniciarPosiciones();
            }
            return;
        }

        // --- 1. ACTUALIZAR TIEMPO DEL NIVEL ---
        if (tiempoRestante > 0) {
            tiempoRestante -= delta;
            if (tiempoRestante <= 0) {
                tiempoRestante = 0;
                System.out.println("¡Se acabó el tiempo! El héroe muere.");
                heroe.iniciarMuerte();
                return;
            }
        }

        // --- 2. DETECTAR SI RECOLECTÓ TODO EL ORO ---
        if (lingotes.isEmpty() && !escaleraSalidaCreada) {
            System.out.println("¡Todo el oro recolectado! Aparece la escalera de salida.");

            escaleraDeSalida = new Escalera(600, 0, 30, 210, false);
            if (imgEscaleraSalida != null) {
                escaleraDeSalida.setImagen(imgEscaleraSalida);
            }

            escaleras.add(escaleraDeSalida);
            escaleraSalidaCreada = true;

            for (Guardia guardia : guardias) {
                guardia.setMapa(escaleras, plataformas, barras, pozos);
            }
        }

        // --- 3. CONDICIÓN DE VICTORIA ---
        if (escaleraSalidaCreada && heroe.isEnEscalera() && heroe.intersects(escaleraDeSalida)) {
            if (heroe.y <= 10) {
                completarNivel();
                return;
            }
        }

        if (heroe.isEstaMuriendo()) {
            boolean animacionTerminada = heroe.actualizarAnimacionMuerte(delta);
            if (animacionTerminada)
                reiniciarPosiciones();
            return;
        }

        // Cavar pozos
        if (teclado.isKeyPressed(KeyEvent.VK_SPACE)) {
            heroe.cavar();
            double puntoImpactoX = heroe.x + (heroe.width / 2.0) + ultimaDireccion;
            double puntoImpactoY = heroe.y + heroe.height + 5;

            // FIX: Ampliamos el límite a 530.
            // Permite cavar la capa de Y=500, pero protege la capa inferior (Y=530).
            if (puntoImpactoY < 530) {
                intentarCavar(puntoImpactoX, puntoImpactoY);
            }
        }

// Actualización pozos
        Iterator<Pozo> iteradorPozos = pozos.iterator();
        while (iteradorPozos.hasNext()) {
            Pozo pozoActual = iteradorPozos.next();
            pozoActual.actualizar(delta);

            if (pozoActual.getEstado() == 2) { // 2 = El pozo se está cerrando

                // 1. Chequear si entierra al Héroe
                if (heroe.intersects(pozoActual) && heroe.y >= pozoActual.y - 10) {
                    System.out.println("¡El héroe fue enterrado vivo!");
                    heroe.iniciarMuerte();
                }

                // 2. FIX: Chequear si entierra a un Guardia
                for (Guardia g : guardias) {
                    // Quitamos la restricción de profundidad. Si el pozo se cierra
                    // y el guardia lo está tocando, ¡fue eliminado!
                    if (g.intersects(pozoActual)) {
                        System.out.println("¡Un guardia fue eliminado! +75 pts");
                        score += 75;
                        g.reaparecer(); // Lo teletransporta a su posición inicial
                    }
                }

                // Regenerar el bloque sólido
                Plataforma bloqueRegenerado = new Plataforma(pozoActual.x, pozoActual.y, 30, 30);
                if (imgBloque != null)
                    bloqueRegenerado.setImagen(imgBloque);
                plataformas.add(bloqueRegenerado);
                iteradorPozos.remove();
            }
        }

        // Movimiento héroe — caída bloquea horizontal
        if (heroe.isEstaCayendo()) {
            heroe.setVelocidadX(0);
        } else {
            if (teclado.isKeyPressed(KeyEvent.VK_RIGHT))
                heroe.setVelocidadX(2);
            else if (teclado.isKeyPressed(KeyEvent.VK_LEFT))
                heroe.setVelocidadX(-2);
            else
                heroe.setVelocidadX(0);
        }

        // Escalera héroe
        if (heroe.isEnEscalera()) {
            if (teclado.isKeyPressed(KeyEvent.VK_UP))
                heroe.setVelocidadY(-2.5);
            else if (teclado.isKeyPressed(KeyEvent.VK_DOWN))
                heroe.setVelocidadY(2.5);
            else
                heroe.setVelocidadY(0);
        } else if (!heroe.isEstaCayendo()) {
            heroe.setVelocidadY(0);
        }

        // Movimiento horizontal
        if (teclado.isKeyPressed(KeyEvent.VK_RIGHT)) {
            heroe.setVelocidadX(2.5);
            ultimaDireccion = 30;
        } else if (teclado.isKeyPressed(KeyEvent.VK_LEFT)) {
            heroe.setVelocidadX(-2.5);
            ultimaDireccion = -30;
        } else {
            heroe.setVelocidadX(0);
        }

        // Sonido pasos
        if (heroe.getVelocidadX() != 0 && !heroe.isEstaCayendo() && !heroe.isEnEscalera()) {
            timerPasos += delta;
            if (timerPasos >= 0.25) {
                audio.reproducirEfecto("pasos");
                timerPasos = 0;
            }
        } else {
            timerPasos = 0.3;
        }

        // Sonido escalera
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
            // Límite físico para los guardias (no pasan del muro izq de 30px ni del der de 1170)
            guardia.x = Math.max(30, Math.min(1170 - guardia.width, guardia.x));
        }

        // Límite físico para el héroe
        heroe.x = Math.max(30, Math.min(1170 - heroe.width, heroe.x));

        detectarColisiones();

        // Cámara
        cameraX = heroe.x - ANCHO_PANTALLA / 2.0;
        cameraX = Math.max(0, cameraX); // Permite ver la pared izquierda
        cameraX = Math.min(ANCHO_MUNDO  - ANCHO_PANTALLA, cameraX); // Permite ver la pared derecha
    }

    // =========================================================
    // CAVAR
    // =========================================================
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

                pozos.add(nuevoPozo);
                return;
            }
        }
    }

    // =========================================================
    // DRAW
    // =========================================================
    @Override
    public void gameDraw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 800, 630);

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

        // --- DIBUJAR PAREDES LÍMITE ---
        if (imgParedLimite != null) {
            for (int y =0; y <= 510; y += 30) {
                // Pared Izquierda (Pegada al X=0 hacia atrás)
                mundo.drawImage(imgParedLimite, 0, y, 30, 30, null);
                // Pared Derecha (Pegada al X=1200 hacia adelante)
                mundo.drawImage(imgParedLimite, ANCHO_MUNDO - 29, y, 35, 30, null);            }
        }
        if (heroe != null)
            heroe.dibujar(mundo);
        for (Guardia guardia : guardias)
            guardia.dibujar(mundo);

        mundo.dispose();

        // HUD
        int hudY = 575;
        double escala = 1.5;

        // 1. SCORE (Más grande: Escala 2.0)
        if (imgScore != null) {
            double escalaScore = 2.0;
            int w = (int) (imgScore.getWidth() * escalaScore);
            int h = (int) (imgScore.getHeight() * escalaScore);
            g.drawImage(imgScore, 20, hudY, w, h, null);
            dibujarNumero(g, String.format("%06d", score), 20 + w + 10, hudY, escalaScore);
        }

        // 2. LIVES (Escala 1.5)
        if (imgLives != null) {
            double escalaLives = 2.0;
            int w = 100;
            int h = w * imgLives.getHeight() / imgLives.getWidth();
            g.drawImage(imgLives, 310, hudY, w, h, null);
            dibujarNumero(g, String.format("%03d", vidas), 310 + w + 10, hudY + (h / 2) - 8, escalaLives);
        }

        // 3. LEVEL (Más grande: Escala 2.0 y desplazado a la izquierda)
        if (imgLevel != null) {
            double escalaLevel = 2.0;
            int w = (int) (imgLevel.getWidth() * escalaLevel);
            int h = (int) (imgLevel.getHeight() * escalaLevel);
            g.drawImage(imgLevel, 560, hudY, w, h, null);
            dibujarNumero(g, String.format("%03d", nivel), 560 + w + 10, hudY, escalaLevel);
        }

        // 4. TIMER (Más chico, en el extremo derecho absoluto)
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Monospaced", Font.BOLD, 14)); // Texto más chico
        g.drawString("TIME:", 740, hudY + 12);

        double escalaTime = 1.2; // Números más chicos para el cronómetro
        dibujarNumero(g, String.format("%03d", (int) tiempoRestante), 740, hudY + 24, escalaTime);

    }

    // =========================================================
    // COLISIONES
    // =========================================================
    @Override
    protected void detectarColisiones() {
        boolean heroeSoportado = false;
        boolean heroeEnEscalera = false;
        boolean heroeEnBarra = false;
        boolean heroeEnPozo = false;

        // ── COLISIONES HÉROE ─────────────────────────────────────────────

        Iterator<Lingote> itOro = lingotes.iterator();
        while (itOro.hasNext()) {
            Lingote oro = itOro.next();
            if (heroe.intersects(oro)) {
                heroe.recolectarOro();
                itOro.remove();
                score += 250;
                audio.reproducirEfecto("oro");
            }
        }

        for (Pozo pozo : pozos) {
            if (heroe.intersects(pozo) && pozo.getEstado() == 0) {
                if (heroe.y >= pozo.y - 10) {
                    heroeEnPozo = true;
                    heroeSoportado = true;
                    heroe.x = pozo.x;
                    heroe.setVelocidadX(0);
                }
            }
        }

        for (Escalera escalera : escaleras) {
            if (heroe.intersects(escalera)) {
                heroeSoportado = true;
                heroeEnEscalera = true;
            }
        }

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

        for (Pozo pozo : pozos) {
            if (heroe.intersects(pozo) && pozo.getEstado() == -1) {
                if (heroe.x + heroe.width > pozo.x + 8 &&
                        heroe.x < pozo.x + pozo.width - 8) {
                    heroeSoportado = true;
                    if (!heroe.isEnEscalera())
                        heroe.y = pozo.y - heroe.height + 1;
                }
            }
        }

        for (BarraDeManos barra : barras) {
            if (heroe.intersects(barra) && !heroeEnPozo) {
                heroeSoportado = true;
                heroeEnBarra = true;
                if (!heroeEnEscalera)
                    heroe.y = barra.y;
            }
        }

        heroe.setEnBarra(heroeEnBarra);
        heroe.setEstaCayendo(!heroeSoportado);
        heroe.setEnEscalera(heroeEnEscalera);

        // ── COLISIONES GUARDIAS ──────────────────────────────────────────

        for (Guardia guardia : guardias) {
            boolean guardiaSoportado = false;
            boolean guardiaEnEscalera = false;
            boolean guardiaEnBarra = false;
            boolean guardiaEnPozo = false;

            // Guardamos si estaba cayendo ANTES de este tick para detectar aterrizaje
            boolean estabaCayendo = (guardia.getEstado() == Guardia.Estado.CAYENDO);

            // --- ESCALERAS ---
            for (Escalera escalera : escaleras) {
                double pieX = guardia.x + guardia.width / 2.0;
                double pieY = guardia.y + guardia.height;

                // A) Lógica estricta para Trepar (Alineación al centro)
                boolean alineadoX = Math.abs(pieX - (escalera.x + escalera.width / 2.0)) <= 5;
                boolean dentroY = pieY >= escalera.y - 5 && guardia.y <= (escalera.y + escalera.height) + 5;

                if (alineadoX && dentroY) {
                    guardiaEnEscalera = true;
                    guardiaSoportado = true; // Soporte mientras trepa

                    boolean subiendo = guardia.getEstado() == Guardia.Estado.SUBIENDO_ESCALERA;
                    boolean bajando = guardia.getEstado() == Guardia.Estado.BAJANDO_ESCALERA;

                    if (subiendo || bajando) {
                        guardia.x = escalera.x; // Lo centramos a la escalera
                    }
                }

                // B) FIX ZONA MUERTA: Soporte de Piso para el hueco
                // Si el guardia está tocando la escalera y sus pies están en la parte superior,
                // la escalera actúa como un piso sólido para que pueda caminar hacia los
                // costados.
                if (guardia.intersects(escalera) && pieY <= escalera.y + 15) {
                    guardiaSoportado = true;
                }
            }

            // --- PLATAFORMAS ---
            for (Plataforma plataforma : plataformas) {
                if (guardia.intersects(plataforma)) {
                    if (guardia.x + guardia.width > plataforma.x + 8 &&
                            guardia.x < plataforma.x + plataforma.width - 8) {
                        if (guardia.y + guardia.height <= plataforma.y + 20) {
                            if (!guardiaEnPozo) {
                                guardiaSoportado = true;
                                if (!guardiaEnEscalera)
                                    guardia.y = plataforma.y - guardia.height + 1;
                            }
                        }
                    }
                }
            }

// --- POZOS ---
            for (Pozo pozo : pozos) {
                if (guardia.intersects(pozo)) {
                    if (pozo.getEstado() == 0) { // Pozo abierto

                        if (guardia.getEstado() == Guardia.Estado.ESCAPANDO_POZO) {
                            guardiaEnPozo = true;
                            guardiaSoportado = true;

                            // FIX: ¿Ya levitó hasta la superficie?
                            if (guardia.y <= pozo.y - guardia.height + 2) {
                                guardia.y = pozo.y - guardia.height;
                                guardia.notificarSalidaPozo();

                                // Empujón a tierra firme (16 píxeles) para que el piso lo sostenga
                                double nuevaX = guardia.x + ((heroe.x > guardia.x) ? 16 : -16);
                                guardia.x = Math.max(0, Math.min(1200 - guardia.width, nuevaX));
                            }
                        }
                        else if (guardia.getEstado() == Guardia.Estado.ATRAPADO_POZO) {
                            guardiaEnPozo = true;
                            guardiaSoportado = true;
                            guardia.x = pozo.x; // FIX: Mantenerlo perfectamente centrado
                        }
                        else {
                            // Está cayendo o caminando y entra al pozo por primera vez
                            if (guardia.y >= pozo.y - 10) {
                                guardiaEnPozo = true;
                                guardiaSoportado = true;
                                guardia.x = pozo.x; // FIX: Centrado automático al caer
                                guardia.y = pozo.y; // Ajuste visual de profundidad

                                // Notificar captura y dar puntos directamente acá
                                guardia.notificarEntradaPozo();
                                score += 75;
                                System.out.println("¡Guardia atrapado! +75 pts");
                            }
                        }

                    } else if (pozo.getEstado() == -1) { // Pozo cerrándose (pisable)
                        if (guardia.x + guardia.width > pozo.x + 8 &&
                                guardia.x < pozo.x + pozo.width - 8) {
                            guardiaSoportado = true;
                            if (!guardiaEnEscalera)
                                guardia.y = pozo.y - guardia.height + 1;
                        }
                    }
                }
            }

            // --- BARRAS ---
            for (BarraDeManos barra : barras) {
                if (guardia.intersects(barra) && !guardiaEnPozo) {
                    guardiaSoportado = true;
                    guardiaEnBarra = true;
                    // CAMBIO 2b: solo snap vertical si NO está ya usando una escalera
                    if (!guardiaEnEscalera)
                        guardia.y = barra.y;
                }
            }

            // Informar soporte físico (para Runner / gravedad base)
            guardia.setEstaCayendo(!guardiaSoportado);

            // CAMBIO 2c: setEnBarra respeta la máquina de estados (no pisa escalera)
            guardia.setEnBarra(guardiaEnBarra);

            // ── NOTIFICACIONES A LA MÁQUINA DE ESTADOS ──────────────────

            // A) Aterrizó: estaba cayendo, ahora tiene soporte y no es un pozo
            if (estabaCayendo && guardiaSoportado && !guardiaEnPozo) {
                guardia.notificarAterrizaje();
            }



            // D) Llegó al tope o al fondo de una escalera
            if (guardiaEnEscalera) {
                for (Escalera e : escaleras) {
                    if (guardia.intersects(e)) {

                        // FIX: Subiendo, debe detenerse cuando SUS PIES llegan a la cima (e.y + 2 px)
                        // Esto garantiza que quede perfectamente apoyado arriba.
                        if (guardia.getEstado() == Guardia.Estado.SUBIENDO_ESCALERA
                                && guardia.y + guardia.height <= e.y + 2) {
                            guardia.notificarFinEscalera();
                        }

                        // Bajando: se detiene cuando sus pies tocan el fondo
                        if (guardia.getEstado() == Guardia.Estado.BAJANDO_ESCALERA
                                && guardia.y + guardia.height >= e.y + e.height - 2) {
                            guardia.notificarFinEscalera();
                        }
                    }
                }
            }

            // E) Sin soporte y no está atrapado ni escapando → forzar CAYENDO
            if (!guardiaSoportado
                    && guardia.getEstado() != Guardia.Estado.ATRAPADO_POZO
                    && guardia.getEstado() != Guardia.Estado.ESCAPANDO_POZO
                    && guardia.getEstado() != Guardia.Estado.SUBIENDO_ESCALERA
                    && guardia.getEstado() != Guardia.Estado.BAJANDO_ESCALERA) {
                guardia.setEstado(Guardia.Estado.CAYENDO);
            }
        }

// ── COLISIÓN HÉROE vs GUARDIA ────────────────────────────────────

        boolean atrapado = false;
        Keyboard teclado = this.getKeyboard();

        for (Guardia guardia : guardias) {
            if (heroe.intersects(guardia)) {

                // REGLA 1 y 3: Pisarle la cabeza (incluso cayendo del aire)
                // Comparamos si los pies del héroe están en la mitad superior del guardia
                boolean pisandoCabeza = (heroe.y + heroe.height <= guardia.y + 15);

                if (pisandoCabeza) {

                    // REGLA 4: Bajar mientras se está sobre un guardia es fatal ☠
                    if (teclado.isKeyPressed(KeyEvent.VK_DOWN)) {
                        atrapado = true;
                    } else {
                        // REGLA 2: Usarlo como "puente" o caminar sobre él
                        heroeSoportado = true;
                        heroe.y = guardia.y - heroe.height + 1; // Lo anclamos encima de la cabeza
                        heroe.setEstaCayendo(false);
                    }

                } else {
                    // Contacto cuerpo a cuerpo (NO le pisó la cabeza)

                    // Si el guardia está atrapado en el pozo, es inofensivo
                    if (guardia.getEstado() == Guardia.Estado.ATRAPADO_POZO) {
                        continue;
                    } else {
                        // Si está libre caminando o en escalera, es letal
                        atrapado = true;
                    }
                }
            }
        }

        // Ejecutar Muerte
        if (atrapado) {
            System.out.println("¡El guardia atrapó al héroe! Pierdes una vida.");
            audio.reproducirEfecto("miss");
            heroe.iniciarMuerte();
        }
    } // FIN DEL MÉTODO detectarColisiones()

    // =========================================================
    // REINICIAR
    // =========================================================
    private void reiniciarPosiciones() {
        vidas--;

        if (vidas <= 0) {
            System.out.println("¡GAME OVER! Te quedaste sin vidas.");
            audio.detenerMusica();
            audio.reproducirEfecto("game_over");
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
            }
            System.exit(0);
        } else {
            System.out.println("Vidas restantes: " + vidas);

            heroe.x = 450;
            heroe.y = 470;
            heroe.setVelocidadX(0);
            heroe.setVelocidadY(0);
            heroe.setEstaCayendo(false);
            heroe.setEnEscalera(false);

            for (Guardia guardia : guardias) {
                guardia.reaparecer();
            }
        }
    }

    // =========================================================
    // SHUTDOWN
    // =========================================================
    @Override
    public void gameShutdown() {
        System.out.println("Cerrando Lode Runner...");
    }

    // =========================================================
    // HELPERS
    // =========================================================
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

        // 1. Bonificación base por completar nivel
        int bonusNivel = 1500;
        score += bonusNivel;

        // 2. Bonificación por Tiempo Sobrante (Ej: 10 puntos por cada segundo que quedó)
        int puntosTiempo = (int) (tiempoRestante * 10);
        score += puntosTiempo;

        System.out.println("Bonus Nivel: +" + bonusNivel + " pts");
        System.out.println("Bonus Tiempo (" + (int)tiempoRestante + "s): +" + puntosTiempo + " pts");

        // Pausa dramática de victoria
        try {
            Thread.sleep(2000);
        } catch (Exception e) {}

        // 3. Pasar al siguiente nivel y resetear condiciones
        nivel++;
        vidas++; // Como en los arcades, te damos una vida extra por pasar de nivel

        // Reseteamos el juego para el nivel nuevo
        escaleraSalidaCreada = false;
        escaleras.remove(escaleraDeSalida); // Limpiamos la escalera de salida anterior
        escaleraDeSalida = null;

        // Volvemos a llenar los lingotes, reposicionar personajes, etc.
        // Como de momento mantenés el mismo mapa estructural (nivel estático), llamamos a Startup
        // o a reiniciarPosiciones() modificando el flujo para regenerar lingotes.
        gameStartup();
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