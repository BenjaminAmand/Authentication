package com.brisage.authentication.services.impls;


import com.brisage.authentication.dtos.RegisterDTO;
import com.brisage.authentication.entity.Role;
import com.brisage.authentication.entity.User;
import com.brisage.authentication.repository.RoleRepository;
import com.brisage.authentication.repository.UserRepository;
import com.brisage.authentication.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @Override
    public User findByEmail(String username) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user was found"));
    }

    @Override
    public String register(RegisterDTO registerDto) {
        String roleName = "ROLE_USER";

        // define the role
        Role role = roleRepository.findByNomRole(roleName)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setNomRole(roleName);
                    newRole.setPriority(0);
                    return roleRepository.save(newRole);
                });

        // create a BCryptPasswordEncoder object to encode passwords
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // creation of the user with his information
        User user = new User(null, registerDto.getUsername(), registerDto.getEmail(), passwordEncoder.encode(registerDto.getPassword()), role);
        userRepository.save(user);
        return "ok";
    }

}