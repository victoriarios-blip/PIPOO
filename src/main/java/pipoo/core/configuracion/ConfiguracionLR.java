package pipoo.core.configuracion;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfiguracionLR extends Configuracion {
    private String skinPersonaje, nombreJ1;
    private int teclaEfectos, teclaMusica, teclaCavar, teclaEnter;

    public ConfiguracionLR() {
        super("jgame.properties");
        this.leer();
    }

    @Override
    public void reset() {
        this.pantallaCompleta = false;
        this.sonidoActivado = true;
        this.pistaMusical = "Ninguna";
        this.skinPersonaje = "Original";
        this.teclaEfectos = 81;  // Q
        this.teclaMusica = 87;   // W
        this.teclaCavar = 32;    // Barra Espaciadora
        this.teclaEnter = 10;    // Enter
    }

    @Override
    public void leer() {
        // intentamos cargar el archivo, si no existe, nos quedamos con los valores del reset()
        try (FileInputStream in = new FileInputStream(this.nombreArchivo)) {
            this.propiedades.load(in);
        } catch (IOException e) {
            System.out.println("Archivo '" + this.nombreArchivo + "' no encontrado. Usando valores por defecto.");
            return;
        }
        // parametros comunes
        this.pantallaCompleta = Boolean.parseBoolean(propiedades.getProperty("fullScreen", "false"));
        this.sonidoActivado   = Boolean.parseBoolean(propiedades.getProperty("sonidoActivado", "true"));
        this.pistaMusical     = propiedades.getProperty("pistaMusical", "Ninguna");
        // parametros Lode Runner
        this.skinPersonaje = propiedades.getProperty("skinPersonaje", "Original");
        this.teclaEfectos  = Integer.parseInt(propiedades.getProperty("teclaEfectos", "81"));
        this.teclaMusica   = Integer.parseInt(propiedades.getProperty("teclaMusica",  "87"));
        this.teclaCavar    = Integer.parseInt(propiedades.getProperty("teclaCavar",   "32"));
        this.teclaEnter    = Integer.parseInt(propiedades.getProperty("teclaEnter",   "10"));

        System.out.println("DEBUG: Cambios del usuario LR cargados desde " + this.nombreArchivo);
        System.out.println("DEBUG: ¿Sonido LR activado?: " + this.sonidoActivado);
    }

    @Override
    public void guardar() {
        Properties prop = new Properties();

        // parametros comunes
        prop.setProperty("fullScreen",     String.valueOf(this.pantallaCompleta));
        prop.setProperty("sonidoActivado", String.valueOf(this.sonidoActivado));
        prop.setProperty("pistaMusical",   this.pistaMusical != null ? this.pistaMusical : "Ninguna");

        // parametros Lode Runner
        prop.setProperty("skinPersonaje", this.skinPersonaje);
        prop.setProperty("teclaEfectos",  String.valueOf(this.teclaEfectos));
        prop.setProperty("teclaMusica",   String.valueOf(this.teclaMusica));
        prop.setProperty("teclaCavar",    String.valueOf(this.teclaCavar));
        prop.setProperty("teclaEnter",    String.valueOf(this.teclaEnter));
        persistirEnArchivo(prop, this.nombreArchivo);
        try (FileOutputStream out = new FileOutputStream("jgame.properties")) {
            prop.store(out, "Configuracion PIPOO");
            System.out.println("DEBUG: Configuración guardada.");
        } catch (IOException e) {
            System.err.println("Error al guardar: " + e.getMessage());
        }
    }
    // Getters y Setters
    public String getSkinPersonaje() { return skinPersonaje; }
    public void setSkinPersonaje(String skin) { this.skinPersonaje = skin; }

    public String getNombreJ1() { return nombreJ1;}
    public void setNombreJ1(String nombreJ1) { this.nombreJ1 = nombreJ1; }
}