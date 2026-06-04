package pipoo.spaceinvaders;

import com.entropyinteractive.Keyboard;
import pipoo.core.*;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;

public class SpaceInvaders extends Juego {
    private NaveHeroe jugador;
    private List<Enemigo> oleada;
    private List<Escudo> escudos;
    private NaveNodriza enemigoFinal;
    private List<Proyectil> proyectilesEnemigos;
    private List<Proyectil> proyectilesHeroe;

    private double tiempoNodriza = 0;
    private static final double intervaloNodriza = 20; // aparece cada 20 segundos

    // puntaje
    private int puntaje = 0;

    private BufferedImage imgProyectilHeroe;
    private BufferedImage imgProyectilEnemigo;
    private BufferedImage imgNaveNodriza;

    public SpaceInvaders() {
        super("Retro Space Invaders", 800, 600);
    }

    @Override
    public void gameStartup() {
        java.net.URL test = this.getClass().getResource("/pipoo/spaceinvaders/imagenes/pulpo1.png");
        System.out.println("PATH TEST: " + test);

        System.out.println("Iniciando Space Invaders...");

        oleada = new ArrayList<>();
        escudos = new ArrayList<>();
        proyectilesEnemigos = new ArrayList<>();
        proyectilesHeroe = new ArrayList<>();

        jugador = new NaveHeroe(380, 540);

        int[] posicionesEscudosX = {104, 272, 440, 608};
        for (int x : posicionesEscudosX) {
            escudos.add(new Escudo(x, 450));
        }

        // oleada enemiga (5 filas x 11 columnas)
        int inicioX = (800 - 638) / 2;
        int inicioY = 50;

        for (int fila = 0; fila < 5; fila++) {
            for (int col = 0; col < 11; col++) {
                double x = inicioX + (col * 58); // 48px sprite + 10px separación
                double y = inicioY + (fila * 34); // 24px sprite + 10px separación
                // ← acá falta agregar el enemigo!
                if (fila == 0) oleada.add(new Pulpo(x, y));
                else if (fila < 3) oleada.add(new Cangrejo(x, y));
                else oleada.add(new Calamar(x, y));
            }
        }

        // carga de assets
        try {
            BufferedImage naveHeroe = ImageIO.read(this.getClass().getResource("imagenes/naveHeroeIntacta.png"));
            imgNaveNodriza = ImageIO.read(this.getClass().getResource("imagenes/naveNodriza.png"));

            // enemigos (2 frames cada uno)
            BufferedImage pulpo1 = ImageIO.read(this.getClass().getResource("imagenes/pulpo1.png"));
            BufferedImage pulpo2 = ImageIO.read(this.getClass().getResource("imagenes/pulpo2.png"));

            BufferedImage cangrejo1 = ImageIO.read(this.getClass().getResource("imagenes/cangrejo1.png"));
            BufferedImage cangrejo2 = ImageIO.read(this.getClass().getResource("imagenes/cangrejo2.png"));

            BufferedImage calamar1 = ImageIO.read(this.getClass().getResource("imagenes/calamar1.png"));
            BufferedImage calamar2 = ImageIO.read(this.getClass().getResource("imagenes/calamar2.png"));

            BufferedImage proyectilHeroe = ImageIO.read(this.getClass().getResource("imagenes/proyectilHeroe.png"));
            BufferedImage proyectilEnemigo = ImageIO.read(this.getClass().getResource("imagenes/proyectilEnemigo.png"));

            BufferedImage escudoIntacto = ImageIO.read(this.getClass().getResource("imagenes/escudoIntacto.png"));
            BufferedImage escudo1daño = ImageIO.read(this.getClass().getResource("imagenes/escudo1daño.png"));
            BufferedImage escudo2daño = ImageIO.read(this.getClass().getResource("imagenes/escudo2daño.png"));
            BufferedImage escudo3daño = ImageIO.read(this.getClass().getResource("imagenes/escudo3daño.png"));
            BufferedImage escudo4daño = ImageIO.read(this.getClass().getResource("imagenes/escudo4daño.png"));

            imgProyectilHeroe   = ImageIO.read(this.getClass().getResource("imagenes/proyectilHeroe.png"));
            imgProyectilEnemigo = ImageIO.read(this.getClass().getResource("imagenes/proyectilEnemigo.png"));

            System.out.println("pulpo1 tamaño: " + pulpo1.getWidth() + "x" + pulpo1.getHeight());
            System.out.println("cangrejo1 tamaño: " + cangrejo1.getWidth() + "x" + cangrejo1.getHeight());
            System.out.println("calamar1 tamaño: " + calamar1.getWidth() + "x" + calamar1.getHeight());
            System.out.println("naveHeroe tamaño: " + naveHeroe.getWidth() + "x" + naveHeroe.getHeight());
            System.out.println("escudo tamaño: " + escudoIntacto.getWidth() + "x" + escudoIntacto.getHeight());

            for (Escudo escudo : escudos) {
                escudo.setImgIntacto(escudoIntacto);
                escudo.setEscudo1daño(escudo1daño);
                escudo.setEscudo2daño(escudo2daño);
                escudo.setEscudo3daño(escudo3daño);
                escudo.setEscudo4daño(escudo4daño);
            }

            jugador.setImagen(naveHeroe);
            //if (naveNodriza != null) naveNodriza.setImagen(naveNodriza); // Si instanciaron la Nave Nodriza

            for (Escudo escudo : escudos) {
                escudo.setImagen(escudoIntacto);
            }

            // 3. Asignación de ambos frames a los enemigos usando el nuevo método
            for (Enemigo e : oleada) {
                if (e instanceof Pulpo) {
                    e.setImagenes(pulpo1, pulpo2);
                } else if (e instanceof Cangrejo) {
                    e.setImagenes(cangrejo1, cangrejo2);
                } else if (e instanceof Calamar) {
                    e.setImagenes(calamar1, calamar2);
                }
            }

            for (Enemigo e : oleada) {
                e.setVelocidadX(40);
            }


        } catch (Exception e) {
            System.out.println("Error cargando los assets: " + e.getMessage());
        }
    }

    @Override
    public void gameUpdate(double delta) {
        Keyboard teclado = this.getKeyboard();

        if (teclado.isKeyPressed(KeyEvent.VK_LEFT))  jugador.moverIzquierda();
        else if (teclado.isKeyPressed(KeyEvent.VK_RIGHT)) jugador.moverDerecha();
        else jugador.detener();

        if (teclado.isKeyPressed(KeyEvent.VK_SPACE)) {
            Proyectil p = jugador.disparar();
            if (p != null) {
                p.setImagen(imgProyectilHeroe);
                proyectilesHeroe.add(p);
            }
        }

        jugador.mover(delta);

        // 1. Mover todos
        for (Enemigo enemigo : oleada) {
            enemigo.mover(delta);
            enemigo.actualizarFrame(delta);
        }

// 2. Detectar borde UNA sola vez
        boolean tocoBorde = false;
        for (Enemigo e : oleada) {
            if (e.x <= 0 || e.x + e.width >= 800) {
                tocoBorde = true;
                break;
            }
        }

// 3. Si tocó el borde, bajar toda la formación
        if (tocoBorde) {
            for (Enemigo e : oleada) {
                e.bajarFila(20);
            }
        }

// 4. Disparo enemigos
        for (Enemigo enemigo : oleada) {
            Proyectil p = enemigo.disparar();
            if (p != null) {
                p.setImagen(imgProyectilEnemigo);
                proyectilesEnemigos.add(p);
            }
        }

        for (Proyectil p : proyectilesEnemigos) { p.mover(delta); }
        for (Proyectil p : proyectilesHeroe)    { p.mover(delta); }

        detectarColisiones();
        actualizarPuntaje();
        limpiarNoVisibles();

        // game over
        if (!jugador.isVisible()) {
            if (jugador.getVidas() <= 0) {
                System.out.println("GAME OVER");
                this.stop();
            } else {
                // reaparecer
                jugador.setX(380);
                jugador.setY(540);
                jugador.setVisible(true);
                jugador.resetImagen();
            }
        }

        for (Enemigo e : oleada) {
            if (e.y + e.height >= 500) {
                System.out.println("GAME OVER - enemigos llegaron");
                this.stop();
                break;
            }
        }

        // win
        if (oleada.isEmpty()) {
            System.out.println("GANASTE");
            this.stop();
        }

        // timer nave nodriza
        tiempoNodriza += delta;
        if (tiempoNodriza >= intervaloNodriza) {
            enemigoFinal = new NaveNodriza(-60, 30); // entra desde la izquierda
            enemigoFinal.setImagen(imgNaveNodriza);
            tiempoNodriza = 0;
        }
        if (enemigoFinal != null && enemigoFinal.isVisible()) {
            enemigoFinal.mover(delta);
        }
    }

    void limpiarNoVisibles(){
        proyectilesEnemigos.removeIf(p -> !p.isVisible());
        proyectilesHeroe.removeIf(p -> !p.isVisible());
        oleada.removeIf(e -> !e.isVisible());
    }

    @Override
    public void gameDraw(Graphics2D g) {
        // Limpiar pantalla
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        // Enemigos
        for (Enemigo e : oleada) {
            e.dibujar(g);
        }
        // Escudos
        for (Escudo escudo : escudos) {
            escudo.dibujar(g);
        }
        // Proyectiles
        for (Proyectil p : proyectilesHeroe) {
            p.dibujar(g);
        }
        for (Proyectil p : proyectilesEnemigos) {
            p.dibujar(g);
        }
        // Jugador
        jugador.dibujar(g);

        // nave nodriza
        if (enemigoFinal != null && enemigoFinal.isVisible()) {
            enemigoFinal.dibujar(g);
        }
    }

    @Override
    public void gameShutdown() {
        // Guardar puntajes
    }

    @Override
    protected void detectarColisiones() {
        // 1. Proyectiles del héroe vs enemigos
        for (Proyectil proyectil : proyectilesHeroe) {
            if (!proyectil.isVisible()) continue;
            for (Enemigo enemigo : oleada) {
                if (!enemigo.isVisible()) continue;
                if (proyectil.colisionaCon(enemigo)) {
                    proyectil.reaccionarAColision(enemigo);
                    enemigo.reaccionarAColision(proyectil);
                    puntaje += enemigo.getValorPuntaje();
                }
            }
        }

        // 2. Proyectiles del héroe vs escudos
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

        // 3. Proyectiles enemigos vs jugador
        for (Proyectil proyectil : proyectilesEnemigos) {
            if (!proyectil.isVisible()) continue;
            if (proyectil.colisionaCon(jugador)) {
                proyectil.reaccionarAColision(jugador);
                jugador.reaccionarAColision(proyectil);
            }
        }

        // 4. Proyectiles enemigos vs escudos
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

        // 5. Proyectiles entre sí
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

        // 6. Proyectil héroe vs NaveNodriza
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
    protected void actualizarPuntaje() {
        // Actualizar UI del puntaje actual
    }
}

