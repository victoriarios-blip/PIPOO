package pipoo.spaceinvaders;

import pipoo.core.Juego;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import pipoo.spaceinvaders.assets;

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
            BufferedImage naveHeroe = ImageIO.read(this.getClass().getResource("assets/naveHeroe.png"));
            BufferedImage naveNodriza = ImageIO.read(this.getClass().getResource("assets/naveNodriza.png"));
            BufferedImage escudoIntacto = ImageIO.read(this.getClass().getResource("assets/escudoIntacto.png"));

            // enemigos (2 frames cada uno)
            BufferedImage pulpo1 = ImageIO.read(this.getClass().getResource("assets/pulpo1.png"));
            BufferedImage pulpo2 = ImageIO.read(this.getClass().getResource("assets/pulpo2.png"));

            BufferedImage cangrejo1 = ImageIO.read(this.getClass().getResource("assets/cangrejo1.png"));
            BufferedImage cangrejo2 = ImageIO.read(this.getClass().getResource("assets/cangrejo2.png"));

            BufferedImage calamar1 = ImageIO.read(this.getClass().getResource("assets/calamar1.png"));
            BufferedImage calamar2 = ImageIO.read(this.getClass().getResource("assets/calamar2.png"));

            BufferedImage proyectilHeroe = ImageIO.read(this.getClass().getResource("assets/proyectilHeroe.png"));
            BufferedImage proyectilEnemigo = ImageIO.read(this.getClass().getResource("assets/proyectilEnemigo.png"));


            jugador.setImagen(naveHeroe);
            if (ufo != null) ufo.setImagen(imgUFO); // Si instanciaron la Nave Nodriza

            for (Escudo escudo : escudos) {
                escudo.setImagen(imgEscudo);
            }

            // 3. Asignación de ambos frames a los enemigos usando el nuevo método
            for (Enemigo e : oleada) {
                if (e instanceof Pulpo) {
                    e.setImagenes(imgPulpo1, imgPulpo2);
                } else if (e instanceof Cangrejo) {
                    e.setImagenes(imgCangrejo1, imgCangrejo2);
                } else if (e instanceof Calamar) {
                    e.setImagenes(imgCalamar1, imgCalamar2);
                }
            }

        } catch (Exception e) {
            System.out.println("Error cargando los assets: " + e.getMessage());
        }
    }

    @Override
    public void gameUpdate(double delta) {
        jugador.mover(delta);
        for (Enemigo enemigo : oleada) {
            enemigo.mover(delta);
            Proyectil p = enemigo.disparar();
            if (p != null) {
                proyectiles.add(p);}}
        for (Proyectil p : proyectiles) {
            p.mover(delta);
        }
        detectarColisiones();
        actualizarPuntaje();
    }

    void limpiarNoVisibles(){
        proyectiles.removeIf(p -> !p.isVisible());
        oleada.removeIf(e -> !e.isVisible());
    }

    @Override
    public void gameDraw(Graphics2D g) {
        // Limpiar pantalla
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Iterar sobre tus listas e invocar objeto.dibujar(g);
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

