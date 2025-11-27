package pe.edu.upeu.sysventas.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.upeu.sysventas.components.ColumnInfo;
import pe.edu.upeu.sysventas.components.TableViewHelper;
import pe.edu.upeu.sysventas.enums.TipoDocumento;
import pe.edu.upeu.sysventas.model.Cliente;
import pe.edu.upeu.sysventas.service.IClienteService;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ClienteController {

    private final IClienteService clienteService;

    @FXML private TextField txtDniRuc;
    @FXML private TextField txtNombres;
    @FXML private TextField txtRepLegal;
    @FXML private ComboBox<TipoDocumento> cbxTipoDocumento;
    @FXML private TableView<Cliente> tableView;
    @FXML private TextField txtFiltroDato;
    @FXML private Label lbnMsg;

    private final TableViewHelper<Cliente> tableHelper = new TableViewHelper<>();
    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();

    private String dniEditar = null;

    @FXML
    public void initialize() {
        cargarComboTipoDocumento();
        cargarTabla();
        listarClientes();

        if (txtFiltroDato != null) {
            txtFiltroDato.textProperty().addListener((obs, oldVal, newVal) -> filtrarClientes(newVal));
        }
    }

    // ============================
    //       FILTRO LISTA
    // ============================
    private void filtrarClientes(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            tableView.setItems(listaClientes);
            return;
        }

        String lower = filtro.toLowerCase();

        List<Cliente> filtrados = listaClientes.stream()
                .filter(c ->
                        c.getDniruc().toLowerCase().contains(lower) ||
                                c.getNombres().toLowerCase().contains(lower) ||
                                c.getRepLegal().toLowerCase().contains(lower)
                )
                .collect(Collectors.toList());

        tableView.setItems(FXCollections.observableArrayList(filtrados));
    }

    // ============================
    //        CARGA COMBO
    // ============================
    private void cargarComboTipoDocumento() {
        cbxTipoDocumento.setItems(FXCollections.observableArrayList(TipoDocumento.values()));
    }

    // ============================
    //         TABLA
    // ============================
    private void cargarTabla() {
        LinkedHashMap<String, ColumnInfo> columnas = new LinkedHashMap<>();
        columnas.put("DNI/RUC", new ColumnInfo("dniruc", 120.0));
        columnas.put("Nombres", new ColumnInfo("nombres", 200.0));
        columnas.put("Rep. Legal", new ColumnInfo("repLegal", 200.0));
        columnas.put("Tipo Doc.", new ColumnInfo("tipoDocumento", 120.0));

        tableHelper.addColumnsInOrderWithSize(
                tableView,
                columnas,
                this::cargarDataEnFormulario,
                this::eliminarCliente
        );
    }

    private void listarClientes() {
        listaClientes.setAll(clienteService.findAll());
        tableView.setItems(listaClientes);
    }


    @FXML
    public void clearForm() {
        limpiarError();

        txtDniRuc.clear();
        txtNombres.clear();
        txtRepLegal.clear();
        cbxTipoDocumento.getSelectionModel().clearSelection();

        lbnMsg.setText("Formulario limpiado correctamente");
    }

    public void limpiarError() {
        // Lista de los controles reales del ClienteController
        List<Control> controles = List.of(
                txtDniRuc,
                txtNombres,
                txtRepLegal,
                cbxTipoDocumento
        );

        // Quita la clase de error
        controles.forEach(c -> {
            if (c != null) {
                c.getStyleClass().remove("text-field-error");
            }
        });
    }


    // ============================
    //     PASAR DATOS AL FORM
    // ============================
    private void cargarDataEnFormulario(Cliente c) {
        dniEditar = c.getDniruc();

        txtDniRuc.setText(c.getDniruc());
        txtNombres.setText(c.getNombres());
        txtRepLegal.setText(c.getRepLegal());
        cbxTipoDocumento.setValue(c.getTipoDocumento());

        limpiarErrores();
    }

    // ============================
    //      ELIMINAR CLIENTE
    // ============================
    private void eliminarCliente(Cliente c) {
        clienteService.deleteById(c.getDniruc());
        listarClientes();

        lbnMsg.setText("Cliente eliminado correctamente");
        lbnMsg.setStyle("-fx-text-fill: green; -fx-font-size: 14px;");
    }

    // ============================
    //     VALIDACIÓN ESTILO USUARIO
    // ============================
    @FXML
    public void validarFormulario() {
        limpiarErrores();

        // Capturar datos
        String dni = txtDniRuc.getText().trim();
        String nombres = txtNombres.getText().trim();
        String rep = txtRepLegal.getText().trim();
        TipoDocumento tipo = cbxTipoDocumento.getValue();

        // Validaciones específicas con mensajes
        if (dni.isEmpty()) {
            marcarError(txtDniRuc, "El DNI/RUC no puede estar vacío");
            return;
        }
        if (nombres.isEmpty()) {
            marcarError(txtNombres, "Los nombres no pueden estar vacíos");
            return;
        }
        if (rep.isEmpty()) {
            marcarError(txtRepLegal, "El representante legal no puede estar vacío");
            return;
        }
        if (tipo == null) {
            marcarError(cbxTipoDocumento, "Debe seleccionar un tipo de documento");
            return;
        }

        // Crear objeto Cliente
        Cliente cli = Cliente.builder()
                .dniruc(dni)
                .nombres(nombres)
                .repLegal(rep)
                .tipoDocumento(tipo)
                .build();

        // Registro o actualización
        if (dniEditar != null) {
            clienteService.update(dniEditar, cli);
            lbnMsg.setText("Cliente actualizado correctamente");
        } else {
            clienteService.save(cli);
            lbnMsg.setText("Cliente registrado correctamente");
        }

        lbnMsg.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
        listarClientes();
        limpiarFormulario();
    }

    // ============================
    //     UTILIDADES DE ERRORES
    // ============================
    private void marcarError(Control control, String mensaje) {
        if (!control.getStyleClass().contains("text-field-error")) {
            control.getStyleClass().add("text-field-error");

        }
        lbnMsg.setText(mensaje);
        lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");

        Platform.runLater(control::requestFocus);
    }

    private void limpiarErrores() {
        List<Control> controles = List.of(txtDniRuc, txtNombres, txtRepLegal, cbxTipoDocumento);
        controles.forEach(c -> c.getStyleClass().remove("text-field-error"));
    }

    private void limpiarFormulario() {
        txtDniRuc.clear();
        txtNombres.clear();
        txtRepLegal.clear();
        cbxTipoDocumento.getSelectionModel().clearSelection();
        dniEditar = null;
    }
}
