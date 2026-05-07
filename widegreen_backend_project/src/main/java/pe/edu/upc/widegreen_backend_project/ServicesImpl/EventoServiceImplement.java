package pe.edu.upc.widegreen_backend_project.ServicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importante para deletes
import pe.edu.upc.widegreen_backend_project.Entities.*;
import pe.edu.upc.widegreen_backend_project.Repository.*;
import pe.edu.upc.widegreen_backend_project.Services.IEventoService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventoServiceImplement implements IEventoService {

    @Autowired
    private EventoRepository eventoRepository;
    @Autowired
    private EventoPorClienteRepository inscripcionRepo;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private DistritoRepository distritoRepository;

    @Override
    public Evento crearEvento(Evento evento) {
        if (evento.getEvento_fechaFin() != null && evento.getEvento_fechaInicio() != null) {
            if (evento.getEvento_fechaFin().isBefore(evento.getEvento_fechaInicio())) {
                throw new IllegalArgumentException("La fecha fin no puede ser antes de la fecha inicio");
            }
        }
        
        // Generar coordenadas aleatorias si no existen
        if ((evento.getLatitud() == null || evento.getLongitud() == null) && evento.getDistrito() != null) {
            if (evento.getDistrito().getLatitud() != null && evento.getDistrito().getLongitud() != null) {
                double[] coordenadas = generarCoordenadaAleatoria(
                    evento.getDistrito().getLatitud(),
                    evento.getDistrito().getLongitud(),
                    500  // radio de 500 metros
                );
                evento.setLatitud(coordenadas[0]);
                evento.setLongitud(coordenadas[1]);
            }
        }
        
        return eventoRepository.save(evento);
    }

    @Transactional
    public String gestionarInscripcion(Long idEvento, Long idCliente) {
        boolean yaInscrito = inscripcionRepo.existeInscripcion(idCliente, idEvento);

        if (yaInscrito) {
            EventoPorCliente inscripcion = inscripcionRepo.buscarPorClienteYEvento(idCliente, idEvento);
            inscripcionRepo.delete(inscripcion);

            Cliente c = clienteRepository.findById(idCliente).get();
            c.setCant_eventos_asistidos(c.getCant_eventos_asistidos() - 1);
            clienteRepository.save(c);

            return "Desinscrito correctamente";
        } else {
            EventoPorCliente nueva = new EventoPorCliente();
            nueva.setId_evento(eventoRepository.findById(idEvento).orElseThrow());
            nueva.setId_Cliente(clienteRepository.findById(idCliente).orElseThrow());
            nueva.setFechaInscripcion(LocalDate.now());
            nueva.setAsistencia("Pendiente");
            inscripcionRepo.save(nueva);

            Cliente c = clienteRepository.findById(idCliente).get();
            c.setCant_eventos_asistidos(c.getCant_eventos_asistidos() + 1);
            clienteRepository.save(c);

            return "Inscrito correctamente";
        }
    }

    public List<Evento> buscarPorDistrito(String nombreDistrito) {
        // List<Evento> findByDistritoDistritoNombreContaining(String nombre);
        return eventoRepository.findAll().stream()
                .filter(e -> e.getDistrito() != null &&
                        e.getDistrito().getDistrito_nombre().equalsIgnoreCase(nombreDistrito))
                .collect(Collectors.toList());
    }

    public Integer obtenerInscritos(Long idEvento) {
        return inscripcionRepo.contarInscritosPorEvento(idEvento);
    }

    @Override
    public List<Evento> listarEventos() { return eventoRepository.findAll(); }
    @Override
    public Evento obtenerEventoPorId(Long id) { return eventoRepository.findById(id).orElse(null); }
    @Override
    public void eliminarEvento(Long id) {
        // Cargar el evento para que Hibernate cargue la colección multimedia
        var evento = eventoRepository.findById(id);
        if(evento.isPresent()) {
            // La referencia a evento.get().getMultimedia() fuerza la carga de la colección
            // Luego deleteById() dispara el cascade delete
            eventoRepository.deleteById(id);
        }
    }
    @Override
    public List<Evento> listarEventosActivos() { return eventoRepository.findByEstadoJPQL("Activo"); }
    @Override
    public List<Evento> listarEventosPorRangoFechas(LocalDate inicio, LocalDate fin) { return eventoRepository.listarEventosPorRangoFechas(inicio, fin); }
    @Override
    public List<Evento> buscarPorTitulo(String keyword) { return eventoRepository.buscarPorTituloJPQL(keyword); }
    @Override
    public int contarEventosActivos() { return eventoRepository.contarEventosPorEstadoNative("Activo"); }

    /**
     * Genera una coordenada aleatoria dentro de un radio especificado
     * usando el algoritmo de Haversine
     * 
     * @param latitudCentro Latitud del centro (distrito)
     * @param longitudCentro Longitud del centro (distrito)
     * @param radioMetros Radio en metros
     * @return Array [latitud, longitud]
     */
    private double[] generarCoordenadaAleatoria(double latitudCentro, double longitudCentro, int radioMetros) {
        // Generar ángulo aleatorio (0 a 2π radianes)
        double angulo = Math.random() * 2 * Math.PI;
        
        // Generar distancia aleatoria (0 a radioMetros)
        double distancia = Math.random() * radioMetros;
        
        // Conversión: 1 grado de latitud ≈ 111 km
        double lat_aleatoria = latitudCentro + (distancia * Math.cos(angulo)) / 111000;
        
        // Conversión: 1 grado de longitud depende de la latitud
        double lng_aleatoria = longitudCentro + (distancia * Math.sin(angulo)) / 
                              (111000 * Math.cos(Math.toRadians(latitudCentro)));
        
        return new double[]{lat_aleatoria, lng_aleatoria};
    }
}
