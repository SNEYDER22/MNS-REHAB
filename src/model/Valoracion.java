package model;

import java.time.LocalDate;

public class Valoracion implements Validable {
    private LocalDate fecha;
    private int nivelDolor;
    private int puntajeTinetti;
    private NivelRiesgo riesgoCaida;

    public Valoracion(LocalDate fecha, int nivelDolor, int puntajeTinetti) {
        this.fecha = fecha;
        this.nivelDolor = nivelDolor;
        this.puntajeTinetti = puntajeTinetti;
        this.riesgoCaida = clasificarRiesgo();
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getNivelDolor() {
        return nivelDolor;
    }

    public int getPuntajeTinetti() {
        return puntajeTinetti;
    }

    public NivelRiesgo getRiesgoCaida() {
        return riesgoCaida;
    }

    public NivelRiesgo clasificarRiesgo() {
        return NivelRiesgo.desdePuntaje(puntajeTinetti);
    }

    public int diferenciaDolor(Valoracion anterior) {
        return nivelDolor - anterior.getNivelDolor();
    }

    public int diferenciaTinetti(Valoracion anterior) {
        return puntajeTinetti - anterior.getPuntajeTinetti();
    }

    @Override
    public void validar() throws ValidacionException {
        if (fecha == null) {
            throw new ValidacionException("fecha", "La fecha de la valoración es obligatoria.");
        }
        if (nivelDolor < 0 || nivelDolor > 10) {
            throw new ValidacionException("nivelDolor", "El dolor debe estar entre 0 y 10.");
        }
        if (puntajeTinetti < 0 || puntajeTinetti > 28) {
            throw new ValidacionException("puntajeTinetti", "El puntaje de Tinetti debe estar entre 0 y 28.");
        }
    }

    @Override
    public String toString() {
        return fecha + " | Dolor " + nivelDolor + "/10 | Tinetti " + puntajeTinetti + "/28 | Riesgo " + riesgoCaida;
    }
}
