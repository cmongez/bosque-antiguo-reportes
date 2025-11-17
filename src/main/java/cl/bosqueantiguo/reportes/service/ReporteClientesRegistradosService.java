package cl.bosqueantiguo.reportes.service;

// Importa clases necesarias para fechas y listas
import java.time.LocalDateTime;
import java.util.List;

// Importa componentes de Spring para inyección de dependencias y manejo HTTP
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// Importa tus propios DTOs, modelos y repositorios
import cl.bosqueantiguo.reportes.DTO.UsuarioDTO;
import cl.bosqueantiguo.reportes.DTO.ProductoDTO;
import cl.bosqueantiguo.reportes.model.ReporteClientesRegistrados;
import cl.bosqueantiguo.reportes.repository.ReporteClientesRegistradosRepository;

// Marca la clase como un componente de servicio de Spring
@Service
public class ReporteClientesRegistradosService {
    @Autowired
    private ReporteClientesRegistradosRepository reporteRepo;
    @Autowired// Inyección de RestTemplate para hacer peticiones HTTP a otros microservicios
    private RestTemplate restTemplate;
    public ReporteClientesRegistrados generarReporteClientesRegistrados() {

        String usuariosServiceUrl = "http://localhost:8080/usuarios";// URL del microservicio de usuarios 
        // Realiza una petición GET para obtener la lista de usuarios como una lista de UsuarioDTO
        ResponseEntity<List<UsuarioDTO>> response = restTemplate.exchange(
                usuariosServiceUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UsuarioDTO>>() {});

        List<UsuarioDTO> usuarios = response.getBody(); // Extrae la lista de usuarios de la respuesta
        // Cuenta cuántos usuarios tienen el rol "CLIENTE"
        int totalClientes = (int) usuarios.stream()
                .filter(u -> u.getRol() != null && "CLIENTE".equalsIgnoreCase(u.getRol().getNombre()))
                .count();

        ReporteClientesRegistrados reporte = new ReporteClientesRegistrados();
        reporte.setTotalClientes(totalClientes); // Asigna la cantidad de clientes encontrados
        reporte.setFechaGeneracion(LocalDateTime.now()); // Asigna la fecha y hora actual
        return reporteRepo.save(reporte);
    }

    // Método para listar todos los reportes de clientes registrados guardados en la base de datos
    public List<ReporteClientesRegistrados> listarReportes() {
        return reporteRepo.findAll();
    }

    // JPQL: reportes con más de X clientes
    public List<ReporteClientesRegistrados> reportesConMasDe(int minClientes) {
        return reporteRepo.findReportesConMasDe(minClientes);
    }

    // SQl Nativa: últimos N reportes por fecha
    public List<ReporteClientesRegistrados> ultimosNReportes(int n) {
        return reporteRepo.findUltimosNReportes(n);
    }

    // Nuevo método: obtener productos desde el microservicio productos
    public List<ProductoDTO> obtenerProductos() {
        String productosServiceUrl = "http://localhost:8082/api/productos";
        ResponseEntity<List<ProductoDTO>> response = restTemplate.exchange(
                productosServiceUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProductoDTO>>() {});
        return response.getBody();
    }
}