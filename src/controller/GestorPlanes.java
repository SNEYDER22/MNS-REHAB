package controller;

import model.Contraindicacion;
import model.Ejercicio;
import model.Fisioterapeuta;
import model.Paciente;
import model.PlanEjercicio;
import model.ValidacionException;
import model.Valoracion;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GestorPlanes {
    private List<PlanEjercicio> planes;
    private List<Fisioterapeuta> fisioterapeutas;

    public GestorPlanes() {
        this.planes = new ArrayList<>();
        this.fisioterapeutas = new ArrayList<>();
        cargarFisioterapeutas();
    }

    public String asignarPlan(Paciente paciente, Fisioterapeuta fisio, Ejercicio ejercicio, int series, int repeticiones,
                              List<DayOfWeek> dias, LocalDate inicio, int semanas, boolean confirmaPrecaucion) {
        if (paciente == null || fisio == null || ejercicio == null) {
            return "Error: debe elegir un paciente, un fisioterapeuta y un ejercicio existentes.";
        }
        Valoracion ult = paciente.getUltimaValoracion();
        if (ult == null) {
            return "Error: el paciente no tiene valoraciones; registre una antes de asignar un plan.";
        }
        PlanEjercicio pla = new PlanEjercicio(paciente, fisio, ejercicio, series, repeticiones, dias, inicio, semanas);
        try {
            pla.validar();
        } catch (ValidacionException e) {
            return "Error: " + e.getMessage();
        }
        if (!dificultadPermitida(paciente, ejercicio)) {
            return "Bloqueado: el ejercicio tiene dificultad " + ejercicio.getDificultad() + " y el riesgo "
                    + ult.getRiesgoCaida() + " solo permite hasta " + ult.getRiesgoCaida().getDificultadMaxima() + ".";
        }
        Contraindicacion con = verificarSeguridad(paciente, ejercicio);
        if (con != null && con.esProhibido()) {
            return "Bloqueado: " + ejercicio.getNombre() + " está PROHIBIDO para " + con.getCondicionSalud() + ". " + con.getRecomendacion();
        }
        if (con != null && !confirmaPrecaucion) {
            return "Pendiente: hay una PRECAUCIÓN por " + con.getCondicionSalud() + " y requiere confirmación.";
        }
        planes.add(pla);
        String men = "Plan asignado y ACTIVO: " + ejercicio.getNombre() + " para " + paciente.getNombreCompleto() + ".";
        if (ejercicio.getContraindicaciones().isEmpty()) {
            men = men + " El ejercicio no tiene contraindicaciones registradas; la verificación de seguridad corresponde al fisioterapeuta.";
        }
        return men;
    }

    public Contraindicacion verificarSeguridad(Paciente p, Ejercicio e) {
        Contraindicacion pre = null;
        for (Contraindicacion con : e.getContraindicaciones()) {
            if (p.tieneCondicion(con.getCondicionSalud())) {
                if (con.esProhibido()) {
                    return con;
                }
                pre = con;
            }
        }
        return pre;
    }

    public boolean dificultadPermitida(Paciente p, Ejercicio e) {
        Valoracion ult = p.getUltimaValoracion();
        if (ult == null) {
            return false;
        }
        return e.getDificultad() <= ult.getRiesgoCaida().getDificultadMaxima();
    }

    public String generarCalendario(PlanEjercicio plan) {
        if (plan == null) {
            return "Error: el plan no existe.";
        }
        if (plan.tieneCalendario()) {
            return "El calendario ya fue generado; no se duplicaron registros.";
        }
        int can = plan.generarCalendario();
        return "Calendario generado: " + can + " registros en estado PENDIENTE.";
    }

    public List<PlanEjercicio> planesDePaciente(Paciente p) {
        List<PlanEjercicio> lis = new ArrayList<>();
        for (PlanEjercicio pla : planes) {
            if (pla.getPaciente() == p) {
                lis.add(pla);
            }
        }
        return lis;
    }

    public Fisioterapeuta buscarFisioterapeuta(String registro) {
        for (Fisioterapeuta fis : fisioterapeutas) {
            if (fis.getRegistroProfesional().equalsIgnoreCase(registro)) {
                return fis;
            }
        }
        return null;
    }

    public List<Fisioterapeuta> listarFisioterapeutas() {
        return fisioterapeutas;
    }

    private void cargarFisioterapeutas() {
        fisioterapeutas.add(new Fisioterapeuta("Andrea", "Salazar Vega", "1718345679", "0995556677", "MSP-FT-0457"));
        fisioterapeutas.add(new Fisioterapeuta("Carlos", "Mena Ortiz", "1720987658", "0984443322", "MSP-FT-0812"));
    }
}
