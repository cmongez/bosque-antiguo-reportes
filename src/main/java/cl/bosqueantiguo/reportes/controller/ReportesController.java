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
@RequestMapping("/reportes")
@CrossOrigin(origins = "*")
public class ReportesController {
    
    // Inyectamos el servicio de Clientes
    @Autowired
    private ReporteClientesRegistradosService reporteClientesRegistradosService;

    // Inyectamos el NUEVO servicio de Productos
    @Autowired
    private ReporteProductosService reporteProductosService;


    // --- Endpoints de Reporte de PRODUCTOS ---
    
    // Moví el endpoint de productos a su propia ruta
    @Operation(summary = "Obtiene la lista de productos desde el microservicio de Productos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos obtenida",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ProductoDTO.class)))
    })
    @GetMapping("/productos")
    public List<ProductoDTO> obtenerProductos() {
        // Llama al servicio correcto
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