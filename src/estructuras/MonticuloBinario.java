package estructuras;

import modelos.RegistroCola;

/**
 * Implementacion de un Monticulo Binario Minimo para la cola de impresion
 * @author Proyecto2ColaPrioridad Juanes
 */
public class MonticuloBinario {
    private RegistroCola[] heap;
    private int tamaño;
    private static final int CAPACIDAD_INICIAL = 10;
    
    /**
     * Constructor del monticulo binario
     */
    public MonticuloBinario() {
        this.heap = new RegistroCola[CAPACIDAD_INICIAL];
        this.tamaño = 0;
    }
    
    /**
     * Verifica si el monticulo esta vacio
     * @return true si esta vacio, false si no
     */
    public boolean estaVacio() {
        return tamaño == 0;
    }
    
    /**
     * Obtiene el tamaño actual del monticulo
     * @return numero de elementos
     */
    public int getTamaño() {
        return tamaño;
    }
    
    /**
     * Inserta un nuevo registro en el monticulo
     * Complejidad: O(log n)
     * @param registro registro a insertar
     */
    public void insertar(RegistroCola registro) {
        // Si el array esta lleno, lo expandimos
        if (tamaño == heap.length) {
            expandirCapacidad();
        }
        
        // Insertamos al final del array
        heap[tamaño] = registro;
        tamaño++;
        
        // Aplicamos filtrar arriba desde la ultima posicion
        filtrarArriba(tamaño - 1);
    }
    
    /**
     * Elimina y retorna el elemento minimo del monticulo
     * Complejidad: O(log n)
     * @return registro con menor etiqueta de tiempo
     */
    public RegistroCola eliminarMin() {
        if (estaVacio()) {
            return null;
        }
        
        // Guardamos el minimo (raiz)
        RegistroCola minimo = heap[0];
        
        // Movemos el ultimo elemento a la raiz
        heap[0] = heap[tamaño - 1];
        heap[tamaño - 1] = null;
        tamaño--;
        
        // Aplicamos filtrar abajo desde la raiz
        if (tamaño > 0) {
            filtrarAbajo(0);
        }
        
        return minimo;
    }
    
    /**
     * Filtra un elemento hacia arriba para mantener la propiedad del heap
     * @param posicion posicion del elemento a filtrar
     */
    private void filtrarArriba(int posicion) {
        // Mientras no sea la raiz y sea menor que su padre
        while (posicion > 0) {
            int posPadre = (posicion - 1) / 2;
            
            // Si el hijo es menor que el padre, intercambiamos
            if (heap[posicion].getEtiquetaTiempo() < heap[posPadre].getEtiquetaTiempo()) {
                intercambiar(posicion, posPadre);
                posicion = posPadre;
            } else {
                break;
            }
        }
    }
    
    /**
     * Filtra un elemento hacia abajo para mantener la propiedad del heap
     * @param posicion posicion del elemento a filtrar
     */
    private void filtrarAbajo(int posicion) {
        while (posicion < tamaño) {
            int posHijoIzq = 2 * posicion + 1;
            int posHijoDer = 2 * posicion + 2;
            int posMenor = posicion;
            
            // Verificamos si el hijo izquierdo existe y es menor
            if (posHijoIzq < tamaño && 
                heap[posHijoIzq].getEtiquetaTiempo() < heap[posMenor].getEtiquetaTiempo()) {
                posMenor = posHijoIzq;
            }
            
            // Verificamos si el hijo derecho existe y es menor
            if (posHijoDer < tamaño && 
                heap[posHijoDer].getEtiquetaTiempo() < heap[posMenor].getEtiquetaTiempo()) {
                posMenor = posHijoDer;
            }
            
            // Si el menor no es el padre, intercambiamos y continuamos
            if (posMenor != posicion) {
                intercambiar(posicion, posMenor);
                posicion = posMenor;
            } else {
                break;
            }
        }
    }
    
    /**
     * Intercambia dos elementos del heap
     * @param pos1 primera posicion
     * @param pos2 segunda posicion
     */
    private void intercambiar(int pos1, int pos2) {
        RegistroCola temp = heap[pos1];
        heap[pos1] = heap[pos2];
        heap[pos2] = temp;
    }
    
    /**
     * Expande la capacidad del array cuando se llena
     */
    private void expandirCapacidad() {
        int nuevaCapacidad = heap.length * 2;
        RegistroCola[] nuevoHeap = new RegistroCola[nuevaCapacidad];
        
        // Copiamos los elementos al nuevo array
        for (int i = 0; i < heap.length; i++) {
            nuevoHeap[i] = heap[i];
        }
        
        heap = nuevoHeap;
    }
    
    /**
     * Obtiene el elemento minimo sin eliminarlo
     * @return registro con menor etiqueta o null si esta vacio
     */
    public RegistroCola obtenerMin() {
        if (estaVacio()) {
            return null;
        }
        return heap[0];
    }
    
    /**
     * Cambia la prioridad de un elemento en el heap
     * eliminar elementos especificos (no solo el minimo)
     * @param etiquetaVieja etiqueta actual a buscar
     * @param etiquetaNueva nueva etiqueta a asignar
     * @return true si se encontro y cambio, false si no
     */
    public boolean cambiarPrioridad(long etiquetaVieja, long etiquetaNueva) {
        // Buscar el elemento en el array
        for (int i = 0; i < tamaño; i++) {
            if (heap[i].getEtiquetaTiempo() == etiquetaVieja) {
                heap[i].setEtiquetaTiempo(etiquetaNueva);
                // Filtrar arriba porque ahora tiene menor prioridad
                filtrarArriba(i);
                return true;
            }
        }
        return false;
    }
}
