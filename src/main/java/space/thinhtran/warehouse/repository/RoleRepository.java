package space.thinhtran.warehouse.repository;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import space.thinhtran.warehouse.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(String roleName);

    @Query("SELECT r FROM Role r JOIN FETCH r.permissions WHERE r.roleName = :roleName")
    Optional<Role> findWithPermissionsByRoleName(@Param("roleName") String roleName);
}
