package space.thinhtran.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import space.thinhtran.warehouse.entity.Permission;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    Optional<Permission> findByApiEndpoint(String apiEndpoint);
}
