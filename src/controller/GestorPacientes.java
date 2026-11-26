package controller;

import model.Cuidador;
import model.Paciente;
import model.ValidacionException;
import model.Valoracion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GestorPacientes {
    private List<Paciente> pacientes;

    public GestorPacientes() {
        this.pacientes = new ArrayList<>();
    }

    public String registrarPaciente(String nombres, String apellidos, String cedula, LocalDate nacimiento, String telefono,
                                    List<String> condiciones, Cuidador cuidador, LocalDate hoy) {
        if (!validarCedula(cedula)) {
            return "Error: la cédula " + cedula + " no es una cédula ecuatoriana válida.";
        }
        if (existeCedula(cedula)) {
            return "Error: ya existe un paciente con la cédula " + cedula + ".";
        }
        Paciente pac = new Paciente(generarCodigo(), nombres, apellidos, cedula, telefono, nacimiento, condiciones, cuidador);
        try {
            pac.validar();
        } catch (ValidacionException e) {
            return "Error: " + e.getMessage();
        }
        if (nacimiento.isAfter(hoy)) {
            return "Error: la fecha de nacimiento no puede ser futura.";
        }
        int eda = pac.calcularEdad(hoy);
        if (eda < 65) {
            return "Error: el paciente debe tener 65 años o más (tiene " + eda + ").";
        }
        pacientes.add(pac);
        return "Paciente registrado con código " + pac.getCodigo() + ".";
    }

    public boolean validarCedula(String cedula) {
        if (cedula == null || cedula.length() != 10) {
            return false;
        }
        for (int i = 0; i < 10; i++) {
            if (!Character.isDigit(cedula.charAt(i))) {
                return false;
            }
        }
        int pro = Integer.parseInt(cedula.substring(0, 2));
        if (pro < 1 || (pro > 24 && pro != 30)) {
            return false;
        }
        if (cedula.charAt(2) - '0' > 5) {
            return false;
        }
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            int dig = cedula.charAt(i) - '0';
            if (i % 2 == 0) {
                dig = dig * 2;
                if (dig > 9) {
                    dig = dig - 9;
                }
            }
            sum = sum + dig;
        }
        int ver = (10 - sum % 10) % 10;
        return ver == cedula.charAt(9) - '0';
    }

    public boolean existeCedula(String cedula) {
        return buscarPorCedula(cedula) != null;
    }

    public String registrarValoracion(Paciente p, LocalDate fecha, int dolor, int tinetti, LocalDate hoy) {
        if (p == null) {
            return "Error: el paciente no existe.";
        }
        if (fecha != null && fecha.isAfter(hoy)) {
            return "Error: la fecha de la valoración no puede ser futura.";
        }
        Valoracion val = new Valoracion(fecha, dolor, tinetti);
        try {
            val.validar();
        } catch (ValidacionException e) {
            return "Error: " + e.getMessage();
        }
        Valoracion ant = p.getUltimaValoracion();
        p.agregarValoracion(val);
        String men = "Valoración registrada. Riesgo de caída: " + val.getRiesgoCaida() + ".";
        if (ant == null) {
            return men + " Primera valoración.";
        }
        return men + " Diferencia con la anterior: dolor " + val.diferenciaDolor(ant) + ", Tinetti " + val.diferenciaTinetti(ant) + ".";
    }

    public Paciente buscarPorCedula(String cedula) {
        for (Paciente pac : pacientes) {
            if (pac.getCedula().equals(cedula)) {
                return pac;
            }
        }
        return null;
    }

    public List<Paciente> listarPacientes() {
        return pacientes;
    }

    private String generarCodigo() {
        return "PAC-" + String.format("%03d", pacientes.size() + 1);
    }
}
