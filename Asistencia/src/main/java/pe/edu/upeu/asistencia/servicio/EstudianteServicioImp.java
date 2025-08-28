package pe.edu.upeu.asistencia.servicio;

import pe.edu.upeu.asistencia.modelo.Estudiante;
import pe.edu.upeu.asistencia.repositorio.EstudianteRepositori;

import java.util.List;

public class EstudianteServicioImp extends EstudianteRepositori implements EstudianteServicioI {


    @Override
    public void seve(Estudiante estudiante) {
        estudiantes.add(estudiante);

    }

    @Override
    public List<Estudiante> findEstudiantes(){
        if (estudiantes.size()==1){
            return  super.findAllEstudiantes();
        }
        return estudiantes;
    }

    @Override
    public Estudiante updateEstudiante(Estudiante estudiante, int idex) {
        return estudiantes.set(idex, estudiante);
    }

    @Override
    public void delete(int index) {
        estudiantes.remove(index);

    }

    @Override
    public Estudiante finsdEstudiante(int index) {
        return estudiantes.get(index);
    }
}
