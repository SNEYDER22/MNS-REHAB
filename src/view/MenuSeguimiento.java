package view;

import model.Paciente;
import model.PlanEjercicio;
import model.RegistroEjercicio;
import model.ReporteProgreso;
import model.ValidacionException;
import controller.GestorPacientes;
import controller.GestorPlanes;
import controller.GestorSeguimiento;

import java.time.LocalDate;
import java.util.List;

public class MenuSeguimiento {
    private MenuPrincipal principal;
    private GestorSeguimiento gestorSeguimiento;
    private GestorPlanes gestorPlanes;
    private GestorPacientes gestorPacientes;

    public MenuSeguimiento(MenuPrincipal principal, GestorSeguimiento gestorSeguimiento, GestorPlanes gestorPlanes,
                           GestorPacientes gestorPacientes) {
        this.principal = principal;
        this.gestorSeguimiento = gestorSeguimiento;
        this.gestorPlanes = gestorPlanes;
        this.gestorPacientes = gestorPacientes;
    }

    public void mostrar() {
        int op = -1;
        while (op != 0) {
            System.out.println();
            System.out.println("----- Módulo 4: Seguimiento de ejercicios -----");
            System.out.println("1. Registrar ejercicio realizado (RF7)");
            System.out.println("2. Registrar ejercicio omitido (RF7)");
            System.out.println("3. Calcular cumplimiento y generar reporte (RF8)");
            System.out.println("4. Ver ejercicios pendientes de un paciente");
            System.out.println("0. Volver");
            op = principal.leerEntero("Opción: ");
            switch (op) {
                case 1:
                    registrar(true);
                    break;
                case 2:
                    registrar(false);
                    break;
                case 3:
                    generarReporte();
                    break;
                case 4:
                    verPendientes();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private void registrar(boolean rea) {
        Paciente pac = gestorPacientes.buscarPorCedula(principal.leerTexto("Cédula del paciente: "));
        if (pac == null) {
            System.out.println("Error: no existe un paciente con esa cédula.");
            return;
        }
        List<PlanEjercicio> pls = gestorPlanes.planesDePaciente(pac);
        for (int i = 0; i < pls.size(); i++) {
            System.out.println((i + 1) + ". " + pls.get(i));
        }
        int pp = principal.elegir("Plan", pls.size());
        if (pp < 0) {
            return;
        }
        PlanEjercicio pla = pls.get(pp);
        List<RegistroEjercicio> reg = pla.getRegistros();
        for (int i = 0; i < reg.size(); i++) {
            System.out.println((i + 1) + ". " + reg.get(i));
        }
        int pr = principal.elegir("Registro", reg.size());
        if (pr < 0) {
            return;
        }
        if (rea) {
            int ser = principal.leerEntero("Series realizadas: ");
            int dol = principal.leerEntero("Dolor percibido (0 a 10): ");
            System.out.println(gestorSeguimiento.registrarRealizado(pla, reg.get(pr), ser, dol, LocalDate.now()));
        } else {
            String mot = principal.leerTexto("Motivo de la omisión: ");
            System.out.println(gestorSeguimiento.registrarOmitido(reg.get(pr), mot, LocalDate.now()));
        }
    }

    private void generarReporte() {
        Paciente pac = gestorPacientes.buscarPorCedula(principal.leerTexto("Cédula del paciente: "));
        if (pac == null) {
            System.out.println("Error: no existe un paciente con esa cédula.");
            return;
        }
        LocalDate ini = principal.leerFecha("Desde");
        LocalDate fin = principal.leerFecha("Hasta");
        try {
            ReporteProgreso rep = gestorSeguimiento.generarReporte(pac, ini, fin, LocalDate.now());
            System.out.println("REPORTE DE PROGRESO · " + pac.getNombreCompleto() + " · " + ini + " a " + fin);
            System.out.println("Realizados: " + rep.getRealizados() + " | Omitidos: " + rep.getOmitidos()
                    + " | Pendientes vencidos: " + rep.getPendientesVencidos());
            if (!rep.hayCumplimiento()) {
                System.out.println("Cumplimiento: No aplica (no hay ejercicios vencidos en el rango).");
            } else {
                for (String eje : rep.getCumplimientoPorEjercicio().keySet()) {
                    System.out.println("  " + eje + ": " + String.format("%.1f", rep.getCumplimientoPorEjercicio().get(eje)) + " %");
                }
                System.out.println("Cumplimiento global: " + String.format("%.1f", rep.getCumplimientoGlobal()) + " %");
            }
            if (rep.getVariacionDolor() == null) {
                System.out.println("Variación de dolor y Tinetti: No aplica (menos de dos valoraciones en el rango).");
            } else {
                System.out.println("Variación del dolor: " + rep.getVariacionDolor() + " | Variación de Tinetti: " + rep.getVariacionTinetti());
            }
            if (rep.requiereAlerta()) {
                System.out.println("ALERTA: el cumplimiento es menor al 80 %.");
            }
        } catch (ValidacionException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void verPendientes() {
        Paciente pac = gestorPacientes.buscarPorCedula(principal.leerTexto("Cédula del paciente: "));
        if (pac == null) {
            System.out.println("Error: no existe un paciente con esa cédula.");
            return;
        }
        List<RegistroEjercicio> lis = gestorSeguimiento.registrosPendientes(pac, LocalDate.now());
        if (lis.isEmpty()) {
            System.out.println("No hay ejercicios pendientes.");
        }
        for (RegistroEjercicio reg : lis) {
            System.out.println("  " + reg);
        }
    }
}
