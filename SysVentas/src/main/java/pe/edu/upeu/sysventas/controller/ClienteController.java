package pe.edu.upeu.sysventas.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.sysventas.components.ColumnInfo;
import pe.edu.upeu.sysventas.components.ComboBoxAutoComplete;
import pe.edu.upeu.sysventas.components.TableViewHelper;
import pe.edu.upeu.sysventas.components.Toast;
import pe.edu.upeu.sysventas.dto.ComboBoxOption;
import pe.edu.upeu.sysventas.model.Cliente;
import pe.edu.upeu.sysventas.service.IClienteService;

import java.util.*;
import java.util.function.Consumer;

@Controller
public class ClienteController {

    @FXML
    TextField txtDniRuc, txtNombres, txtRepLegal, txtFiltroDato;

    @FXML
    ComboBox<ComboBoxOption> cbxTipoDocumento;

    @FXML
    private TableView<Cliente> tableView;

    @FXML
    Label lbnMsg, idPrueba;

    @FXML
    private AnchorPane miContenedor;

    Stage stage;

    @Autowired
    IClienteService clienteService;

    private Validator validator;

    ObservableList<Cliente> listarCliente;

    Cliente formulario;
    String idClienteCE;

    private void filtrarClientes(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            tableView.getItems().clear();
            tableView.getItems().addAll(listarCliente);
        } else {
            String lowerCaseFilter = filtro.toLowerCase();

            List<Cliente> filtrados = listarCliente.stream()
                    .filter(c -> {
                        if (c.getDniruc().toLowerCase().contains(lowerCaseFilter)) return true;
                        if (c.getNombres().toLowerCase().contains(lowerCaseFilter)) return true;
                        if (c.getRepLegal() != null && c.getRepLegal().toLowerCase().contains(lowerCaseFilter)) return true;
                        if (c.getTipoDocumento() != null && c.getTipoDocumento().name().toLowerCase().contains(lowerCaseFilter)) return true;
                        return false;
                    })
                    .toList();

            tableView.getItems().clear();
            tableView.getItems().addAll(filtrados);
        }
    }



    public void listar() {
        try {
            tableView.getItems().clear();
            listarCliente = FXCollections.observableArrayList(clienteService.findAll());
            tableView.getItems().addAll(listarCliente);

            txtFiltroDato.textProperty().addListener((o, ov, nv) -> filtrarClientes(nv));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void initialize() {

        // Obtener stage después de que cargue el AnchorPane
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), event -> {
            stage = (Stage) miContenedor.getScene().getWindow();
        }));
        timeline.setCycleCount(1);
        timeline.play();

        // Llenar ComboBox Tipo Doc
        try {
            ObservableList<ComboBoxOption> opciones = FXCollections.observableArrayList();
            for (pe.edu.upeu.sysventas.enums.TipoDocumento td : pe.edu.upeu.sysventas.enums.TipoDocumento.values()) {
                opciones.add(new ComboBoxOption(td.name(), td.name()));
            }
            cbxTipoDocumento.setItems(opciones);
        } catch (Exception ex) {
            System.out.println("Error cargando Tipo Documento: " + ex.getMessage());
            cbxTipoDocumento.setItems(FXCollections.observableArrayList());
        }

        cbxTipoDocumento.setOnAction(event -> {
            ComboBoxOption selected = cbxTipoDocumento.getSelectionModel().getSelectedItem();
            if (selected != null) {
                System.out.println("ID Tipo Documento: " + selected.getKey());
            }
        });

        new ComboBoxAutoComplete<>(cbxTipoDocumento);

        // VALIDADOR
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        // TABLA
        TableViewHelper<Cliente> helper = new TableViewHelper<>();

        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("DNI/RUC", new ColumnInfo("dniruc", 120.0));
        columns.put("Nombres", new ColumnInfo("nombres", 200.0));
        columns.put("Rep. Legal", new ColumnInfo("repLegal", 200.0));
        columns.put("Tipo Doc.", new ColumnInfo("tipoDocumento", 150.0));

        Consumer<Cliente> updateAction = cliente -> editForm(cliente);

        Consumer<Cliente> deleteAction = cliente -> {
            clienteService.delete(cliente.getDniruc());
            double w = stage.getWidth() / 1.5;
            double h = stage.getHeight() / 2;
            Toast.showToast(stage, "Cliente eliminado correctamente!!", 2000, w, h);
            listar();
        };

        helper.addColumnsInOrderWithSize(tableView, columns, updateAction, deleteAction);
        tableView.setTableMenuButtonVisible(true);

        listar();
    }

    public void limpiarError() {
        List<Control> controles = List.of(txtDniRuc, txtNombres, txtRepLegal, cbxTipoDocumento);
        controles.forEach(c -> c.getStyleClass().remove("text-field-error"));
    }

    public void clearForm() {
        txtDniRuc.clear();
        txtNombres.clear();
        txtRepLegal.clear();
        cbxTipoDocumento.getSelectionModel().clearSelection();
        idClienteCE = null;
        limpiarError();
    }

    private void editForm(Cliente cliente) {
        if (cliente == null) { return; }
        try {
            // Cargar datos al formulario
            if (txtDniRuc != null) txtDniRuc.setText(cliente.getDniruc());
            if (txtNombres != null) txtNombres.setText(cliente.getNombres());
            if (txtRepLegal != null) txtRepLegal.setText(cliente.getRepLegal());
            idClienteCE = cliente.getDniruc();

            // Seleccionar Tipo de Documento en el ComboBox
            if (cbxTipoDocumento != null) {
                if (cliente.getTipoDocumento() != null) {
                    String key = cliente.getTipoDocumento().name();
                    ObservableList<ComboBoxOption> items = cbxTipoDocumento.getItems();
                    if (items != null) {
                        for (ComboBoxOption opt : items) {
                            if (key.equals(opt.getKey())) {
                                cbxTipoDocumento.getSelectionModel().select(opt);
                                break;
                            }
                        }
                    }
                } else {
                    cbxTipoDocumento.getSelectionModel().clearSelection();
                }
            }
        } catch (Exception ex) {
            System.out.println("Error cargando formulario de cliente: " + ex.getMessage());
        }
    }

    private void mostrarErroresValidacion(List<ConstraintViolation<Cliente>> violaciones) {
        limpiarError();

        Map<String, Control> campos = new LinkedHashMap<>();
        campos.put("dniruc", txtDniRuc);
        campos.put("nombres", txtNombres);
        campos.put("repLegal", txtRepLegal);
        campos.put("tipoDocumento", cbxTipoDocumento);

        LinkedHashMap<String, String> erroresOrdenados = new LinkedHashMap<>();
        final Control[] primerControl = {null};

        for (String campo : campos.keySet()) {
            violaciones.stream()
                    .filter(v -> v.getPropertyPath().toString().equals(campo))
                    .findFirst()
                    .ifPresent(v -> {
                        erroresOrdenados.put(campo, v.getMessage());
                        Control ctrl = campos.get(campo);

                        if (!ctrl.getStyleClass().contains("text-field-error"))
                            ctrl.getStyleClass().add("text-field-error");

                        if (primerControl[0] == null) primerControl[0] = ctrl;
                    });
        }

        if (!erroresOrdenados.isEmpty()) {
            var error = erroresOrdenados.entrySet().iterator().next();
            lbnMsg.setText(error.getValue());
            lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");

            if (primerControl[0] != null) {
                Platform.runLater(primerControl[0]::requestFocus);
            }
        }
    }

    private void procesarFormulario() {
        lbnMsg.setText("Formulario válido");
        lbnMsg.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
        limpiarError();

        double w = stage.getWidth() / 1.5;
        double h = stage.getHeight() / 2;

        if (idClienteCE != null && !idClienteCE.isEmpty()) {
            // Actualizar usando el ID (dniruc) existente
            clienteService.update(idClienteCE, formulario);
            Toast.showToast(stage, "Cliente actualizado!!", 2000, w, h);
        } else {
            // Guardar nuevo cliente
            clienteService.save(formulario);
            Toast.showToast(stage, "Cliente guardado!!", 2000, w, h);
        }

        clearForm();
        listar();
    }

    @FXML
    public void validarFormulario() {
        formulario = new Cliente();
        formulario.setDniruc(txtDniRuc.getText());
        formulario.setNombres(txtNombres.getText());
        formulario.setRepLegal(txtRepLegal.getText());

        String idTD = cbxTipoDocumento.getSelectionModel().getSelectedItem() == null
                ? "0"
                : cbxTipoDocumento.getSelectionModel().getSelectedItem().getKey();

        formulario.setTipoDocumento(idTD.equals("0") ? null : pe.edu.upeu.sysventas.enums.TipoDocumento.valueOf(idTD));

        Set<ConstraintViolation<Cliente>> violaciones = validator.validate(formulario);

        List<ConstraintViolation<Cliente>> ordenadas =
                violaciones.stream()
                        .sorted(Comparator.comparing(v -> v.getPropertyPath().toString()))
                        .toList();

        if (ordenadas.isEmpty()) {
            procesarFormulario();
        } else {
            mostrarErroresValidacion(ordenadas);
        }
    }
}