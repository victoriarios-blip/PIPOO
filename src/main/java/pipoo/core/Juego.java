package pipoo.core;

import com.entropyinteractive.JGame;
import pipoo.core.configuracion.Configuracion;

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

    @Override
    protected void readPropertiesFile() {
        System.out.println("DEBUG: Usando buscador de configuración personalizado en Juego.java");
        try (java.io.InputStream input = getClass().getClassLoader().getResourceAsStream("jgame.properties")) {

            if (input != null) {
                this.appProperties.load(input);
                System.out.println("DEBUG: jgame.properties cargado con éxito desde el Classpath.");
            } else {
                java.io.File fileRaiz = new java.io.File("jgame.properties");
                if (fileRaiz.exists()) {
                    try (java.io.FileInputStream fis = new java.io.FileInputStream(fileRaiz)) {
                        this.appProperties.load(fis);
                        System.out.println("DEBUG: jgame.properties cargado desde la raíz del proyecto.");
                    }
                } else {
                    System.err.println("ADVERTENCIA: No se encontró jgame.properties en ninguna ubicación.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error al leer propiedades: " + e.getMessage());
        }
    }


}
