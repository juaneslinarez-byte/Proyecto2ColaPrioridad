package interfaz;

import logica.GestorCola;
import modelos.Usuario;
import modelos.Documento;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel para la gestion de documentos
 * @author Proyecto2ColaPrioridad Juanes
 */
public class PanelDocumentos extends JPanel {
    private GestorCola gestor;
    private JComboBox<String> comboUsuarios;
    private JTable tablaDocumentos;
    private DefaultTableModel modeloTabla;
    
    /**
     * Constructor del panel de documentos
     * @param gestor gestor de cola
     */
    public PanelDocumentos(GestorCola gestor) {
        this.gestor = gestor;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        inicializarComponentes();
    }
    
    /**
     * Inicializa los componentes del panel
     */
    private void inicializarComponentes() {
        // Panel superior con titulo y selector de usuario
        JPanel panelSuperior = crearPanelSuperior();
        add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central con la tabla
        JPanel panelCentral = crearPanelTabla();
        add(panelCentral, BorderLayout.CENTER);
        
        // Panel inferior con botones
        JPanel panelBotones = crearPanelBotones();
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    /**
     * Crea el panel superior con titulo y selector de usuario
     * @return panel superior
     */
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Titulo
        JLabel lblTitulo = new JLabel("Gestion de Documentos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(lblTitulo, BorderLayout.WEST);
        
        // Panel de seleccion de usuario
        JPanel panelSelector = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSelector.setBackground(Color.WHITE);
        
        JLabel lblUsuario = new JLabel("Seleccionar usuario:");
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 12));
        
        comboUsuarios = new JComboBox<>();
        comboUsuarios.setPreferredSize(new Dimension(200, 25));
        comboUsuarios.addActionListener(e -> actualizarTabla());
        
        panelSelector.add(lblUsuario);
        panelSelector.add(comboUsuarios);
        
        panel.add(panelSelector, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Crea el panel con la tabla de documentos
     * @return panel con tabla
     */
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Crear modelo de tabla
        String[] columnas = {"Nombre", "Tamaño (pag)", "Tipo", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla no editable
            }
        };
        
        // Crear tabla
        tablaDocumentos = new JTable(modeloTabla);
        tablaDocumentos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaDocumentos.setRowHeight(25);
        tablaDocumentos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tablaDocumentos);
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
        
        // Boton crear documento
        JButton btnCrear = crearBoton("Crear Documento", new Color(46, 204, 113));
        btnCrear.addActionListener(e -> mostrarDialogoCrear());
        
        // Boton enviar a cola normal
        JButton btnEncolarNormal = crearBoton("Enviar a Cola", new Color(52, 152, 219));
        btnEncolarNormal.addActionListener(e -> encolarDocumento(false));
        
        // Boton enviar a cola prioritario
        JButton btnEncolarPrioritario = crearBoton("Enviar Prioritario", new Color(155, 89, 182));
        btnEncolarPrioritario.addActionListener(e -> encolarDocumento(true));
        
        // Boton eliminar documento creado
        JButton btnEliminar = crearBoton("Eliminar", new Color(231, 76, 60));
        btnEliminar.addActionListener(e -> eliminarDocumento());
        
        panel.add(btnCrear);
        panel.add(btnEncolarNormal);
        panel.add(btnEncolarPrioritario);
        panel.add(btnEliminar);
        
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
     * Muestra el dialogo para crear un nuevo documento
     */
    private void mostrarDialogoCrear() {
        String usuarioSeleccionado = (String) comboUsuarios.getSelectedItem();
        
        if (usuarioSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un usuario",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Crear panel con campos de entrada
        JPanel panelForm = new JPanel(new GridLayout(4, 2, 10, 10));
        
        JLabel lblNombre = new JLabel("Nombre del documento:");
        JTextField txtNombre = new JTextField();
        
        JLabel lblTamaño = new JLabel("Tamaño (paginas):");
        JSpinner spinnerTamaño = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        
        JLabel lblTipo = new JLabel("Tipo de documento:");
        String[] tipos = {"PDF", "Word", "Excel", "Texto", "Imagen"};
        JComboBox<String> comboTipo = new JComboBox<>(tipos);
        
        panelForm.add(lblNombre);
        panelForm.add(txtNombre);
        panelForm.add(lblTamaño);
        panelForm.add(spinnerTamaño);
        panelForm.add(lblTipo);
        panelForm.add(comboTipo);
        
        // Mostrar dialogo
        int resultado = JOptionPane.showConfirmDialog(
            this,
            panelForm,
            "Crear Nuevo Documento",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );
        
        // Si el usuario presiono OK
        if (resultado == JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText().trim();
            
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar un nombre",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int tamaño = (int) spinnerTamaño.getValue();
            String tipo = (String) comboTipo.getSelectedItem();
            
            // Crear documento
            Documento nuevoDoc = new Documento(nombre, tamaño, tipo);
            
            if (gestor.crearDocumento(usuarioSeleccionado, nuevoDoc)) {
                actualizarTabla();
                JOptionPane.showMessageDialog(this, "Documento creado correctamente",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al crear documento",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Encola el documento seleccionado
     * @param esPrioritario si debe enviarse con prioridad
     */
    private void encolarDocumento(boolean esPrioritario) {
        String usuarioSeleccionado = (String) comboUsuarios.getSelectedItem();
        int filaSeleccionada = tablaDocumentos.getSelectedRow();
        
        if (usuarioSeleccionado == null || filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un documento",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Obtener informacion del documento
        String nombreDoc = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        String estado = (String) modeloTabla.getValueAt(filaSeleccionada, 3);
        
        // Verificar que no este ya en cola
        if (estado.equals("En Cola")) {
            JOptionPane.showMessageDialog(this, "El documento ya esta en la cola de impresion",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Buscar el documento en la lista del usuario
        ArrayList<Documento> documentos = gestor.getDocumentosUsuario(usuarioSeleccionado);
        Documento docSeleccionado = null;
        
        for (Documento doc : documentos) {
            if (doc.getNombre().equals(nombreDoc)) {
                docSeleccionado = doc;
                break;
            }
        }
        
        if (docSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Documento no encontrado",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Encolar el documento
        if (gestor.encolarDocumento(usuarioSeleccionado, docSeleccionado, esPrioritario)) {
            actualizarTabla();
            
            String tipoMensaje = esPrioritario ? "prioritario" : "normal";
            JOptionPane.showMessageDialog(this,
                "Documento enviado a cola (" + tipoMensaje + ")",
                "Exito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error al encolar documento",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Elimina el documento seleccionado (solo si no esta en cola)
     */
    private void eliminarDocumento() {
        String usuarioSeleccionado = (String) comboUsuarios.getSelectedItem();
        int filaSeleccionada = tablaDocumentos.getSelectedRow();
        
        if (usuarioSeleccionado == null || filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un documento",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Obtener informacion del documento
        String nombreDoc = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        String estado = (String) modeloTabla.getValueAt(filaSeleccionada, 3);
        
        // Verificar que no este en cola
        if (estado.equals("En Cola")) {
            JOptionPane.showMessageDialog(this,
                "No puede eliminar un documento que esta en cola de impresion",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Confirmar eliminacion
        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Esta seguro de eliminar este documento?",
            "Confirmar eliminacion",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            // Buscar el documento
            ArrayList<Documento> documentos = gestor.getDocumentosUsuario(usuarioSeleccionado);
            Documento docAEliminar = null;
            
            for (Documento doc : documentos) {
                if (doc.getNombre().equals(nombreDoc)) {
                    docAEliminar = doc;
                    break;
                }
            }
            
            if (docAEliminar != null &&
                gestor.eliminarDocumentoCreado(usuarioSeleccionado, docAEliminar)) {
                actualizarTabla();
                JOptionPane.showMessageDialog(this, "Documento eliminado correctamente",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar documento",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Actualiza el combo de usuarios y la tabla
     */
    public void actualizarPanel() {
        // Actualizar combo de usuarios
        comboUsuarios.removeAllItems();
        
        for (Usuario usuario : gestor.getUsuarios()) {
            comboUsuarios.addItem(usuario.getNombreUsuario());
        }
        
        // Actualizar tabla
        actualizarTabla();
    }
    
    /**
     * Actualiza la tabla con los documentos del usuario seleccionado
     */
    private void actualizarTabla() {
        // Limpiar tabla
        modeloTabla.setRowCount(0);
        
        String usuarioSeleccionado = (String) comboUsuarios.getSelectedItem();
        
        if (usuarioSeleccionado == null) {
            return;
        }
        
        // Obtener documentos del usuario
        ArrayList<Documento> documentos = gestor.getDocumentosUsuario(usuarioSeleccionado);
        
        if (documentos == null) {
            return;
        }
        
        // Agregar documentos a la tabla
        for (Documento doc : documentos) {
            String nombre = doc.getNombre();
            int tamaño = doc.getTamaño();
            String tipo = doc.getTipo();
            String estado = doc.isEnColaImpresion() ? "En Cola" : "Creado";
            
            modeloTabla.addRow(new Object[]{nombre, tamaño, tipo, estado});
        }
    }
}
