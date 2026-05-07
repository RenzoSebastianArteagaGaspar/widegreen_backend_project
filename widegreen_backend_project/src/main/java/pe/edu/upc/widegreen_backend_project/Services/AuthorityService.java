package pe.edu.upc.widegreen_backend_project.Services;

import pe.edu.upc.widegreen_backend_project.Entities.Authority;

public interface AuthorityService {

    public Authority findById(Long id);
    public Authority findByName(String name);

    public Authority add(Authority authority);

}
