package pipoo.core.configuracion;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;


public class ConfiguracionGeneral extends Configuracion {

    //constructor config general
    public ConfiguracionGeneral() {
        // defino su propio nombre del archivo usando el constructor padre
        super("config_general.properties");
    }

    @Override
    public void guardar() {
        Properties prop = new Properties();

        prop.setProperty("pantallaCompleta", String.valueOf(this.isPantallaCompleta()));
        prop.setProperty("sonidoActivado", String.valueOf(this.isSonidoActivado()));
        prop.setProperty("pistaMusical", this.getPistaMusical());

        persistirEnArchivo(prop, "config_general.properties");
    }


    @Override
    public void reset() {
        this.pantallaCompleta = false; // default: desactivado
        this.sonidoActivado = true;    // default: activado
        this.pistaMusical = "Tema Principal";
    }

}
