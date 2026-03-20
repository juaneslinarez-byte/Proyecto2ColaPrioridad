package logica;

import modelos.Usuario;
import modelos.Documento;
import modelos.RegistroCola;
import modelos.TipoUsuario;
import estructuras.MonticuloBinario;
import estructuras.TablaHash;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Gestor principal que coordina el sistema de cola de impresion
 * Integra el Monticulo Binario y la Tabla Hash
 * @author Proyecto2ColaPrioridad Juanes
 */
public class GestorCola {
    private MonticuloBinario heap;
    private TablaHash tabla;
    private Reloj reloj;
    private ArrayList<Usuario> usuarios;
    
    /**
     * Constructor del gestor de cola
     */
    public GestorCola() {
        this.heap = new MonticuloBinario();
        this.tabla = new TablaHash();
        this.reloj = new Reloj();
        this.usuarios = new ArrayList<>();
        
        // Iniciar el reloj automaticamente
        reloj.iniciar();
    }
    
    //  GESTION DE USUARIOS
    
    /**
     * Agrega un usuario al sistema
     * @param usuario usuario a agregar
     */
    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }
    
    /**
     * Elimina un usuario del sistema
     * Elimina sus documentos creados pero NO los que estan en cola
     * @param nombreUsuario nombre del usuario a eliminar
     * @return true si se elimino, false si no se encontro
     */
    public boolean eliminarUsuario(String nombreUsuario) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getNombreUsuario().equals(nombreUsuario)) {
                usuarios.remove(i);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Busca un usuario por su nombre
     * @param nombreUsuario nombre del usuario
     * @return usuario encontrado o null si no existe
     */
    public Usuario buscarUsuario(String nombreUsuario) {
        for (Usuario usuario : usuarios) {
            if (usuario.getNombreUsuario().equals(nombreUsuario)) {
                return usuario;
            }
        }
        return null;
    }
    
    /**
     * Obtiene la lista de todos los usuarios
     * @return lista de usuarios
     */
    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }
    
    // GESTION DE DOCUMENTOS 
    
    /**
     * Crea un documento para un usuario
     * @param nombreUsuario nombre del usuario
     * @param documento documento a crear
     * @return true si se creo, false si el usuario no existe
     */
    public boolean crearDocumento(String nombreUsuario, Documento documento) {
        Usuario usuario = buscarUsuario(nombreUsuario);
        if (usuario == null) {
            return false;
        }
        usuario.agregarDocumento(documento);
        return true;
    }
    
    /**
     * Elimina un documento creado (no encolado) de un usuario
     * @param nombreUsuario nombre del usuario
     * @param documento documento a eliminar
     * @return true si se elimino, false si no se encontro
     */
    public boolean eliminarDocumentoCreado(String nombreUsuario, Documento documento) {
        Usuario usuario = buscarUsuario(nombreUsuario);
        if (usuario == null) {
            return false;
        }
        return usuario.eliminarDocumento(documento);
    }
    
    /**
     * Obtiene los documentos de un usuario
     * @param nombreUsuario nombre del usuario
     * @return lista de documentos o null si el usuario no existe
     */
    public ArrayList<Documento> getDocumentosUsuario(String nombreUsuario) {
        Usuario usuario = buscarUsuario(nombreUsuario);
        if (usuario == null) {
            return null;
        }
        return usuario.getDocumentosCreados();
    }
    
    //OPERACIONES DE COLA
    
    /**
     * Aplica el modificador de prioridad segun el tipo de usuario
     * @param tiempo tiempo base
     * @param tipo tipo de usuario
     * @return tiempo modificado
     */
    private long aplicarModificadorPrioridad(long tiempo, TipoUsuario tipo) {
        switch (tipo) {
            case PRIORIDAD_ALTA:
                return (long)(tiempo * 0.5);  // Se adelanta mucho
            case PRIORIDAD_MEDIA:
                return (long)(tiempo * 0.75); // Se adelanta poco
            case PRIORIDAD_BAJA:
            default:
                return tiempo; // No se adelanta
        }
    }
    
    /**
     * Encola un documento para impresion
     * @param nombreUsuario nombre del usuario
     * @param documento documento a encolar
     * @param esPrioritario si se debe aplicar prioridad especial
     * @return true si se encolo, false si el usuario no existe
     */
    public boolean encolarDocumento(String nombreUsuario, Documento documento, boolean esPrioritario) {
        // Buscar el usuario
        Usuario usuario = buscarUsuario(nombreUsuario);
        if (usuario == null) {
            return false;
        }
        
        // Obtener tiempo actual del reloj
        long tiempo = reloj.getTiempoTranscurrido();
        
        // Calcular etiqueta de tiempo segun prioridad
        long etiqueta;
        if (esPrioritario) {
            // Aplicar modificador segun tipo de usuario
            etiqueta = aplicarModificadorPrioridad(tiempo, usuario.getTipo());
        } else {
            // Sin prioridad, usar tiempo actual
            etiqueta = tiempo;
        }
        
        // Crear registro y encolar
        RegistroCola registro = new RegistroCola(documento, etiqueta);
        heap.insertar(registro);
        tabla.insertar(nombreUsuario, registro);
        
        // Marcar documento como encolado
        documento.setEnColaImpresion(true);
        
        return true;
    }
    
    /**
     * Libera la impresora, imprimiendo el siguiente documento
     * @return registro impreso o null si la cola esta vacia
     */
    public RegistroCola liberarImpresora() {
        if (heap.estaVacio()) {
            return null;
        }
        
        // Eliminar el minimo (el de mayor prioridad)
        RegistroCola registro = heap.eliminarMin();
        
        return registro;
    }
    
    /**
     * Elimina un documento especifico de la cola de impresion
     * 
     * Como el heap solo puede eliminar el minimo,
     * cambiamos la prioridad del documento a -1 (la mas alta),
     * luego lo eliminamos con eliminarMin()
     * 
     * @param nombreUsuario usuario del documento
     * @param nombreDocumento nombre del documento a eliminar
     * @return true si se elimino, false si no se encontro
     */
    public boolean eliminarDocumentoCola(String nombreUsuario, String nombreDocumento) {
        // Paso 1: Buscar los documentos del usuario en la tabla hash
        ArrayList<RegistroCola> documentos = tabla.buscar(nombreUsuario);
        
        if (documentos == null || documentos.isEmpty()) {
            return false; // Usuario no tiene documentos en cola
        }
        
        // Paso 2: Buscar el documento especifico por nombre
        RegistroCola registroAEliminar = null;
        for (RegistroCola reg : documentos) {
            if (reg.getNombreDocumento().equals(nombreDocumento)) {
                registroAEliminar = reg;
                break;
            }
        }
        
        if (registroAEliminar == null) {
            return false; // Documento no encontrado
        }
        
        // Paso 3: Cambiar su prioridad a -1 (maxima prioridad)
        // Esto lo convierte en el elemento minimo del heap
        long etiquetaOriginal = registroAEliminar.getEtiquetaTiempo();
        boolean cambiado = heap.cambiarPrioridad(etiquetaOriginal, -1);
        
        if (!cambiado) {
            return false; // No se pudo cambiar la prioridad
        }
        
        // Paso 4: Eliminar el minimo (que ahora es nuestro documento)
        heap.eliminarMin();
        
        // Paso 5: Eliminar de la tabla hash
        tabla.eliminar(nombreUsuario, etiquetaOriginal);
        
        return true;
    }
    
    //CONSULTAS
    
    /**
     * Obtiene el proximo documento a imprimir sin eliminarlo
     * @return registro proximo o null si la cola esta vacia
     */
    public RegistroCola obtenerProximoImprimir() {
        return heap.obtenerMin();
    }
    
    /**
     * Obtiene el tamaño actual de la cola
     * @return cantidad de documentos en cola
     */
    public int getTamañoCola() {
        return heap.getTamaño();
    }
    
    /**
     * Verifica si la cola esta vacia
     * @return true si esta vacia, false si no
     */
    public boolean colaVacia() {
        return heap.estaVacio();
    }
    
    /**
     * Obtiene el reloj del sistema
     * @return reloj
     */
    public Reloj getReloj() {
        return reloj;
    }
    
    //CARGA DE DATOS
    
    /**
     * Carga usuarios desde un archivo CSV
     * Formato esperado: usuario,tipo
     * @param rutaArchivo ruta del archivo CSV
     * @return cantidad de usuarios cargados
     * @throws IOException si hay error al leer el archivo
     */
    public int cargarUsuariosCSV(String rutaArchivo) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));
        String linea;
        int contador = 0;
        
        // Saltar la primera linea (encabezados)
        br.readLine();
        
        // Leer cada linea del archivo
        while ((linea = br.readLine()) != null) {
            String[] partes = linea.split(",");
            
            if (partes.length < 2) {
                continue; // Linea invalida, saltar
            }
            
            String nombreUsuario = partes[0].trim();
            String tipoStr = partes[1].trim();
            
            // Convertir string a enum TipoUsuario
            TipoUsuario tipo;
            switch (tipoStr.toLowerCase()) {
                case "prioridad_alta":
                    tipo = TipoUsuario.PRIORIDAD_ALTA;
                    break;
                case "prioridad_media":
                    tipo = TipoUsuario.PRIORIDAD_MEDIA;
                    break;
                case "prioridad_baja":
                    tipo = TipoUsuario.PRIORIDAD_BAJA;
                    break;
                default:
                    tipo = TipoUsuario.PRIORIDAD_BAJA; // Por defecto
            }
            
            // Crear y agregar usuario
            Usuario usuario = new Usuario(nombreUsuario, tipo);
            agregarUsuario(usuario);
            contador++;
        }
        
        br.close();
        return contador;
    }
}
