package pipoo.loderunner;
import java.awt.Graphics2D;
import java.util.Random;

public class Guardia extends Runner {
    private Heroe heroe;
    private boolean tieneOro; 
    private Random random;


    public Guardia(double x, double y, int ancho, int alto) {
        super(x, y, ancho, alto);
        this.heroe = heroe;
        this.tieneOro = false;
        this.random = new Random();
    }

    public void perseguir() {
        if (heroe != null) {
            // 1. Lógica de persecución básica (buscar la coordenada X del héroe)
            if (this.x < heroe.y) {
                this.velocidadX = 2; // Moverse a la derecha
            } else if (this.x > heroe.x) {
                this.velocidadX = -2; // Moverse a la izquierda
            } else {
                this.velocidadX = 0; // Está alineado
            }

            // 2. Movimiento ilógico (Regla: "A veces se mueven de forma aparentemente ilógica")
            // Hay un 5% de probabilidades en cada actualización de que el guardia vaya al revés
            if (random.nextInt(100) < 5) {
                this.velocidadX = this.velocidadX * -1;
            }
        }
    }

    public void reaparecer() {
        // Regla: "reaparece en la parte superior del nivel en una posición aleatoria"
        this.y = 0; // Parte superior de la pantalla

        // Asumiendo que tu ventana tiene 800px de ancho (ajusta este valor si es distinto)
        this.x = random.nextInt((int) (800 - this.width));

        // Al reaparecer en el aire, obligatoriamente empieza a caer
        this.estaCayendo = true;
    }

    // Polimorfismo: Redefinición del método abstracto heredado de Movible
    @Override
    public void mover(double delta) {
        // Regla: "Durante la caída libre no se pueden desplazar horizontalmente"
        if (estaCayendo) {
            this.velocidadX = 0;
            this.velocidadY = 5; // Velocidad de caída hacia abajo
        } else {
            this.velocidadY = 0;
            perseguir(); // Solo persigue si está pisando suelo firme o una escalera
        }

        // Aplicamos la velocidad final a las coordenadas heredadas de ElementoGrafico
        this.x += this.velocidadX;
        this.y += this.velocidadY;
    }

    // Métodos Getter y Setter para controlar si el guardia agarró o soltó oro
    public boolean tieneOro() {
        return tieneOro;
    }

    public void setTieneOro(boolean tieneOro) {
        this.tieneOro = tieneOro;
    }

    @Override
    public void dibujar(Graphics2D g) {
        // dibujar al guardia.
    }
}




