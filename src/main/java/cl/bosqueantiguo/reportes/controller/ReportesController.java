package cl.bosqueantiguo.reportes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.bosqueantiguo.reportes.model.ReporteClientesRegistrados;
import cl.bosqueantiguo.reportes.service.ReporteClientesRegistradosService;
import cl.bosqueantiguo.reportes.DTO.ProductoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/reportes/clientes-registrados")
public class ReportesController {
    @Autowired
    private ReporteClientesRegistradosService reporteClientesRegistradosService;

    @GetMapping("/productos")
    public List<ProductoDTO> obtenerProductos() {
        return reporteClientesRegistradosService.obtenerProductos();
    }


    @Operation(summary = "Genera un nuevo reporte de clientes registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ReporteClientesRegistrados.class)))
    })
    @PostMapping
    public ReporteClientesRegistrados generarReporte() {
        return reporteClientesRegistradosService.generarReporteClientesRegistrados();
    }

    @Operation(summary = "Lista todos los reportes de clientes registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ReporteClientesRegistrados.class)))
    })
    @GetMapping
    public List<ReporteClientesRegistrados> listarReportes() {
        return reporteClientesRegistradosService.listarReportes();
    }

    @Operation(summary = "Obtiene reportes con más de una cantidad mínima de clientes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reportes encontrados",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ReporteClientesRegistrados.class)))
    })
    @GetMapping("/mayor-a/{minClientes}")
    public List<ReporteClientesRegistrados> getReportesConMasDe(@PathVariable int minClientes) {
        return reporteClientesRegistradosService.reportesConMasDe(minClientes);
    }


    @Operation(summary = "Obtiene los últimos N reportes ordenados por fecha")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reportes encontrados",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ReporteClientesRegistrados.class)))
    })
    @GetMapping("/ultimos/{n}")
    public List<ReporteClientesRegistrados> getUltimosN(@PathVariable int n) {
        return reporteClientesRegistradosService.ultimosNReportes(n);
    }
}