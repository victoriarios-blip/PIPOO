package pipoo.core;
import java.awt.Shape;

public interface Colisionable {
    public Shape getLimites();

    // Comportamiento vacío por defecto (para el Fondo, etc.)
    void reaccionarAColision();
    void reaccionarAColision(ElementoGrafico otro);

    boolean colisionaCon(ElementoGrafico otro) // de Colisionable
    ;
}

