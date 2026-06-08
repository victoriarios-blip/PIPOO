package pipoo.core;

import javax.sound.sampled.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GestorAudio {
    private Clip musicaFondo; // para la musica principal
    private Map<String, Clip> efectos = new HashMap<>(); // para sonidos cortos


    public void precargarEfecto(String nombre, URL ruta) {
        if (ruta == null) {
            System.err.println("ERROR: No se encontro el efecto: " + nombre);
            return;
        }
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(ruta);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            efectos.put(nombre, clip);
        } catch (Exception e) {
            System.err.println("Error precargando " + nombre + ": " + e.getMessage());
        }
    }

    public void reproducirEfecto(String nombre) {
        Clip clip = efectos.get(nombre);
        if (clip != null) {
            clip.stop();
            clip.setFramePosition(0); // Rebina el sonido
            clip.start();             // Le da play
        }
    }

//musica de fondo
    public void reproducirMusica(URL ruta) {
        detenerMusica(); // Si había otra sonando, la apaga primero

        if (ruta == null) {
            System.err.println("ERROR: No se encontro el archivo de musica");
            return;
        }
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(ruta);
            musicaFondo = AudioSystem.getClip();
            musicaFondo.open(audioIn);
            musicaFondo.loop(Clip.LOOP_CONTINUOUSLY); // Queda en bucle infinito
        } catch (Exception e) {
            System.err.println("Error reproduciendo musica: " + e.getMessage());
        }
    }

    public void detenerMusica() {
        if (musicaFondo != null && musicaFondo.isRunning()) {
            musicaFondo.stop();
        }
    }
}