package view;

import model.Cuidador;
import model.Paciente;
import model.Valoracion;
import controller.GestorPacientes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MenuPacientes {
    private MenuPrincipal principal;
    private GestorPacientes gestorPacientes;

    public MenuPacientes(MenuPrincipal principal, GestorPacientes gestorPacientes) {
        this.principal = principal;
        this.gestorPacientes = gestorPacientes;
    }

    public void mostrar() {
        int op = -1;
        while (op != 0) {
            System.out.println();
            System.out.println("----- Módulo 1: Pacientes y valoración -----");
            System.out.println("1. Registrar paciente (RF1)");
            System.out.println("2. Registrar valoración y clasificar riesgo (RF2)");
            System.out.println("3. Listar pacientes");
            System.out.println("4. Ver valoraciones de un paciente");
            System.out.println("0. Volver");
            op = principal.leerEntero("Opción: ");
            switch (op) {
                case 1:
                    registrarPaciente();
                    break;
                case 2:
                    registrarValoracion();
                    break;
                case 3:
                    listarPacientes();
                    break;
                case 4:
                    verValoraciones();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private void registrarPaciente() {
        String nom = principal.leerTexto("Nombres: ");
        String ape = principal.leerTexto("Apellidos: ");
        String ced = principal.leerTexto("Cédula: ");
        LocalDate nac = principal.leerFecha("Fecha de nacimiento");
        String tel = principal.leerTexto("Teléfono: ");
        String txt = principal.leerTexto("Condiciones de salud separadas por coma (Enter si no tiene): ");
        List<String> con = new ArrayList<>();
        for (String par : txt.split(",")) {
            if (!par.trim().isEmpty()) {
                con.add(par.trim());
            }
        }
        System.out.println("-- Cuidador de contacto --");
        String cno = principal.leerTexto("Nombres: ");
        String cap = principal.leerTexto("Apellidos: ");
        String cce = principal.leerTexto("Cédula: ");
        String cte = principal.leerTexto("Teléfono: ");
        String par = principal.leerTexto("Parentesco: ");
        Cuidador cui = new Cuidador(cno, cap, cce, cte, par);
        System.out.println(gestorPacientes.registrarPaciente(nom, ape, ced, nac, tel, con, cui, LocalDate.now()));
    }

    private void registrarValoracion() {
        Paciente pac = gestorPacientes.buscarPorCedula(principal.leerTexto("Cédula del paciente: "));
        if (pac == null) {
            System.out.println("Error: no existe un paciente con esa cédula.");
            return;
        }
        LocalDate fec = principal.leerFecha("Fecha de la valoración");
        int dol = principal.leerEntero("Nivel de dolor (0 a 10): ");
        int tin = principal.leerEntero("Puntaje de Tinetti (0 a 28): ");
        System.out.println(gestorPacientes.registrarValoracion(pac, fec, dol, tin, LocalDate.now()));
    }

    private void listarPacientes() {
        if (gestorPacientes.listarPacientes().isEmpty()) {
            System.out.println("No hay pacientes registrados.");
        }
        for (Paciente pac : gestorPacientes.listarPacientes()) {
            System.out.println(pac.mostrarDatos());
            System.out.println("    " + pac.getCuidador().mostrarDatos());
        }
    }

    private void verValoraciones() {
        Paciente pac = gestorPacientes.buscarPorCedula(principal.leerTexto("Cédula del paciente: "));
        if (pac == null) {
            System.out.println("Error: no existe un paciente con esa cédula.");
            return;
        }
        if (pac.getValoraciones().isEmpty()) {
            System.out.println("El paciente no tiene valoraciones.");
        }
        for (Valoracion val : pac.getValoraciones()) {
            System.out.println("  " + val);
        }
    }
}
