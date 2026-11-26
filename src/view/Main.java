package view;

import model.Cuidador;
import model.Ejercicio;
import model.Gravedad;
import model.Paciente;
import model.PlanEjercicio;
import model.RegistroEjercicio;
import controller.GestorEjercicios;
import controller.GestorPacientes;
import controller.GestorPlanes;
import controller.GestorSeguimiento;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        GestorPacientes gpa = new GestorPacientes();
        GestorEjercicios gej = new GestorEjercicios();
        GestorPlanes gpl = new GestorPlanes();
        GestorSeguimiento gse = new GestorSeguimiento(gpl);
        cargarDatos(gpa, gej, gpl, gse);
        MenuPrincipal men = new MenuPrincipal(gpa, gej, gpl, gse);
        men.mostrar();
    }

    private static void cargarDatos(GestorPacientes gpa, GestorEjercicios gej, GestorPlanes gpl, GestorSeguimiento gse) {
        LocalDate hoy = LocalDate.now();
        List<String> co1 = new ArrayList<>();
        co1.add("Osteoporosis");
        gpa.registrarPaciente("Rosa Elena", "Guamán Pérez", "1705123451", LocalDate.of(1952, 3, 14), "0991234567", co1,
                new Cuidador("Diego", "Guamán", "1104567894", "0987654321", "Hijo"), hoy);
        List<String> co2 = new ArrayList<>();
        co2.add("Prótesis de cadera");
        gpa.registrarPaciente("Luis Alberto", "Andrade Mora", "1002456786", LocalDate.of(1948, 8, 2), "022345678", co2,
                new Cuidador("Carmen", "Andrade", "0603451238", "0998877665", "Hija"), hoy);
        Paciente ros = gpa.buscarPorCedula("1705123451");
        Paciente lui = gpa.buscarPorCedula("1002456786");
        gpa.registrarValoracion(ros, hoy.minusDays(30), 6, 20, hoy);
        gpa.registrarValoracion(ros, hoy.minusDays(2), 4, 23, hoy);
        gpa.registrarValoracion(lui, hoy.minusDays(20), 5, 17, hoy);

        gej.registrarEjercicio("Sentadilla asistida", "Piernas", 2, "Sentarse y levantarse de una silla con apoyo.");
        gej.registrarEjercicio("Elevación de talones", "Piernas", 1, "Ponerse de puntillas sujetándose de una mesa.");
        gej.registrarEjercicio("Saltos suaves", "Piernas", 3, "Saltos pequeños en el mismo lugar.");
        gej.registrarEjercicio("Puente de glúteos", "Cadera", 1, "Acostado boca arriba, elevar la cadera.");
        gej.registrarContraindicacion(gej.buscarEjercicio("Saltos suaves", "Piernas"), "Osteoporosis", Gravedad.PROHIBIDO,
                "El impacto puede causar fracturas.");
        gej.registrarContraindicacion(gej.buscarEjercicio("Sentadilla asistida", "Piernas"), "Osteoporosis", Gravedad.PRECAUCION,
                "Hacerlo con apoyo y sin peso extra.");
        gej.registrarContraindicacion(gej.buscarEjercicio("Puente de glúteos", "Cadera"), "Prótesis de cadera", Gravedad.PRECAUCION,
                "No elevar la cadera más de 30 grados.");

        List<DayOfWeek> dia = new ArrayList<>();
        dia.add(DayOfWeek.MONDAY);
        dia.add(DayOfWeek.WEDNESDAY);
        dia.add(DayOfWeek.FRIDAY);
        Ejercicio tal = gej.buscarEjercicio("Elevación de talones", "Piernas");
        gpl.asignarPlan(ros, gpl.buscarFisioterapeuta("MSP-FT-0457"), tal, 3, 12, dia, hoy.minusDays(14), 4, false);
        PlanEjercicio pla = gpl.planesDePaciente(ros).get(0);
        gpl.generarCalendario(pla);
        int num = 0;
        for (RegistroEjercicio reg : pla.getRegistros()) {
            if (reg.getFechaProgramada().isBefore(hoy.minusDays(2))) {
                num++;
                if (num == 3) {
                    gse.registrarOmitido(reg, "Tuvo cita médica", hoy);
                } else {
                    gse.registrarRealizado(pla, reg, 3, 3, hoy);
                }
            }
        }
    }
}
