package pe.edu.upeu.sysventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.sysventas.model.Stock;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    // Autocompletado por nombre (consulta nativa)
    @Query(value = "SELECT s.* FROM upeu_stock s WHERE s.nombre LIKE :filter", nativeQuery = true)
    List<Stock> listAutoCompletStock(@Param("filter") String filter);

    // Autocompletado usando JPQL
    @Query("SELECT s FROM Stock s WHERE s.nombre LIKE :filter")
    List<Stock> listAutoCompletStockJ(@Param("filter") String filter);

    // Filtrar por marca
    @Query(value = "SELECT s.* FROM upeu_stock s WHERE s.id_marca = :filter", nativeQuery = true)
    List<Stock> listStockMarca(@Param("filter") Integer filter);

    @Query("SELECT s FROM Stock s WHERE s.marca.idMarca = :filter")
    List<Stock> listStockMarcaJ(@Param("filter") Integer filter);

    // Filtrar por categoría
    @Query(value = "SELECT s.* FROM upeu_stock s WHERE s.id_categoria = :filter", nativeQuery = true)
    List<Stock> listStockCategoria(@Param("filter") Integer filter);

    @Query("SELECT s FROM Stock s WHERE s.categoria.idCategoria = :filter")
    List<Stock> listStockCategoriaJ(@Param("filter") Integer filter);
}
