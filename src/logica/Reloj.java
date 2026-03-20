package logica;

/**
 * Clase que simula un reloj para medir el tiempo transcurrido
 * desde el inicio de la simulacion
 * @author Proyecto2ColaPrioridad Juanes
 */
public class Reloj {
    private long tiempoInicio;
    private boolean activo;
    
    /**
     * Constructor del reloj
     */
    public Reloj() {
        this.tiempoInicio = 0;
        this.activo = false;
    }
    
    /**
     * Inicia el reloj desde cero
     */
    public void iniciar() {
        this.tiempoInicio = System.currentTimeMillis();
        this.activo = true;
    }
    
    /**
     * Detiene el reloj
     */
    public void detener() {
        this.activo = false;
    }
    
    /**
     * Reinicia el reloj a cero
     */
    public void reiniciar() {
        this.tiempoInicio = System.currentTimeMillis();
        this.activo = true;
    }
    
    /**
     * Obtiene el tiempo transcurrido desde el inicio
     * @return tiempo en milisegundos
     */
    public long getTiempoTranscurrido() {
        if (!activo) {
            return 0;
        }
        return System.currentTimeMillis() - tiempoInicio;
    }
    
    /**
     * Verifica si el reloj esta activo
     * @return true si esta activo, false si no
     */
    public boolean isActivo() {
        return activo;
    }
}
