package modelos;

/**
 * Representa un registro en la cola de impresion
 * @author Proyecto2ColaPrioridad Juanes
 */
public class RegistroCola {
    private String nombreDocumento;
    private int tamaño;
    private String tipo;
    private long etiquetaTiempo;
    
    /**
     * Constructor del registro
     * @param doc documento base
     * @param etiqueta etiqueta de tiempo
     */
    public RegistroCola(Documento doc, long etiqueta) {
        this.nombreDocumento = doc.getNombre();
        this.tamaño = doc.getTamaño();
        this.tipo = doc.getTipo();
        this.etiquetaTiempo = etiqueta;
    }
    
    /**
     * Obtiene el nombre del documento
     * @return nombre del documento
     */
    public String getNombreDocumento() {
        return nombreDocumento;
    }
    
    /**
     * Obtiene el tamaño del documento
     * @return tamaño en paginas
     */
    public int getTamaño() {
        return tamaño;
    }
    
    /**
     * Obtiene el tipo de documento
     * @return tipo del documento
     */
    public String getTipo() {
        return tipo;
    }
    
    /**
     * Obtiene la etiqueta de tiempo
     * @return etiqueta de tiempo
     */
    public long getEtiquetaTiempo() {
        return etiquetaTiempo;
    }
    
    /**
     * Modifica la etiqueta de tiempo
     * @param etiqueta nueva etiqueta
     */
    public void setEtiquetaTiempo(long etiqueta) {
        this.etiquetaTiempo = etiqueta;
    }
}
