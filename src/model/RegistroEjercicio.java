package model;

import java.time.LocalDate;

public class RegistroEjercicio implements Validable {
    private LocalDate fechaProgramada;
    private EstadoRegistro estado;
    private int seriesRealizadas;
    private int dolorPercibido;
    private String motivoOmision;

    public RegistroEjercicio(LocalDate fechaProgramada) {
        this.fechaProgramada = fechaProgramada;
        this.estado = EstadoRegistro.PENDIENTE;
        this.seriesRealizadas = 0;
        this.dolorPercibido = 0;
        this.motivoOmision = "";
    }

    public LocalDate getFechaProgramada() {
        return fechaProgramada;
    }

    public EstadoRegistro getEstado() {
        return estado;
    }

    public int getSeriesRealizadas() {
        return seriesRealizadas;
    }

    public int getDolorPercibido() {
        return dolorPercibido;
    }

    public String getMotivoOmision() {
        return motivoOmision;
    }

    public void marcarRealizado(int series, int dolor) {
        this.estado = EstadoRegistro.REALIZADO;
        this.seriesRealizadas = series;
        this.dolorPercibido = dolor;
    }

    public void marcarOmitido(String motivo) {
        this.estado = EstadoRegistro.OMITIDO;
        this.motivoOmision = motivo;
    }

    public boolean estaCerrado() {
        return estado != EstadoRegistro.PENDIENTE;
    }

    public boolean estaVencido(LocalDate hoy) {
        return estado == EstadoRegistro.PENDIENTE && fechaProgramada.isBefore(hoy);
    }

    @Override
    public void validar() throws ValidacionException {
        if (dolorPercibido < 0 || dolorPercibido > 10) {
            throw new ValidacionException("dolorPercibido", "El dolor debe estar entre 0 y 10.");
        }
        if (seriesRealizadas < 0) {
            throw new ValidacionException("seriesRealizadas", "Las series realizadas no pueden ser negativas.");
        }
        if (estado == EstadoRegistro.OMITIDO && (motivoOmision == null || motivoOmision.trim().isEmpty())) {
            throw new ValidacionException("motivoOmision", "El motivo es obligatorio cuando se omite.");
        }
    }

    @Override
    public String toString() {
        String det = "";
        if (estado == EstadoRegistro.REALIZADO) {
            det = " | Series " + seriesRealizadas + " | Dolor " + dolorPercibido + "/10";
        } else if (estado == EstadoRegistro.OMITIDO) {
            det = " | Motivo: " + motivoOmision;
        }
        return fechaProgramada + " (" + fechaProgramada.getDayOfWeek() + ") | " + estado + det;
    }
}
