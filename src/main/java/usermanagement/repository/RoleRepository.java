package usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import usermanagement.model.ERole;
import usermanagement.model.Role;

import java.util.Optional;
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(ERole name);

}

