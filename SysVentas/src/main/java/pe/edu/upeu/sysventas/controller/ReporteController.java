package pe.edu.upeu.sysventas.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.StackPane;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.sysventas.service.IVentaService;
import win.zqxu.jrviewer.JRViewerFX;

import java.time.format.DateTimeFormatter;

@Controller
public class ReporteController {

    @FXML
    DatePicker txtFechaI, txtFechaF;
    @FXML
    StackPane paneRepor;

    private JasperPrint jasperPrint;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    IVentaService daoV;

    @FXML
    void generarReporte(){
        if(txtFechaI.getValue()!=null && txtFechaF.getValue()!=null){
            if(txtFechaI.getValue().isBefore(txtFechaF.getValue()) ){
                String fechaI = txtFechaI.getValue().format(formatter);
                String fechaF = txtFechaF.getValue().format(formatter);
                try {
                    jasperPrint=daoV.runReportVentas(fechaI,fechaF);
                    JRViewerFX viewer = new JRViewerFX(jasperPrint);
                    paneRepor.getChildren().clear();
                    paneRepor.getChildren().add(viewer);
                    StackPane.setAlignment(viewer, Pos.CENTER);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }else{
                System.out.println("La fecha final debe ser mayor o igual a "+txtFechaI.getValue());
                return;
            }
        }else{
            System.out.println("Fecha Incorrecto");
            return;
        }
    }

}
