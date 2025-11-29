package cl.bosqueantiguo.reportes.service;

import cl.bosqueantiguo.reportes.DTO.*;
import cl.bosqueantiguo.reportes.client.ProductosClient;
import cl.bosqueantiguo.reportes.client.VentasClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportesService {
    
    private final VentasClient ventasClient;
    private final ProductosClient productosClient;
    
    public ReportesService(VentasClient ventasClient, ProductosClient productosClient) {
        this.ventasClient = ventasClient;
        this.productosClient = productosClient;
    }
    
    public VentaSummaryDTO getSalesSummary() {
        List<VentasClient.VentaDTO> ventas = ventasClient.getAllVentas();
        
        if (ventas == null || ventas.isEmpty()) {
            return new VentaSummaryDTO(0.0, 0.0, 0L, 0.0, LocalDateTime.now());
        }
        
        Double totalVentas = ventas.stream()
            .mapToDouble(v -> v.total)
            .sum();
        
        // Total del mes actual
        LocalDateTime ahora = LocalDateTime.now();
        Double totalMes = ventas.stream()
            .filter(v -> v.fecha.getYear() == ahora.getYear() && 
                        v.fecha.getMonth() == ahora.getMonth())
            .mapToDouble(v -> v.total)
            .sum();
        
        Long cantidadVentas = (long) ventas.size();
        Double promedioVenta = cantidadVentas > 0 ? totalVentas / cantidadVentas : 0.0;
        
        return new VentaSummaryDTO(totalVentas, totalMes, cantidadVentas, promedioVenta, LocalDateTime.now());
    }
    
    public List<TopProductoDTO> getTopProducts() {
        List<VentasClient.VentaDTO> ventas = ventasClient.getAllVentas();
        List<ProductoDTO> productos = productosClient.getAllProductos();
        
        // Crear mapa de productos para lookup rápido
        Map<Long, String> productosMap = productos.stream()
            .collect(Collectors.toMap(ProductoDTO::getId, ProductoDTO::getNombre));
        
        // Agrupar por producto y sumar cantidades/totales
        Map<Long, TopProductoDTO> productosVendidos = new HashMap<>();
        
        for (VentasClient.VentaDTO venta : ventas) {
            for (VentasClient.VentaDTO.DetalleVentaDTO detalle : venta.detalles) {
                productosVendidos.merge(detalle.productoId, 
                    new TopProductoDTO(
                        detalle.productoId,
                        productosMap.getOrDefault(detalle.productoId, "Producto Desconocido"),
                        detalle.cantidad,
                        detalle.subtotal
                    ),
                    (existing, nuevo) -> new TopProductoDTO(
                        existing.getProductoId(),
                        existing.getNombreProducto(),
                        existing.getCantidadVendida() + nuevo.getCantidadVendida(),
                        existing.getTotalVentas() + nuevo.getTotalVentas()
                    )
                );
            }
        }
        
        return productosVendidos.values().stream()
            .sorted((a, b) -> Integer.compare(b.getCantidadVendida(), a.getCantidadVendida()))
            .limit(5)
            .collect(Collectors.toList());
    }
    
    public List<VentasPorCategoriaDTO> getSalesByCategory() {
        List<VentasClient.VentaDTO> ventas = ventasClient.getAllVentas();
        List<ProductoDTO> productos = productosClient.getAllProductos();
        
        // Crear mapa producto -> categoria
        Map<Long, String> productoCategoriaMap = productos.stream()
            .collect(Collectors.toMap(
                ProductoDTO::getId, 
                p -> p.getCategoria() != null ? p.getCategoria().getNombre() : "Sin Categoría"
            ));
        
        Map<String, VentasPorCategoriaDTO> ventasPorCategoria = new HashMap<>();
        
        for (VentasClient.VentaDTO venta : ventas) {
            for (VentasClient.VentaDTO.DetalleVentaDTO detalle : venta.detalles) {
                String categoria = productoCategoriaMap.getOrDefault(detalle.productoId, "Sin Categoría");
                
                ventasPorCategoria.merge(categoria,
                    new VentasPorCategoriaDTO(categoria, 1, detalle.subtotal),
                    (existing, nuevo) -> new VentasPorCategoriaDTO(
                        existing.getCategoria(),
                        existing.getCantidadVentas() + 1,
                        existing.getTotalVentas() + nuevo.getTotalVentas()
                    )
                );
            }
        }
        
        return ventasPorCategoria.values().stream()
            .sorted((a, b) -> Double.compare(b.getTotalVentas(), a.getTotalVentas()))
            .collect(Collectors.toList());
    }
    
    public List<TopClienteDTO> getTopClients() {
        List<VentasClient.VentaDTO> ventas = ventasClient.getAllVentas();
        
        Map<Long, TopClienteDTO> clientesMap = new HashMap<>();
        
        for (VentasClient.VentaDTO venta : ventas) {
            clientesMap.merge(venta.usuarioId,
                new TopClienteDTO(venta.usuarioId, "Usuario " + venta.usuarioId, 1, venta.total),
                (existing, nuevo) -> new TopClienteDTO(
                    existing.getUsuarioId(),
                    existing.getNombreCliente(),
                    existing.getCantidadCompras() + 1,
                    existing.getTotalGastado() + nuevo.getTotalGastado()
                )
            );
        }
        
        return clientesMap.values().stream()
            .sorted((a, b) -> Double.compare(b.getTotalGastado(), a.getTotalGastado()))
            .limit(5)
            .collect(Collectors.toList());
    }
    
    public List<StockStatusDTO> getStockStatus() {
        return productosClient.getStockStatus();
    }
}
