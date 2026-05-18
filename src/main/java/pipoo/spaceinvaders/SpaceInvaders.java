package pipoo.spaceinvaders;

import pipoo.core.Juego;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.List;

public class SpaceInvaders extends Juego {
    private NaveHeroe jugador;
    private List<Enemigo> oleada;
    private List<Escudo> escudos;
    private NaveNodriza enemigoFinal;

    public SpaceInvaders() {
        super("Retro Space Invaders", 800, 600);
    }

    @Override
    public void gameStartup() {
        // 1. Instanciar al jugador en la parte inferior
        // 2. Instanciar los 4 escudos
        // 3. Crear los ciclos for para llenar la lista de Enemigos
    }

    @Override
    public void gameUpdate(double delta) {
        // Leer teclado para mover a NaveHeroe
        // Bucle para mover los proyectiles y a los enemigos
        detectarColisiones();
        actualizarPuntaje();
        // Verificar condición de Game Over
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
}
