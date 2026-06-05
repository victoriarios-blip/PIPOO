package pipoo.core.configuracion;

import java.util.Properties;

public class ConfiguracionSI extends Configuracion {
    private String skinNave, skinInvasores, skinProyectiles;
    private String velocidadInvasores; // "Lenta", "Media", "Rápida" <- String?? ver
    private int teclaIzq, teclaDer, teclaDisparo;

    public ConfiguracionSI() {
        super("jgame.properties");
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

        persistirEnArchivo(prop, "config_space_invaders.properties");
    }

    public void setVelocidadInvasores(String velocidad) {
        this.velocidadInvasores = velocidad;
    }
    public String getVelocidadInvasores() {
        return velocidadInvasores;
    }
}

