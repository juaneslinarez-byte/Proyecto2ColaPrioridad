package interfaz;

import logica.GestorCola;
import modelos.RegistroCola;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Panel que muestra la cola de impresion de forma secuencial
 * @author Proyecto2ColaPrioridad Juanes
 */
public class PanelColaSecuencial extends JPanel {
    private GestorCola gestor;
    private JTable tablaCola;
    private DefaultTableModel modeloTabla;
    
    /**
     * Constructor del panel de cola secuencial
     * @param gestor gestor de cola
     */
    public PanelColaSecuencial(GestorCola gestor) {
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
        
        JLabel lblTitulo = new JLabel("Cola de Impresion - Vista Secuencial");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        panelTitulo.add(lblTitulo);
        
        add(panelTitulo, BorderLayout.NORTH);
        
        // Panel central con la tabla
        JPanel panelCentral = crearPanelTabla();
        add(panelCentral, BorderLayout.CENTER);
        
        // Panel inferior con botones
        JPanel panelBotones = crearPanelBotones();
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    /**
     * Crea el panel con la tabla de la cola
     * @return panel con tabla
     */
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Crear modelo de tabla
        String[] columnas = {"Documento", "Tamaño (pag)", "Tipo", "Etiqueta Tiempo"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Crear tabla
        tablaCola = new JTable(modeloTabla);
        tablaCola.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCola.setRowHeight(25);
        tablaCola.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tablaCola);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Crea el panel con botones de accion
     * @return panel con botones
     */
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Boton liberar impresora
        JButton btnLiberar = crearBoton("Liberar Impresora", new Color(46, 204, 113));
        btnLiberar.addActionListener(e -> liberarImpresora());
        
        // Boton eliminar de cola
        JButton btnEliminar = crearBoton("Eliminar de Cola", new Color(231, 76, 60));
        btnEliminar.addActionListener(e -> eliminarDeCola());
        
        // Boton actualizar
        JButton btnActualizar = crearBoton("Actualizar", new Color(52, 152, 219));
        btnActualizar.addActionListener(e -> actualizarTabla());
        
        panel.add(btnLiberar);
        panel.add(btnEliminar);
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
     * Libera el siguiente documento de la cola (elimina minimo del heap)
     */
    private void liberarImpresora() {
        RegistroCola impreso = gestor.liberarImpresora();
        
        if (impreso != null) {
            actualizarTabla();
            JOptionPane.showMessageDialog(this,
                "Documento impreso: " + impreso.getNombreDocumento() + 
                "\nTamaño: " + impreso.getTamaño() + " paginas" +
                "\nTipo: " + impreso.getTipo(),
                "Impresion Completada", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "La cola de impresion esta vacia",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Elimina un documento de la cola usando su nombre
     */
    private void eliminarDeCola() {
        int filaSeleccionada = tablaCola.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar un documento de la cola",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Obtener nombre del documento
        String nombreDoc = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        
        // Confirmar eliminacion
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Esta seguro de eliminar el documento de la cola?\n" + nombreDoc,
            "Confirmar eliminacion",
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            // Necesitamos el usuario propietario del documento
            // Para simplificar, pedimos el nombre del usuario
            String usuario = JOptionPane.showInputDialog(this,
                "Ingrese el nombre del usuario propietario del documento:",
                "Nombre de Usuario",
                JOptionPane.QUESTION_MESSAGE);
            
            if (usuario != null && !usuario.trim().isEmpty()) {
                if (gestor.eliminarDocumentoCola(usuario, nombreDoc)) {
                    actualizarTabla();
                    JOptionPane.showMessageDialog(this,
                        "Documento eliminado de la cola",
                        "Exito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Error al eliminar el documento",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    /**
     * Actualiza la tabla con los elementos actuales de la cola
     */
    public void actualizarTabla() {
        // Limpiar tabla
        modeloTabla.setRowCount(0);
        
        // Obtener elementos de la cola desde el heap
        RegistroCola[] elementos = gestor.obtenerElementosCola();
        
        if (elementos == null || elementos.length == 0) {
            return;
        }
        
        // Agregar elementos a la tabla (ya vienen ordenados por prioridad)
        for (RegistroCola registro : elementos) {
            String nombre = registro.getNombreDocumento();
            int tamaño = registro.getTamaño();
            String tipo = registro.getTipo();
            long etiqueta = registro.getEtiquetaTiempo();
            
            modeloTabla.addRow(new Object[]{nombre, tamaño, tipo, etiqueta});
        }
    }
}
