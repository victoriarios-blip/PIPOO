package pipoo.core.configuracion;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfiguracionSI extends Configuracion {
    private String skinNave, skinInvasores, skinProyectiles;
    private String velocidadInvasores; // "Lenta", "Media", "Rápida" <- String?? ver
    private int teclaIzq, teclaDer, teclaDisparo;
    private String nombreJ1;



    public ConfiguracionSI() {
        super("jgame.properties");
        this.leer();
    }


    @Override
    public void reset() {
        this.sonidoActivado = true;
        this.skinNave = "Default";
        this.velocidadInvasores = "Media";
        this.teclaIzq = 37;      // Flecha Izquierda
        this.teclaDer = 39;      // Flecha Derecha
        this.teclaDisparo = 32;  // Barra Espaciadora
    }

    @Override public void guardar() {
        Properties prop = new Properties();
        // guardar parametros de la clase abstracta (comunes)
        prop.setProperty("pantallaCompleta", String.valueOf(this.pantallaCompleta));
        prop.setProperty("sonidoActivado", String.valueOf(this.sonidoActivado));
        prop.setProperty("pistaMusical", this.pistaMusical);

        // guardar parametros específicos de Space Invaders
        prop.setProperty("skinNave", this.skinNave);
        prop.setProperty("skinInvasores", this.skinInvasores);
        prop.setProperty("skinProyectiles", this.skinProyectiles); //los proyectiles tienen skin??
        prop.setProperty("velocidadInvasores", String.valueOf(this.velocidadInvasores));
        prop.setProperty("teclaIzq", String.valueOf(this.teclaIzq));
        prop.setProperty("teclaDer", String.valueOf(this.teclaDer));
        prop.setProperty("teclaDisparo", String.valueOf(this.teclaDisparo));
        prop.setProperty("nombreJ1", this.nombreJ1);

        persistirEnArchivo(prop, "jgame.properties");
        try (FileOutputStream out = new FileOutputStream("jgame.properties")) {
            prop.store(out, "Configuracion PIPOO");
            System.out.println("DEBUG: Configuración guardada.");
        } catch (IOException e) {
            System.err.println("Error al guardar: " + e.getMessage());
        }


    }

    public void leer() {
        // Parametros de audio y sistema
        this.pistaMusical = propiedades.getProperty("pistaMusical", "Ninguna");
        this.sonidoActivado = Boolean.parseBoolean(propiedades.getProperty("sonidoActivado", "true"));
        this.pantallaCompleta = Boolean.parseBoolean(propiedades.getProperty("fullScreen", "false"));

        // Parametros de personalizacion (Skins)
        this.skinNave = propiedades.getProperty("skinNave", "Original");
        this.skinInvasores = propiedades.getProperty("skinInvasores", "Original");

        // Logica de juego
        this.velocidadInvasores = propiedades.getProperty("velocidadInvasores", "Media");

    }





    public void setVelocidadInvasores(String velocidad) {
        this.velocidadInvasores = velocidad;
    }
    public String getVelocidadInvasores() {
        return velocidadInvasores;
    }
    public String getNombreJ1() { return nombreJ1;}
    public void setNombreJ1(String nombreJ1) { this.nombreJ1 = nombreJ1; }

}

