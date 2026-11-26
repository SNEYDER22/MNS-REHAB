package model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ReporteProgreso {
    private Paciente paciente;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int realizados;
    private int omitidos;
    private int pendientesVencidos;
    private Map<String, Double> cumplimientoPorEjercicio;
    private Integer variacionDolor;
    private Integer variacionTinetti;

    public ReporteProgreso(Paciente paciente, LocalDate fechaInicio, LocalDate fechaFin) {
        this.paciente = paciente;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.realizados = 0;
        this.omitidos = 0;
        this.pendientesVencidos = 0;
        this.cumplimientoPorEjercicio = new HashMap<>();
        this.variacionDolor = null;
        this.variacionTinetti = null;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public int getRealizados() {
        return realizados;
    }

    public void setRealizados(int realizados) {
        this.realizados = realizados;
    }

    public int getOmitidos() {
        return omitidos;
    }

    public void setOmitidos(int omitidos) {
        this.omitidos = omitidos;
    }

    public int getPendientesVencidos() {
        return pendientesVencidos;
    }

    public void setPendientesVencidos(int pendientesVencidos) {
        this.pendientesVencidos = pendientesVencidos;
    }

    public Map<String, Double> getCumplimientoPorEjercicio() {
        return cumplimientoPorEjercicio;
    }

    public Integer getVariacionDolor() {
        return variacionDolor;
    }

    public void setVariacionDolor(Integer variacionDolor) {
        this.variacionDolor = variacionDolor;
    }

    public Integer getVariacionTinetti() {
        return variacionTinetti;
    }

    public void setVariacionTinetti(Integer variacionTinetti) {
        this.variacionTinetti = variacionTinetti;
    }

    public int getTotalVencidos() {
        return realizados + omitidos + pendientesVencidos;
    }

    public boolean hayCumplimiento() {
        return getTotalVencidos() > 0;
    }

    public Double getCumplimientoGlobal() {
        if (!hayCumplimiento()) {
            return null;
        }
        return realizados * 100.0 / getTotalVencidos();
    }

    public boolean requiereAlerta() {
        return hayCumplimiento() && getCumplimientoGlobal() < 80;
    }

    public void agregarCumplimiento(String ejercicio, double pct) {
        cumplimientoPorEjercicio.put(ejercicio, pct);
    }
}
