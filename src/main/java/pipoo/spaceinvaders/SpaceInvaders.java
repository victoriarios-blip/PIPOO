package pipoo.spaceinvaders;

import com.entropyinteractive.Keyboard;
import pipoo.core.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;

public class SpaceInvaders extends Juego {
    private NaveHeroe jugador;
    private List<Enemigo> oleada;
    private List<Escudo> escudos;
    private NaveNodriza enemigoFinal;

    // proyectiles
    private List<Proyectil> proyectilesEnemigos;
    private List<Proyectil> proyectilesHeroe;

    // marcador
    private double tiempoJuego = 0; // Acumulador de segundos
    private java.util.Map<Character, java.awt.image.BufferedImage> fuenteArcade;

    //aparicion nave nodriza
    private double tiempoNodriza = 0;
    private static final double intervaloNodriza = 20; // aparece cada 20 segundos

    // cooldown heroe
    private double tiempoDisparo = 0;
    private static final double COOLDOWN_DISPARO = 0.5; // segundos entre disparos

    // puntaje
    private int puntaje = 0;

    private BufferedImage imgProyectilHeroe;
    private BufferedImage imgProyectilEnemigo;
    private BufferedImage imgNaveNodriza;

    // pantalla de carga
    private enum Estado { CARGA, JUGANDO, GAMEOVER }
    private Estado estadoActual = Estado.CARGA; // Arranca en modo carga
    private double acumuladorCarga = 0;
    private static final double DURACION_CARGA = 3.0; // Duración en segundos (ej: 3 segundos)
    private BufferedImage imgPantallaCarga;

    // pantalla game over
    private BufferedImage imgGameOver;
    private double tiempoGameOver = 0;
    private double escalaGameOver = 0.0;

    public SpaceInvaders() {
        super("Retro Space Invaders", 800, 600);
    }

    @Override
    public void gameStartup() {
        java.net.URL test = this.getClass().getResource("/pipoo/spaceinvaders/imagenes/pulpo1.png");
        System.out.println("PATH TEST: " + test);

        System.out.println("Iniciando Space Invaders...");

        oleada = new ArrayList<>();
        escudos = new ArrayList<>();
        proyectilesEnemigos = new ArrayList<>();
        proyectilesHeroe = new ArrayList<>();

        jugador = new NaveHeroe(380, 525);

        int[] posicionesEscudosX = {104, 272, 440, 608};
        for (int x : posicionesEscudosX) {
            escudos.add(new Escudo(x, 410));
        }

        // oleada enemiga (5 filas x 11 columnas)
        int inicioX = (800 - 638) / 2;
        int inicioY = 50;

        for (int fila = 0; fila < 5; fila++) {
            for (int col = 0; col < 11; col++) {
                double x = inicioX + (col * 58); // 48px sprite + 10px separación
                double y = inicioY + (fila * 34); // 24px sprite + 10px separación
                // ← acá falta agregar el enemigo!
                if (fila == 0) oleada.add(new Pulpo(x, y));
                else if (fila < 3) oleada.add(new Cangrejo(x, y));
                else oleada.add(new Calamar(x, y));
            }
        }

        // carga de assets
        try {
            BufferedImage naveHeroe = ImageIO.read(this.getClass().getResource("imagenes/naveHeroeIntacta.png"));
            BufferedImage naveHeroeExplosion1 = ImageIO.read(this.getClass().getResource("imagenes/naveHeroeExplosion1.png"));
            BufferedImage naveHeroeExplosion2 = ImageIO.read(this.getClass().getResource("imagenes/naveHeroeExplosion2.png"));

            imgNaveNodriza = ImageIO.read(this.getClass().getResource("imagenes/naveNodriza.png"));

            // enemigos (2 frames cada uno)
            BufferedImage pulpo1 = ImageIO.read(this.getClass().getResource("imagenes/pulpo1.png"));
            BufferedImage pulpo2 = ImageIO.read(this.getClass().getResource("imagenes/pulpo2.png"));

            BufferedImage cangrejo1 = ImageIO.read(this.getClass().getResource("imagenes/cangrejo1.png"));
            BufferedImage cangrejo2 = ImageIO.read(this.getClass().getResource("imagenes/cangrejo2.png"));

            BufferedImage calamar1 = ImageIO.read(this.getClass().getResource("imagenes/calamar1.png"));
            BufferedImage calamar2 = ImageIO.read(this.getClass().getResource("imagenes/calamar2.png"));

            BufferedImage proyectilHeroe = ImageIO.read(this.getClass().getResource("imagenes/proyectilHeroe.png"));
            BufferedImage proyectilEnemigo = ImageIO.read(this.getClass().getResource("imagenes/proyectilEnemigo.png"));

            BufferedImage escudoIntacto = ImageIO.read(this.getClass().getResource("imagenes/escudoIntacto.png"));
            BufferedImage escudo1daño = ImageIO.read(this.getClass().getResource("imagenes/escudo1daño.png"));
            BufferedImage escudo2daño = ImageIO.read(this.getClass().getResource("imagenes/escudo2daño.png"));
            BufferedImage escudo3daño = ImageIO.read(this.getClass().getResource("imagenes/escudo3daño.png"));
            BufferedImage escudo4daño = ImageIO.read(this.getClass().getResource("imagenes/escudo4daño.png"));

            imgProyectilHeroe   = ImageIO.read(this.getClass().getResource("imagenes/proyectilHeroe.png"));
            imgProyectilEnemigo = ImageIO.read(this.getClass().getResource("imagenes/proyectilEnemigo.png"));

            // pantalla carga
            imgPantallaCarga = ImageIO.read(this.getClass().getResource("imagenes/pantalla_carga_SI.png"));

            // game over
            imgGameOver = ImageIO.read(this.getClass().getResource("imagenes/game_over.png"));

            //muerte enemigo
            BufferedImage muerteEnemigo = ImageIO.read(this.getClass().getResource("imagenes/muerte_enemigo.png"));

            fuenteArcade = new java.util.HashMap<>();
            tiempoJuego = 0;

        //números del 0 al 9 (asumiendo que se llaman "0.png", "1.png", etc.)
            for (int i = 0; i <= 9; i++) {
                char numero = (char) ('0' + i);
                BufferedImage imgNum = ImageIO.read(this.getClass().getResource("imagenes/" + i + ".png"));
                fuenteArcade.put(numero, imgNum);
            }

        // letras de la A a la Z
            for (char c = 'A'; c <= 'Z'; c++) {
                // Si tus archivos están en minúsculas (ej: "a.png"), usamos toLowerCase()
                String nombreArchivo = String.valueOf(c).toLowerCase() + ".png";
                BufferedImage imgLetra = ImageIO.read(this.getClass().getResource("imagenes/" + nombreArchivo));
                fuenteArcade.put(c, imgLetra);
            }

            for (Escudo escudo : escudos) {
                escudo.setImgIntacto(escudoIntacto);
                escudo.setEscudo1daño(escudo1daño);
                escudo.setEscudo2daño(escudo2daño);
                escudo.setEscudo3daño(escudo3daño);
                escudo.setEscudo4daño(escudo4daño);
            }

            jugador.setImagenIntacta(naveHeroe);
            jugador.setImagenExplosion1(naveHeroeExplosion1);
            jugador.setImagenExplosion2(naveHeroeExplosion2);

            for (Escudo escudo : escudos) {
                escudo.setImagen(escudoIntacto);
            }

            // 3. Asignación de ambos frames a los enemigos usando el nuevo método
            for (Enemigo e : oleada) {
                if (e instanceof Pulpo) {
                    e.setImagenes(pulpo1, pulpo2);
                } else if (e instanceof Cangrejo) {
                    e.setImagenes(cangrejo1, cangrejo2);
                } else if (e instanceof Calamar) {
                    e.setImagenes(calamar1, calamar2);
                }
                e.setImagenMuerte(muerteEnemigo);
            }

        } catch (Exception e) {
            System.out.println("Error cargando los assets: " + e.getMessage());
        }

       for (Enemigo e : oleada) {
            e.setVelocidadX(40);
        }
    }

    @Override
    public void gameUpdate(double delta) {
        // === CONTROL DE TIEMPO DE LA PANTALLA DE CARGA ===
        if (estadoActual == Estado.CARGA) {
            acumuladorCarga += delta;
            if (acumuladorCarga >= DURACION_CARGA) {
                estadoActual = Estado.JUGANDO; // Rompe el ciclo y arranca el juego real
            }
            return; // IMPORTANTE: Bloquea el resto del juego mientras carga
        }

        // === CONTROL DE LA PANTALLA DE GAME OVER (NUEVO) ===
        if (estadoActual == Estado.GAMEOVER) {
            tiempoGameOver += delta;

            // Efecto zoom del cartel
            if (escalaGameOver < 1.0) {
                escalaGameOver += delta * 2.0;
                if (escalaGameOver > 1.0) escalaGameOver = 1.0;
            }

            // Escuchar teclado para reiniciar la partida
            Keyboard teclado = this.getKeyboard();
            if (teclado.isKeyPressed(KeyEvent.VK_ENTER) || teclado.isKeyPressed(KeyEvent.VK_SPACE)) {
                reiniciarJuego(); // El método que borra las listas y vuelve a hacer el startup
            }
            return; // IMPORTANTE: Congela la actualización del juego de fondo
        }

        // === LÓGICA DEL JUEGO ACTIVO ===
        Keyboard teclado = this.getKeyboard();

        // 1. Control de teclado y disparo del héroe (Bloqueado si está explotando)
        if (!jugador.isMuriendo()) {
            if (teclado.isKeyPressed(KeyEvent.VK_LEFT))       jugador.moverIzquierda();
            else if (teclado.isKeyPressed(KeyEvent.VK_RIGHT)) jugador.moverDerecha();
            else                                              jugador.detener();

            // Cooldown y lógica de disparos del héroe
            tiempoDisparo += delta;
            if (teclado.isKeyPressed(KeyEvent.VK_SPACE) && tiempoDisparo >= COOLDOWN_DISPARO) {
                Proyectil p = jugador.disparar();
                if (p != null) {
                    p.setImagen(imgProyectilHeroe);
                    proyectilesHeroe.add(p);
                    tiempoDisparo = 0;
                }
            }
        } else {
            // Si la nave está en plena explosión, se congela
            jugador.detener();
        }

        // El movimiento físico de la nave se aplica siempre
        jugador.mover(delta);

        // 2. Movimiento y animación de la oleada enemiga
        for (Enemigo enemigo : oleada) {
            enemigo.mover(delta);
            enemigo.actualizarFrame(delta);
        }

        // 3. Detección de bordes de los enemigos
        boolean tocoBorde = false;
        for (Enemigo e : oleada) {
            if (e.x <= 0 || e.x + e.width >= 800) {
                tocoBorde = true;
                break;
            }
        }

        // 4. Si un alien tocó el borde, baja toda la formación e invierte dirección
        if (tocoBorde) {
            for (Enemigo e : oleada) {
                e.bajarFila(20);
            }
        }

        // 5. Lógica de disparo aleatorio de los enemigos
        for (Enemigo enemigo : oleada) {
            Proyectil p = enemigo.disparar();
            if (p != null) {
                p.setImagen(imgProyectilEnemigo);
                proyectilesEnemigos.add(p);
            }
        }

        // 6. Actualizar posición de todos los proyectiles en pantalla
        for (Proyectil p : proyectilesEnemigos) { p.mover(delta); }
        for (Proyectil p : proyectilesHeroe)    { p.mover(delta); }

        // 7. Resetear flags de daño de los escudos
        for (Escudo escudo : escudos) {
            escudo.resetFrame();
        }

        // 8. Procesar colisiones, puntajes y limpieza de entidades muertas/fuera de pantalla
        detectarColisiones();
        actualizarPuntaje();
        limpiarNoVisibles();

        // 9. Verificación de GAME OVER (Por quedarse sin vidas) - CAMBIADO
        if (!jugador.isVisible() && jugador.getVidas() <= 0) {
            System.out.println("GAME OVER - Te quedaste sin vidas");
            estadoActual = Estado.GAMEOVER;
            tiempoGameOver = 0;
            escalaGameOver = 0.0;
            return;
        }

        // 10. Verificación de GAME OVER (Si los enemigos invaden la Tierra) - CAMBIADO
        for (Enemigo e : oleada) {
            if (e.y + e.height >= 500) {
                System.out.println("GAME OVER - Los enemigos llegaron a la línea límite");
                estadoActual = Estado.GAMEOVER;
                tiempoGameOver = 0;
                escalaGameOver = 0.0;
                return;
            }
        }

        // 11. Verificación de VICTORIA
        if (oleada.isEmpty()) {
            System.out.println("¡GANASTE! Limpiaste la oleada");
            this.stop();
        }

        // 12. Temporizador y movimiento de la Nave Nodriza
        tiempoNodriza += delta;
        if (tiempoNodriza >= intervaloNodriza) {
            enemigoFinal = new NaveNodriza(-60, 45);
            enemigoFinal.setImagen(imgNaveNodriza);
            tiempoNodriza = 0;
        }
        if (enemigoFinal != null && enemigoFinal.isVisible()) {
            enemigoFinal.mover(delta);
        }

        // 13. Actualizar estado del héroe (Procesa los timers y frames de la explosión)
        jugador.actualizar(delta);

        // Suma la fracción de segundo que pasó en este frame al HUD
        tiempoJuego += delta;
    }

    void limpiarNoVisibles() {
        proyectilesEnemigos.removeIf(p -> !p.isVisible() || p.y > 600);
        proyectilesHeroe.removeIf(p -> !p.isVisible() || p.y < 0); // ← sale por arriba
        oleada.removeIf(e -> !e.isVisible());
    }

    @Override
    public void gameDraw(Graphics2D g) {
        // === 1. CONTROL DE LA PANTALLA DE CARGA ===
        if (estadoActual == Estado.CARGA) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight()); // Fondo negro base

            if (imgPantallaCarga != null) {
                g.drawImage(imgPantallaCarga, 0, 0, getWidth(), getHeight(), null);
            }
            return;
        }

        // === 2. PANTALLA DE GAME OVER ABSOLUTA (Mudada acá arriba) ===
        if (estadoActual == Estado.GAMEOVER) {
            // Pintamos TODA la pantalla de negro absoluto primero (limpio, sin escalar)
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            // Guardamos la configuración gráfica original para los efectos
            java.awt.geom.AffineTransform transformOriginal = g.getTransform();
            java.awt.Composite compositeOriginal = g.getComposite();

            // A) EFECTO AGRANDAR DESDE EL CENTRO (Afecta solo al cartel y al texto de abajo)
            g.translate(400, 300);
            g.scale(escalaGameOver, escalaGameOver);
            g.translate(-400, -300);

            // B) EFECTO PALPITAR BRILLO (Opacidad sutil en el logo)
            float alphaPalpitar = (float) (0.65f + 0.35f * Math.sin(tiempoGameOver * 5.0));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaPalpitar));

            // Dibujamos el cartel de tus compañeros
            if (imgGameOver != null) {
                g.drawImage(imgGameOver, 250, 110, 300, 180, null);
            }

            // C) TEXTO DE REINICIO PARPADEANTE
            g.setComposite(compositeOriginal); // Restablecemos opacidad al 100% para las letras
            if ((int)(tiempoGameOver * 2.5) % 2 == 0) {
                dibujarTextoRetro(g, "PRESS ENTER TO PLAY AGAIN", 176, 340);
            }

            // Restauramos la matriz original y cortamos el renderizado
            g.setTransform(transformOriginal);
            return; // IMPORTANTE: Al cortar acá, destruye el renderizado de la oleada vieja de abajo
        }

        // === 3. RENDERIZADO DEL JUEGO ACTIVO (Solo corre si estás jugando) ===
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Enemigos
        for (Enemigo e : oleada) {
            e.dibujar(g);
        }

        // Escudos
        for (Escudo escudo : escudos) {
            escudo.dibujar(g);
        }

        // Proyectiles
        for (Proyectil p : proyectilesHeroe) {
            p.dibujar(g);
        }
        for (Proyectil p : proyectilesEnemigos) {
            p.dibujar(g);
        }

        // Jugador
        jugador.dibujar(g);

        // Nave nodriza
        if (enemigoFinal != null && enemigoFinal.isVisible()) {
            enemigoFinal.dibujar(g);
        }

        // === 4. RENDERIZADO INTERFAZ (HUD) ===
        g.setColor(Color.GREEN);
        g.fillRect(0, 555, 800, 4);

        String strPuntaje = "SCORE " + String.format("%04d", puntaje);
        String strTiempo  = "TIME "  + String.format("%03d", (int)tiempoJuego);
        String strVidas   = "LIVES " + jugador.getVidas();

        dibujarTextoRetro(g, strPuntaje, 40, 570);
        dibujarTextoRetro(g, strTiempo, 330, 570);
        dibujarTextoRetro(g, strVidas, 620, 570);
    }

    private void dibujarTextoRetro(Graphics2D g, String texto, int x, int y) {
        texto = texto.toUpperCase(); // Nos aseguramos de buscar siempre en mayúsculas
        int anchoCaracter = 16;      // Tamaño en píxeles al que se va a dibujar cada letra
        int altoCaracter = 16;
        int espaciado = 2;           // Píxeles de separación entre letras

        for (int i = 0; i < texto.length(); i++) {
            char caracter = texto.charAt(i);

            if (caracter != ' ') { // Si no es un espacio en blanco, dibujamos el sprite
                BufferedImage img = fuenteArcade.get(caracter);
                if (img != null) {
                    g.drawImage(img, x, y, anchoCaracter, altoCaracter, null);
                }
            }
            // Avanzamos la X para el siguiente caracter (incluso si era un espacio)
            x += anchoCaracter + espaciado;
        }
    }

    @Override
    public void gameShutdown() {
        // Guardar puntajes
    }

    @Override
    protected void detectarColisiones() {
        // 1. Proyectiles del héroe vs enemigos
        for (Proyectil proyectil : proyectilesHeroe) {
            if (!proyectil.isVisible()) continue;
            for (Enemigo enemigo : oleada) {
                if (!enemigo.isVisible()) continue;
                if (proyectil.colisionaCon(enemigo)) { // ← enemigo llama colisionaCon, igual que escudo
                    proyectil.reaccionarAColision(enemigo);
                    enemigo.reaccionarAColision(proyectil);
                    puntaje += enemigo.getValorPuntaje();
                }
            }
        }

        // 2. Proyectiles del héroe vs escudos
        for (Proyectil proyectil : proyectilesHeroe) {
            if (!proyectil.isVisible()) continue;
            for (Escudo escudo : escudos) {
                if (!escudo.isVisible()) continue;
                if (proyectil.colisionaCon(escudo)) {
                    proyectil.reaccionarAColision(escudo);
                    escudo.reaccionarAColision(proyectil);
                }
            }
        }

        // 3. Proyectiles enemigos vs jugador
        for (Proyectil proyectil : proyectilesEnemigos) {
            if (!proyectil.isVisible()) continue;
            if (proyectil.colisionaCon(jugador)) {
                proyectil.reaccionarAColision(jugador);
                jugador.reaccionarAColision(proyectil);
            }
        }

        // 4. Proyectiles enemigos vs escudos
        for (Proyectil proyectil : proyectilesEnemigos) {
            if (!proyectil.isVisible()) continue;
            for (Escudo escudo : escudos) {
                if (!escudo.isVisible()) continue;
                if (proyectil.colisionaCon(escudo)) {
                    proyectil.reaccionarAColision(escudo);
                    escudo.reaccionarAColision(proyectil);
                }
            }
        }

        // 5. Proyectiles entre sí
        for (Proyectil ph : proyectilesHeroe) {
            if (!ph.isVisible()) continue;
            for (Proyectil pe : proyectilesEnemigos) {
                if (!pe.isVisible()) continue;
                if (ph.colisionaCon(pe)) {
                    ph.reaccionarAColision(pe);
                    pe.reaccionarAColision(ph);
                }
            }
        }

        // 6. Proyectil héroe vs NaveNodriza
        if (enemigoFinal != null && enemigoFinal.isVisible()) {
            for (Proyectil proyectil : proyectilesHeroe) {
                if (!proyectil.isVisible()) continue;
                if (proyectil.colisionaCon(enemigoFinal)) {
                    proyectil.reaccionarAColision(enemigoFinal);
                    enemigoFinal.reaccionarAColision(proyectil);
                    puntaje += enemigoFinal.getValorPuntaje();
                }
            }
        }
    }

    @Override
    protected void actualizarPuntaje() {
        // Actualizar UI del puntaje actual
    }

    private void reiniciarJuego() {
        // 1. Limpiamos todas las listas del juego viejo
        oleada.clear();
        escudos.clear();
        proyectilesHeroe.clear();
        proyectilesEnemigos.clear();
        enemigoFinal = null;

        // 2. Reseteamos los marcadores principales
        puntaje = 0;
        tiempoJuego = 0;
        tiempoGameOver = 0;
        escalaGameOver = 0.0;

        // 3. Volvemos a ejecutar tu lógica de inicio de oleadas y jugador
        // (Llamamos a tu método existente para rearmar el mapa desde cero)
        gameStartup();

        // 4. Cambiamos el estado directo a jugar (nos saltamos la carga inicial)
        estadoActual = Estado.JUGANDO;
    }
}

