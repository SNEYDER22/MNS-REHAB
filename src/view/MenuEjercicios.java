package view;

import model.Contraindicacion;
import model.Ejercicio;
import model.Gravedad;
import controller.GestorEjercicios;

import java.util.List;

public class MenuEjercicios {
    private MenuPrincipal principal;
    private GestorEjercicios gestorEjercicios;

    public MenuEjercicios(MenuPrincipal principal, GestorEjercicios gestorEjercicios) {
        this.principal = principal;
        this.gestorEjercicios = gestorEjercicios;
    }

    public void mostrar() {
        int op = -1;
        while (op != 0) {
            System.out.println();
            System.out.println("----- Módulo 2: Ejercicios y contraindicaciones -----");
            System.out.println("1. Registrar ejercicio (RF3)");
            System.out.println("2. Registrar contraindicación (RF4)");
            System.out.println("3. Ver catálogo con contraindicaciones");
            System.out.println("0. Volver");
            op = principal.leerEntero("Opción: ");
            switch (op) {
                case 1:
                    registrarEjercicio();
                    break;
                case 2:
                    registrarContraindicacion();
                    break;
                case 3:
                    verCatalogo();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private void registrarEjercicio() {
        String nom = principal.leerTexto("Nombre: ");
        String zon = principal.leerTexto("Zona del cuerpo: ");
        int dif = principal.leerEntero("Dificultad (1 a 3): ");
        String des = principal.leerTexto("Descripción: ");
        System.out.println(gestorEjercicios.registrarEjercicio(nom, zon, dif, des));
    }

    private void registrarContraindicacion() {
        List<Ejercicio> lis = gestorEjercicios.listarEjercicios();
        for (int i = 0; i < lis.size(); i++) {
            System.out.println((i + 1) + ". " + lis.get(i));
        }
        int pos = principal.elegir("Ejercicio", lis.size());
        if (pos < 0) {
            return;
        }
        String con = principal.leerTexto("Condición de salud: ");
        int gra = principal.leerEntero("Gravedad (1 = PRECAUCION, 2 = PROHIBIDO): ");
        Gravedad gve = null;
        if (gra == 1) {
            gve = Gravedad.PRECAUCION;
        } else if (gra == 2) {
            gve = Gravedad.PROHIBIDO;
        }
        String rec = principal.leerTexto("Recomendación: ");
        System.out.println(gestorEjercicios.registrarContraindicacion(lis.get(pos), con, gve, rec));
    }

    private void verCatalogo() {
        if (gestorEjercicios.listarEjercicios().isEmpty()) {
            System.out.println("El catálogo está vacío.");
        }
        for (Ejercicio eje : gestorEjercicios.listarEjercicios()) {
            System.out.println(eje);
            for (Contraindicacion con : eje.getContraindicaciones()) {
                System.out.println("    - " + con);
            }
        }
    }
}
