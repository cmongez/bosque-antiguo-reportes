package cl.bosqueantiguo.reportes.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaSummaryDTO {
    private Double totalVentas;
    private Double totalMes;
    private Long cantidadVentas;
    private Double promedioVenta;
    private LocalDateTime fechaConsulta;
}
