package com.brisage.authentication.services.impls;

import com.brisage.authentication.entity.Role;
import com.brisage.authentication.repository.RoleRepository;
import com.brisage.authentication.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    public RoleRepository repository;

    @Override
    public List<Role> getRolesLessThan(Role role) {
        List<Role> roles = repository.findAll();
        List<Role> res = new ArrayList<>();
        for(Role elt : roles){
            if(elt.getPriority() < role.getPriority())
                res.add(elt);
        }
        res.add(role);
        return res;
    }
}
