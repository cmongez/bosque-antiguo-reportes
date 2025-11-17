package cl.bosqueantiguo.reportes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cl.bosqueantiguo.reportes.model.ReporteClientesRegistrados;

public interface ReporteClientesRegistradosRepository extends JpaRepository<ReporteClientesRegistrados, Long> {
    //JPQL
    //Es un lenguaje de consultas orientado a objetos, diseñado para JPA (Java Persistence API).
    //Usa nombres de entidades y atributos de tus clases Java, NO nombres de tablas ni columnas reales.
    // Consulta JPQL: busca reportes con más de X clientes
    @Query("SELECT r FROM ReporteClientesRegistrados r WHERE r.totalClientes > :minClientes")
    List<ReporteClientesRegistrados> findReportesConMasDe(@Param("minClientes") int minClientes);

    // Consulta SQL nativa: últimos N reportes por fecha
    //Es SQL puro, como lo escribiría en el gestor de base de datos directamente.
    //Usa nombres de tablas y columnas de la base de datos, NO los de las clases Java.
    @Query(value = "SELECT * FROM reporte_usuarios_registrados ORDER BY fecha_generacion DESC LIMIT :n", nativeQuery = true)
    List<ReporteClientesRegistrados> findUltimosNReportes(@Param("n") int n);

}