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
import pe.edu.upeu.sysventas.model.Stock;
import pe.edu.upeu.sysventas.service.ICategoriaService;
import pe.edu.upeu.sysventas.service.IMarcaService;
import pe.edu.upeu.sysventas.service.StockIService;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Controller
public class StockController {

    @FXML
    private TextField txtNombreStock, txtCantidad, txtFiltroDato;

    @FXML
    private ComboBox<ComboBoxOption> cbxMarca;

    @FXML
    private ComboBox<ComboBoxOption> cbxCategoria;

    @FXML
    private TableView<Stock> tableView;

    @FXML
    private Label lbnMsg;

    @FXML
    private AnchorPane miContenedor;

    Stage stage;

    @Autowired
    IMarcaService ms;

    @Autowired
    ICategoriaService cs;

    @Autowired
    StockIService ss;

    private Validator validator;
    ObservableList<Stock> listarStock;
    Stock formulario;
    Long idStockCE = 0L;

    /** FILTRO **/
    private void filtrarStock(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            tableView.getItems().setAll(listarStock);
        } else {
            String lower = filtro.toLowerCase();
            List<Stock> resultado = listarStock.stream()
                    .filter(s ->
                            s.getNombre().toLowerCase().contains(lower) ||
                                    String.valueOf(s.getCantidad()).contains(lower) ||
                                    s.getMarca().getNombre().toLowerCase().contains(lower) ||
                                    s.getCategoria().getNombre().toLowerCase().contains(lower)
                    ).collect(Collectors.toList());

            tableView.getItems().setAll(resultado);
        }
    }

    /** LISTAR **/
    public void listar() {
        try {
            listarStock = FXCollections.observableArrayList(ss.findAll());
            tableView.getItems().setAll(listarStock);

            txtFiltroDato.textProperty().addListener((obs, oldVal, newVal) -> {
                filtrarStock(newVal);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000),
                event -> {
                    stage = (Stage) miContenedor.getScene().getWindow();
                }));
        timeline.setCycleCount(1);
        timeline.play();

        /** COMBOBOX MARCA **/
        cbxMarca.getItems().addAll(ms.listarCombobox());
        new ComboBoxAutoComplete<>(cbxMarca);

        /** COMBOBOX CATEGORÍA **/
        cbxCategoria.getItems().addAll(cs.listarCombobox());
        new ComboBoxAutoComplete<>(cbxCategoria);

        /** VALIDATOR **/
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        /** TABLA **/
        TableViewHelper<Stock> tableViewHelper = new TableViewHelper<>();

        LinkedHashMap<String, ColumnInfo> columnas = new LinkedHashMap<>();
        columnas.put("ID", new ColumnInfo("idProducto", 80.0));
        columnas.put("Nombre", new ColumnInfo("nombre", 200.0));
        columnas.put("Cantidad", new ColumnInfo("cantidad", 120.0));
        columnas.put("Marca", new ColumnInfo("marca.nombre", 150.0));
        columnas.put("Categoría", new ColumnInfo("categoria.nombre", 150.0));

        Consumer<Stock> actualizar = (Stock st) -> {
            editForm(st);
        };

        Consumer<Stock> eliminar = (Stock st) -> {
            ss.delete(st.getIdProducto());
            double w = stage.getWidth() / 1.5;
            double h = stage.getHeight() / 2;
            Toast.showToast(stage, "Se eliminó correctamente!!", 2000, w, h);
            listar();
        };

        tableViewHelper.addColumnsInOrderWithSize(tableView,
                columnas, actualizar, eliminar);

        tableView.setTableMenuButtonVisible(true);

        listar();
    }

    /** CLEAR **/
    public void clearForm() {
        txtNombreStock.clear();
        txtCantidad.clear();
        cbxMarca.getSelectionModel().clearSelection();
        cbxCategoria.getSelectionModel().clearSelection();
        idStockCE = 0L;
        limpiarError();
    }

    /** LIMPIAR ESTILOS **/
    public void limpiarError() {
        List<Control> controles = List.of(
                txtNombreStock, txtCantidad, cbxMarca, cbxCategoria
        );
        controles.forEach(c -> c.getStyleClass().remove("text-field-error"));
    }

    /** EDITAR **/
    public void editForm(Stock s) {
        txtNombreStock.setText(s.getNombre());
        txtCantidad.setText(String.valueOf(s.getCantidad()));

        cbxMarca.getSelectionModel().select(
                cbxMarca.getItems().stream()
                        .filter(m -> Long.parseLong(m.getKey()) == s.getMarca().getIdMarca())
                        .findFirst().orElse(null)
        );

        cbxCategoria.getSelectionModel().select(
                cbxCategoria.getItems().stream()
                        .filter(c -> Long.parseLong(c.getKey()) == s.getCategoria().getIdCategoria())
                        .findFirst().orElse(null)
        );

        idStockCE = s.getIdProducto();
        limpiarError();
    }

    /** VALIDAR **/
    private void mostrarErroresValidacion(List<ConstraintViolation<Stock>> violaciones) {
        limpiarError();

        Map<String, Control> controles = new LinkedHashMap<>();
        controles.put("nombre", txtNombreStock);
        controles.put("cantidad", txtCantidad);
        controles.put("categoria", cbxCategoria);
        controles.put("marca", cbxMarca);

        final Control[] primerError = {null};

        for (String campo : controles.keySet()) {
            violaciones.stream()
                    .filter(v -> v.getPropertyPath().toString().equals(campo))
                    .findFirst()
                    .ifPresent(v -> {
                        Control ctrl = controles.get(campo);
                        ctrl.getStyleClass().add("text-field-error");
                        if (primerError[0] == null) primerError[0] = ctrl;

                        lbnMsg.setText(v.getMessage());
                        lbnMsg.setStyle("-fx-text-fill: red;");
                    });
        }

        if (primerError[0] != null) {
            Platform.runLater(primerError[0]::requestFocus);
        }
    }

    /** GUARDAR / ACTUALIZAR **/
    private void procesarFormulario() {
        double w = stage.getWidth() / 1.5;
        double h = stage.getHeight() / 2;

        if (idStockCE > 0L) {
            formulario.setIdProducto(idStockCE);
            ss.update(formulario);
            Toast.showToast(stage, "Actualizado correctamente!", 2000, w, h);
        } else {
            ss.save(formulario);
            Toast.showToast(stage, "Guardado correctamente!", 2000, w, h);
        }

        clearForm();
        listar();
    }

    @FXML
    public void validarFormulario() {

        formulario = new Stock();
        formulario.setNombre(txtNombreStock.getText());

        try {
            formulario.setCantidad(Integer.parseInt(txtCantidad.getText()));
        } catch (Exception e) {
            formulario.setCantidad(-1);
        }

        String idM = cbxMarca.getSelectionModel().getSelectedItem() == null ? "0"
                : cbxMarca.getSelectionModel().getSelectedItem().getKey();

        formulario.setMarca(idM.equals("0") ? null : ms.findById(Long.parseLong(idM)));

        String idC = cbxCategoria.getSelectionModel().getSelectedItem() == null ? "0"
                : cbxCategoria.getSelectionModel().getSelectedItem().getKey();

        formulario.setCategoria(idC.equals("0") ? null : cs.findById(Long.parseLong(idC)));

        Set<ConstraintViolation<Stock>> violaciones = validator.validate(formulario);
        List<ConstraintViolation<Stock>> ordenadas = violaciones.stream()
                .sorted(Comparator.comparing(v -> v.getPropertyPath().toString()))
                .toList();

        if (ordenadas.isEmpty()) {
            lbnMsg.setText("Formulario válido");
            lbnMsg.setStyle("-fx-text-fill: green;");
            procesarFormulario();
        } else {
            mostrarErroresValidacion(ordenadas);
        }
    }


}

