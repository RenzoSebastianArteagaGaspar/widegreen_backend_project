package pe.edu.upc.widegreen_backend_project.Services;

import pe.edu.upc.widegreen_backend_project.Dtos.MultimediaDTO;
import java.util.List;

public interface MultimediaService {
    MultimediaDTO subirFoto(String tipoEntidad, Long idEntidad, String nombreArchivo, String tipoArchivo, byte[] contenido) throws Exception;
    List<MultimediaDTO> obtenerFotosPorEntidad(String tipoEntidad, Long idEntidad);
    void eliminarFoto(Long idMultimedia);
    MultimediaDTO obtenerFoto(Long idMultimedia);
}
