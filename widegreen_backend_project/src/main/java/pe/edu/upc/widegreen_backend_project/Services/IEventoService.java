package pe.edu.upc.widegreen_backend_project.Services;

import pe.edu.upc.widegreen_backend_project.Entities.Evento;

import java.time.LocalDate;
import java.util.List;

public interface IEventoService {
    Evento crearEvento(Evento evento);
    List<Evento> listarEventos();
    Evento obtenerEventoPorId(Long id);
    void eliminarEvento(Long id);
    List<Evento> listarEventosActivos(); // usa estado = "Activo"
    List<Evento> listarEventosPorRangoFechas(LocalDate inicio, LocalDate fin);
    List<Evento> buscarPorTitulo(String keyword);
    int contarEventosActivos(); // usa native query
    String gestionarInscripcion(Long idEvento, Long idCliente);
    Integer obtenerInscritos(Long idEvento);
    List<Evento> buscarPorDistrito(String nombreDistrito);
}