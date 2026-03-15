package modelos;

/**
 * Representa un documento creado por un usuario
 * @author Proyecto2ColaPrioridad Juanes
 */
public class Documento {
    private String nombre;
    private int tamaño;
    private String tipo;
    private boolean enColaImpresion;
    
    /**
     * Constructor del documento
     * @param nombre nombre del documento
     * @param tamaño tamaño en paginas
     * @param tipo tipo de documento
     */
    public Documento(String nombre, int tamaño, String tipo) {
        this.nombre = nombre;
        this.tamaño = tamaño;
        this.tipo = tipo;
        this.enColaImpresion = false;
    }
    
    /**
     * Obtiene el nombre del documento
     * @return nombre del documento
     */
    public String getNombre() {
        return nombre;
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
     * @return tipo de documento
     */
    public String getTipo() {
        return tipo;
    }
    
    /**
     * Verifica si el documento esta en la cola de impresion
     * @return true si esta en cola, false si no
     */
    public boolean isEnColaImpresion() {
        return enColaImpresion;
    }
    
    /**
     * Marca el documento como encolado o no
     * @param enCola true para marcar como encolado
     */
    public void setEnColaImpresion(boolean enCola) {
        this.enColaImpresion = enCola;
    }
}
