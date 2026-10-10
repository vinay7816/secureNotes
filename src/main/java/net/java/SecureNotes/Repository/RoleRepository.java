package net.java.SecureNotes.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.java.SecureNotes.entity.AppRole;
import net.java.SecureNotes.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);

}