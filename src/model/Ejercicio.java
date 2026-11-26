package model;

import java.util.ArrayList;
import java.util.List;

public class Ejercicio implements Validable {
    private String nombre;
    private String zonaCuerpo;
    private int dificultad;
    private String descripcion;
    private List<Contraindicacion> contraindicaciones;

    public Ejercicio(String nombre, String zonaCuerpo, int dificultad, String descripcion) {
        this.nombre = nombre;
        this.zonaCuerpo = zonaCuerpo;
        this.dificultad = dificultad;
        this.descripcion = descripcion;
        this.contraindicaciones = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getZonaCuerpo() {
        return zonaCuerpo;
    }

    public void setZonaCuerpo(String zonaCuerpo) {
        this.zonaCuerpo = zonaCuerpo;
    }

    public int getDificultad() {
        return dificultad;
    }

    public void setDificultad(int dificultad) {
        this.dificultad = dificultad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Contraindicacion> getContraindicaciones() {
        return contraindicaciones;
    }

    public boolean agregarContraindicacion(Contraindicacion c) {
        if (buscarContraindicacion(c.getCondicionSalud()) != null) {
            return false;
        }
        contraindicaciones.add(c);
        return true;
    }

    public Contraindicacion buscarContraindicacion(String cond) {
        for (Contraindicacion con : contraindicaciones) {
            if (con.getCondicionSalud().equalsIgnoreCase(cond)) {
                return con;
            }
        }
        return null;
    }

    public boolean esIgualA(String nombre, String zona) {
        return this.nombre.equalsIgnoreCase(nombre) && this.zonaCuerpo.equalsIgnoreCase(zona);
    }

    @Override
    public void validar() throws ValidacionException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ValidacionException("nombre", "El nombre del ejercicio es obligatorio.");
        }
        if (zonaCuerpo == null || zonaCuerpo.trim().isEmpty()) {
            throw new ValidacionException("zonaCuerpo", "La zona del cuerpo es obligatoria.");
        }
        if (dificultad < 1 || dificultad > 3) {
            throw new ValidacionException("dificultad", "La dificultad debe estar entre 1 y 3.");
        }
    }

    @Override
    public String toString() {
        return nombre + " | Zona: " + zonaCuerpo + " | Dificultad " + dificultad + " | Contraindicaciones: " + contraindicaciones.size();
    }
}
