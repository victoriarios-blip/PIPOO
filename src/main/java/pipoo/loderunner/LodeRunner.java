package pipoo.loderunner;
import com.entropyinteractive.Keyboard;
import pipoo.core.Juego;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

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

        // 5. CARGA DE ASSETS (Imágenes)
        // ==========================================
        //try {
            // 1. Cargamos las imágenes a la memoria (Asegúrate de que la carpeta
            // "imagenes" exista en el lugar correcto de tu proyecto)
            //BufferedImage imgHeroe = ImageIO.read(this.getClass().getResource("imagenes/heroe.png"));
            //BufferedImage imgGuardia = ImageIO.read(this.getClass().getResource("imagenes/guardia.png"));
            //BufferedImage imgOro = ImageIO.read(this.getClass().getResource("imagenes/oro.png"));
            //BufferedImage imgPlataforma = ImageIO.read(this.getClass().getResource("imagenes/ladrillo.png"));
            //BufferedImage imgEscalera = ImageIO.read(this.getClass().getResource("imagenes/escalera.png"));
            //BufferedImage imgBarra = ImageIO.read(this.getClass().getResource("imagenes/barra.png"));

            // 2. Le asignamos la imagen al héroe
            //heroe.setImagen(imgHeroe);

            // 3. Usamos bucles (for) para asignarle la misma imagen a todos
            // los elementos de las listas que comparten gráficos
            //for (Guardia guardia : guardias) {guardia.setImagen(imgGuardia);}

            //for (Lingote oro : lingotes) { oro.setImagen(imgOro);}

            //for (Plataforma plataforma : plataformas) {plataforma.setImagen(imgPlataforma);}

            //for (Escalera escalera : escaleras) {escalera.setImagen(imgEscalera);}

            //for (BarraDeManos barra : barras) {barra.setImagen(imgBarra);}

        //} catch (Exception e) {System.out.println("Error cargando los assets: " + e.getMessage());}
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
        // oro
        for (Lingote oro : lingotes) {
            oro.dibujar(g);
        }
        // pozos
        for (Pozo pozo : pozos) {
            pozo.dibujar(g);
        }
        // enemigos
        for (Guardia guardia : guardias) {
            guardia.dibujar(g);
        }
        //  heroe
        if (heroe != null) {
            heroe.dibujar(g);
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
}


