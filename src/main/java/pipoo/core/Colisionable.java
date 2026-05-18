package pipoo.core;
import java.awt.Shape;

public interface Colisionable {
    public Shape getLimites();

    void reaccionarAColision();

    //boolean colisinaCon(Colisionable otro);
    //void reaccionarAColision(Colisionable otro);
}

