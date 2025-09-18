package pe.edu.upeu.venta.clienteapp.service;

import javafx.collections.ObservableList;
import pe.edu.upeu.venta.clienteapp.model.Cliente;
import pe.edu.upeu.venta.clienteapp.repository.ClienteRepository;

import java.time.LocalDate;

public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
        seed();
    }

    public ObservableList<Cliente> getClientes() {
        return repository.findAll();
    }

    public void add(Cliente cliente) {
        repository.add(cliente);
    }

    public void update(Cliente cliente) {
        repository.update(cliente);
    }

    public void delete(Cliente cliente) {
        repository.delete(cliente);
    }

    private void seed() {
        if (!repository.findAll().isEmpty()) return;
        repository.add(new Cliente(0, "Ana Pérez", "ana@example.com", "555-123", LocalDate.now().minusDays(10)));
        repository.add(new Cliente(0, "Luis Gómez", "luis@example.com", "555-456", LocalDate.now().minusDays(20)));
        repository.add(new Cliente(0, "María López", "maria@example.com", "555-789", LocalDate.now().minusDays(30)));
    }
}
