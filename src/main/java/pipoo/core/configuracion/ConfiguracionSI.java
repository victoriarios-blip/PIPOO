package pipoo.core.configuracion;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfiguracionSI extends Configuracion {
    private String skinModo;          // "Original", "Color", "Halloween"
    private String velocidadInvasores; // "Lenta", "Media", "Rápida"
    private String mapeoControles;     // "Flechas + Espacio" o "A, D + K"
    private String nombreJ1;
    private int teclaIzq;
    private int teclaDer;
    private int teclaDisparo;

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
        this.mapeoControles = "Flechas + Espacio";
        this.nombreJ1 = "Invitado";

        // Teclas por defecto (Flechas + Espacio)
        this.teclaIzq = 37;
        this.teclaDer = 39;
        this.teclaDisparo = 32;
    }

    @Override
    public void guardar() {
        Properties prop = new Properties();

        // Leemos lo existente en el archivo compartido para no pisar datos de otros juegos
        try (InputStream in = Files.newInputStream(Paths.get("jgame.properties"))) {
            prop.load(in);
        } catch (IOException e) {
            // Archivo inexistente aún, se creará de cero
        }

        // Parámetros comunes y específicos de Space Invaders con prefijo 'si_'
        prop.setProperty("si_fullScreen", String.valueOf(this.isPantallaCompleta()));
        prop.setProperty("si_sonidoActivado", String.valueOf(this.isSonidoActivado()));
        prop.setProperty("si_pistaMusical", this.pistaMusical);
        prop.setProperty("si_skinModo", this.skinModo);
        prop.setProperty("si_velocidadInvasores", this.velocidadInvasores);
        prop.setProperty("si_mapeoControles", this.mapeoControles);
        prop.setProperty("si_nombreJ1", this.nombreJ1);
        prop.setProperty("si_teclaIzq", String.valueOf(this.teclaIzq));
        prop.setProperty("si_teclaDer", String.valueOf(this.teclaDer));
        prop.setProperty("si_teclaDisparo", String.valueOf(this.teclaDisparo));

        persistirEnArchivo(prop, "jgame.properties");
    }

    @Override
    public void leer() {
        try (InputStream in = Files.newInputStream(Paths.get("jgame.properties"))) {
            propiedades.load(in);
        } catch (IOException e) {
            System.out.println("No se encontró jgame.properties, inicializando Space Invaders con defaults.");
        }

        this.sonidoActivado = Boolean.parseBoolean(propiedades.getProperty("si_sonidoActivado", "true"));
        this.pantallaCompleta = Boolean.parseBoolean(propiedades.getProperty("si_fullScreen", "false"));
        this.pistaMusical = propiedades.getProperty("si_pistaMusical", "Tema 1 (Original)");
        this.skinModo = propiedades.getProperty("si_skinModo", "Original");
        this.velocidadInvasores = propiedades.getProperty("si_velocidadInvasores", "Media");
        this.mapeoControles = propiedades.getProperty("si_mapeoControles", "Flechas + Espacio");
        this.nombreJ1 = propiedades.getProperty("si_nombreJ1", "Invitado");

        this.teclaIzq = Integer.parseInt(propiedades.getProperty("si_teclaIzq", "37"));
        this.teclaDer = Integer.parseInt(propiedades.getProperty("si_teclaDer", "39"));
        this.teclaDisparo = Integer.parseInt(propiedades.getProperty("si_teclaDisparo", "32"));
    }

    // === GETTERS Y SETTERS ===
    public String getSkinModo() { return skinModo; }
    public void setSkinModo(String skinModo) { this.skinModo = skinModo; }

    public String getVelocidadInvasores() { return velocidadInvasores; }
    public void setVelocidadInvasores(String velocidad) { this.velocidadInvasores = velocidad; }

    public String getMapeoControles() { return mapeoControles; }
    public void setMapeoControles(String mapeoControles) { this.mapeoControles = mapeoControles; }

    public String getNombreJ1() { return nombreJ1; }
    public void setNombreJ1(String nombreJ1) { this.nombreJ1 = nombreJ1; }

    public int getTeclaIzq() { return teclaIzq; }
    public void setTeclaIzq(int tecla) { this.teclaIzq = tecla; }

    public int getTeclaDer() { return teclaDer; }
    public void setTeclaDer(int tecla) { this.teclaDer = tecla; }

    public int getTeclaDisparo() { return teclaDisparo; }
    public void setTeclaDisparo(int tecla) { this.teclaDisparo = tecla; }
}