package pipoo.core.configuracion;


import java.io.OutputStream;
import java.nio.file.Files; //api nio
import java.nio.file.Paths; //api nio
import java.util.Properties;
import java.io.IOException;

public class ConfiguracionPong extends Configuracion {
    private String skinPaletas, skinCancha, skinPelota;
    private int puntosParaGanar;
    private int teclaUpJ1, teclaDownJ1, teclaUpJ2, teclaDownJ2;

    //constructor config pong
    public ConfiguracionPong() {
        // defino su propio nombre de archivo
        super("config_pong.properties");
    }

    @Override
    public void reset() {
        this.pantallaCompleta = false;
        this.sonidoActivado = true;
        this.pistaMusical = "Pong Arcade";
        this.skinPaletas = "Original";
        this.skinCancha = "Original";
        this.skinPelota = "Original";
        this.puntosParaGanar = 11; //11 o 15 puntos


        // Teclas por defecto
        this.teclaUpJ1 = 38;   // Flecha Arriba
        this.teclaDownJ1 = 40; // Flecha Abajo
        this.teclaUpJ2 = 87;   // W
        this.teclaDownJ2 = 83; // S
    }

    @Override
    public void guardar() {
        Properties prop = new Properties();

        // guardar parametros de la clase abstracta (comunes)
        prop.setProperty("pantallaCompleta", String.valueOf(this.pantallaCompleta));
        prop.setProperty("sonidoActivado", String.valueOf(this.sonidoActivado));
        prop.setProperty("pistaMusical", this.pistaMusical);

        // guardar parametros específicos de Pong
        prop.setProperty("skinPelota", this.skinPelota);
        prop.setProperty("puntosParaGanar", String.valueOf(this.puntosParaGanar));
        prop.setProperty("teclaUpJ1", String.valueOf(this.teclaUpJ1));
        prop.setProperty("teclaDownJ1", String.valueOf(this.teclaDownJ1));
        prop.setProperty("teclaUpJ2", String.valueOf(this.teclaUpJ2));
        prop.setProperty("teclaDownJ2", String.valueOf(this.teclaDownJ2));
        prop.setProperty("skinPaletas", String.valueOf(this.skinPaletas));
        prop.setProperty("skinCancha", String.valueOf(this.skinCancha));
        persistirEnArchivo(prop, "config_pong.properties");
    }
}
