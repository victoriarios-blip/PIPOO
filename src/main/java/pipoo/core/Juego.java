package pipoo.core;

import com.entropyinteractive.JGame;
import pipoo.core.configuracion.Configuracion;

import java.awt.*;

public abstract class Juego extends JGame {

    protected GestorAudio gestorAudio= new GestorAudio();
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
        // PRIORIDAD ALTA: El archivo físico en la raíz (donde el Panel guarda)
        java.io.File fileRaiz = new java.io.File("jgame.properties");

        if (fileRaiz.exists()) {
            try (java.io.FileInputStream fis = new java.io.FileInputStream(fileRaiz)) {
                this.appProperties.load(fis);
                System.out.println("DEBUG: Cambios del usuario cargados desde la raiz ");
                return;
            } catch (Exception e) {
                System.err.println("Error al leer archivo de raíz: " + e.getMessage());
            }
        }

        // PRIORIDAD BAJA: El Classpath (Solo si no hay cambios del usuario en la raíz)
        try (java.io.InputStream input = getClass().getClassLoader().getResourceAsStream("jgame.properties")) {
            if (input != null) {
                this.appProperties.load(input);
                System.out.println("DEBUG: Cargada configuración por defecto del Classpath.");
            } else {
                System.err.println("ADVERTENCIA: No se encontró jgame.properties en ninguna ubicacion.");
            }
        } catch (Exception e) {
            System.err.println("Error al leer recursos: " + e.getMessage());
        }
    }


}
