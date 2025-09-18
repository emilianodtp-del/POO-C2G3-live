package pe.edu.upeu.venta.clienteapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pe.edu.upeu.venta.clienteapp.model.Cliente;

import java.time.LocalDate;
import java.util.regex.Pattern;
import java.awt.*;

public class ClienteFormController {

    @FXML private TextField nombreField;
    @FXML private TextField emailField;
    @FXML private TextField telefonoField;
    @FXML private DatePicker fechaPicker;

    private Stage stage;
    private Cliente cliente;
    private boolean saved = false;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        nombreField.setText(cliente.getNombre());
        emailField.setText(cliente.getEmail());
        telefonoField.setText(cliente.getTelefono());
        fechaPicker.setValue(cliente.getFechaRegistro() != null ? cliente.getFechaRegistro() : LocalDate.now());
    }

    public boolean isSaved() {
        return saved;
    }

    @FXML
    private void onCancel() {
        saved = false;
        if (stage != null) stage.close();
    }

    @FXML
    private void onSave() {
        if (!validate()) return;
        cliente.setNombre(nombreField.getText().trim());
        cliente.setEmail(emailField.getText().trim());
        cliente.setTelefono(telefonoField.getText().trim());
        cliente.setFechaRegistro(fechaPicker.getValue());
        saved = true;
        if (stage != null) stage.close();
    }

    private boolean validate() {
        String nombre = nombreField.getText() == null ? "" : nombreField.getText().trim();
        String email = emailField.getText() == null ? "" : emailField.getText().trim();
        if (nombre.isBlank()) {
            nombreField.requestFocus();
            return false;
        }
        if (!Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$").matcher(email).matches()) {
            emailField.requestFocus();
            return false;
        }
        return true;
    }
}
