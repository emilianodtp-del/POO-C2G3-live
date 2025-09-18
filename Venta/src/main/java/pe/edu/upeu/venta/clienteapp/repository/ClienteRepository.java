package pe.edu.upeu.venta.clienteapp.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pe.edu.upeu.venta.clienteapp.model.Cliente;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class ClienteRepository {
    private final ObservableList<Cliente> store = FXCollections.observableArrayList();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public ObservableList<Cliente> findAll() {
        return store;
    }

    public Cliente add(Cliente cliente) {
        if (cliente.getId() <= 0) {
            cliente.setId(idGenerator.getAndIncrement());
        }
        store.add(cliente);
        return cliente;
    }

    public void update(Cliente cliente) {
    }

    public void delete(Cliente cliente) {
        store.remove(cliente);
    }

    public Optional<Cliente> findById(long id) {
        return store.stream().filter(c -> c.getId() == id).findFirst();
    }
}
