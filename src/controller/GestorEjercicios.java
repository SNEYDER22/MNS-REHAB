package controller;

import model.Contraindicacion;
import model.Ejercicio;
import model.Gravedad;
import model.ValidacionException;

import java.util.ArrayList;
import java.util.List;

public class GestorEjercicios {
    private List<Ejercicio> ejercicios;

    public GestorEjercicios() {
        this.ejercicios = new ArrayList<>();
    }

    public String registrarEjercicio(String nombre, String zona, int dificultad, String desc) {
        Ejercicio eje = new Ejercicio(nombre, zona, dificultad, desc);
        try {
            eje.validar();
        } catch (ValidacionException e) {
            return "Error: " + e.getMessage();
        }
        if (existeEjercicio(nombre, zona)) {
            return "Error: ya existe el ejercicio " + nombre + " para la zona " + zona + ".";
        }
        ejercicios.add(eje);
        return "Ejercicio registrado: " + nombre + " (" + zona + ").";
    }

    public String registrarContraindicacion(Ejercicio ejercicio, String condicion, Gravedad gravedad, String recomendacion) {
        if (ejercicio == null) {
            return "Error: el ejercicio no existe.";
        }
        Contraindicacion con = new Contraindicacion(condicion, gravedad, recomendacion);
        try {
            con.validar();
        } catch (ValidacionException e) {
            return "Error: " + e.getMessage();
        }
        if (!ejercicio.agregarContraindicacion(con)) {
            return "Error: " + ejercicio.getNombre() + " ya tiene una contraindicación para " + condicion + ".";
        }
        return "Contraindicación registrada para " + ejercicio.getNombre() + ".";
    }

    public boolean existeEjercicio(String nombre, String zona) {
        return buscarEjercicio(nombre, zona) != null;
    }

    public Ejercicio buscarEjercicio(String nombre, String zona) {
        for (Ejercicio eje : ejercicios) {
            if (eje.esIgualA(nombre, zona)) {
                return eje;
            }
        }
        return null;
    }

    public List<Ejercicio> listarEjercicios() {
        return ejercicios;
    }
}
