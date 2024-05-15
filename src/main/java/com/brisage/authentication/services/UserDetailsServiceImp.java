package com.brisage.authentication.services;

import com.brisage.authentication.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;

public interface UserDetailsServiceImp extends UserDetailsService {
    public Collection<? extends GrantedAuthority> getAuthoritiesByRole(Role role);
}
