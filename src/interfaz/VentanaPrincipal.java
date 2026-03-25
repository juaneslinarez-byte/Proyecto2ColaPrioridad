package interfaz;

import logica.GestorCola;
import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal de la aplicacion
 * @author Proyecto2ColaPrioridad Juanes
 */
public class VentanaPrincipal extends JFrame {
    private GestorCola gestor;
    private CardLayout cardLayout;
    private JPanel panelContenido;
    
    // Paneles de la aplicacion
    private PanelUsuarios panelUsuarios;
    private PanelDocumentos panelDocumentos;
    private PanelColaSecuencial panelColaSecuencial;
    private PanelArbolHeap panelArbolHeap;
    
    /**
     * Constructor de la ventana principal
     */
    public VentanaPrincipal() {
        this.gestor = new GestorCola();
        
        // Iniciar el reloj automaticamente
        gestor.getReloj().iniciar();
        
        // Configuracion basica de la ventana
        setTitle("Sistema de Cola de Impresion");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Inicializar componentes
        inicializarComponentes();
    }
    
    /**
     * Inicializa todos los componentes de la interfaz
     */
    private void inicializarComponentes() {
        // Panel superior con menu de navegacion
        JPanel panelSuperior = crearPanelNavegacion();
        add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central con CardLayout para cambiar entre vistas
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        
        // Inicializar paneles
        panelUsuarios = new PanelUsuarios(gestor);
        panelDocumentos = new PanelDocumentos(gestor);
        panelColaSecuencial = new PanelColaSecuencial(gestor);
        panelArbolHeap = new PanelArbolHeap(gestor);
        
        // Agregar paneles al CardLayout
        panelContenido.add(crearPanelInicio(), "inicio");
        panelContenido.add(panelUsuarios, "usuarios");
        panelContenido.add(panelDocumentos, "documentos");
        panelContenido.add(panelColaSecuencial, "colaSecuencial");
        panelContenido.add(panelArbolHeap, "arbolHeap");
        
        add(panelContenido, BorderLayout.CENTER);
        
        // Panel inferior con informacion del reloj
        JPanel panelInferior = crearPanelInferior();
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    /**
     * Crea el panel de navegacion superior
     * @return panel con botones de navegacion
     */
    private JPanel crearPanelNavegacion() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(52, 73, 94));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Boton de inicio
        JButton btnInicio = crearBotonMenu("Inicio");
        btnInicio.addActionListener(e -> cardLayout.show(panelContenido, "inicio"));
        
        // Boton de usuarios
        JButton btnUsuarios = crearBotonMenu("Usuarios");
        btnUsuarios.addActionListener(e -> {
            panelUsuarios.actualizarTabla();
            cardLayout.show(panelContenido, "usuarios");
        });
        
        // Boton de documentos
        JButton btnDocumentos = crearBotonMenu("Documentos");
        btnDocumentos.addActionListener(e -> {
            panelDocumentos.actualizarPanel();
            cardLayout.show(panelContenido, "documentos");
        });
        
        // Boton de cola secuencial
        JButton btnColaSecuencial = crearBotonMenu("Cola");
        btnColaSecuencial.addActionListener(e -> {
            panelColaSecuencial.actualizarTabla();
            cardLayout.show(panelContenido, "colaSecuencial");
        });
        
        // Boton de arbol heap
        JButton btnArbolHeap = crearBotonMenu("Arbol");
        btnArbolHeap.addActionListener(e -> {
            panelArbolHeap.actualizarArbol();
            cardLayout.show(panelContenido, "arbolHeap");
        });
        
        panel.add(btnInicio);
        panel.add(btnUsuarios);
        panel.add(btnDocumentos);
        panel.add(btnColaSecuencial);
        panel.add(btnArbolHeap);
        
        return panel;
    }
    
    /**
     * Crea un boton estilizado para el menu
     * @param texto texto del boton
     * @return boton configurado
     */
    private JButton crearBotonMenu(String texto) {
        JButton boton = new JButton(texto);
        boton.setForeground(Color.WHITE);
        boton.setBackground(new Color(41, 128, 185));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(120, 35));
        
        return boton;
    }
    
    /**
     * Crea el panel de inicio con informacion de bienvenida
     * @return panel de inicio
     */
    private JPanel crearPanelInicio() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        JLabel lblTitulo = new JLabel("Sistema de Cola de Impresion");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        
        JLabel lblSubtitulo = new JLabel("Gestion de documentos con prioridades");
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 16));
        lblSubtitulo.setForeground(Color.GRAY);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        panel.add(lblTitulo, gbc);
        
        gbc.gridy = 1;
        panel.add(lblSubtitulo, gbc);
        
        return panel;
    }
    
    /**
     * Crea el panel inferior con informacion del reloj
     * @return panel inferior
     */
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JLabel lblReloj = new JLabel("Tiempo transcurrido: 0 ms");
        lblReloj.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Actualizar el reloj cada segundo
        Timer timer = new Timer(1000, e -> {
            long tiempo = gestor.getReloj().getTiempoTranscurrido();
            lblReloj.setText("Tiempo transcurrido: " + tiempo + " ms");
        });
        timer.start();
        
        panel.add(lblReloj);
        
        return panel;
    }
    
    /**
     * Metodo main para ejecutar la aplicacion
     * @param args argumentos de linea de comandos
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}
