package pe.edu.upeu.sysventas.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.sysventas.components.ColumnInfo;
import pe.edu.upeu.sysventas.components.ComboBoxAutoComplete;
import pe.edu.upeu.sysventas.components.TableViewHelper;
import pe.edu.upeu.sysventas.components.Toast;
import pe.edu.upeu.sysventas.dto.ComboBoxOption;
import pe.edu.upeu.sysventas.model.Usuario;
import pe.edu.upeu.sysventas.service.IPerfilService;
import pe.edu.upeu.sysventas.service.IUsuarioService;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Controller
public class UsuarioController {

    @FXML
    private TextField txtUser;
    @FXML
    private PasswordField txtClave;
    @FXML
    private TextField txtFiltroDato;
    @FXML
    private ComboBox<ComboBoxOption> cbxPerfil;
    @FXML
    private TableView<Usuario> tableView;
    @FXML
    private Label lbnMsg;
    @FXML
    private AnchorPane miContenedor;

    private Stage stage;

    @Autowired
    private IPerfilService perfilService;

    @Autowired
    private IUsuarioService usuarioService;

    private Validator validator;
    private ObservableList<Usuario> listarUsuario;
    private Usuario formulario;
    private Long idUsuarioCE = 0L;

    private void filtrarUsuarios(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            tableView.getItems().clear();
            tableView.getItems().addAll(listarUsuario);
            return;
        }
        String lowerCaseFilter = filtro.toLowerCase();
        List<Usuario> usuariosFiltrados = listarUsuario.stream()
                .filter(u -> {
                    try {
                        if (u.getUser() != null && u.getUser().toLowerCase().contains(lowerCaseFilter)) return true;
                        if (u.getEstado() != null && u.getEstado().toLowerCase().contains(lowerCaseFilter)) return true;
                        if (u.getIdPerfil() != null && u.getIdPerfil().getNombre() != null &&
                                u.getIdPerfil().getNombre().toLowerCase().contains(lowerCaseFilter)) return true;
                    } catch (Exception ignored) { }
                    return false;
                })
                .collect(Collectors.toList());
        tableView.getItems().clear();
        tableView.getItems().addAll(usuariosFiltrados);
    }

    public void listar() {
        try {
            tableView.getItems().clear();
            listarUsuario = FXCollections.observableArrayList(usuarioService.findAll());
            tableView.getItems().addAll(listarUsuario);
            if (txtFiltroDato != null) {
                txtFiltroDato.textProperty().addListener((observable, oldValue, newValue) -> filtrarUsuarios(newValue));
            }
        } catch (Exception e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        // Obtener stage después de que se cargue la ventana
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000),
                event -> {
                    try {
                        stage = (Stage) miContenedor.getScene().getWindow();
                    } catch (Exception ignored) { }
                }));
        timeline.setCycleCount(1);
        timeline.play();

        // Cargar perfiles en el combo
        try {
            List<ComboBoxOption> opciones = perfilService.listarCombobox();
            if (cbxPerfil != null && opciones != null) cbxPerfil.getItems().addAll(opciones);
        } catch (Exception e) {
            System.out.println("No se pudo cargar perfiles: " + e.getMessage());
        }
        if (cbxPerfil != null) new ComboBoxAutoComplete<>(cbxPerfil);

        // Validator
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        // TableView setup
        TableViewHelper<Usuario> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID", new ColumnInfo("idUsuario", 60.0));
        columns.put("Usuario", new ColumnInfo("user", 200.0));
        columns.put("Estado", new ColumnInfo("estado", 120.0));
        columns.put("Perfil", new ColumnInfo("idPerfil.nombre", 200.0));

        Consumer<Usuario> updateAction = (Usuario u) -> editForm(u);
        Consumer<Usuario> deleteAction = (Usuario u) -> {
            try {
                usuarioService.deleteByid(u.getIdUsuario());
                double w = (stage != null) ? stage.getWidth() / 1.5 : 600;
                double h = (stage != null) ? stage.getHeight() / 2 : 300;
                Toast.showToast(stage, "Se eliminó correctamente!!", 2000, w, h);
                listar();
            } catch (Exception ex) {
                System.out.println("Error al eliminar usuario: " + ex.getMessage());
                lbnMsg.setText("Error al eliminar usuario");
                lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
            }
        };

        tableViewHelper.addColumnsInOrderWithSize(tableView, columns, updateAction, deleteAction);
        tableView.setTableMenuButtonVisible(true);
        listar();
    }

    public void limpiarError() {
        List<Control> controles = List.of(txtUser, txtClave, cbxPerfil);
        controles.forEach(c -> {
            if (c != null) c.getStyleClass().remove("text-field-error");
        });
    }

    public void clearForm() {
        if (txtUser != null) txtUser.clear();
        if (txtClave != null) txtClave.clear();
        if (cbxPerfil != null) cbxPerfil.getSelectionModel().clearSelection();
        idUsuarioCE = 0L;
        limpiarError();
    }

    public void editForm(Usuario usuario) {
        if (usuario == null) return;
        if (txtUser != null) txtUser.setText(usuario.getUser() != null ? usuario.getUser() : "");
        if (txtClave != null) txtClave.clear();

        if (cbxPerfil != null && usuario.getIdPerfil() != null) {
            cbxPerfil.getSelectionModel().select(
                    cbxPerfil.getItems().stream()
                            .filter(p -> {
                                try {
                                    return Objects.equals(Long.valueOf(p.getKey()), usuario.getIdPerfil().getIdPerfil());
                                } catch (Exception e) {
                                    return false;
                                }
                            })
                            .findFirst()
                            .orElse(null)
            );
        }
        idUsuarioCE = usuario.getIdUsuario() != null ? usuario.getIdUsuario() : 0L;
        limpiarError();
    }

    private void procesarFormulario() {
        lbnMsg.setText("Formulario válido");
        lbnMsg.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
        limpiarError();
        double width = (stage != null) ? stage.getWidth() / 1.5 : 600;
        double height = (stage != null) ? stage.getHeight() / 2 : 300;

        try {
            formulario.setEstado("Activo");

            if (idUsuarioCE > 0L) {
                formulario.setIdUsuario(idUsuarioCE);
                Usuario actual = usuarioService.findById(idUsuarioCE);
                if (txtClave != null && (txtClave.getText() == null || txtClave.getText().isBlank())) {
                    formulario.setClave(actual != null ? actual.getClave() : null);
                } else {
                    formulario.setClave(formulario.getClave()); // ✅ sin encriptar
                }
                usuarioService.update(idUsuarioCE, formulario);
                Toast.showToast(stage, "Se actualizó correctamente!!", 2000, width, height);
            } else {
                formulario.setClave(formulario.getClave()); // ✅ sin encriptar
                usuarioService.save(formulario);
                Toast.showToast(stage, "Se guardó correctamente!!", 2000, width, height);
            }
        } catch (Exception e) {
            System.out.println("Error al procesar formulario: " + e.getMessage());
            lbnMsg.setText("Error al guardar usuario");
            lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        }

        clearForm();
        listar();
    }

    @FXML
    public void validarFormulario() {
        formulario = new Usuario();
        formulario.setUser(txtUser != null ? txtUser.getText().trim() : null);
        formulario.setClave(txtClave != null ? txtClave.getText().trim() : null);

        String idxP = cbxPerfil.getSelectionModel().getSelectedItem() == null ? "0"
                : cbxPerfil.getSelectionModel().getSelectedItem().getKey();
        try {
            formulario.setIdPerfil(idxP.equals("0") ? null : perfilService.findById(Long.parseLong(idxP)));
        } catch (Exception e) {
            formulario.setIdPerfil(null);
        }

        Set<ConstraintViolation<Usuario>> violaciones = validator.validate(formulario);
        List<ConstraintViolation<Usuario>> violacionesOrdenadas = violaciones.stream()
                .sorted(Comparator.comparing(v -> v.getPropertyPath().toString()))
                .collect(Collectors.toList());

        if (formulario.getUser() == null || formulario.getUser().isBlank()) {
            lbnMsg.setText("El usuario no puede estar vacío");
            lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
            if (txtUser != null && !txtUser.getStyleClass().contains("text-field-error"))
                txtUser.getStyleClass().add("text-field-error");
            Platform.runLater(() -> txtUser.requestFocus());
            return;
        }

        if (idUsuarioCE <= 0L && (formulario.getClave() == null || formulario.getClave().isBlank())) {
            lbnMsg.setText("La clave no puede estar vacía");
            lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
            if (txtClave != null && !txtClave.getStyleClass().contains("text-field-error"))
                txtClave.getStyleClass().add("text-field-error");
            Platform.runLater(() -> txtClave.requestFocus());
            return;
        }

        if (formulario.getIdPerfil() == null) {
            lbnMsg.setText("Seleccione un perfil");
            lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
            if (cbxPerfil != null && !cbxPerfil.getStyleClass().contains("text-field-error"))
                cbxPerfil.getStyleClass().add("text-field-error");
            Platform.runLater(() -> cbxPerfil.requestFocus());
            return;
        }

        if (violacionesOrdenadas.isEmpty()) {
            procesarFormulario();
        } else {
            mostrarErroresValidacion(violacionesOrdenadas);
        }
    }

    private void mostrarErroresValidacion(List<ConstraintViolation<Usuario>> violaciones) {
        limpiarError();
        Map<String, Control> campos = new LinkedHashMap<>();
        campos.put("user", txtUser);
        campos.put("clave", txtClave);
        campos.put("idPerfil", cbxPerfil);

        for (ConstraintViolation<Usuario> v : violaciones) {
            Control c = campos.get(v.getPropertyPath().toString());
            if (c != null && !c.getStyleClass().contains("text-field-error")) {
                c.getStyleClass().add("text-field-error");
            }
        }
        if (!violaciones.isEmpty()) {
            lbnMsg.setText(violaciones.iterator().next().getMessage());
            lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
        }
    }
}



