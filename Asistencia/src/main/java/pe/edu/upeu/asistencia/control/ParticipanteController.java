package pe.edu.upeu.asistencia.control;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.asistencia.enums.Carrera;
import pe.edu.upeu.asistencia.enums.TipoParticipante;

@Controller
public class ParticipanteController {

    @FXML
    private ComboBox<Carrera> cbxCarrera;

    @FXML
    private ComboBox<TipoParticipante> cbxTipoParticipante;

    @FXML
    public void  initialize(){
        cbxCarrera.getItems().setAll(Carrera.values());
        cbxTipoParticipante.getItems().setAll(TipoParticipante.values());
    }
}
