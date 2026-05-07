package pe.edu.upc.widegreen_backend_project.ServicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.widegreen_backend_project.Dtos.MultimediaDTO;
import pe.edu.upc.widegreen_backend_project.Entities.Evento;
import pe.edu.upc.widegreen_backend_project.Entities.Multimedia;
import pe.edu.upc.widegreen_backend_project.Entities.Publicacion;
import pe.edu.upc.widegreen_backend_project.Repository.EventoRepository;
import pe.edu.upc.widegreen_backend_project.Repository.MultimediaRepository;
import pe.edu.upc.widegreen_backend_project.Repository.PublicacionRepository;
import pe.edu.upc.widegreen_backend_project.Services.MultimediaService;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MultimediaServiceImpl implements MultimediaService {

    @Autowired
    private MultimediaRepository multimediaRepository;
    
    @Autowired
    private PublicacionRepository publicacionRepository;
    
    @Autowired
    private EventoRepository eventoRepository;

    @Override
    @Transactional
    public MultimediaDTO subirFoto(String tipoEntidad, Long idEntidad, String nombreArchivo, String tipoArchivo, byte[] contenido) throws Exception {
        
        if (!tipoArchivo.startsWith("image/")) {
            throw new Exception("El archivo no es una imagen válida. Tipo: " + tipoArchivo);
        }
        
        if (contenido.length > 5242880) { // 5MB
            throw new Exception("El archivo es demasiado grande. Máximo: 5MB");
        }

        // Validar que la entidad existe
        if ("PUBLICACION".equals(tipoEntidad)) {
            if (!publicacionRepository.existsById(idEntidad)) {
                throw new Exception("Publicación no encontrada");
            }
        } else if ("EVENTO".equals(tipoEntidad)) {
            if (!eventoRepository.existsById(idEntidad)) {
                throw new Exception("Evento no encontrado");
            }
        } else {
            throw new Exception("Tipo de entidad inválido: " + tipoEntidad);
        }

        Multimedia multimedia = new Multimedia();
        multimedia.setNombreArchivo(nombreArchivo);
        multimedia.setTipoArchivo(tipoArchivo);
        // Convertir bytes a Base64
        multimedia.setContenidoImagenBase64(Base64.getEncoder().encodeToString(contenido));
        multimedia.setTamañoArchivo((long) contenido.length);
        multimedia.setFechaSubida(LocalDateTime.now());
        multimedia.setTipoEntidad(tipoEntidad);
        multimedia.setIdEntidad(idEntidad);
        
        // IMPORTANTE: Cargar la entidad y guardarla en la relación ManyToOne
        if ("PUBLICACION".equals(tipoEntidad)) {
            Publicacion publicacion = publicacionRepository.findById(idEntidad)
                    .orElseThrow(() -> new Exception("Publicación no encontrada"));
            multimedia.setIdPublicacion(publicacion);
        } else if ("EVENTO".equals(tipoEntidad)) {
            Evento evento = eventoRepository.findById(idEntidad)
                    .orElseThrow(() -> new Exception("Evento no encontrado"));
            multimedia.setIdEvento(evento);
        }

        System.out.println("=== MULTIMEDIA SERVICE - ANTES DE GUARDAR ===");
        System.out.println("TipoEntidad: " + multimedia.getTipoEntidad());
        System.out.println("IdEntidad: " + multimedia.getIdEntidad());
        System.out.println("NombreArchivo: " + multimedia.getNombreArchivo());
        System.out.println("Tamaño: " + multimedia.getTamañoArchivo());
        System.out.println("=========================================");

        Multimedia guardada = multimediaRepository.save(multimedia);
        
        System.out.println("=== MULTIMEDIA SERVICE - DESPUÉS DE GUARDAR ===");
        System.out.println("ID generado: " + guardada.getId());
        System.out.println("TipoEntidad guardado: " + guardada.getTipoEntidad());
        System.out.println("IdEntidad guardado: " + guardada.getIdEntidad());
        System.out.println("NombreArchivo guardado: " + guardada.getNombreArchivo());
        System.out.println("===========================================");
        
        return convertirADTO(guardada);
    }

    @Override
    public List<MultimediaDTO> obtenerFotosPorEntidad(String tipoEntidad, Long idEntidad) {
        return multimediaRepository.findByTipoEntidadAndIdEntidad(tipoEntidad, idEntidad).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminarFoto(Long idMultimedia) {
        multimediaRepository.deleteById(idMultimedia);
    }

    @Override
    public MultimediaDTO obtenerFoto(Long idMultimedia) {
        return multimediaRepository.findById(idMultimedia)
                .map(this::convertirADTO)
                .orElse(null);
    }

    private MultimediaDTO convertirADTO(Multimedia multimedia) {
        return new MultimediaDTO(
                multimedia.getId(),
                multimedia.getNombreArchivo(),
                multimedia.getTipoArchivo(),
                multimedia.getContenidoImagenBase64(),
                multimedia.getTamañoArchivo(),
                multimedia.getFechaSubida(),
                multimedia.getTipoEntidad(),
                multimedia.getIdEntidad()
        );
    }
}
