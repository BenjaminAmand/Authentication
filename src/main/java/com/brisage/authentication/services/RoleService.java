package com.brisage.authentication.services;

import com.brisage.authentication.entity.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {
    public List<Role> getRolesLessThan(Role role);
}
