package pipoo.core;

import java.io.Serializable;

/**
 * Implementa Serializable para facilitar el guardado de objetos en archivos.
 */

public class RankingEntry implements Serializable {
    private String nombreJugador;
    private int puntaje;
    private String nivel; // Ej: "Nivel 3" o "Final"
    private String fecha; // "dd/mm/yyyy"

    public RankingEntry(String nombreJugador, int puntaje, String nivel, String fecha) {
        this.nombreJugador = nombreJugador;
        this.puntaje = puntaje;
        this.nivel = nivel;
        this.fecha = fecha;
    }

    // getters y setters
    public String getNombreJugador() { return nombreJugador; }
    public void setNombreJugador(String nombre) { this.nombreJugador = nombre; }

    public int getPuntaje() { return puntaje; }
    public void setPuntaje(int puntaje) { this.puntaje = puntaje; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}
