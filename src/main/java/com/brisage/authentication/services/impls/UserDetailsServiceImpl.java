package com.brisage.authentication.services.impls;

import com.brisage.authentication.entity.Role;
import com.brisage.authentication.entity.UserDetailsImpl;
import com.brisage.authentication.services.RoleService;
import com.brisage.authentication.services.UserDetailsServiceImp;
import com.brisage.authentication.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsServiceImp {
    private final UserService userService;
    private final RoleService roleService;

    // allows loading user details
    @Override
    public UserDetails loadUserByUsername(String username) {
        return new UserDetailsImpl(this, this.userService.findByEmail(username));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthoritiesByRole(Role role) {
        List<Role> roles = roleService.getRolesLessThan(role);
        List<GrantedAuthority> auth = new ArrayList<>();
        for(Role elt : roles){
            auth.add(new SimpleGrantedAuthority(elt.getNomRole()));
        }
        return auth;
    }
}