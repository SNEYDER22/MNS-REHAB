package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Paciente extends Persona {
    private String codigo;
    private LocalDate fechaNacimiento;
    private List<String> condicionesSalud;
    private Cuidador cuidador;
    private List<Valoracion> valoraciones;

    public Paciente(String codigo, String nombres, String apellidos, String cedula, String telefono,
                    LocalDate fechaNacimiento, List<String> condicionesSalud, Cuidador cuidador) {
        super(nombres, apellidos, cedula, telefono);
        this.codigo = codigo;
        this.fechaNacimiento = fechaNacimiento;
        this.condicionesSalud = condicionesSalud;
        this.cuidador = cuidador;
        this.valoraciones = new ArrayList<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public List<String> getCondicionesSalud() {
        return condicionesSalud;
    }

    public void setCondicionesSalud(List<String> condicionesSalud) {
        this.condicionesSalud = condicionesSalud;
    }

    public Cuidador getCuidador() {
        return cuidador;
    }

    public void setCuidador(Cuidador cuidador) {
        this.cuidador = cuidador;
    }

    public List<Valoracion> getValoraciones() {
        return valoraciones;
    }

    public int calcularEdad(LocalDate hoy) {
        int eda = hoy.getYear() - fechaNacimiento.getYear();
        if (fechaNacimiento.plusYears(eda).isAfter(hoy)) {
            eda--;
        }
        return eda;
    }

    public void agregarValoracion(Valoracion v) {
        valoraciones.add(v);
    }

    public Valoracion getUltimaValoracion() {
        Valoracion ult = null;
        for (Valoracion val : valoraciones) {
            if (ult == null || !val.getFecha().isBefore(ult.getFecha())) {
                ult = val;
            }
        }
        return ult;
    }

    public List<Valoracion> valoracionesEnRango(LocalDate ini, LocalDate fin) {
        List<Valoracion> lis = new ArrayList<>();
        for (Valoracion val : valoraciones) {
            if (!val.getFecha().isBefore(ini) && !val.getFecha().isAfter(fin)) {
                int pos = lis.size();
                while (pos > 0 && lis.get(pos - 1).getFecha().isAfter(val.getFecha())) {
                    pos--;
                }
                lis.add(pos, val);
            }
        }
        return lis;
    }

    public boolean tieneCondicion(String condicion) {
        for (String con : condicionesSalud) {
            if (con.equalsIgnoreCase(condicion)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getRol() {
        return "Paciente";
    }

    @Override
    public String mostrarDatos() {
        String con = condicionesSalud.isEmpty() ? "ninguna" : String.join(", ", condicionesSalud);
        return super.mostrarDatos() + " | Código " + codigo + " | Condiciones: " + con;
    }

    @Override
    public void validar() throws ValidacionException {
        super.validar();
        if (fechaNacimiento == null) {
            throw new ValidacionException("fechaNacimiento", "La fecha de nacimiento es obligatoria.");
        }
        if (cuidador == null) {
            throw new ValidacionException("cuidador", "El cuidador de contacto es obligatorio.");
        }
        cuidador.validar();
    }
}
