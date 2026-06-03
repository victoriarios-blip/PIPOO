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

    // victoria - tiempo
    private double tiempoRestante = 150.0;
    private boolean escaleraSalidaCreada = false;
    private boolean juegoTerminado = false;
    private Escalera escaleraDeSalida = null;
    private BufferedImage imgEscaleraSalida;

    private boolean pantallaVictoria = false;
    private int highscore = 10000; // Arranca con un puntaje retro por defecto
    private int vidasFinales = 0;
    private int tiempoFinal = 0;

    // ATRIBUTOS DEL HUD
    private BufferedImage imgScore, imgLevel, imgTitulo, imgLives, imgPozo, imgFragmentoPozo, imgBloque, imgLadrilloInferior, imgParedLimite,imgGameOver;
    private BufferedImage[] numeros;
    private BufferedImage imgHighscore;

    private int score = 0;
    private int vidas = 5;
    private int nivel = 1;



    // AUDIO
    private GestorAudio audio = new GestorAudio();
    private double timerPasos = 0.15;
    private double timerEscalera = 0.18;

    public LodeRunner() {
        super("Retro Lode Runner", 800, 630);
    }

    @Override
    protected void actualizarPuntaje() {
    }

    //carga de mapa y nivel
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
            imgGameOver = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/game_over.png"));
            numeros = new BufferedImage[10];
            for (int i = 0; i <= 9; i++) {
                numeros[i] = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/" + i + ".png"));
            }

            // heroe
            BufferedImage imgHeroeDer = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/heroe_mirando_der.png"));
            BufferedImage imgHeroeIzq = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/heroe_mirando_izq.png"));
            BufferedImage imgHeroeEscalera = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/heroe_subiendo_escalera.png"));
            BufferedImage imgHeroeColgado = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/heroe_colgado_barramanos.png"));

            // guardia
            BufferedImage imgGuardiaDer = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/guardia_derecha_1.png"));
            BufferedImage imgGuardiaIzq = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/guardia_izquierda_1.png"));
            BufferedImage imgGuardiaEscalera = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/guardia_en_escalera.png"));
            BufferedImage imgGuardiaColgado = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/guardia_izq_colgado.png"));
            BufferedImage imgGuardiaAtrapado = ImageIO.read(this.getClass().getResource("/pipoo/loderunner/imagenes/guardia_atrapado_pozo.png"));

            // escenario
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

            // ESCALERAS

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

            imgEscaleraSalida = ImageIO.read(getClass().getResource("/pipoo/loderunner/imagenes/escalera larga.png"));

            // reiniciar el tiempo al arrancar el juego
            tiempoRestante = 150.0;

            // POS BARRAS DE MANOS

            BarraDeManos b1 = new BarraDeManos(420, 115, 385, 15);
            b1.setImagen(imgBarra);
            barras.add(b1);

            BarraDeManos b2 = new BarraDeManos(538, 370, 324, 15);
            b2.setImagen(imgBarra);
            barras.add(b2);

            // POS LINGOTES

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

            // POS HÉROE

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

            // POS GUARDIAS

            guardias.add(new Guardia(230, 180, 30, 30, heroe)); // G1 — piso 2 izq
            guardias.add(new Guardia(1000, 180, 30, 30, heroe)); // G2 — piso 2 der
            guardias.add(new Guardia(691, 250, 30, 30, heroe)); // G3 — piso 3 centro


            for (Guardia guardia : guardias) {
                guardia.setMapa(escaleras, plataformas, barras, pozos); // ← NUEVO
                guardia.setImagen(imgGuardiaDer);
                guardia.setImgDer(imgGuardiaDer);
                guardia.setImgIzq(imgGuardiaIzq);
                guardia.setImgEscalera(imgGuardiaEscalera);
                guardia.setImgColgado(imgGuardiaColgado);
                guardia.setImgAtrapado(imgGuardiaAtrapado);
            }

            // SONIDOS

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

    @Override
    public void gameUpdate(double delta) {
        Keyboard teclado = this.getKeyboard();

        if (juegoTerminado) {
            if (teclado.isKeyPressed(KeyEvent.VK_ENTER) || teclado.isKeyPressed(KeyEvent.VK_ESCAPE)) {
                System.exit(0);
            }
            return; // Corta el update acá, nadie se mueve
        }

        // NUEVO: Si completó el nivel, esperamos que presione ENTER para cambiar de mapa
        if (pantallaVictoria) {
            if (teclado.isKeyPressed(KeyEvent.VK_ENTER)) {
                pantallaVictoria = false;
                avanzarSiguienteNivel(); // Método nuevo que crearemos en el Paso 4
            }
            return; // Corta el update, congela enemigos y reloj
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

        // DETECTAR SI RECOLECTÓ TODO EL ORO
        if (lingotes.isEmpty() && !escaleraSalidaCreada) {
            System.out.println("¡Todo el oro recolectado! Aparece la escalera de salida.");

            // FIX DEFINITIVO:
            // X = 1140 (Justo al lado de la pared de X=1170)
            // Y = 0 (Arranca en el techo absoluto)
            // Alto = 180 (Llega justo hasta la plataforma de Y=150 para que el héroe pueda subir)
            escaleraDeSalida = new Escalera(1140, 0, 30, 180, false);

            if (imgEscaleraSalida != null) {
                escaleraDeSalida.setImagen(imgEscaleraSalida);
            }

            escaleras.add(escaleraDeSalida);
            escaleraSalidaCreada = true;

            // Notificar a los guardias para que actualicen su mapa de rutas
            for (Guardia guardia : guardias) {
                guardia.setMapa(escaleras, plataformas, barras, pozos);
            }
        }

        // CONDICION DE VICTORIA
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

            if (puntoImpactoY < 530) {
                intentarCavar(puntoImpactoX, puntoImpactoY);
            }
        }

        // Actualización pozos
        Iterator<Pozo> iteradorPozos = pozos.iterator();
        while (iteradorPozos.hasNext()) {
            Pozo pozoActual = iteradorPozos.next();
            pozoActual.actualizar(delta);

            if (pozoActual.getEstado() == 2) { // 2= el pozo se esta cerrando

                // ver si entierra al Héroe
                if (heroe.intersects(pozoActual) && heroe.y >= pozoActual.y - 10) {
                    System.out.println("¡El héroe fue enterrado vivo!");
                    heroe.iniciarMuerte();
                }

                for (Guardia g : guardias) {
                    if (g.intersects(pozoActual)) {
                        System.out.println("¡Un guardia fue eliminado! +75 pts");
                        score += 75;
                        g.reaparecer(); // vuelve a su posición inicial
                    }
                }

                // regenerar el bloque solido
                Plataforma bloqueRegenerado = new Plataforma(pozoActual.x, pozoActual.y, 30, 30);
                if (imgBloque != null)
                    bloqueRegenerado.setImagen(imgBloque);
                plataformas.add(bloqueRegenerado);
                iteradorPozos.remove();
            }
        }

        // movimiento heroe — caida bloquea horizontal
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

        // escalera heroe
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

        // movimiento horizontal
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
        cameraX = Math.max(0, cameraX); // Permite ver la pared izquierda
        cameraX = Math.min(ANCHO_MUNDO  - ANCHO_PANTALLA, cameraX);
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

                pozos.add(nuevoPozo);
                return;
            }
        }
    }

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

        // PAREDES LIMITE
        if (imgParedLimite != null) {
            for (int y =0; y <= 510; y += 30) {
                // pared izquierda
                mundo.drawImage(imgParedLimite, 0, y, 30, 30, null);
                // pared derecha
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

        // SCORE
        if (imgScore != null) {
            double escalaScore = 2.0;
            int w = (int) (imgScore.getWidth() * escalaScore);
            int h = (int) (imgScore.getHeight() * escalaScore);
            g.drawImage(imgScore, 20, hudY, w, h, null);
            dibujarNumero(g, String.format("%06d", score), 20 + w + 10, hudY, escalaScore);
        }

        // LIVES
        if (imgLives != null) {
            double escalaLives = 2.0;
            int w = 100;
            int h = w * imgLives.getHeight() / imgLives.getWidth();
            g.drawImage(imgLives, 310, hudY, w, h, null);
            dibujarNumero(g, String.format("%03d", vidas), 310 + w + 10, hudY + (h / 2) - 8, escalaLives);
        }

        // LEVEL
        if (imgLevel != null) {
            double escalaLevel = 2.0;
            int w = (int) (imgLevel.getWidth() * escalaLevel);
            int h = (int) (imgLevel.getHeight() * escalaLevel);
            g.drawImage(imgLevel, 560, hudY, w, h, null);
            dibujarNumero(g, String.format("%03d", nivel), 560 + w + 10, hudY, escalaLevel);
        }

        // TIMER
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Monospaced", Font.BOLD, 14));
        g.drawString("TIME:", 740, hudY + 12);

        double escalaTime = 1.2;
        dibujarNumero(g, String.format("%03d", (int) tiempoRestante), 740, hudY + 24, escalaTime);
        // GAME OVER
        if (juegoTerminado && imgGameOver != null) {
            g.setColor(new Color(0, 0, 0, 195));
            g.fillRect(0, 0, this.getWidth(), this.getHeight());

            // NUEVO TAMAÑO: Lo escalamos a 580px de ancho (un tamaño ideal y nítido)
            int nuevoAncho = 580;
            int nuevoAlto = (nuevoAncho * imgGameOver.getHeight()) / imgGameOver.getWidth();

            // CENTRADO Y POSICIÓN: Quitamos el descuento negativo y le sumamos +20
            // para desplazar el cartel sutilmente hacia abajo, dándole un encuadre perfecto
            int x = (800 - nuevoAncho) / 2;
            int y = (630 - nuevoAlto) / 2 + 20;

            // Dibujamos la imagen con el nuevo escalado robusto
            g.drawImage(imgGameOver, x, y, nuevoAncho, nuevoAlto, null);
        }

        // ── OVERLAY DE PANTALLA DE VICTORIA INTERMEDIA (CORREGIDO) ──────
        if (pantallaVictoria) {
            // Fondo COMPLETAMENTE negro como pediste
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());

            // Título principal en BLANCO (eliminamos el verde), más grande y centrado
            g.setColor(Color.WHITE);
            g.setFont(new Font("Monospaced", Font.BOLD, 42));
            g.drawString("STAGE CLEAR", 265, 140);

            // Mensaje de guía inferior en gris retro
            g.setColor(Color.LIGHT_GRAY);
            g.setFont(new Font("Monospaced", Font.BOLD, 16));
            g.drawString("PRESS ENTER TO NEXT STAGE", 280, 520);

            // Coordenadas de alineación de las filas de estadísticas
            int startX = 240; // Un toque más a la izquierda para dar aire
            int numX = 490;   // Alineación perfecta para la columna de números
            int startY = 230; // Bajamos el bloque completo de datos
            int espaciado = 65; // Más separación entre filas
            double escalaItems = 2.0;

            // 1. Dibujar VIDAS (Copiamos la lógica de ancho fijo del HUD para que aparezca sí o sí)
            if (imgLives != null) {
                int wLives = 120; // Forzamos ancho base para que Java lo renderice impecable
                int hLives = wLives * imgLives.getHeight() / imgLives.getWidth();
                g.drawImage(imgLives, startX, startY, wLives, hLives, null);
                // Centramos los números verticalmente con el sprite
                dibujarNumero(g, String.format("%03d", vidasFinales), numX, startY + (hLives / 2) - 12, escalaItems);
            }

            // 2. Dibujar TIEMPO SOBRANTE
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Monospaced", Font.BOLD, 18));
            g.drawString("TIME BONUS", startX, startY + espaciado + 22);
            dibujarNumero(g, String.format("%03d", tiempoFinal), numX, startY + espaciado, escalaItems);

            // 3. Dibujar TOTAL SCORE
            if (imgScore != null) {
                int wScore = (int) (imgScore.getWidth() * escalaItems);
                int hScore = (int) (imgScore.getHeight() * escalaItems);
                g.drawImage(imgScore, startX, startY + (espaciado * 2), wScore, hScore, null);
                dibujarNumero(g, String.format("%06d", score), numX, startY + (espaciado * 2), escalaItems);
            }

            // 4. Dibujar HI-SCORE de la sesión
            g.setColor(Color.RED);
            g.setFont(new Font("Monospaced", Font.BOLD, 18));
            g.drawString("HI-SCORE", startX, startY + (espaciado * 3) + 22);
            dibujarNumero(g, String.format("%06d", highscore), numX, startY + (espaciado * 3), escalaItems);
        }
    }

    // COLISIONES
    @Override
    protected void detectarColisiones() {
        boolean heroeSoportado = false;
        boolean heroeEnEscalera = false;
        boolean heroeEnBarra = false;
        boolean heroeEnPozo = false;

        Keyboard teclado = this.getKeyboard();

        // ── 1. COLISIONES HÉROE ──────────────────────────────────────────

        // Héroe con oro
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
                    // ANTI-LEVITACIÓN: Solo da soporte si el héroe viene caminando desde ARRIBA de la superficie
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


        // ── 2. COLISIONES GUARDIAS ───────────────────────────────────────

        for (Guardia guardia : guardias) {
            boolean guardiaSoportado = false;
            boolean guardiaEnEscalera = false;
            boolean guardiaEnBarra = false;
            boolean guardiaEnPozo = false;
            boolean estabaCayendo = (guardia.getEstado() == Guardia.Estado.CAYENDO);

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

            // Guardia con plataforma
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

            // Guardia con barra de manos
            for (BarraDeManos barra : barras) {
                if (guardia.intersects(barra) && !guardiaEnPozo) {
                    guardiaSoportado = true;
                    guardiaEnBarra = true;
                    if (!guardiaEnEscalera)
                        guardia.y = barra.y;
                }
            }

            // Guardia con Pozos (Abiertos y Cerrándose)
            for (Pozo pozo : pozos) {
                if (guardia.intersects(pozo)) {
                    if (pozo.getEstado() == 0) { // Pozo abierto

                        if (guardia.getEstado() == Guardia.Estado.ESCAPANDO_POZO) {
                            guardiaEnPozo = true;
                            guardiaSoportado = true;

                            if (guardia.y <= pozo.y - guardia.height + 2) {
                                guardia.y = pozo.y - guardia.height;
                                guardia.notificarSalidaPozo();

                                double nuevaX = guardia.x + ((heroe.x > guardia.x) ? 16 : -16);
                                guardia.x = Math.max(30, Math.min(1170 - guardia.width, nuevaX));
                            }
                        }
                        else if (guardia.getEstado() == Guardia.Estado.ATRAPADO_POZO) {
                            guardiaEnPozo = true;
                            guardiaSoportado = true;
                            guardia.x = pozo.x;
                        }
                        else {
                            // Escaneamos si el pozo tiene un suelo sólido abajo
                            boolean tieneSueloAbajo = false;
                            for (Plataforma plat : plataformas) {
                                if (Math.abs(plat.x - pozo.x) < 5 && Math.abs(plat.y - (pozo.y + 30)) < 5) {
                                    tieneSueloAbajo = true;
                                    break;
                                }
                            }

                            if (tieneSueloAbajo && guardia.y >= pozo.y - 10) {
                                guardiaEnPozo = true;
                                guardiaSoportado = true;
                                guardia.x = pozo.x;
                                guardia.y = pozo.y;
                                guardia.notificarEntradaPozo();
                                score += 75;
                                System.out.println("¡Guardia atrapado! +75 pts");
                            }
                        }

                    } else if (pozo.getEstado() == -1) { // Pozo cerrándose
                        // ANTI-LEVITACIÓN: Solo da soporte si el guardia viene caminando desde ARRIBA de la superficie
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

        // ── 3. COLISIÓN HÉROE vs GUARDIA ─────────────────────────────────

        boolean atrapado = false;
        for (Guardia guardia : guardias) {
            if (heroe.intersects(guardia)) {
                boolean pisandoCabeza = (heroe.y + heroe.height <= guardia.y + 15);

                if (pisandoCabeza) {
                    if (teclado.isKeyPressed(KeyEvent.VK_DOWN)) {
                        atrapado = true;
                    } else {
                        heroeSoportado = true;
                        heroe.y = guardia.y - heroe.height + 1;
                        heroe.setEstaCayendo(false);
                    }
                } else {
                    if (guardia.getEstado() == Guardia.Estado.ATRAPADO_POZO) {
                        continue;
                    } else {
                        atrapado = true;
                    }
                }
            }
        }

        if (atrapado) {
            System.out.println("¡El guardia atrapó al héroe! Pierdes una vida.");
            audio.reproducirEfecto("miss");
            heroe.iniciarMuerte();
        }
    }

    private void reiniciarPosiciones() {
        vidas--;

        if (vidas <= 0) {
            System.out.println("¡GAME OVER! Te quedaste sin vidas.");
            audio.detenerMusica();
            audio.reproducirEfecto("game_over");

            // FIX: Activamos las banderas sin congelar el renderizado
            juegoTerminado = true;
            vidas = 0; // Para que el HUD quede estéticamente en 000
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

        // 1. Congelamos las estadísticas exactas de este nivel para mostrarlas
        vidasFinales = vidas;
        tiempoFinal = (int) tiempoRestante;

        // 2. Sumamos las bonificaciones al score
        int bonusNivel = 1500;
        score += bonusNivel;

        int puntosTiempo = tiempoFinal * 10;
        score += puntosTiempo;

        // 3. Comprobamos si hay nuevo récord histórico
        if (score > highscore) {
            highscore = score;
            System.out.println("¡NUEVO HI-SCORE LOGRADO!: " + highscore);
        }

        // 4. Activamos la pantalla intermedia de victoria
        pantallaVictoria = true;
    }

    private void avanzarSiguienteNivel() {
        // Pasar al siguiente nivel y dar la vida de recompensa clásica
        nivel++;
        vidas++;

        // Resetear condiciones de la escalera de escape
        escaleraSalidaCreada = false;
        if (escaleraDeSalida != null) {
            escaleras.remove(escaleraDeSalida);
        }
        escaleraDeSalida = null;

        // Volver a generar el mapa limpio
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