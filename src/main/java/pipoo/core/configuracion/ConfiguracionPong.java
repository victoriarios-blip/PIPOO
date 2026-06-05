package pipoo.core.configuracion;


import java.io.FileOutputStream;
import java.util.Properties;
import java.io.IOException;

public class ConfiguracionPong extends Configuracion {
    private String skinPaletas, skinCancha, skinPelota;
    private String pistaMusical;
    private int puntosParaGanar;
    private int teclaUpJ1, teclaDownJ1, teclaUpJ2, teclaDownJ2;

    private String nombreJ1;
    private String nombreJ2;
    private boolean contraBot = true;

    //constructor config pong
    public ConfiguracionPong() {
        // defino su propio nombre de archivo
        super("jgame.properties");
        this.pistaMusical = "Ninguna";
        this.leer();

    }

    @Override
    public void reset() {
        this.pantallaCompleta = false;
        this.sonidoActivado = true;
        this.pistaMusical = "Ninguna";
        this.skinPaletas = "Original";
        this.skinCancha = "Original";
        this.skinPelota = "Original";
        this.puntosParaGanar = 11; //11 o 15 puntos
        this.contraBot = true;
        this.nombreJ2 = "Jugador 2";


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
        prop.setProperty("fullScreen", String.valueOf(this.isPantallaCompleta()));
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
        prop.setProperty("nombreJ1", this.nombreJ1);
        prop.setProperty("nombreJ2", this.nombreJ2);
        prop.setProperty("contraBot", String.valueOf(this.contraBot));
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
        this.skinPelota = propiedades.getProperty("skinPelota", "Original");
        this.skinPaletas = propiedades.getProperty("skinPaletas", "Original");
        this.skinCancha = propiedades.getProperty("skinCancha", "Original");

        // Logica de juego
        this.puntosParaGanar = Integer.parseInt(propiedades.getProperty("puntosParaGanar", "11"));
        this.contraBot = Boolean.parseBoolean(propiedades.getProperty("contraBot", "true"));
        this.nombreJ2 = propiedades.getProperty("nombreJ2", "PIPOO BOT");

        // 4. Teclas (Conversión de String a int)
        this.teclaUpJ1 = Integer.parseInt(propiedades.getProperty("teclaUpJ1", "38"));
        this.teclaDownJ1 = Integer.parseInt(propiedades.getProperty("teclaDownJ1", "40"));
        this.teclaUpJ2 = Integer.parseInt(propiedades.getProperty("teclaUpJ2", "87"));
        this.teclaDownJ2 = Integer.parseInt(propiedades.getProperty("teclaDownJ2", "83"));
    }

    // Getters y Setters específicos de Pong para la GUI
    public int getPuntosParaGanar() { return puntosParaGanar; }
    public void setPuntosParaGanar(int puntos) { this.puntosParaGanar = puntos; }

    public String getSkinPelota() { return skinPelota; }
    public void setSkinPelota(String skinPelota) { this.skinPelota = skinPelota; }

    // Getters y Setters específicos de Pong para la GUI
    public String getSkinPaletas() { return skinPaletas; }
    public void setSkinPaletas(String skinPaletas) { this.skinPaletas = skinPaletas; }

    public String getNombreJ1() { return nombreJ1;}
    public void setNombreJ1(String nombreJ1) { this.nombreJ1 = nombreJ1; }

    public String getNombreJ2() { return nombreJ2;}
    public void setNombreJ2(String nombreJ2) { this.nombreJ2 = nombreJ2; }

    public boolean getContraBot() { return contraBot; }
    public void setContraBot(boolean contraBot) { this.contraBot = contraBot;}


    public String getSkinCancha() { return skinPelota; }
    public void setSkinCancha(String skinCancha) { this.skinPelota = skinCancha; }

    public int getTeclaUpJ1() { return teclaUpJ1; }
    public void setTeclaUpJ1(int teclaUpJ1) { this.teclaUpJ1 = teclaUpJ1; }

    public int getTeclaDownJ1() { return teclaDownJ1; }
    public void setTeclaDownJ1(int teclaDownJ1) { this.teclaDownJ1 = teclaDownJ1; }

    public int getTeclaUpJ2() { return teclaUpJ2; }
    public void setTeclaUpJ2(int teclaUpJ2) { this.teclaUpJ2 = teclaUpJ2; }

    public int getTeclaDownJ2() { return teclaDownJ2; }
    public void setTeclaDownJ2(int teclaDownJ2) { this.teclaDownJ2 = teclaDownJ2; }

}



