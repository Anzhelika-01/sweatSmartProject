package bg.softuni.sweatsmartproject.repository;

import bg.softuni.sweatsmartproject.domain.entity.UserRole;
import bg.softuni.sweatsmartproject.domain.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.management.relation.Role;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRoleRepo extends JpaRepository<UserRole, UUID> {
    UserRole findByRole(RoleEnum role);
}