package pipoo.loderunner;
import com.entropyinteractive.Keyboard;
import pipoo.core.Juego;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import pipoo.core.Humano;

import java.awt.*;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

public class LodeRunner extends Juego {
    private Heroe heroe;
    private ArrayList<Pozo> pozos;
    private ArrayList<Guardia> guardias;
    private ArrayList<Lingote> lingotes;
    private ArrayList<BarraDeManos> barras;
    private ArrayList<Plataforma> plataformas;
    private ArrayList<Escalera> escaleras;

    // --- ATRIBUTOS DEL HUD (Interfaz) ---
    private BufferedImage imgScore, imgLevel, imgTitulo, imgMenu;
    private BufferedImage imgNum1, imgNum3, imgNum4, imgNum5, imgNum6, imgNum7, imgNum8, imgNum9;
    private BufferedImage imgHighscore; // Para la imagen "24138"

    public LodeRunner() {
        super("Retro Lode Runner", 800, 600);
    }

    @Override
    protected void actualizarPuntaje() {

    }

    @Override
    protected void detectarColisiones() {

        boolean heroeSoportado = false;
        boolean heroeEnEscalera = false;


        // COLISIÓN: HEROE vs GUARDIA
        for (Guardia guardia : guardias) {
            if (heroe.intersects(guardia)) {
                // si un guardia atrapa al jugador, se pierde una vida y se reinicia el nivel
                System.out.println("¡El guardia atrapó al héroe!");
                //agregar que pasa cuando lo agarra
            }
        }

        // COLISIÓN: HÉROE vs LINGOTES
        Iterator<Lingote> itOro = lingotes.iterator();
        while (itOro.hasNext()) {
            Lingote oro = itOro.next();
            if (heroe.intersects(oro)) {
                // recolectar oro suma puntos
                heroe.recolectarOro();
                itOro.remove(); // el oro desaparece
            }
        }

        // COLISION: GUARDIA vs POZO
        for (Guardia guardia : guardias) {
            for (Pozo pozo : pozos) {
                if (guardia.intersects(pozo) && pozo.getEstado() == 0) { // estado 0: abierto
                    // quedan atrapados
                    guardia.setEstaCayendo(true);

                    // si tienen oro, lo sueltan
                    if (guardia.isTieneOro()) {
                        guardia.setTieneOro(false);
                        // creamos un nuevo lingote en la posición donde cayo el guardia
                        lingotes.add(new Lingote(guardia.x, guardia.y, 20, 20));
                    }
                }
            }
        }

        // soporte en plataformas
        for (Plataforma plataforma : plataformas) {
            if (heroe.intersects(plataforma)) {
                heroeSoportado = true;
            }
        }

        // soporte en barras de manos
        for (BarraDeManos barra : barras) {
            if (heroe.intersects(barra)) {
                heroeSoportado = true;
            }
        }

        // soporte en escaleras
        for (Escalera escalera : escaleras) {
            if (heroe.intersects(escalera)) {
                heroeSoportado = true;
                heroeEnEscalera = true;
            }
        }

        // gravedad final al heroe
        heroe.setEstaCayendo(!heroeSoportado);
        heroe.setEnEscalera(heroeEnEscalera);
    }

    @Override
    public void gameStartup() {
        System.out.println("Iniciando Lode Runner...");
        //inicializamos jugador
        //jugadorLD= new Jugador();
        //jugadorLD.setVidas(5);

        //inicializamos listas de todos los elementos
        pozos = new ArrayList<>();
        guardias = new ArrayList<>();
        lingotes = new ArrayList<>();
        plataformas = new ArrayList<>();
        escaleras = new ArrayList<>();
        barras = new ArrayList<>();


        //inicializamos heroe, guardias y lingote
        heroe = new Heroe(400, 500, 30, 30);
        guardias.add(new Guardia(100, 100, 30, 30, heroe));
        lingotes.add(new Lingote(200, 500, 20, 20));
        // lingote extra arriba para probar las escaleras
        lingotes.add(new Lingote(480, 270, 20, 20));


        //MAPA
        //plataformas
        plataformas.add(new Plataforma(0, 530, 800, 40));
        //escaleras
        //escalera 1: conecta el piso principal con la plataforma de la izquierda
        escaleras.add(new Escalera(250, 400, 30, 130, false));
        //escalera 2: conecta el piso principal con la plataforma de la derecha
        escaleras.add(new Escalera(450, 300, 30, 230, false));

        //barra de manos
        barras.add(new BarraDeManos(300, 300, 100, 10));

        try {
            // A. HUD e Interfaz
            imgScore = ImageIO.read(this.getClass().getResource("pipoo/loderunner/imagenes/score.png"));
            imgLevel = ImageIO.read(this.getClass().getResource("pipoo/loderunner/imagenes/level.png"));
            imgTitulo = ImageIO.read(this.getClass().getResource("pipoo/loderunner/imagenes/titulo_lode_runner.png"));
            imgMenu = ImageIO.read(this.getClass().getResource("pipoo/loderunner/imagenes/men.png"));
            imgHighscore = ImageIO.read(this.getClass().getResource("pipoo/loderunner/imagenes/24138.png"));
            // (Carga de números omitida por brevedad, pero es igual)
            imgNum1 = ImageIO.read(this.getClass().getResource("pipoo/loderunner/imagenes/1.png"));

            // B. Cargar Héroe
            BufferedImage imgHeroeDer = ImageIO.read(this.getClass().getResource("pipoo/loderunner/imagenes/heroe_mirando_der.png"));
            BufferedImage imgHeroeIzq = ImageIO.read(this.getClass().getResource("pipoo/loderunner/imagenes/heroe_mirando_izq.png"));
            BufferedImage imgHeroeEscalera = ImageIO.read(this.getClass().getResource("pipoo/loderunner/imagenes/heroe_subiendo_escalera.png"));
            BufferedImage imgHeroeColgado = ImageIO.read(this.getClass().getResource("pipoo/loderunner/imagenes/heroe_colgado_barramanos.png"));

            heroe.setImagen(imgHeroeDer); // Imagen por defecto
            heroe.setImagenDer(imgHeroeDer);
            heroe.setImagenIzq(imgHeroeIzq);
            heroe.setImagenEscalera(imgHeroeEscalera);
            heroe.setImagenColgado(imgHeroeColgado);

            // Cargar animación de muerte (las 7 imágenes)
            for (int i = 1; i <= 7; i++) {
                BufferedImage frameMuerte = ImageIO.read(this.getClass().getResource("pipoo/loderunner/imagenes/heroe_muriendo_" + i + ".png"));
                heroe.setImagenMuriendo(i - 1, frameMuerte);
            }

            // C. Cargar Guardia
            BufferedImage imgGuardiaDer = ImageIO.read(this.getClass().getResource("pipoo/loderunner/imagenes/guardia_derecha_1.png"));
            BufferedImage imgGuardiaIzq = ImageIO.read(this.getClass().getResource("pipoo/loderunner/imagenes/guardia_izquierda_1.png"));
            BufferedImage imgGuardiaEscalera = ImageIO.read(this.getClass().getResource("pipoo/loderunner/imagenes/guardia_en_escalera.png"));
            BufferedImage imgGuardiaColgado = ImageIO.read(this.getClass().getResource("pipoo/loderunner/imagenes/guardia_izq_colgado.png"));
            BufferedImage imgGuardiaAtrapado = ImageIO.read(this.getClass().getResource("pipoo/loderunner/imagenes/guardia_atrapado_pozo.png"));

            for (Guardia guardia : guardias) {
                guardia.setImagen(imgGuardiaDer);
                guardia.setImgDer(imgGuardiaDer);
                guardia.setImgIzq(imgGuardiaIzq);
                guardia.setImgEscalera(imgGuardiaEscalera);
                guardia.setImgColgado(imgGuardiaColgado);
                guardia.setImgAtrapado(imgGuardiaAtrapado);
            }

            // D. Entorno (Carga de los bloques básicos)
            BufferedImage imgPlataforma = ImageIO.read(this.getClass().getResource("pipoo/loderunner/imagenes/plataforma.png"));
            BufferedImage imgBloque = ImageIO.read(this.getClass().getResource("pipoo/loderunner/imagenes/bloque.png"));
            BufferedImage imgOro = ImageIO.read(this.getClass().getResource("pipoo/loderunner/imagenes/oro.png"));
            BufferedImage imgEscaleraMediana = ImageIO.read(this.getClass().getResource("pipoo/loderunner/imagenes/escalera_mediana.png"));
            BufferedImage imgBarra = ImageIO.read(this.getClass().getResource("pipoo/loderunner/imagenes/barramanos.png"));
            BufferedImage imgPozo = ImageIO.read(this.getClass().getResource("pipoo/loderunner/imagenes/pozo.png"));

            // Asignación a las listas
            for (Plataforma p : plataformas) {
                p.setImagen(imgPlataforma);
            }
            for (Escalera e : escaleras) {
                e.setImagen(imgEscaleraMediana);
            }
            for (Lingote o : lingotes) {
                o.setImagen(imgOro);
            }
            for (BarraDeManos b : barras) {
                b.setImagen(imgBarra);
            }
            for (Pozo p : pozos) {
                p.setImagen(imgPozo);
            }

            // NOTA: Para usar plataforma2x1, plataforma3x1, escalera_corta, etc.,
            // debes cargarlas aquí con ImageIO.read() y luego asignárselas manualmente
            // a la plataforma específica que crees en el mapa.

        } catch (Exception e) {
            System.out.println("Error cargando imagenes. Verifica que los nombres sean exactos y terminen en .png: " + e.getMessage());
        }
    }


    @Override
    public void gameUpdate(double delta) {
        Keyboard teclado = this.getKeyboard();

        // movimiento heroe
        heroe.mover(delta);

        // cavar Pozos
        if (teclado.isKeyPressed(KeyEvent.VK_SPACE)) {
            heroe.cavar();
            double pozoX = heroe.x + heroe.width;
            double pozoY = heroe.y + heroe.height;
            pozos.add(new Pozo(pozoX, pozoY, 30, 30));
        }

        // actualización pozos
        Iterator<Pozo> iteradorPozos = pozos.iterator();
        while (iteradorPozos.hasNext()) {
            Pozo pozoActual = iteradorPozos.next();
            pozoActual.actualizar(delta);
            if (pozoActual.getEstado() == 2) {
                iteradorPozos.remove();
            }
        }

        // movimiento enemigos
        for (Guardia guardia : guardias) {
            guardia.mover(delta);
        }

        // MOVIMIENTO VERTICAL (solo si esta en una escalera)
        if (heroe.isEnEscalera()) {
            if (teclado.isKeyPressed(KeyEvent.VK_UP)) {
                heroe.setVelocidadY(-2); // subir
            } else if (teclado.isKeyPressed(KeyEvent.VK_DOWN)) {
                heroe.setVelocidadY(2);  // bajar
            } else {
                heroe.setVelocidadY(0);  // quieto colgando de la escalera
            }
        } else if (!heroe.isEstaCayendo()) {
            // si NO esta en escalera y NO esta cayendo (piso normal),
            heroe.setVelocidadY(0);
        }

        // MOVIMIENTO HORIZONTAL
        if (teclado.isKeyPressed(KeyEvent.VK_RIGHT)) {
            heroe.setVelocidadX(2);
        } else if (teclado.isKeyPressed(KeyEvent.VK_LEFT)) {
            heroe.setVelocidadX(-2);
        } else {
            heroe.setVelocidadX(0);
        }

        heroe.mover(delta);
        // colisiones
        detectarColisiones();

    }


    @Override
    public void gameDraw(Graphics2D g) {
        // 1. Dibujado de objetos del mapa que interactúan (Usa los dibujar() del padre)
        for (Lingote oro : lingotes) {
            oro.dibujar(g);
        }
        for (Pozo pozo : pozos) {
            pozo.dibujar(g);
        }
        for (Guardia guardia : guardias) {
            guardia.dibujar(g);
        }
        if (heroe != null) {
            heroe.dibujar(g);
        }

        // 2. Estampado de la Interfaz (HUD)
        if (imgTitulo != null) {
            g.drawImage(imgTitulo, 300, 10, null); // Titulo centrado arriba
        }
        if (imgScore != null) {
            g.drawImage(imgScore, 20, 20, null); // Score esquina izquierda
        }
        if (imgLevel != null) {
            g.drawImage(imgLevel, 650, 20, null); // Nivel esquina derecha
        }
        if (imgHighscore != null) {
            g.drawImage(imgHighscore, 120, 20, null); // Dibuja tu record numérico
        }
    }

    @Override
    public void gameShutdown() {
        System.out.println("Cerrando Lode Runner...");

        // 1. Recopilar los datos finales de la partida
        // (Asumiendo que tienes variables para el nombre, nivel actual y el puntaje total)
        // String nombreJugador = "Jugador1";
        // int puntajeFinal = heroe.getPuntaje();
        // int nivelAlcanzado = nivelActual;
        // String fechaActual = ... // (Usando java.util.Date o LocalDate)

        // 2. Crear la entrada del ranking (Basado en tu clase RankingEntry del UML)
        // RankingEntry nuevaEntrada = new RankingEntry(nivelAlcanzado, puntajeFinal, fechaActual, nombreJugador);

        // 3. Instanciar el Ranking, agregar la entrada y guardar en el archivo
        // Ranking sistemaRanking = new Ranking();
        // sistemaRanking.agregarEntrada(nuevaEntrada);
        // sistemaRanking.guardarRanking();

        // 4. Liberar recursos adicionales
        // Si implementaste música con MediaPlayer (como sugiere la DemoSonido),
        // aquí deberías detenerla:
        // if (mediaPlayer != null) {
        //     mediaPlayer.stop();
        // }
    }
    public static void main(String[] args) {
        // 1. Creamos la instancia de tu juego
        LodeRunner juego = new LodeRunner();

        // 2. Arrancamos el bucle del juego a 60 FPS (1.0 / 60.0)
        juego.run(1.0 / 60.0);

        // 3. Aseguramos que el proceso termine correctamente al cerrar la ventana
        System.exit(0);
    }

}


