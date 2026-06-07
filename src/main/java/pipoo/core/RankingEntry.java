package pipoo.core;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * Implementa Serializable para facilitar el guardado de objetos en archivos.
 */

public class RankingEntry implements Serializable {
    private String nombreJugador;
    private int puntaje;
    private int nivel;
    private String fecha; // "dd/mm/yyyy"

    public RankingEntry(String nombreJugador, int nivel, int puntaje, String fecha) {
        this.nombreJugador = nombreJugador;
        this.nivel = nivel;
        this.puntaje = puntaje;
        this.fecha = fecha;
    }

    // getters y setters
    public String getNombreJugador() { return nombreJugador; }
    public int getNivel() { return nivel; }
    public int getPuntaje() { return puntaje; }
    public String getFecha() { return fecha; }
}
