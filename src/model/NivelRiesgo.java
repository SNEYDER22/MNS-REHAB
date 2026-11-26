package model;

public enum NivelRiesgo {
    ALTO, MODERADO, BAJO;

    public int getDificultadMaxima() {
        if (this == ALTO) {
            return 1;
        }
        if (this == MODERADO) {
            return 2;
        }
        return 3;
    }

    public static NivelRiesgo desdePuntaje(int p) {
        if (p < 19) {
            return ALTO;
        }
        if (p <= 24) {
            return MODERADO;
        }
        return BAJO;
    }
}
