package pe.edu.upeu.sysventas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.sysventas.enums.TipoDocumento;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "upeu_cliente")
public class Cliente {

    @Id
    @Size(min = 2, max = 50, message = "DNI/RUC no debe de estar vacio")
    @Column(name = "dniruc", nullable = false, length = 12)
    private String dniruc;

    @Size(min = 2, max = 50, message = "El nombre no debe de estar vacio")
    @Column(name = "nombres", nullable = false, length = 160)
    private String nombres;

    @Size(min = 2, max = 50, message = "Reporte Legal no debe de estar vacio")
    @Column(name = "rep_legal", nullable = false,length = 160)
    private String repLegal;

    @NotNull(message = "Tipo Documento no puede estar vacío")
    @Column(name = "tipo_documento", nullable = false, length = 12)
    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;
}