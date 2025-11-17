package cl.bosqueantiguo.reportes.model;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reporte_usuarios_registrados")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Reporte generado con la cantidad de usuarios con rol CLIENTE")
public class ReporteClientesRegistrados {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del reporte", example = "1")
    private Long id;

    @Schema(description = "Cantidad total de usuarios con rol CLIENTE", example = "12")
    private Integer totalClientes;

    @Schema(description = "Fecha y hora en que se generó el reporte", example = "2024-06-17T14:30:00")
    private LocalDateTime fechaGeneracion;
}
