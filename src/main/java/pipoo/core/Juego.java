package pipoo.core;

import com.entropyinteractive.JGame;
import pipoo.core.configuracion.Configuracion;
import java.util.Properties;
import java.awt.*;

public abstract class Juego extends JGame {
    protected int dificultad;
    protected Configuracion configuracion;


    public Juego(String titulo, int ancho, int alto) {
        super(titulo, ancho, alto);
}

    protected abstract void actualizarPuntaje();
    protected abstract void detectarColisiones();

    @Override
    public void gameStartup() {}

    @Override
    public void gameUpdate(double v) {}

    @Override
    public void gameDraw(Graphics2D graphics2D) {}

    @Override
    public void gameShutdown(){}

    public void setConfiguracion(Configuracion config) {
        this.configuracion = config;
    }

    public Configuracion getConfiguracion() {
        return configuracion;
    }

    public void aplicarModoPantalla(boolean pc) {
        this.appProperties.setProperty("fullscreen", pc ? "true" : "false");
    }
}
