package model;

public class Cuidador extends Persona {
    private String parentesco;

    public Cuidador(String nombres, String apellidos, String cedula, String telefono, String parentesco) {
        super(nombres, apellidos, cedula, telefono);
        this.parentesco = parentesco;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    @Override
    public String getRol() {
        return "Cuidador";
    }

    @Override
    public String mostrarDatos() {
        return super.mostrarDatos() + " | Parentesco: " + parentesco;
    }

    @Override
    public void validar() throws ValidacionException {
        super.validar();
        if (parentesco == null || parentesco.trim().isEmpty()) {
            throw new ValidacionException("parentesco", "El parentesco del cuidador es obligatorio.");
        }
    }
}
