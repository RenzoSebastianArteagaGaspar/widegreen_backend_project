package pe.edu.upc.widegreen_backend_project.Services;

import pe.edu.upc.widegreen_backend_project.Dtos.ComentarioDTO;
import pe.edu.upc.widegreen_backend_project.Dtos.PublicacionDTO;
import java.util.List;

public interface PublicacionService {
    PublicacionDTO crearPublicacion(PublicacionDTO dto);
    PublicacionDTO editarPublicacion(Long id, PublicacionDTO dto); // US #6 Sc 4
    void eliminarPublicacion(Long id); // US #6 Sc 5

    List<PublicacionDTO> listarTodas();
    List<PublicacionDTO> buscarPorCategoria(String categoria);
    List<PublicacionDTO> buscarPorPalabra(String keyword);
    List<PublicacionDTO> listarPorCliente(Long idCliente);

    ComentarioDTO comentar(ComentarioDTO dto);
    List<ComentarioDTO> listarComentarios(Long idPublicacion);
    void eliminarComentario(Long id);
}
