package cl.bosqueantiguo.reportes.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopProductoDTO {
    private Long productoId;
    private String nombreProducto;
    private Integer cantidadVendida;
    private Double totalVentas;
}