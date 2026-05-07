package pe.edu.upc.widegreen_backend_project.Services;


import pe.edu.upc.widegreen_backend_project.Dtos.DTOUser;
import pe.edu.upc.widegreen_backend_project.Entities.User;

public interface UserService {

    public User findById(Long id);
    public User findByUsername(String username);
    public DTOUser add(DTOUser userDTO);
    public DTOUser addWithRole(DTOUser userDTO, String roleName);
    public User addUser(User user);
}
