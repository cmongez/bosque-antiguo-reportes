package cl.bosqueantiguo.reportes.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos mínimos de un usuario para generar reportes")
public class UsuarioDTO {
    @Schema(description = "ID único del usuario", example = "10")
    private Long id;

    @Schema(description = "Rol asociado al usuario")
    private RolDTO rol;
}