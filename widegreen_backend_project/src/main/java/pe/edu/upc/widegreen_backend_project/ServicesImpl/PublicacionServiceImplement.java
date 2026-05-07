package pe.edu.upc.widegreen_backend_project.ServicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upc.widegreen_backend_project.Dtos.ComentarioDTO;
import pe.edu.upc.widegreen_backend_project.Dtos.PublicacionDTO;
import pe.edu.upc.widegreen_backend_project.Entities.Cliente;
import pe.edu.upc.widegreen_backend_project.Entities.Comentario;
import pe.edu.upc.widegreen_backend_project.Entities.Publicacion;
import pe.edu.upc.widegreen_backend_project.Repository.ClienteRepository;
import pe.edu.upc.widegreen_backend_project.Repository.ComentarioRepository;
import pe.edu.upc.widegreen_backend_project.Repository.PublicacionRepository;
import pe.edu.upc.widegreen_backend_project.Services.PublicacionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicacionServiceImplement implements PublicacionService {

    @Autowired
    private PublicacionRepository publicacionRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ComentarioRepository comentarioRepository;


    @Override
    public PublicacionDTO crearPublicacion(PublicacionDTO dto) {
        Publicacion p = new Publicacion();
        p.setTitulo(dto.getTitulo());
        p.setContenido(dto.getContenido());
        p.setFecha(LocalDateTime.now());
        p.setCategoria(dto.getCategoria());

        Cliente cliente = clienteRepository.findById(dto.getId_cliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        p.setId_Cliente(cliente);

        Publicacion guardada = publicacionRepository.save(p);
        return convertirPubADTO(guardada);
    }

    @Override
    public PublicacionDTO editarPublicacion(Long id, PublicacionDTO dto) {
        Publicacion p = publicacionRepository.findById(id).orElse(null);
        if (p != null) {
            p.setTitulo(dto.getTitulo());
            p.setContenido(dto.getContenido());
            if(dto.getCategoria() != null) p.setCategoria(dto.getCategoria());
            return convertirPubADTO(publicacionRepository.save(p));
        }
        return null;
    }

    @Override
    public void eliminarPublicacion(Long id) {
        // Cargar la publicación para que Hibernate cargue la colección multimedia
        var publicacion = publicacionRepository.findById(id);
        if(publicacion.isPresent()) {
            // La referencia a publicacion.get().getMultimedia() fuerza la carga de la colección
            // Luego deleteById() dispara el cascade delete
            publicacionRepository.deleteById(id);
        }
    }


    @Override
    public List<PublicacionDTO> listarTodas() {
        return publicacionRepository.findAllByOrderByFechaDesc().stream()
                .map(this::convertirPubADTO).collect(Collectors.toList());
    }

    @Override
    public List<PublicacionDTO> buscarPorCategoria(String categoria) {
        return publicacionRepository.findByCategoria(categoria).stream()
                .map(this::convertirPubADTO).collect(Collectors.toList());
    }

    @Override
    public List<PublicacionDTO> buscarPorPalabra(String keyword) {
        return publicacionRepository.buscarPorPalabraClave(keyword).stream()
                .map(this::convertirPubADTO).collect(Collectors.toList());
    }

    @Override
    public List<PublicacionDTO> listarPorCliente(Long idCliente) {
        return publicacionRepository.listarPublicacionesPorCliente(idCliente).stream()
                .map(this::convertirPubADTO).collect(Collectors.toList());
    }


    @Override
    public ComentarioDTO comentar(ComentarioDTO dto) {
        Comentario c = new Comentario();
        c.setContenido(dto.getContenido());

        Cliente cliente = clienteRepository.findById(dto.getId_cliente()).orElseThrow();
        c.setId_Cliente(cliente);

        Publicacion pub = publicacionRepository.findById(dto.getId_publicacion()).orElseThrow();
        c.setPublicacion(pub);

        Comentario guardado = comentarioRepository.save(c);

        return new ComentarioDTO(
                guardado.getId_comentario(),
                guardado.getContenido(),
                guardado.getId_Cliente().getId_cliente(),
                guardado.getId_Cliente().getName(),
                guardado.getPublicacion().getId()
        );
    }

    @Override
    public List<ComentarioDTO> listarComentarios(Long idPublicacion) {
        return comentarioRepository.findByPublicacionId(idPublicacion).stream()
                .map(c -> new ComentarioDTO(
                        c.getId_comentario(),
                        c.getContenido(),
                        c.getId_Cliente().getId_cliente(),
                        c.getId_Cliente().getName(),
                        c.getPublicacion().getId()
                )).collect(Collectors.toList());
    }

    @Override
    public void eliminarComentario(Long id) {
        comentarioRepository.deleteById(id);
    }

    private PublicacionDTO convertirPubADTO(Publicacion p) {
        return new PublicacionDTO(
                p.getId(),
                p.getTitulo(),
                p.getContenido(),
                p.getFecha(),
                p.getCategoria(),
                p.getId_Cliente().getId_cliente(),
                p.getId_Cliente().getName() + " " + p.getId_Cliente().getApellido()
        );
    }
}

