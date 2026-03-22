package interfaz;

import logica.GestorCola;
import modelos.Usuario;
import modelos.TipoUsuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;

/**
 * Panel para la gestion de usuarios
 * @author Proyecto2ColaPrioridad Juanes
 */
public class PanelUsuarios extends JPanel {
    private GestorCola gestor;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    
    /**
     * Constructor del panel de usuarios
     * @param gestor gestor de cola
     */
    public PanelUsuarios(GestorCola gestor) {
        this.gestor = gestor;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        inicializarComponentes();
    }
    
    /**
     * Inicializa componentes del panel
     */
    private void inicializarComponentes() {
        // Panel superior con titulo
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTitulo.setBackground(Color.WHITE);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblTitulo = new JLabel("Gestion de Usuarios");
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
     * Crea el panel con la tabla de usuarios
     * @return panel con tabla
     */
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Crear modelo de tabla
        String[] columnas = {"Nombre de Usuario", "Tipo de Prioridad", "Documentos Creados"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla no editable
            }
        };
        
        // Crear tabla
        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaUsuarios.setRowHeight(25);
        tablaUsuarios.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        // Scroll pane para la tabla
        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
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
        
        // Boton agregar usuario
        JButton btnAgregar = crearBoton("Agregar Usuario", new Color(46, 204, 113));
        btnAgregar.addActionListener(e -> mostrarDialogoAgregar());
        
        // Boton eliminar usuario
        JButton btnEliminar = crearBoton("Eliminar Usuario", new Color(231, 76, 60));
        btnEliminar.addActionListener(e -> eliminarUsuarioSeleccionado());
        
        // Boton cargar CSV
        JButton btnCargarCSV = crearBoton("Cargar desde CSV", new Color(52, 152, 219));
        btnCargarCSV.addActionListener(e -> cargarUsuariosDesdeCSV());
        
        panel.add(btnAgregar);
        panel.add(btnEliminar);
        panel.add(btnCargarCSV);
        
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
     * Muestra el dialogo para agregar un nuevo usuario
     */
    private void mostrarDialogoAgregar() {
        // Crear panel con campos de entrada
        JPanel panelForm = new JPanel(new GridLayout(3, 2, 10, 10));
        
        JLabel lblNombre = new JLabel("Nombre de usuario:");
        JTextField txtNombre = new JTextField();
        
        JLabel lblTipo = new JLabel("Tipo de prioridad:");
        String[] opciones = {"Prioridad Alta", "Prioridad Media", "Prioridad Baja"};
        JComboBox<String> comboTipo = new JComboBox<>(opciones);
        
        panelForm.add(lblNombre);
        panelForm.add(txtNombre);
        panelForm.add(lblTipo);
        panelForm.add(comboTipo);
        
        // Mostrar dialogo
        int resultado = JOptionPane.showConfirmDialog(
            this,
            panelForm,
            "Agregar Nuevo Usuario",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );
        
        // Si el usuario presiono OK
        if (resultado == JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText().trim();
            
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar un nombre de usuario",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Determinar tipo de usuario
            TipoUsuario tipo;
            int seleccion = comboTipo.getSelectedIndex();
            switch (seleccion) {
                case 0:
                    tipo = TipoUsuario.PRIORIDAD_ALTA;
                    break;
                case 1:
                    tipo = TipoUsuario.PRIORIDAD_MEDIA;
                    break;
                case 2:
                default:
                    tipo = TipoUsuario.PRIORIDAD_BAJA;
                    break;
            }
            
            // Crear y agregar usuario
            Usuario nuevoUsuario = new Usuario(nombre, tipo);
            gestor.agregarUsuario(nuevoUsuario);
            
            // Actualizar tabla
            actualizarTabla();
            
            JOptionPane.showMessageDialog(this, "Usuario agregado correctamente",
                "Exito", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Elimina el usuario seleccionado en la tabla
     */
    private void eliminarUsuarioSeleccionado() {
        int filaSeleccionada = tablaUsuarios.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un usuario",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Confirmar eliminacion
        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Esta seguro de eliminar este usuario?",
            "Confirmar eliminacion",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            String nombreUsuario = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
            
            if (gestor.eliminarUsuario(nombreUsuario)) {
                actualizarTabla();
                JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar usuario",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Carga usuarios desde un archivo CSV usando JFileChooser
     */
    private void cargarUsuariosDesdeCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo CSV de usuarios");
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        
        // Filtro para archivos CSV
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".csv");
            }
            
            @Override
            public String getDescription() {
                return "Archivos CSV (*.csv)";
            }
        });
        
        int resultado = fileChooser.showOpenDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            
            try {
                int cantidadCargados = gestor.cargarUsuariosCSV(archivo.getAbsolutePath());
                actualizarTabla();
                
                JOptionPane.showMessageDialog(this,
                    "Se cargaron " + cantidadCargados + " usuarios correctamente",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error al cargar el archivo: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Actualiza la tabla con los usuarios actuales
     */
    public void actualizarTabla() {
        // Limpiar tabla
        modeloTabla.setRowCount(0);
        
        // Agregar usuarios a la tabla
        for (Usuario usuario : gestor.getUsuarios()) {
            String nombre = usuario.getNombreUsuario();
            String tipo = obtenerTextoTipo(usuario.getTipo());
            int cantidadDocs = usuario.getDocumentosCreados().size();
            
            modeloTabla.addRow(new Object[]{nombre, tipo, cantidadDocs});
        }
    }
    
    /**
     * Convierte el tipo de usuario a texto legible
     * @param tipo tipo de usuario
     * @return texto descriptivo
     */
    private String obtenerTextoTipo(TipoUsuario tipo) {
        switch (tipo) {
            case PRIORIDAD_ALTA:
                return "Prioridad Alta";
            case PRIORIDAD_MEDIA:
                return "Prioridad Media";
            case PRIORIDAD_BAJA:
                return "Prioridad Baja";
            default:
                return "Desconocido";
        }
    }
}
