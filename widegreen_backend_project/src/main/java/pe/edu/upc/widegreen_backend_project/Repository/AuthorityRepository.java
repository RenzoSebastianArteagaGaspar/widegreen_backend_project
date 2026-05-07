package pe.edu.upc.widegreen_backend_project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.widegreen_backend_project.Entities.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    public Authority findByName(String name);

}
