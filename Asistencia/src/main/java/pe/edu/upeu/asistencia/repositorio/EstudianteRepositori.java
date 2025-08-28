package pe.edu.upeu.asistencia.repositorio;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import pe.edu.upeu.asistencia.modelo.Estudiante;

import java.util.ArrayList;

import java.util.List;

public abstract class EstudianteRepositori {
    public List<Estudiante> estudiantes=new ArrayList<>();

    List<Estudiante> findAllEstudiante() {
        estudiantes.add(new Estudiante(
                new SimpleStringProperty("Juan"),
                new SimpleBooleanProperty(true)
        )
    );
        return estudiantes;
    }

    protected List<Estudiante> findAllEstudiantes() {
        return null;
    }

    public abstract List<Estudiante> findEstudiantes();
}
