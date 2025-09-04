package pe.edu.upeu.asistencia.control;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;


@Controller
public class MainguiController {

    @FXML
    private MenuItem menuItem1, menuItem2;
    @FXML
    private MenuBar menuBar;

    @FXML
    private TabPane tabPane;

    @Autowired
    private ApplicationContext context;

    @FXML
    public void initialize() {

    }

    class MenuItemListener{



        public void handle(ActionEvent e) {
        }

        public void abrirArchivoFXML(String filename, String tittle){
            try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(filename));
            loader.setControllerFactory(context::getBean);
            Parent root = loader.load();

            ScrollPane scrollPane = (ScrollPane) root;
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            Tab newTab = new  Tab(tittle, scrollPane);
            tabPane.getTabs().clear();
            tabPane.getTabs().add(newTab);

            }catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }




    }

    class MenuListener{
        public void handle(Event e) {

        }
    }


}
