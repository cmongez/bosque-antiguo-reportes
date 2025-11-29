package cl.bosqueantiguo.reportes.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentasPorCategoriaDTO {
    private String categoria;
    private Integer cantidadVentas;
    private Double totalVentas;
}
