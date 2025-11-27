package cl.bosqueantiguo.reportes.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopClienteDTO {
    private Long usuarioId;
    private String nombreCliente;
    private Integer cantidadCompras;
    private Double totalGastado;
}