package model;

public class Contraindicacion implements Validable {
    private String condicionSalud;
    private Gravedad gravedad;
    private String recomendacion;

    public Contraindicacion(String condicionSalud, Gravedad gravedad, String recomendacion) {
        this.condicionSalud = condicionSalud;
        this.gravedad = gravedad;
        this.recomendacion = recomendacion;
    }

    public String getCondicionSalud() {
        return condicionSalud;
    }

    public Gravedad getGravedad() {
        return gravedad;
    }

    public void setGravedad(Gravedad gravedad) {
        this.gravedad = gravedad;
    }

    public String getRecomendacion() {
        return recomendacion;
    }

    public void setRecomendacion(String recomendacion) {
        this.recomendacion = recomendacion;
    }

    public boolean esProhibido() {
        return gravedad == Gravedad.PROHIBIDO;
    }

    @Override
    public void validar() throws ValidacionException {
        if (condicionSalud == null || condicionSalud.trim().isEmpty()) {
            throw new ValidacionException("condicionSalud", "La condición de salud es obligatoria.");
        }
        if (gravedad == null) {
            throw new ValidacionException("gravedad", "La gravedad es obligatoria.");
        }
        if (recomendacion == null || recomendacion.trim().isEmpty()) {
            throw new ValidacionException("recomendacion", "La recomendación es obligatoria.");
        }
    }

    @Override
    public String toString() {
        return condicionSalud + " | " + gravedad + " | " + recomendacion;
    }
}
