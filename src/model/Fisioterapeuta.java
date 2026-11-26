package model;

public class Fisioterapeuta extends Persona {
    private String registroProfesional;

    public Fisioterapeuta(String nombres, String apellidos, String cedula, String telefono, String registroProfesional) {
        super(nombres, apellidos, cedula, telefono);
        this.registroProfesional = registroProfesional;
    }

    public String getRegistroProfesional() {
        return registroProfesional;
    }

    public void setRegistroProfesional(String registroProfesional) {
        this.registroProfesional = registroProfesional;
    }

    @Override
    public String getRol() {
        return "Fisioterapeuta";
    }

    @Override
    public String mostrarDatos() {
        return super.mostrarDatos() + " | Registro " + registroProfesional;
    }

    @Override
    public void validar() throws ValidacionException {
        super.validar();
        if (registroProfesional == null || registroProfesional.trim().isEmpty()) {
            throw new ValidacionException("registroProfesional", "El registro profesional es obligatorio.");
        }
    }
}
