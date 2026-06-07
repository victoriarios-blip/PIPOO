package pipoo.core;

import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.ArrayList;

public class Ranking {
    private List<RankingEntry> listaRankingEntry;
    private String nombreArchivo;

    public Ranking(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
        this.listaRankingEntry = new ArrayList<>();
    }

    public void agregarEntrada(RankingEntry entrada) {
        this.listaRankingEntry.add(entrada);
        ordenarPorPuntaje(); // Mantiene la lista organizada
    }

    public void ordenarPorPuntaje() {
        // ordenamos
        this.listaRankingEntry.sort((e1, e2) -> Integer.compare(e2.getPuntaje(), e1.getPuntaje()));
    }

    public List<RankingEntry> obtenerTope(int cantidad) {
        ordenarPorPuntaje();
        int fin = Math.min(cantidad, listaRankingEntry.size());
        return new ArrayList<>(listaRankingEntry.subList(0, fin));
    }


    public void guardarRanking() {
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(Paths.get(nombreArchivo)))) {
            out.writeObject(listaRankingEntry);
            System.out.println("Ranking guardado en " + nombreArchivo);
        } catch (IOException e) {
            System.err.println("Error al guardar ranking: " + e.getMessage());
        }
    }


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

    public String toStrOrdenado() {
        List<RankingEntry> top10 = obtenerTope(10);
        StringBuilder sb = new StringBuilder();

        sb.append("=====================================================\n");
        sb.append("             TOP 10 MEJORES PUNTAJES       \n");
        sb.append("=====================================================\n\n");

        sb.append(String.format("%-5s | %-15s | %-8s | %-10s\n",
                "Pos", "Jugador", "Puntos", "Fecha"));
        sb.append("------------------------------------------------------------\n");

        int pos = 1;
        for (RankingEntry entry : top10) {
            sb.append(String.format("%-5d | %-15s | %-8d | %-10s\n",
                    pos, entry.getNombreJugador(), entry.getPuntaje(), entry.getFecha()));
            pos++;
        }

        if (top10.isEmpty()) {
            sb.append("\n   ¡Aun no hay récords grabados!\n");
        }

        return sb.toString();
    }



}
