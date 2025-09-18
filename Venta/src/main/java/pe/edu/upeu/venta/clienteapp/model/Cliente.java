package pe.edu.upeu.venta.clienteapp.model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Cliente {
    private final LongProperty id = new SimpleLongProperty(this, "id", 0L);
    private final StringProperty nombre = new SimpleStringProperty(this, "nombre", "");
    private final StringProperty email = new SimpleStringProperty(this, "email", "");
    private final StringProperty telefono = new SimpleStringProperty(this, "telefono", "");
    private final ObjectProperty<LocalDate> fechaRegistro = new SimpleObjectProperty<>(this, "fechaRegistro", LocalDate.now());

    public Cliente() {}

    public Cliente(long id, String nombre, String email, String telefono, LocalDate fechaRegistro) {
        setId(id);
        setNombre(nombre);
        setEmail(email);
        setTelefono(telefono);
        setFechaRegistro(fechaRegistro);
    }

    public long getId() { return id.get(); }
    public void setId(long value) { id.set(value); }
    public LongProperty idProperty() { return id; }

    public String getNombre() { return nombre.get(); }
    public void setNombre(String value) { nombre.set(value); }
    public StringProperty nombreProperty() { return nombre; }

    public String getEmail() { return email.get(); }
    public void setEmail(String value) { email.set(value); }
    public StringProperty emailProperty() { return email; }

    public String getTelefono() { return telefono.get(); }
    public void setTelefono(String value) { telefono.set(value); }
    public StringProperty telefonoProperty() { return telefono; }

    public LocalDate getFechaRegistro() { return fechaRegistro.get(); }
    public void setFechaRegistro(LocalDate value) { fechaRegistro.set(value); }
    public ObjectProperty<LocalDate> fechaRegistroProperty() { return fechaRegistro; }

    @Override
    public String toString() {
        return getNombre();
    }

}
