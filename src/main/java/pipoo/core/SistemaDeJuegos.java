package pipoo.core;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import pipoo.core.configuracion.*;
import pipoo.loderunner.LodeRunner;
import pipoo.pong.Pong;
import pipoo.spaceinvaders.SpaceInvaders;


public class SistemaDeJuegos extends JPanel implements ActionListener {
    private Juego juegoActual;
    Thread hiloJuego;

    private JButton btnPong, btnConfigP, btnRankingP;
    private JButton btnLodeRunner, btnConfigLR, btnRankingLR;
    private JButton btnSpaceInvaders, btnConfigSI, btnRankingSI;
    private JTextField txtNombreUsuario;

    private CardLayout cardLayout;

    private ConfiguracionPong configPong = new ConfiguracionPong();
    private ConfiguracionSI configSI = new ConfiguracionSI();
    private ConfiguracionLR configLR = new ConfiguracionLR();

    private JPanel panelInicio, panelJuegos, panelPong, panelLodeRunner, panelSpaceInvaders;
    private JTextArea areaTexto;
    Color fondoAzul = new Color(12,6,46); // azul suave


    public SistemaDeJuegos() {
        setLayout(new GridLayout(1, 3, 10, 10));

        cardLayout = new CardLayout();
        this.setLayout(cardLayout);

        panelInicio = new JPanel();
        panelInicio.setBackground(fondoAzul);

        panelInicio.setLayout(new BorderLayout());

        // panel con titulo
        JPanel panelNorte = new JPanel();
        panelNorte.setBackground(fondoAzul);
        JLabel lblTitulo;
        try {
            BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/pipoo/core/imagenes/pipoo_arcade.png"));
            Image scaled = img.getScaledInstance(400, 170, Image.SCALE_SMOOTH);
            lblTitulo = new JLabel(new ImageIcon(scaled));
        } catch (Exception ex) {
            lblTitulo = new JLabel("PIPOO ARCADE", SwingConstants.CENTER);
            lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        }
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panelNorte.add(lblTitulo);
        panelInicio.add(panelNorte, BorderLayout.NORTH);

        JPanel panelCentralContenedor = new JPanel(new BorderLayout(0, 15));
        panelCentralContenedor.setBackground(fondoAzul);

        // panel con los 3 juegos
        panelJuegos = new JPanel();
        panelJuegos.setBackground(fondoAzul);
        panelJuegos.setLayout(new GridLayout(1, 3, 15, 0));

        // panel pong
        panelPong = crearPanelJuego("PONG", "/pipoo/core/imagenes/pong.png");
        btnPong = new JButton("¡Jugar!");
        btnConfigP = new JButton("Configuración");
        btnRankingP = new JButton("Ranking");
        registrarBotones(panelPong, btnPong, btnConfigP, btnRankingP, new Color(180, 30, 10));
        //panel configuracion PONG
        PanelConfiguracionPong guiPong = new PanelConfiguracionPong(configPong, this, cardLayout);

        // panel lode runner
        panelLodeRunner = crearPanelJuego("LODE RUNNER", "/pipoo/core/imagenes/lode_runner.png");
        btnLodeRunner = new JButton("¡Jugar!");
        btnConfigLR = new JButton("Configuración");
        btnRankingLR = new JButton("Ranking");
        registrarBotones(panelLodeRunner, btnLodeRunner, btnConfigLR, btnRankingLR, new Color(200, 140, 20));

        //panel configuracion lode runner
        PanelConfiguracionLR guiLR = new PanelConfiguracionLR(configLR, this, cardLayout);


        // panel space invaders
        panelSpaceInvaders = crearPanelJuego("SPACE INVADERS", "/pipoo/core/imagenes/space_invaders.png"); // por ahora sin imagen
        btnSpaceInvaders = new JButton("¡Jugar!");
        btnConfigSI = new JButton("Configuración");
        btnRankingSI = new JButton("Ranking");
        registrarBotones(panelSpaceInvaders, btnSpaceInvaders, btnConfigSI, btnRankingSI,new Color(60, 120, 200) );

        //panel configuracion Space Invaders
        PanelConfiguracionSI guiSI = new PanelConfiguracionSI(configSI, this, cardLayout);


        //agregar cada panel de juego al panel de juegos
        panelJuegos.add(panelPong);
        panelJuegos.add(panelSpaceInvaders);
        panelJuegos.add(panelLodeRunner);

        panelInicio.add(panelJuegos, BorderLayout.CENTER);

        txtNombreUsuario = new JTextField("Jugador 1", 15);

        // Panel para el nombre
        JPanel panelNombre = new JPanel();
        panelNombre.setLayout(new BoxLayout(panelNombre, BoxLayout.Y_AXIS));
        panelNombre.setBackground(new Color(12,6,46));

        try {
            BufferedImage imgEnter = ImageIO.read(getClass().getResourceAsStream("/pipoo/core/imagenes/enter_name.png"));
            Image scaledEnter = imgEnter.getScaledInstance(115, 15, Image.SCALE_SMOOTH);
            JLabel lblEnterName = new JLabel(new ImageIcon(scaledEnter));
            lblEnterName.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelNombre.add(lblEnterName);
        } catch (Exception ex) {
            JLabel lblIdentificacion = new JLabel("Ingresa tu Nombre: ");
            lblIdentificacion.setForeground(Color.WHITE);
            lblIdentificacion.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelNombre.add(lblIdentificacion);
        }

        txtNombreUsuario.setMaximumSize(new Dimension(200, 30));
        txtNombreUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelNombre.add(Box.createVerticalStrut(5));
        panelNombre.add(txtNombreUsuario);

        // Centro
        panelCentralContenedor.add(panelNombre, BorderLayout.NORTH);
        panelCentralContenedor.add(panelJuegos, BorderLayout.CENTER);

        // Contenedor central al panel de inicio principal
        panelInicio.add(panelCentralContenedor, BorderLayout.CENTER);

        //card layout
        this.add(panelInicio, "INICIO");
        this.add(guiPong, "CONFIG_PONG");
        this.add(guiSI, "CONFIG_SPACE");
        this.add(guiLR, "CONFIG_LODE");


        //RANKING: NOMBRES, COLORES, PANEL, BOTON PARA VOLVER AL INICIO
        this.areaTexto = new JTextArea(20, 50);
        this.areaTexto.setEditable(false);
        this.areaTexto.setBackground(Color.BLACK);
        this.areaTexto.setForeground(Color.WHITE);
        this.areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JPanel vistaRanking = new JPanel(new BorderLayout());
        vistaRanking.add(new JScrollPane(this.areaTexto), BorderLayout.CENTER);
        JButton btnVolver = new JButton("Volver al Menu");
        btnVolver.addActionListener(e -> cardLayout.show(this, "INICIO"));
        vistaRanking.add(btnVolver, BorderLayout.SOUTH);
        this.add(vistaRanking, "PANTALLA_RANKING");

    }

    //metodos auxiliares para creacion de paneles
    private JPanel crearPanelJuego(String titulo, String rutaImagen) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(12,6,46));
        if (rutaImagen != null) {
            try {
                BufferedImage img = ImageIO.read(getClass().getResourceAsStream(rutaImagen));
                Image scaled = img.getScaledInstance(130, 50, Image.SCALE_SMOOTH);
                JLabel lblImg = new JLabel(new ImageIcon(scaled));
                lblImg.setAlignmentX(Component.CENTER_ALIGNMENT);
                panel.add(lblImg);
            } catch (Exception ex) {
                panel.setBorder(BorderFactory.createTitledBorder(titulo));
            }
        } else {
            panel.setBorder(BorderFactory.createTitledBorder(titulo));
        }
        return panel;
    }

    private void registrarBotones(JPanel panel, JButton btnJugar, JButton btnConfig, JButton btnRanking, Color colorBase) {
        btnJugar.addActionListener(this);
        btnConfig.addActionListener(this);
        btnRanking.addActionListener(this);

        Dimension tamBoton = new Dimension(160, 35);


        for (JButton btn : new JButton[]{btnJugar, btnConfig, btnRanking}) {
            btn.setPreferredSize(tamBoton);
            btn.setMaximumSize(tamBoton);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setBackground(colorBase);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        }

        panel.add(Box.createVerticalStrut(20));
        panel.add(btnJugar);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnConfig);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnRanking);
    }

    private void prepararJuego(Juego juego, Configuracion config) {
        if (config != null) {
            juego.setConfiguracion(config);
            juego.aplicarModoPantalla(config.isPantallaCompleta());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object origen = e.getSource();
        boolean esLanzamientoDeJuego = false; // Bandera para controlar el hilo
        //lanzamiento de juegos
        if (origen == btnPong) {
            String nombreUsuario = txtNombreUsuario.getText();
            configPong.setNombreJ1(nombreUsuario);
            juegoActual = new Pong();
            prepararJuego(juegoActual, configPong);
            esLanzamientoDeJuego = true;
        } else if (origen == btnLodeRunner) {
            String nombreUsuario = txtNombreUsuario.getText();
            configLR.setNombreJ1(nombreUsuario);
            juegoActual = new LodeRunner();
            prepararJuego(juegoActual, configLR);
            esLanzamientoDeJuego = true;
        } else if (origen == btnSpaceInvaders) {
            String nombreUsuario = txtNombreUsuario.getText();
            configSI.setNombreJ1(nombreUsuario);
            juegoActual = new SpaceInvaders();
            prepararJuego(juegoActual, configSI);
            esLanzamientoDeJuego = true;
        } else if (origen == btnConfigP) {
            cardLayout.show(this, "CONFIG_PONG");
        } else if (origen == btnConfigSI) {
            cardLayout.show(this, "CONFIG_SPACE");
        } else if (origen == btnConfigLR) {
            cardLayout.show(this, "CONFIG_LODE");
        }

        if (origen == btnRankingP || origen == btnRankingSI || origen == btnRankingLR) {

                if (origen == btnRankingP) {
                    Ranking manager = new Ranking("ranking_pong.dat");
                    manager.cargarRanking();
                    this.areaTexto.setText(manager.toStrOrdenado());

                } else if (origen == btnRankingSI) {
                    Ranking manager = new Ranking("ranking_si.dat");
                    manager.cargarRanking();
                    this.areaTexto.setText(manager.toStrOrdenado());

                } else if (origen == btnRankingLR) {
                    Ranking lr = new Ranking("ranking_lr.dat");
                    lr.cargarRanking();
                    this.areaTexto.setText(
                            lr.toStrOrdenado("TOP 10 --- ARCADE MODE ", "ARCADE") + "\n\n" + lr.toStrOrdenado("TOP 10 --- STAGE MODE", "INDIVIDUAL")
                    );
                }
                this.areaTexto.setCaretPosition(0);
                cardLayout.show(this, "PANTALLA_RANKING");
            }
        if (esLanzamientoDeJuego && juegoActual != null) {
            hiloJuego = new Thread(() -> juegoActual.run(1.0 / 60.0));
            hiloJuego.start();
        }
    }

    public static void main(String[] args) {
        JFrame ventanaMenu = new JFrame("PIPOO Menú Principal");
        ventanaMenu.add(new SistemaDeJuegos());
        ventanaMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaMenu.pack();
        ventanaMenu.setLocationRelativeTo(null);
        ventanaMenu.setVisible(true);
    }
}
