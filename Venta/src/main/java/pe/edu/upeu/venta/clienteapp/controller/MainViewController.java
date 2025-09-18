package pe.edu.upeu.venta.clienteapp.controller;
import pe.edu.upeu.venta.clienteapp.model.Cliente;
import pe.edu.upeu.venta.clienteapp.repository.ClienteRepository;
import pe.edu.upeu.venta.clienteapp.service.ClienteService;
import javafx.beans.binding.Bindings;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.function.Predicate;

public class MainViewController {

    @FXML private TableView<Cliente> clienteTable;
    @FXML private TableColumn<Cliente, Number> idColumn;
    @FXML private TableColumn<Cliente, String> nombreColumn;
    @FXML private TableColumn<Cliente, String> emailColumn;
    @FXML private TableColumn<Cliente, String> telefonoColumn;
    @FXML private TableColumn<Cliente, String> fechaColumn;
    @FXML private TextField searchField;

    private final ClienteService service = new ClienteService(new ClienteRepository());
    private FilteredList<Cliente> filtered;

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(c -> c.getValue().idProperty());
        nombreColumn.setCellValueFactory(c -> c.getValue().nombreProperty());
        emailColumn.setCellValueFactory(c -> c.getValue().emailProperty());
        telefonoColumn.setCellValueFactory(c -> c.getValue().telefonoProperty());
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("es", "ES"));
        fechaColumn.setCellValueFactory(c -> Bindings.createStringBinding(
                () -> c.getValue().getFechaRegistro() == null ? "" :
                        c.getValue().getFechaRegistro().format(fmt),
                c.getValue().fechaRegistroProperty()
        ));

        filtered = new FilteredList<>(service.getClientes(), c -> true);
        SortedList<Cliente> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(clienteTable.comparatorProperty());
        clienteTable.setItems(sorted);

        searchField.textProperty().addListener(
                (obs, old, val) -> filtered.setPredicate(buildPredicate(val))
        );

        clienteTable.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                onEdit();
            }
        });
    }

    private Predicate<Cliente> buildPredicate(String text) {
        if (text == null || text.isBlank()) return c -> true;
        String q = text.toLowerCase(Locale.ROOT);
        return c -> (c.getNombre() != null && c.getNombre().toLowerCase(Locale.ROOT).contains(q)) ||
                (c.getEmail()  != null && c.getEmail().toLowerCase(Locale.ROOT).contains(q)) ||
                (c.getTelefono()!= null && c.getTelefono().toLowerCase(Locale.ROOT).contains(q));
    }

    @FXML
    private void onNew() {
        Cliente nuevo = new Cliente();
        if (openForm(nuevo)) {
            service.add(nuevo);
        }
    }

    @FXML
    private void onEdit() {
        Cliente seleccionado = clienteTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;
        Cliente editable = new Cliente(seleccionado.getId(), seleccionado.getNombre(),
                seleccionado.getEmail(), seleccionado.getTelefono(),
                seleccionado.getFechaRegistro());
        if (openForm(editable)) {
            seleccionado.setNombre(editable.getNombre());
            seleccionado.setEmail(editable.getEmail());
            seleccionado.setTelefono(editable.getTelefono());
            seleccionado.setFechaRegistro(editable.getFechaRegistro());
            service.update(seleccionado);
            clienteTable.refresh();
        }
    }

    @FXML
    private void onDelete() {
        Cliente seleccionado = clienteTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;
        service.delete(seleccionado);
    }

    private boolean openForm(Cliente cliente) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/pe/edu/upeu/venta/clienteapp/view/ClienteFormDialog.fxml")
            );
            Scene scene = new Scene(loader.load());
            Stage dialog = new Stage();
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(clienteTable.getScene().getWindow());
            dialog.setTitle("Cliente");
            dialog.setScene(scene);

            ClienteFormController controller = loader.getController();
            controller.setStage(dialog);
            controller.setCliente(cliente);

            dialog.showAndWait();
            return controller.isSaved();
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}