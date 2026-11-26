package model;

public abstract class Persona implements Validable {
    private String nombres;
    private String apellidos;
    private String cedula;
    private String telefono;

    public Persona(String nombres, String apellidos, String cedula, String telefono) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.cedula = cedula;
        this.telefono = telefono;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCedula() {
        return cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    public abstract String getRol();

    public String mostrarDatos() {
        return getRol() + ": " + getNombreCompleto() + " | C.I. " + cedula + " | Tel. " + telefono;
    }

    @Override
    public void validar() throws ValidacionException {
        if (nombres == null || nombres.trim().isEmpty()) {
            throw new ValidacionException("nombres", "Los nombres son obligatorios.");
        }
        if (apellidos == null || apellidos.trim().isEmpty()) {
            throw new ValidacionException("apellidos", "Los apellidos son obligatorios.");
        }
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new ValidacionException("cedula", "La cédula es obligatoria.");
        }
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new ValidacionException("telefono", "El teléfono es obligatorio.");
        }
    }
}
