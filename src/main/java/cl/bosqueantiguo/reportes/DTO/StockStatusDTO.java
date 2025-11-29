package cl.bosqueantiguo.reportes.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockStatusDTO {
    private Long productoId;
    private String nombreProducto;
    private Integer stockActual;
    private Integer stockCritico;
    private String estado; // "CRITICO", "AGOTADO", "NORMAL"
}
