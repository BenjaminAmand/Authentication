package com.brisage.authentication.services.impl;

import com.brisage.authentication.entity.Role;
import com.brisage.authentication.entity.User;
import com.brisage.authentication.entity.UserDetailsImpl;
import com.brisage.authentication.services.RoleService;
import com.brisage.authentication.services.UserService;
import com.brisage.authentication.services.impls.UserDetailsServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserDetailsServiceImplTest {
    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;

    @Test
    public void loadUserByUsername_Test() {
        // GIVEN
        User mockUser = new User(1, "username", "email", "password", new Role("ROLE_A", 1));
        doReturn(mockUser).when(userService).findByEmail("test");
        // WHEN
        UserDetails res = userDetailsServiceImpl.loadUserByUsername("test");
        // THEN
        assertInstanceOf(UserDetailsImpl.class, res);
        assertEquals(((UserDetailsImpl)res).getUser(), mockUser);
        verify(userService).findByEmail("test");
    }

    @Test
    public void getAuthoritiesByRole_Test() {
        // GIVEN
        List<Role> mockListRole = new ArrayList<>(){{
            add(new Role("ROLE_A", 0));
            add(new Role("ROLE_B", 1));
            add(new Role("ROLE_C", 2));
            add(new Role("ROLE_D", 3));
        }};;
        doReturn(mockListRole).when(roleService).getRolesLessThan(any());
        // WHEN
        Collection<? extends GrantedAuthority> res = userDetailsServiceImpl.getAuthoritiesByRole(mockListRole.get(3));
        // THEN
        assertEquals(4, res.size());
        verify(roleService).getRolesLessThan(any());
    }
}
