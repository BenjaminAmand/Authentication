package com.brisage.authentication.services.impl;

import com.brisage.authentication.entity.Role;
import com.brisage.authentication.repository.RoleRepository;
import com.brisage.authentication.services.impls.RoleServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class RoleServiceImplTest {
    @InjectMocks
    private RoleServiceImpl roleServiceImpl;
    @Mock
    private RoleRepository roleRepository;

    @Test
    public void getRolesLessThan_Test() {
        // GIVEN
        List<Role> mockListRole = new ArrayList<>(){{
            add(new Role("ROLE_A", 0));
            add(new Role("ROLE_B", 1));
            add(new Role("ROLE_C", 2));
            add(new Role("ROLE_D", 3));
        }};
        Role mockRole = new Role("ROLE_C", 2);
        doReturn(mockListRole).when(roleRepository).findAll();
        // WHEN
        List<Role> res = roleServiceImpl.getRolesLessThan(mockRole);
        // THEN
        assertEquals(3, res.size());
        assertTrue(res.stream().allMatch(elt -> elt.getPriority() < mockRole.getPriority() || mockRole.equals(elt)));
        verify(roleRepository).findAll();
    }

    @Test
    public void getRolesLessThan_Empty_Test() {
        // GIVEN
        List<Role> mockListRole = new ArrayList<>(){{
            add(new Role("ROLE_A", 0));
            add(new Role("ROLE_B", 1));
            add(new Role("ROLE_C", 2));
            add(new Role("ROLE_D", 3));
        }};
        Role mockRole = new Role("ROLE_A", 0);
        doReturn(mockListRole).when(roleRepository).findAll();
        // WHEN
        List<Role> res = roleServiceImpl.getRolesLessThan(mockRole);
        // THEN
        assertEquals(1, res.size());
        assertTrue(res.stream().allMatch(elt -> elt.getPriority() < mockRole.getPriority() || mockRole.equals(elt)));
        verify(roleRepository).findAll();
    }

}
