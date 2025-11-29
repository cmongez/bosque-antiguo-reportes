package cl.bosqueantiguo.reportes.client;

import cl.bosqueantiguo.reportes.DTO.ProductoDTO;
import cl.bosqueantiguo.reportes.DTO.StockStatusDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductosClient {
    
    private final RestTemplate restTemplate;
    
    @Value("${microservicio.productos.url:http://localhost:8080}")
    private String productosUrl;
    
    public ProductosClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public List<ProductoDTO> getAllProductos() {
        String url = productosUrl + "/api/v1/products";
        return restTemplate.exchange(url, HttpMethod.GET, null, 
            new ParameterizedTypeReference<List<ProductoDTO>>() {}).getBody();
    }
    
    public List<StockStatusDTO> getStockStatus() {
        List<ProductoDTO> productos = getAllProductos();
        
        return productos.stream()
            .map(p -> {
                String estado;
                if (p.getStock() == 0) {
                    estado = "AGOTADO";
                } else if (p.getStock() <= p.getStockCritico()) {
                    estado = "CRITICO";
                } else {
                    estado = "NORMAL";
                }
                
                return new StockStatusDTO(
                    p.getId(),
                    p.getNombre(),
                    p.getStock(),
                    p.getStockCritico(),
                    estado
                );
            })
            .filter(s -> !"NORMAL".equals(s.getEstado()))
            .collect(Collectors.toList());
    }
}
