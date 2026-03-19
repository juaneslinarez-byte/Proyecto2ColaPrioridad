package estructuras;

import modelos.RegistroCola;
import java.util.ArrayList;

/**
 * Implementacion de una Tabla de Dispersion con encadenamiento
 * Permite buscar documentos de un usuario en O(1)
 * @author Proyecto2ColaPrioridad Juanes
 */
public class TablaHash {
    
    /**
     * Clase interna que representa un nodo en la tabla hash
     */
    private class NodoHash {
        String nombreUsuario;
        ArrayList<RegistroCola> documentos;
        NodoHash siguiente;
        
        /**
         * Constructor del nodo
         * @param nombreUsuario nombre del usuario
         */
        public NodoHash(String nombreUsuario) {
            this.nombreUsuario = nombreUsuario;
            this.documentos = new ArrayList<>();
            this.siguiente = null;
        }
    }
    
    private NodoHash[] tabla;
    private static final int TAMAÑO_TABLA = 101; // Numero primo para mejor distribucion
    
    /**
     * Constructor de la tabla hash
     */
    public TablaHash() {
        this.tabla = new NodoHash[TAMAÑO_TABLA];
    }
    
    /**
     * Funcion de dispersion que convierte un nombre de usuario en un indice
     * @param nombreUsuario nombre del usuario
     * @return indice en la tabla (0 a TAMAÑO_TABLA-1)
     */
    private int funcionHash(String nombreUsuario) {
        int suma = 0;
        for (int i = 0; i < nombreUsuario.length(); i++) {
            suma += nombreUsuario.charAt(i);
        }
        return suma % TAMAÑO_TABLA;
    }
    
    /**
     * Inserta un registro en la tabla asociado a un usuario
     * @param nombreUsuario nombre del usuario
     * @param registro registro a insertar
     */
    public void insertar(String nombreUsuario, RegistroCola registro) {
        int indice = funcionHash(nombreUsuario);
        
        // Si la posicion esta vacia, crear nuevo nodo
        if (tabla[indice] == null) {
            tabla[indice] = new NodoHash(nombreUsuario);
            tabla[indice].documentos.add(registro);
            return;
        }
        
        // Buscar si el usuario ya existe en la lista enlazada
        NodoHash actual = tabla[indice];
        while (actual != null) {
            if (actual.nombreUsuario.equals(nombreUsuario)) {
                // Usuario encontrado, agregar documento a su lista
                actual.documentos.add(registro);
                return;
            }
            
            // Si es el ultimo nodo, agregar nuevo nodo al final
            if (actual.siguiente == null) {
                actual.siguiente = new NodoHash(nombreUsuario);
                actual.siguiente.documentos.add(registro);
                return;
            }
            
            actual = actual.siguiente;
        }
    }
    
    /**
     * Busca todos los documentos asociados a un usuario
     * @param nombreUsuario nombre del usuario a buscar
     * @return lista de registros del usuario o null si no existe
     */
    public ArrayList<RegistroCola> buscar(String nombreUsuario) {
        int indice = funcionHash(nombreUsuario);
        
        // Recorrer la lista enlazada en esa posicion
        NodoHash actual = tabla[indice];
        while (actual != null) {
            if (actual.nombreUsuario.equals(nombreUsuario)) {
                return actual.documentos;
            }
            actual = actual.siguiente;
        }
        
        return null; // Usuario no encontrado
    }
    
    /**
     * Elimina un documento especifico de un usuario
     * @param nombreUsuario nombre del usuario
     * @param etiquetaTiempo etiqueta del documento a eliminar
     * @return true si se elimino, false si no se encontro
     */
    public boolean eliminar(String nombreUsuario, long etiquetaTiempo) {
        ArrayList<RegistroCola> documentos = buscar(nombreUsuario);
        
        if (documentos == null) {
            return false; // Usuario no existe
        }
        
        // Buscar y eliminar el documento con esa etiqueta
        for (int i = 0; i < documentos.size(); i++) {
            if (documentos.get(i).getEtiquetaTiempo() == etiquetaTiempo) {
                documentos.remove(i);
                return true;
            }
        }
        
        return false; // Documento no encontrado
    }
    
    /**
     * Verifica si un usuario existe en la tabla
     * @param nombreUsuario nombre del usuario
     * @return true si existe, false si no
     */
    public boolean existeUsuario(String nombreUsuario) {
        return buscar(nombreUsuario) != null;
    }
    
    /**
     * Obtiene la lista de todos los usuarios registrados en la tabla
     * @return lista con nombres de todos los usuarios
     */
    public ArrayList<String> obtenerTodosUsuarios() {
        ArrayList<String> usuarios = new ArrayList<>();
        
        // Recorrer toda la tabla
        for (int i = 0; i < tabla.length; i++) {
            NodoHash actual = tabla[i];
            
            // Recorrer la lista enlazada en cada posicion
            while (actual != null) {
                usuarios.add(actual.nombreUsuario);
                actual = actual.siguiente;
            }
        }
        
        return usuarios;
    }
}
