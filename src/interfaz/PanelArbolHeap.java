package interfaz;

import logica.GestorCola;
import modelos.RegistroCola;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.view.Viewer;
import javax.swing.*;
import java.awt.*;

/**
 * Panel que muestra el heap como arbol binario usando GraphStream
 * @author Proyecto2ColaPrioridad Juanes
 */
public class PanelArbolHeap extends JPanel {
    private GestorCola gestor;
    private Graph graph;
    private Viewer viewer;
    private JPanel panelGrafo;
    
    /**
     * Constructor del panel de arbol heap
     * @param gestor gestor de cola
     */
    public PanelArbolHeap(GestorCola gestor) {
        this.gestor = gestor;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        inicializarComponentes();
    }
    
    /**
     * Inicializa los componentes del panel
     */
    private void inicializarComponentes() {
        // Panel superior con titulo
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTitulo.setBackground(Color.WHITE);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblTitulo = new JLabel("Cola de Impresion - Vista Arbol Binario");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        panelTitulo.add(lblTitulo);
        
        add(panelTitulo, BorderLayout.NORTH);
        
        // Panel central para el grafo
        panelGrafo = new JPanel(new BorderLayout());
        panelGrafo.setBackground(Color.WHITE);
        panelGrafo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        add(panelGrafo, BorderLayout.CENTER);
        
        // Panel inferior con botones
        JPanel panelBotones = crearPanelBotones();
        add(panelBotones, BorderLayout.SOUTH);
        
        // Configurar GraphStream
        configurarGraphStream();
    }
    
    /**
     * Configura GraphStream con estilos
     */
    private void configurarGraphStream() {
        // Crear grafo
        System.setProperty("org.graphstream.ui", "swing");
        graph = new SingleGraph("HeapTree");
        
        // Desactivar auto-layout (usaremos posiciones manuales si es necesario)
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");
        
        // Aplicar estilos CSS
        String estiloCSS = 
            "node {" +
            "   fill-color: #3498db;" +
            "   size: 30px;" +
            "   text-size: 14px;" +
            "   text-color: white;" +
            "   text-alignment: center;" +
            "}" +
            "edge {" +
            "   fill-color: #95a5a6;" +
            "   arrow-size: 10px, 5px;" +
            "}";
        
        graph.setAttribute("ui.stylesheet", estiloCSS);
        
        // Crear viewer
        viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();
        
        // Agregar vista al panel
        panelGrafo.add((Component) viewer.addDefaultView(false), BorderLayout.CENTER);
    }
    
    /**
     * Crea el panel con botones de accion
     * @return panel con botones
     */
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Boton actualizar arbol
        JButton btnActualizar = crearBoton("Actualizar Arbol", new Color(52, 152, 219));
        btnActualizar.addActionListener(e -> actualizarArbol());
        
        panel.add(btnActualizar);
        
        return panel;
    }
    
    /**
     * Crea un boton estilizado
     * @param texto texto del boton
     * @param color color del boton
     * @return boton configurado
     */
    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(160, 35));
        
        return boton;
    }
    
    /**
     * Actualiza el arbol con los elementos actuales del heap
     */
    public void actualizarArbol() {
        // Limpiar el grafo
        graph.clear();
        
        // Obtener elementos del heap
        RegistroCola[] elementos = gestor.obtenerElementosCola();
        
        if (elementos == null || elementos.length == 0) {
            // Si no hay elementos, mostrar mensaje
            Node nodoVacio = graph.addNode("vacio");
            nodoVacio.setAttribute("ui.label", "Cola vacia");
            return;
        }
        
        // Construir el arbol
        construirArbol(elementos);
    }
    
    /**
     * Construye el arbol binario a partir de los elementos del heap
     * @param elementos array con los registros del heap
     */
    private void construirArbol(RegistroCola[] elementos) {
        // Agregar nodos
        for (int i = 0; i < elementos.length; i++) {
            Node nodo = graph.addNode(String.valueOf(i));
            
            // Etiqueta con la informacion del documento
            String etiqueta = elementos[i].getNombreDocumento() + 
                             "\n[" + elementos[i].getEtiquetaTiempo() + "]";
            nodo.setAttribute("ui.label", etiqueta);
            
            // Agregar arista al padre (si no es la raiz)
            if (i > 0) {
                int indicePadre = (i - 1) / 2;
                String idArista = i + "-" + indicePadre;
                graph.addEdge(idArista, String.valueOf(indicePadre), String.valueOf(i), true);
            }
        }
    }
}
