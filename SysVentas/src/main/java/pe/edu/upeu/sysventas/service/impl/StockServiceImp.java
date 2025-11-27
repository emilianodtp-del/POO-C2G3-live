package pe.edu.upeu.sysventas.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.sysventas.dto.ModeloDataAutocomplet;
import pe.edu.upeu.sysventas.model.Stock;
import pe.edu.upeu.sysventas.repository.StockRepository;
import pe.edu.upeu.sysventas.service.StockIService;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockServiceImp implements StockIService {

    private static final Logger logger = LoggerFactory.getLogger(StockServiceImp.class);

    @Autowired
    StockRepository stockRepo;

    @Override
    public Stock save(Stock stock) {
        return stockRepo.save(stock);
    }

    @Override
    public List<Stock> findAll() {
        return stockRepo.findAll();
    }

    @Override
    public Stock update(Stock stock) {
        return stockRepo.save(stock);
    }

    @Override
    public void delete(Long id) {
        stockRepo.deleteById(id);
    }

    @Override
    public Stock findById(Long id) {
        return stockRepo.findById(id).orElse(null);
    }

    @Override
    public List<ModeloDataAutocomplet> listAutoCompletStock(String nombre) {
        List<ModeloDataAutocomplet> listaStock = new ArrayList<>();
        try {
            for (Stock stock : stockRepo.listAutoCompletStock(nombre + "%")) {
                ModeloDataAutocomplet data = new ModeloDataAutocomplet();
                data.setIdx(stock.getNombre());
                data.setNameDysplay(String.valueOf(stock.getIdProducto()));
                data.setOtherData(
                        stock.getCantidad() + ":" +
                                stock.getCategoria().getNombre() + ":" +
                                stock.getMarca().getNombre()
                );
                listaStock.add(data);
            }
        } catch (Exception e) {
            logger.error("Error al realizar la búsqueda de stock", e);
        }
        return listaStock;
    }

    @Override
    public List<ModeloDataAutocomplet> listAutoCompletStock() {
        List<ModeloDataAutocomplet> listaStock = new ArrayList<>();
        try {
            for (Stock stock : stockRepo.findAll()) {
                ModeloDataAutocomplet data = new ModeloDataAutocomplet();
                data.setIdx(String.valueOf(stock.getIdProducto()));
                data.setNameDysplay(stock.getNombre());
                data.setOtherData(
                        stock.getCantidad() + ":" +
                                stock.getCategoria().getNombre() + ":" +
                                stock.getMarca().getNombre()
                );
                listaStock.add(data);
            }
        } catch (Exception e) {
            logger.error("Error al realizar la búsqueda de stock", e);
        }
        return listaStock;
    }
}
