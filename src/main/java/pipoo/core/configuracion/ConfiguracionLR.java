package pipoo.core.configuracion;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfiguracionLR extends Configuracion {
    private String skinPersonaje;
    private int teclaEfectos, teclaMusica, teclaCavar, teclaEnter;

    public ConfiguracionLR() {
        super("jgame.properties");
    }

    @Override
    public void reset() {
        this.pantallaCompleta = false;
        this.sonidoActivado = true;
        this.skinPersonaje = "Original";
        this.teclaEfectos = 81;  // Q
        this.teclaMusica = 87;   // W
        this.teclaCavar = 32;    // Barra Espaciadora
        this.teclaEnter = 10;    // Enter
    }

    @Override public void guardar() {
        Properties prop = new Properties();
        // guardar parametros de la clase abstracta (comunes)
        prop.setProperty("fullScreen", String.valueOf(this.pantallaCompleta));
        prop.setProperty("sonidoActivado", String.valueOf(this.sonidoActivado));
        prop.setProperty("pistaMusical", this.pistaMusical);

        // guardar parametros específicos de Lode Runner
        prop.setProperty("skinPersonaje", this.skinPersonaje);
        prop.setProperty("teclaEfectos", String.valueOf(this.teclaEfectos)); //que es esto?
        prop.setProperty("teclaMusica", String.valueOf(this.teclaMusica));
        prop.setProperty("teclaCavar", String.valueOf(this.teclaCavar));
        prop.setProperty("teclaEnter", String.valueOf(this.teclaEnter));
        persistirEnArchivo(prop, "config_lode_runner.properties");

        try (FileOutputStream out = new FileOutputStream("jgame.properties")) {
            prop.store(out, "Configuracion PIPOO");
            System.out.println("DEBUG: Configuración guardada.");
        } catch (IOException e) {
            System.err.println("Error al guardar: " + e.getMessage());
        }

    }

    public void setSkinPersonaje(String skin) { this.skinPersonaje = skin; }
    public String getSkinPersonaje() { return skinPersonaje; }

}
