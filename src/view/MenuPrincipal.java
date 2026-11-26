package view;

import model.Paciente;
import model.Persona;
import controller.GestorEjercicios;
import controller.GestorPacientes;
import controller.GestorPlanes;
import controller.GestorSeguimiento;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuPrincipal {
    private Scanner scanner;
    private GestorPacientes gestorPacientes;
    private GestorPlanes gestorPlanes;
    private MenuPacientes menuPacientes;
    private MenuEjercicios menuEjercicios;
    private MenuPlanes menuPlanes;
    private MenuSeguimiento menuSeguimiento;

    public MenuPrincipal(GestorPacientes gestorPacientes, GestorEjercicios gestorEjercicios, GestorPlanes gestorPlanes,
                         GestorSeguimiento gestorSeguimiento) {
        this.scanner = new Scanner(System.in);
        this.gestorPacientes = gestorPacientes;
        this.gestorPlanes = gestorPlanes;
        this.menuPacientes = new MenuPacientes(this, gestorPacientes);
        this.menuEjercicios = new MenuEjercicios(this, gestorEjercicios);
        this.menuPlanes = new MenuPlanes(this, gestorPlanes, gestorPacientes, gestorEjercicios);
        this.menuSeguimiento = new MenuSeguimiento(this, gestorSeguimiento, gestorPlanes, gestorPacientes);
    }

    public void mostrar() {
        int op = -1;
        while (op != 0) {
            System.out.println();
            System.out.println("===== REHABILITACIÓN DE ADULTOS MAYORES =====");
            System.out.println("1. Módulo 1: Pacientes y valoración");
            System.out.println("2. Módulo 2: Ejercicios y contraindicaciones");
            System.out.println("3. Módulo 3: Planes de ejercicio");
            System.out.println("4. Módulo 4: Seguimiento de ejercicios");
            System.out.println("5. Personas registradas");
            System.out.println("0. Salir");
            op = leerEntero("Opción: ");
            switch (op) {
                case 1:
                    menuPacientes.mostrar();
                    break;
                case 2:
                    menuEjercicios.mostrar();
                    break;
                case 3:
                    menuPlanes.mostrar();
                    break;
                case 4:
                    menuSeguimiento.mostrar();
                    break;
                case 5:
                    mostrarPersonas();
                    break;
                case 0:
                    System.out.println("Hasta pronto.");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private void mostrarPersonas() {
        List<Persona> lis = new ArrayList<>();
        for (Paciente pac : gestorPacientes.listarPacientes()) {
            lis.add(pac);
            lis.add(pac.getCuidador());
        }
        lis.addAll(gestorPlanes.listarFisioterapeutas());
        for (Persona per : lis) {
            System.out.println(per.mostrarDatos());
        }
    }

    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    public int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero.");
            }
        }
    }

    public LocalDate leerFecha(String mensaje) {
        while (true) {
            try {
                return LocalDate.parse(leerTexto(mensaje + " (aaaa-mm-dd): "));
            } catch (DateTimeParseException e) {
                System.out.println("Fecha no válida. Ejemplo: 2026-11-25");
            }
        }
    }

    public boolean leerSiNo(String mensaje) {
        while (true) {
            String txt = leerTexto(mensaje + " (S/N): ").toUpperCase();
            if (txt.equals("S")) {
                return true;
            }
            if (txt.equals("N")) {
                return false;
            }
            System.out.println("Responda S o N.");
        }
    }

    public int elegir(String mensaje, int total) {
        if (total == 0) {
            System.out.println("No hay elementos para elegir.");
            return -1;
        }
        int num = leerEntero(mensaje + " (1 a " + total + "): ");
        if (num < 1 || num > total) {
            System.out.println("Número fuera de la lista.");
            return -1;
        }
        return num - 1;
    }
}
