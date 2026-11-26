package controller;

import model.EstadoRegistro;
import model.Paciente;
import model.PlanEjercicio;
import model.RegistroEjercicio;
import model.ReporteProgreso;
import model.ValidacionException;
import model.Valoracion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GestorSeguimiento {
    private GestorPlanes gestorPlanes;

    public GestorSeguimiento(GestorPlanes gestorPlanes) {
        this.gestorPlanes = gestorPlanes;
    }

    public String registrarRealizado(PlanEjercicio plan, RegistroEjercicio registro, int series, int dolor, LocalDate hoy) {
        if (plan == null || registro == null) {
            return "Error: el registro no existe.";
        }
        if (registro.getFechaProgramada().isAfter(hoy)) {
            return "Error: no se puede registrar un ejercicio con fecha futura.";
        }
        if (registro.estaCerrado()) {
            return "Error: el registro ya está cerrado como " + registro.getEstado() + ".";
        }
        if (series <= 0 || series > plan.getSeries()) {
            return "Error: las series realizadas deben estar entre 1 y " + plan.getSeries() + ".";
        }
        if (dolor < 0 || dolor > 10) {
            return "Error: el dolor debe estar entre 0 y 10.";
        }
        registro.marcarRealizado(series, dolor);
        try {
            registro.validar();
        } catch (ValidacionException e) {
            return "Error: " + e.getMessage();
        }
        String men = "Registro del " + registro.getFechaProgramada() + " marcado como REALIZADO.";
        if (dolor >= 7) {
            men = men + " ALERTA: dolor de " + dolor + "/10, avisar al fisioterapeuta.";
        }
        return men;
    }

    public String registrarOmitido(RegistroEjercicio registro, String motivo, LocalDate hoy) {
        if (registro == null) {
            return "Error: el registro no existe.";
        }
        if (registro.getFechaProgramada().isAfter(hoy)) {
            return "Error: no se puede registrar un ejercicio con fecha futura.";
        }
        if (registro.estaCerrado()) {
            return "Error: el registro ya está cerrado como " + registro.getEstado() + ".";
        }
        if (motivo == null || motivo.trim().isEmpty()) {
            return "Error: el motivo es obligatorio cuando el ejercicio se omite.";
        }
        registro.marcarOmitido(motivo);
        return "Registro del " + registro.getFechaProgramada() + " marcado como OMITIDO.";
    }

    public Double calcularCumplimiento(int realizados, int vencidos) {
        if (vencidos == 0) {
            return null;
        }
        return realizados * 100.0 / vencidos;
    }

    public ReporteProgreso generarReporte(Paciente p, LocalDate ini, LocalDate fin, LocalDate hoy) throws ValidacionException {
        if (fin.isBefore(ini)) {
            throw new ValidacionException("fechaFin", "La fecha de fin no puede ser anterior a la de inicio.");
        }
        ReporteProgreso rep = new ReporteProgreso(p, ini, fin);
        int trea = 0;
        int tomi = 0;
        int tpen = 0;
        for (PlanEjercicio pla : gestorPlanes.planesDePaciente(p)) {
            int rea = 0;
            int omi = 0;
            int pen = 0;
            for (RegistroEjercicio reg : pla.registrosEnRango(ini, fin)) {
                if (reg.getEstado() == EstadoRegistro.REALIZADO) {
                    rea++;
                } else if (reg.getEstado() == EstadoRegistro.OMITIDO) {
                    omi++;
                } else if (reg.estaVencido(hoy)) {
                    pen++;
                }
            }
            Double pct = calcularCumplimiento(rea, rea + omi + pen);
            if (pct != null) {
                rep.agregarCumplimiento(pla.getEjercicio().getNombre(), pct);
            }
            trea = trea + rea;
            tomi = tomi + omi;
            tpen = tpen + pen;
        }
        rep.setRealizados(trea);
        rep.setOmitidos(tomi);
        rep.setPendientesVencidos(tpen);
        List<Valoracion> lis = p.valoracionesEnRango(ini, fin);
        if (lis.size() >= 2) {
            Valoracion pri = lis.get(0);
            Valoracion ult = lis.get(lis.size() - 1);
            rep.setVariacionDolor(ult.diferenciaDolor(pri));
            rep.setVariacionTinetti(ult.diferenciaTinetti(pri));
        }
        return rep;
    }

    public List<RegistroEjercicio> registrosPendientes(Paciente p, LocalDate hoy) {
        List<RegistroEjercicio> lis = new ArrayList<>();
        for (PlanEjercicio pla : gestorPlanes.planesDePaciente(p)) {
            for (RegistroEjercicio reg : pla.getRegistros()) {
                if (!reg.estaCerrado()) {
                    lis.add(reg);
                }
            }
        }
        return lis;
    }
}
