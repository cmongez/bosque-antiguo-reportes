package cl.bosqueantiguo.reportes.service;

import cl.bosqueantiguo.reportes.DTO.ProductoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Servicio dedicado a la lógica de negocio
 * relacionada con la obtención de reportes de Productos
 * (Consumiendo el microservicio de Productos).
 */
@Service
public class ReporteProductosService {

    // 1. Inyectamos el RestTemplate
    @Autowired
    private RestTemplate restTemplate;

    // 2. Define la URL del microservicio de Productos
    //    ¡¡CAMBIA ESTA URL por la correcta!!
    private final String PRODUCTOS_API_URL = "http://localhost:8081/api/productos"; // Ajusta el puerto si es necesario


    /**
     * Llama al microservicio de Productos para obtener una lista de todos los productos.
     */
    public List<ProductoDTO> obtenerProductos() {
        
        try {
            ResponseEntity<List<ProductoDTO>> response = restTemplate.exchange(
                PRODUCTOS_API_URL,
                HttpMethod.GET,
                null, // No hay body en una petición GET
                new ParameterizedTypeReference<List<ProductoDTO>>() {}
            );
            
            return response.getBody();

        } catch (Exception e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
            return List.of(); // Devuelve una lista vacía en caso de error
        }
    }
}