package modelos;

/**
 * Enumeracion que representa los tipos de usuario con sus prioridades
 * @author Proyecto2ColaPrioridad Juanes
 */
public enum TipoUsuario {
    PRIORIDAD_ALTA(1),
    PRIORIDAD_MEDIA(2),
    PRIORIDAD_BAJA(3);
    
    private final int nivelPrioridad;
    
    /**
     * Constructor del enum
     * @param nivelPrioridad nivel de prioridad (1 es la mas alta)
     */
    TipoUsuario(int nivelPrioridad) {
        this.nivelPrioridad = nivelPrioridad;
    }
    
    /**
     * Obtiene el nivel de prioridad
     * @return nivel de prioridad
     */
    public int getNivelPrioridad() {
        return nivelPrioridad;
    }
}
