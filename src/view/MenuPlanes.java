package view;

import model.Contraindicacion;
import model.Ejercicio;
import model.Fisioterapeuta;
import model.Paciente;
import model.PlanEjercicio;
import model.RegistroEjercicio;
import controller.GestorEjercicios;
import controller.GestorPacientes;
import controller.GestorPlanes;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MenuPlanes {
    private MenuPrincipal principal;
    private GestorPlanes gestorPlanes;
    private GestorPacientes gestorPacientes;
    private GestorEjercicios gestorEjercicios;

    public MenuPlanes(MenuPrincipal principal, GestorPlanes gestorPlanes, GestorPacientes gestorPacientes,
                      GestorEjercicios gestorEjercicios) {
        this.principal = principal;
        this.gestorPlanes = gestorPlanes;
        this.gestorPacientes = gestorPacientes;
        this.gestorEjercicios = gestorEjercicios;
    }

    public void mostrar() {
        int op = -1;
        while (op != 0) {
            System.out.println();
            System.out.println("----- Módulo 3: Planes de ejercicio -----");
            System.out.println("1. Asignar plan con verificación de seguridad (RF5)");
            System.out.println("2. Generar calendario de ejercicios (RF6)");
            System.out.println("3. Ver planes y calendario de un paciente");
            System.out.println("0. Volver");
            op = principal.leerEntero("Opción: ");
            switch (op) {
                case 1:
                    asignarPlan();
                    break;
                case 2:
                    generarCalendario();
                    break;
                case 3:
                    verPlanes();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private void asignarPlan() {
        Paciente pac = gestorPacientes.buscarPorCedula(principal.leerTexto("Cédula del paciente: "));
        if (pac == null) {
            System.out.println("Error: no existe un paciente con esa cédula.");
            return;
        }
        List<Ejercicio> eje = gestorEjercicios.listarEjercicios();
        for (int i = 0; i < eje.size(); i++) {
            System.out.println((i + 1) + ". " + eje.get(i));
        }
        int pe = principal.elegir("Ejercicio", eje.size());
        if (pe < 0) {
            return;
        }
        List<Fisioterapeuta> fis = gestorPlanes.listarFisioterapeutas();
        for (int i = 0; i < fis.size(); i++) {
            System.out.println((i + 1) + ". " + fis.get(i).mostrarDatos());
        }
        int pf = principal.elegir("Fisioterapeuta", fis.size());
        if (pf < 0) {
            return;
        }
        if (pac.getUltimaValoracion() == null) {
            System.out.println("Error: el paciente no tiene valoraciones; registre una antes de asignar un plan.");
            return;
        }
        if (!gestorPlanes.dificultadPermitida(pac, eje.get(pe))) {
            System.out.println("Bloqueado: la dificultad del ejercicio supera la permitida por el riesgo de caída ("
                    + pac.getUltimaValoracion().getRiesgoCaida() + ").");
            return;
        }
        boolean cnf = false;
        Contraindicacion con = gestorPlanes.verificarSeguridad(pac, eje.get(pe));
        if (con != null && con.esProhibido()) {
            System.out.println("Bloqueado: el ejercicio está PROHIBIDO para " + con.getCondicionSalud() + ". " + con.getRecomendacion());
            return;
        }
        if (con != null) {
            System.out.println("PRECAUCIÓN por " + con.getCondicionSalud() + ": " + con.getRecomendacion());
            cnf = principal.leerSiNo("¿El fisioterapeuta confirma la asignación?");
        }
        int ser = principal.leerEntero("Series: ");
        int rep = principal.leerEntero("Repeticiones: ");
        String txt = principal.leerTexto("Días de la semana (1 = lunes ... 7 = domingo) separados por coma: ");
        List<DayOfWeek> dia = new ArrayList<>();
        for (String par : txt.split(",")) {
            try {
                int num = Integer.parseInt(par.trim());
                if (num >= 1 && num <= 7 && !dia.contains(DayOfWeek.of(num))) {
                    dia.add(DayOfWeek.of(num));
                }
            } catch (NumberFormatException e) {
                System.out.println("Se ignoró el valor: " + par);
            }
        }
        LocalDate ini = principal.leerFecha("Fecha de inicio");
        int sem = principal.leerEntero("Duración en semanas: ");
        System.out.println(gestorPlanes.asignarPlan(pac, fis.get(pf), eje.get(pe), ser, rep, dia, ini, sem, cnf));
    }

    private PlanEjercicio elegirPlan() {
        Paciente pac = gestorPacientes.buscarPorCedula(principal.leerTexto("Cédula del paciente: "));
        if (pac == null) {
            System.out.println("Error: no existe un paciente con esa cédula.");
            return null;
        }
        List<PlanEjercicio> lis = gestorPlanes.planesDePaciente(pac);
        for (int i = 0; i < lis.size(); i++) {
            System.out.println((i + 1) + ". " + lis.get(i));
        }
        int pos = principal.elegir("Plan", lis.size());
        if (pos < 0) {
            return null;
        }
        return lis.get(pos);
    }

    private void generarCalendario() {
        PlanEjercicio pla = elegirPlan();
        if (pla != null) {
            System.out.println(gestorPlanes.generarCalendario(pla));
        }
    }

    private void verPlanes() {
        PlanEjercicio pla = elegirPlan();
        if (pla == null) {
            return;
        }
        if (!pla.tieneCalendario()) {
            System.out.println("El plan aún no tiene calendario.");
        }
        for (RegistroEjercicio reg : pla.getRegistros()) {
            System.out.println("  " + reg);
        }
    }
}
