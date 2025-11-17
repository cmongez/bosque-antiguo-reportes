package cl.bosqueantiguo.reportes.DTO;

import lombok.Data;

@Data
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private CategoriaDTO categoria;
    private boolean disponible;
    private Double precio;
    private Integer stock;
    private String rutaImagen;
}
