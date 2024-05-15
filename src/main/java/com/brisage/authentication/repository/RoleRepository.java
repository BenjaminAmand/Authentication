package com.brisage.authentication.repository;

import com.brisage.authentication.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByNomRole(@Param("name_role") String roleName);
}
