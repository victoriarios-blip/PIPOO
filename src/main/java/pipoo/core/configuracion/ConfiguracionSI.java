package pipoo.core.configuracion;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfiguracionSI extends Configuracion {
    private String skinModo; // "Original", "Color", "Halloween"
    private String velocidadInvasores; // "Lenta", "Media", "Rápida"
    private int teclaIzq, teclaDer, teclaDisparo;
    private String nombreJ1;

    public ConfiguracionSI() {
        super("jgame.properties");
        this.leer();
    }

    @Override
    public void reset() {
        this.pantallaCompleta = false;
        this.sonidoActivado = true;
        this.pistaMusical = "Tema 1 (Original)";
        this.skinModo = "Original";
        this.velocidadInvasores = "Media";
        this.nombreJ1 = "Invitado";

        // Teclas por defecto
        this.teclaIzq = 37;      // Flecha Izquierda
        this.teclaDer = 39;      // Flecha Derecha
        this.teclaDisparo = 32;  // Barra Espaciadora
    }

    @Override
    public void guardar() {
        Properties prop = new Properties();

        // Primero intentamos leer lo que ya existe de Pong para no borrárselo
        try (InputStream in = Files.newInputStream(Paths.get("jgame.properties"))) {
            prop.load(in);
        } catch (IOException e) {
            // Si no existe el archivo todavía, no pasa nada, se crea de cero
        }

        // Guardamos los parámetros específicos de Space Invaders usando prefijo "si_"
        prop.setProperty("si_fullScreen", String.valueOf(this.isPantallaCompleta()));
        prop.setProperty("si_sonidoActivado", String.valueOf(this.isSonidoActivado()));
        prop.setProperty("si_pistaMusical", this.pistaMusical);
        prop.setProperty("si_skinModo", this.skinModo);
        prop.setProperty("si_velocidadInvasores", this.velocidadInvasores);
        prop.setProperty("si_nombreJ1", this.nombreJ1);
        prop.setProperty("si_teclaIzq", String.valueOf(this.teclaIzq));
        prop.setProperty("si_teclaDer", String.valueOf(this.teclaDer));
        prop.setProperty("si_teclaDisparo", String.valueOf(this.teclaDisparo));

        persistirEnArchivo(prop, "jgame.properties");
    }

    public void leer() {
        // Cargamos el archivo de disco a la memoria de propiedades
        try (InputStream in = Files.newInputStream(Paths.get("jgame.properties"))) {
            propiedades.load(in);
        } catch (IOException e) {
            System.out.println("No se encontró jgame.properties, usando valores por defecto para Space Invaders.");
        }

        // Leemos usando el prefijo si_ y asignando un valor fallback por defecto
        this.sonidoActivado = Boolean.parseBoolean(propiedades.getProperty("si_soundActivado", "true"));
        this.pantallaCompleta = Boolean.parseBoolean(propiedades.getProperty("si_fullScreen", "false"));
        this.pistaMusical = propiedades.getProperty("si_pistaMusical", "Tema 1 (Original)");
        this.skinModo = propiedades.getProperty("si_skinModo", "Original");
        this.velocidadInvasores = propiedades.getProperty("si_velocidadInvasores", "Media");
        this.nombreJ1 = propiedades.getProperty("si_nombreJ1", "Invitado");

        this.teclaIzq = Integer.parseInt(propiedades.getProperty("si_teclaIzq", "37"));
        this.teclaDer = Integer.parseInt(propiedades.getProperty("si_teclaDer", "39"));
        this.teclaDisparo = Integer.parseInt(propiedades.getProperty("si_teclaDisparo", "32"));
    }

    // Getters y Setters necesarios para la UI y tu juego
    public String getSkinModo() { return skinModo; }
    public void setSkinModo(String skinModo) { this.skinModo = skinModo; }

    public String getVelocidadInvasores() { return velocidadInvasores; }
    public void setVelocidadInvasores(String velocidad) { this.velocidadInvasores = velocidad; }

    public String getNombreJ1() { return nombreJ1; }
    public void setNombreJ1(String nombreJ1) { this.nombreJ1 = nombreJ1; }
}
