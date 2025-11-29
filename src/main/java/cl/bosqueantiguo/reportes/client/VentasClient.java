package cl.bosqueantiguo.reportes.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VentasClient {
    
    private final RestTemplate restTemplate;
    
    @Value("${microservicio.ventas.url:http://localhost:8081}")
    private String ventasUrl;
    
    public VentasClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public List<VentaDTO> getAllVentas() {
        String url = ventasUrl + "/api/v1/sales";
        return restTemplate.exchange(url, HttpMethod.GET, null, 
            new ParameterizedTypeReference<List<VentaDTO>>() {}).getBody();
    }
    
    // DTO para mapear ventas del microservicio
    public static class VentaDTO {
        public Long id;
        public LocalDateTime fecha;
        public Double total;
        public Long usuarioId;
        public List<DetalleVentaDTO> detalles;
        
        public static class DetalleVentaDTO {
            public Long productoId;
            public Integer cantidad;
            public Double subtotal;
        }
    }
}
