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

    public SpaceInvaders() {
        super("Retro Space Invaders", 800, 600);
    }

    @Override
    public void gameStartup() {
        System.out.println("Iniciando Space Invaders...");

        oleada = new ArrayList<>();
        escudos = new ArrayList<>();
        proyectilesEnemigos = new ArrayList<>();
        proyectilesHeroe = new ArrayList<>();

        jugador = new NaveHeroe(380, 520);

        int[] posicionesEscudosX = {100, 280, 460, 640};
        for (int x : posicionesEscudosX) {
            escudos.add(new Escudo(x, 450));
        }

        // oleada enemiga (5 filas x 11 columnas)
        int inicioX = 150;
        int inicioY = 50;

        for (int fila = 0; fila < 5; fila++) {
            for (int col = 0; col < 11; col++) {
                double x = inicioX + (col * 40);
                double y = inicioY + (fila * 40);

                if (fila == 0) oleada.add(new Pulpo(x, y));         // superior 30 pts
                else if (fila < 3) oleada.add(new Cangrejo(x, y));  // medio 20 pts
                else oleada.add(new Calamar(x, y));                 // inferior 10 pts
            }
        }

        // carga de assets
        try {
            BufferedImage naveHeroe = ImageIO.read(this.getClass().getResource("/pipoo/spaceinvaders/imagenes/naveHeroe.png"));
            BufferedImage naveNodriza = ImageIO.read(this.getClass().getResource("/pipoo/spaceinvaders/imagenes/naveNodriza.png"));

            // enemigos (2 frames cada uno)
            BufferedImage pulpo1 = ImageIO.read(this.getClass().getResource("/pipoo/spaceinvaders/imagenes/pulpo1.png"));
            BufferedImage pulpo2 = ImageIO.read(this.getClass().getResource("/pipoo/spaceinvaders/imagenes/pulpo2.png"));

            BufferedImage cangrejo1 = ImageIO.read(this.getClass().getResource("/pipoo/spaceinvaders/imagenes/cangrejo1.png"));
            BufferedImage cangrejo2 = ImageIO.read(this.getClass().getResource("/pipoo/spaceinvaders/imagenes/cangrejo2.png"));

            BufferedImage calamar1 = ImageIO.read(this.getClass().getResource("/pipoo/spaceinvaders/imagenes/calamar1.png"));
            BufferedImage calamar2 = ImageIO.read(this.getClass().getResource("/pipoo/spaceinvaders/imagenes/calamar2.png"));

            BufferedImage proyectilHeroe = ImageIO.read(this.getClass().getResource("/pipoo/spaceinvaders/imagenes/proyectilHeroe.png"));
            BufferedImage proyectilEnemigo = ImageIO.read(this.getClass().getResource("/pipoo/core/spaceinvaders/imagenes/proyectilEnemigo.png"));

            BufferedImage escudoIntacto = ImageIO.read(this.getClass().getResource("/pipoo/spaceinvaders/imagenes/escudoIntacto.png"));
            BufferedImage escudo1daño = ImageIO.read(this.getClass().getResource("/pipoo/spaceinvaders/imagenes/escudo1daño.png"));
            BufferedImage escudo2daño = ImageIO.read(this.getClass().getResource("/pipoo/spaceinvaders/imagenes/escudo2daño.png"));
            BufferedImage escudo3daño = ImageIO.read(this.getClass().getResource("/pipoo/spaceinvaders/imagenes/escudo3daño.png"));
            BufferedImage escudo4daño = ImageIO.read(this.getClass().getResource("/pipoo/spaceinvaders/imagenes/escudo4daño.png"));

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

        } catch (Exception e) {
            System.out.println("Error cargando los assets: " + e.getMessage());
        }
    }

    @Override
    public void gameUpdate(double delta) {
        jugador.mover(delta);

        Keyboard teclado = this.getKeyboard();

        if (teclado.isKeyPressed(KeyEvent.VK_LEFT))  jugador.moverIzquierda();
        else if (teclado.isKeyPressed(KeyEvent.VK_RIGHT)) jugador.moverDerecha();
        else jugador.detener();

        if (teclado.isKeyPressed(KeyEvent.VK_SPACE)) {
            Proyectil p = jugador.disparar();
            if (p != null) proyectilesHeroe.add(p);
        }

        for (Enemigo enemigo : oleada) {
            enemigo.mover(delta);
            enemigo.actualizarFrame(delta);
            Proyectil p = enemigo.disparar();
            if (p != null) {
                proyectilesEnemigos.add(p); // ← lista correcta
            }
        }
        for (Proyectil p : proyectilesEnemigos) { p.mover(delta); }
        for (Proyectil p : proyectilesHeroe)    { p.mover(delta); }

        detectarColisiones();
        actualizarPuntaje();
        limpiarNoVisibles();
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
    }

    @Override
    public void gameShutdown() {
        // Guardar puntajes
    }

    @Override
    protected void detectarColisiones() {
        // Usar los métodos getLimites() de la interfaz Colisionable
    }

    @Override
    protected void actualizarPuntaje() {
        // Actualizar UI del puntaje actual
    }
}

