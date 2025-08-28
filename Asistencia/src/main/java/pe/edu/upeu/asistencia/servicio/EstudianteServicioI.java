package pe.edu.upeu.asistencia.servicio;

import pe.edu.upeu.asistencia.modelo.Estudiante;

import java.util.List;

public interface EstudianteServicioI {

    void seve(Estudiante estudiante);//C
    List<Estudiante> findAllEstudiante();//R
    Estudiante updateEstudiante(Estudiante estudiante, int idex);//U
    void delete(int index);//D

    Estudiante finsdEstudiante(int id);//Buscar

}
