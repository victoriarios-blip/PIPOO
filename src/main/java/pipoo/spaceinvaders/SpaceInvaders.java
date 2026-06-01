package pipoo.spaceinvaders;

import pipoo.core.Juego;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
javax.imageio.ImageIO;

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
            // cargamos las imágenes a la memoria usando getResource para rutas relativas
            BufferedImage imgNave = ImageIO.read(this.getClass().getResource("imagenes/nave.png"));
            BufferedImage imgPulpo = ImageIO.read(this.getClass().getResource("imagenes/pulpo.png"));
            BufferedImage imgCangrejo = ImageIO.read(this.getClass().getResource("imagenes/cangrejo.png"));
            BufferedImage imgCalamar = ImageIO.read(this.getClass().getResource("imagenes/calamar.png"));
            BufferedImage imgEscudo = ImageIO.read(this.getClass().getResource("imagenes/escudo.png"));

            // le asignamos la imagen a la nave heroe
            jugador.setImagen(imgNave);

            // usamos bucles (for) para asignarle la misma imagen a todos
            // los elementos de las listas que comparten gráficos
            for (Enemigo e : oleada) {
                if (e instanceof Pulpo) {
                    e.setImagen(imgPulpo);
                } else if (e instanceof Cangrejo) {
                    e.setImagen(imgCangrejo);
                } else if (e instanceof Calamar) {
                    e.setImagen(imgCalamar);
                }
            }

            for (Escudo escudo : escudos) {
                escudo.setImagen(imgEscudo);
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

