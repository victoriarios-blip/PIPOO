package pipoo.core.configuracion;


import java.util.Properties;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

//de aca se desprenden las clases hijas: ConfiguracionGeneral, ConfiguracionPong, ConfiguracionSI, ConfiguracionLD
public abstract class Configuracion {
    // atributos protegidos para que las clases hijas los hereden directamente
    protected boolean pantallaCompleta;
    protected boolean sonidoActivado;
    protected String pistaMusical, nombreArchivo;
    protected Properties propiedades;

    public Configuracion(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
        this.propiedades = new Properties();
        this.reset(); //inicializa con valores por defecto
    }

    // metodos setters y getters
    public boolean isPantallaCompleta() { return pantallaCompleta; }
    public void setPantallaCompleta(boolean pc) { this.pantallaCompleta = pc; }

    public boolean isSonidoActivado() { return sonidoActivado; }
    public void setSonidoActivado(boolean s) { this.sonidoActivado = s; }

    public String getPistaMusical() { return pistaMusical; }
    public void setPistaMusical(String pista) { this.pistaMusical = pista; }

    // metodos obligatorios para las demas configuraciones
    protected abstract void guardar();
    protected abstract void reset();


    protected void persistirEnArchivo(Properties prop, String nombreArchivo) {
        // usamos try-with-resources para asegurar el cierre automático del stream
        try (OutputStream out = Files.newOutputStream(Paths.get(nombreArchivo))) {
            // El método store guarda las claves y valores en formato .properties [7]
            prop.store(out, "Configuracion PIPOO - Archivo: " + nombreArchivo);
            System.out.println("Archivo de configuracion '" + nombreArchivo + "' guardado con éxito.");
        } catch (IOException e) {
            System.err.println("Error crítico al intentar guardar en " + nombreArchivo + ": " + e.getMessage());
        }
    }
}
