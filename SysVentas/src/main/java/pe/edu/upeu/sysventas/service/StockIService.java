package pe.edu.upeu.sysventas.service;

import pe.edu.upeu.sysventas.dto.ModeloDataAutocomplet;
import pe.edu.upeu.sysventas.model.Stock;

import java.util.List;

public interface StockIService {

    Stock save(Stock stock);
    List<Stock> findAll();
    Stock update(Stock stock);
    void delete(Long id);
    Stock findById(Long id);

    List<ModeloDataAutocomplet> listAutoCompletStock(String nombre);
    List<ModeloDataAutocomplet> listAutoCompletStock();
}
