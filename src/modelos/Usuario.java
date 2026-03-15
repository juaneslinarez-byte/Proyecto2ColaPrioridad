package modelos;

import java.util.ArrayList;

/**
 * Representa un usuario del sistema
 * @author Proyecto2ColaPrioridad Juanes
 */
public class Usuario {
    private String nombreUsuario;
    private TipoUsuario tipo;
    private ArrayList<Documento> documentosCreados;
    
    /**
     * Constructor del usuario
     * @param nombreUsuario identificador del usuario
     * @param tipo tipo de usuario
     */
    public Usuario(String nombreUsuario, TipoUsuario tipo) {
        this.nombreUsuario = nombreUsuario;
        this.tipo = tipo;
        this.documentosCreados = new ArrayList<>();
    }
    
    /**
     * Obtiene el nombre de usuario
     * @return nombre de usuario
     */
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    
    /**
     * Obtiene el tipo de usuario
     * @return tipo de usuario
     */
    public TipoUsuario getTipo() {
        return tipo;
    }
    
    /**
     * Obtiene la lista de documentos creados
     * @return lista de documentos
     */
    public ArrayList<Documento> getDocumentosCreados() {
        return documentosCreados;
    }
    
    /**
     * Agrega un documento a la lista de documentos creados
     * @param documento documento a agregar
     */
    public void agregarDocumento(Documento documento) {
        documentosCreados.add(documento);
    }
    
    /**
     * Elimina un documento de la lista de documentos creados
     * @param documento documento a eliminar
     * @return true si se elimino, false si no se encontro
     */
    public boolean eliminarDocumento(Documento documento) {
        return documentosCreados.remove(documento);
    }
}
