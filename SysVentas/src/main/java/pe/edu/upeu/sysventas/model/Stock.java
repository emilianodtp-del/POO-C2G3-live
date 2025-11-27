package pe.edu.upeu.sysventas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "upeu_stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long idProducto; //01

    @NotNull(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 160, message = "El nombre debe tener entre 2 y 160 caracteres")
    @Column(name = "nombre", nullable = false, length = 160)
    private String nombre;   //02

    @PositiveOrZero(message = "La cantidad debe ser positiva o cero")
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad; //03

    @NotNull(message = "Categoria no puede estar vacío")
    @ManyToOne
    @JoinColumn(name = "id_categoria", referencedColumnName =
            "id_categoria",
            nullable = false, foreignKey = @ForeignKey(name =
            "FK_CATEGORIA_PRODUCTO") )
    private Categoria categoria;
    @NotNull(message = "Marca no puede estar vacío")
    @ManyToOne
    @JoinColumn(name = "id_marca", referencedColumnName = "id_marca",
            nullable = false, foreignKey = @ForeignKey(name =
            "FK_MARCA_PRODUCTO"))
    private Marca marca;

}