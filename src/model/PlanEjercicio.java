package model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlanEjercicio implements Validable {
    private Paciente paciente;
    private Fisioterapeuta fisioterapeuta;
    private Ejercicio ejercicio;
    private int series;
    private int repeticiones;
    private List<DayOfWeek> diasPorSemana;
    private LocalDate fechaInicio;
    private int duracionSemanas;
    private EstadoPlan estado;
    private List<RegistroEjercicio> registros;

    public PlanEjercicio(Paciente paciente, Fisioterapeuta fisioterapeuta, Ejercicio ejercicio, int series,
                         int repeticiones, List<DayOfWeek> diasPorSemana, LocalDate fechaInicio, int duracionSemanas) {
        this.paciente = paciente;
        this.fisioterapeuta = fisioterapeuta;
        this.ejercicio = ejercicio;
        this.series = series;
        this.repeticiones = repeticiones;
        this.diasPorSemana = diasPorSemana;
        this.fechaInicio = fechaInicio;
        this.duracionSemanas = duracionSemanas;
        this.estado = EstadoPlan.ACTIVO;
        this.registros = new ArrayList<>();
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Fisioterapeuta getFisioterapeuta() {
        return fisioterapeuta;
    }

    public Ejercicio getEjercicio() {
        return ejercicio;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public int getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(int repeticiones) {
        this.repeticiones = repeticiones;
    }

    public List<DayOfWeek> getDiasPorSemana() {
        return diasPorSemana;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public int getDuracionSemanas() {
        return duracionSemanas;
    }

    public EstadoPlan getEstado() {
        return estado;
    }

    public void setEstado(EstadoPlan estado) {
        this.estado = estado;
    }

    public List<RegistroEjercicio> getRegistros() {
        return registros;
    }

    public int generarCalendario() {
        if (tieneCalendario()) {
            return 0;
        }
        int can = 0;
        LocalDate fec = fechaInicio;
        LocalDate fin = getFechaFin();
        while (!fec.isAfter(fin)) {
            if (diasPorSemana.contains(fec.getDayOfWeek())) {
                agregarRegistro(new RegistroEjercicio(fec));
                can++;
            }
            fec = fec.plusDays(1);
        }
        return can;
    }

    public boolean tieneCalendario() {
        return !registros.isEmpty();
    }

    public LocalDate getFechaFin() {
        return fechaInicio.plusWeeks(duracionSemanas).minusDays(1);
    }

    public void agregarRegistro(RegistroEjercicio r) {
        registros.add(r);
    }

    public List<RegistroEjercicio> registrosEnRango(LocalDate ini, LocalDate fin) {
        List<RegistroEjercicio> lis = new ArrayList<>();
        for (RegistroEjercicio reg : registros) {
            if (!reg.getFechaProgramada().isBefore(ini) && !reg.getFechaProgramada().isAfter(fin)) {
                lis.add(reg);
            }
        }
        return lis;
    }

    @Override
    public void validar() throws ValidacionException {
        if (paciente == null || fisioterapeuta == null || ejercicio == null) {
            throw new ValidacionException("plan", "El plan necesita paciente, fisioterapeuta y ejercicio.");
        }
        if (series < 1) {
            throw new ValidacionException("series", "Las series deben ser mayores que cero.");
        }
        if (repeticiones < 1) {
            throw new ValidacionException("repeticiones", "Las repeticiones deben ser mayores que cero.");
        }
        if (diasPorSemana == null || diasPorSemana.isEmpty() || diasPorSemana.size() > 7) {
            throw new ValidacionException("diasPorSemana", "Debe indicar de 1 a 7 días por semana.");
        }
        if (fechaInicio == null) {
            throw new ValidacionException("fechaInicio", "La fecha de inicio es obligatoria.");
        }
        if (duracionSemanas < 1) {
            throw new ValidacionException("duracionSemanas", "La duración debe ser de al menos 1 semana.");
        }
    }

    @Override
    public String toString() {
        return ejercicio.getNombre() + " | " + series + "x" + repeticiones + " | Días: " + diasPorSemana + " | "
                + fechaInicio + " a " + getFechaFin() + " | " + estado + " | " + fisioterapeuta.getNombreCompleto();
    }
}
