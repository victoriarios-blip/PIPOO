package pipoo.loderunner;
import com.entropyinteractive.Keyboard;
import pipoo.core.Juego;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

public class Loderunner extends Juego {
    private ArrayList<Pozo> pozos;

    public Loderunner(String titulo, int ancho, int alto) {
        super(titulo, ancho, alto);
    }

    @Override
    protected void actualizarPuntaje() {

    }

    @Override
    protected void detectarColisiones() {

    }

    @Override
    public void gameStartup() {
        // Inicializar la lista vacía al arrancar el juego
        pozos = new ArrayList<>();

        // ... cargar resto de cosas (héroe, plataformas, etc.)
    }

    @Override
    public void gameUpdate(double delta) {
        Keyboard teclado = this.getKeyboard();
        Iterator<Pozo> iterador = pozos.iterator();

        // Si presiona espacio, calculas las coordenadas X e Y al lado del héroe
        if (teclado.isKeyPressed(KeyEvent.VK_SPACE)) {
            // Lógica para determinar el x e y donde cavar (a la derecha o izquierda del héroe)
            double pozoX = heroe.getX() + unDesplazamiento;
            double pozoY = heroe.getY() + unDesplazamiento;

            // Creas el pozo y lo guardas en la lista
            pozos.add(new Pozo(pozoX, pozoY, ancho, alto));
        }
        // Actualizas la posición de tu héroe, los enemigos, etc...

        while (iterador.hasNext()) {
            Pozo pozoActual = iterador.next();

            // Le pasamos el delta para que descuente el tiempo
            pozoActual.actualizar(delta);

            // Si el estado es 2 (completamente cerrado), lo removemos de la lista
            if (pozoActual.getEstado() == 2) {
                iterador.remove();
            }
        }
        // ... resto de las actualizaciones (héroe, guardias, colisiones) ...

    }

    @Override
    public void gameDraw(Graphics2D g) {
        // ... dibujar plataformas, fondo ...

        // Dibujar cada pozo activo en la lista
        for (Pozo pozo : pozos) {
            pozo.dibujar(g);
        }

        // ... dibujar héroe, enemigos ...
    }
}


