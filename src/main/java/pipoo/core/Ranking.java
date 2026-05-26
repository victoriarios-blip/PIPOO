package pipoo.core;

import java.util.*;
import java.io.*;
import java.nio.file.*;

public class Ranking {
    private List<RankingEntry> listaRankingEntry;
    private String nombreArchivo;

    public Ranking(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
        this.listaRankingEntry = new ArrayList<>();
        cargarRanking(); // Intenta cargar datos existentes al instanciarse
    }

    public void agregarEntrada(RankingEntry entrada) {
        this.listaRankingEntry.add(entrada);
        ordenarPorPuntaje(); // Mantiene la lista organizada

        // Si hay más de 10, eliminamos los sobrantes para cumplir el requerimiento
        if (listaRankingEntry.size() > 10) {
            listaRankingEntry = listaRankingEntry.subList(0, 10);
        }
    }

    public void ordenarPorPuntaje() {
        // ordenamos
        this.listaRankingEntry.sort((e1, e2) -> Integer.compare(e2.getPuntaje(), e1.getPuntaje()));
    }

    public List<RankingEntry> obtenerTope(int cantidad) {
        if (listaRankingEntry.size() < cantidad) {
            return listaRankingEntry;
        }
        return listaRankingEntry.subList(0, cantidad);
    }

    /**
     * Guarda la lista de ranking en un archivo binario.
     */

    public void guardarRanking() {
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(Paths.get(nombreArchivo)))) {
            out.writeObject(listaRankingEntry);
            System.out.println("Ranking guardado en " + nombreArchivo);
        } catch (IOException e) {
            System.err.println("Error al guardar ranking: " + e.getMessage());
        }
    }

    /**
     * Carga la lista de ranking desde el disco.
     */
    @SuppressWarnings("unchecked")
    public void cargarRanking() {
        Path path = Paths.get(nombreArchivo);
        if (!Files.exists(path)) return;

        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(path))) {
            this.listaRankingEntry = (List<RankingEntry>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("No se pudo cargar el ranking: " + e.getMessage());
        }
    }
}
