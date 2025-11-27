package cl.bosqueantiguo.reportes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.bosqueantiguo.reportes.model.ReporteClientesRegistrados;
import cl.bosqueantiguo.reportes.service.ReporteClientesRegistradosService;
import cl.bosqueantiguo.reportes.service.ReporteProductosService; // IMPORTAR el nuevo servicio
import cl.bosqueantiguo.reportes.DTO.ProductoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/reports")
@CrossOrigin(origins = "*")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Reportes", description = "Endpoints para generar reportes y estadísticas")
public class ReportesController {
    
    // Inyectamos el servicio de Clientes
    @Autowired
    private ReporteClientesRegistradosService reporteClientesRegistradosService;

    // Inyectamos el NUEVO servicio de Productos
    @Autowired
    private ReporteProductosService reporteProductosService;
    
    // Inyectamos el nuevo servicio de reportes
    @Autowired
    private cl.bosqueantiguo.reportes.service.ReportesService reportesService;

    @Operation(summary = "Resumen general de ventas")
    @GetMapping("/sales/summary")
    public cl.bosqueantiguo.reportes.DTO.VentaSummaryDTO getSalesSummary() {
        return reportesService.getSalesSummary();
    }
    
    @Operation(summary = "Top 5 productos más vendidos")
    @GetMapping("/products/top")
    public List<cl.bosqueantiguo.reportes.DTO.TopProductoDTO> getTopProducts() {
        return reportesService.getTopProducts();
    }
    
    @Operation(summary = "Ventas agrupadas por categoría")
    @GetMapping("/sales/by-category")
    public List<cl.bosqueantiguo.reportes.DTO.VentasPorCategoriaDTO> getSalesByCategory() {
        return reportesService.getSalesByCategory();
    }
    
    @Operation(summary = "Top clientes que más han comprado")
    @GetMapping("/clients/top")
    public List<cl.bosqueantiguo.reportes.DTO.TopClienteDTO> getTopClients() {
        return reportesService.getTopClients();
    }
    
    @Operation(summary = "Estado del stock (productos críticos y agotados)")
    @GetMapping("/stock/status")
    public List<cl.bosqueantiguo.reportes.DTO.StockStatusDTO> getStockStatus() {
        return reportesService.getStockStatus();
    }

    // --- Endpoints de Reporte de PRODUCTOS (Legacy) ---
    
    @Operation(summary = "Obtiene la lista de productos desde el microservicio de Productos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos obtenida",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ProductoDTO.class)))
    })
    @GetMapping("/productos")
    public List<ProductoDTO> obtenerProductos() {
        return reporteProductosService.obtenerProductos();
    }

    
    // --- Endpoints de Reporte de CLIENTES REGISTRADOS ---

    @Operation(summary = "Genera un nuevo reporte de clientes registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ReporteClientesRegistrados.class)))
    })
    // Ruta específica para este endpoint
    @PostMapping("/clientes-registrados") 
    public ReporteClientesRegistrados generarReporte() {
        return reporteClientesRegistradosService.generarReporteClientesRegistrados();
    }

    @Operation(summary = "Lista todos los reportes de clientes registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ReporteClientesRegistrados.class)))
    })
    // Ruta específica para este endpoint
    @GetMapping("/clientes-registrados")
    public List<ReporteClientesRegistrados> listarReportes() {
        return reporteClientesRegistradosService.listarReportes();
    }

    @Operation(summary = "Obtiene reportes con más de una cantidad mínima de clientes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reportes encontrados",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ReporteClientesRegistrados.class)))
    })
    // Ruta específica para este endpoint
    @GetMapping("/clientes-registrados/mayor-a/{minClientes}")
    public List<ReporteClientesRegistrados> getReportesConMasDe(@PathVariable int minClientes) {
        return reporteClientesRegistradosService.reportesConMasDe(minClientes);
    }


    @Operation(summary = "Obtiene los últimos N reportes ordenados por fecha")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reportes encontrados",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ReporteClientesRegistrados.class)))
    })
    // Ruta específica para este endpoint
    @GetMapping("/clientes-registrados/ultimos/{n}")
    public List<ReporteClientesRegistrados> getUltimosN(@PathVariable int n) {
        return reporteClientesRegistradosService.ultimosNReportes(n);
    }
}