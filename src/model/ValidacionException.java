package model;

public class ValidacionException extends Exception {
    private String campo;

    public ValidacionException(String mensaje) {
        super(mensaje);
        this.campo = "";
    }

    public ValidacionException(String campo, String mensaje) {
        super(mensaje);
        this.campo = campo;
    }

    public String getCampo() {
        return campo;
    }
}
