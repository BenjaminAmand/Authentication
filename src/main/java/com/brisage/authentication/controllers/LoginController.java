package com.brisage.authentication.controllers;

import com.brisage.authentication.dtos.LoginDTO;
import com.brisage.authentication.dtos.RegisterDTO;
import com.brisage.authentication.dtos.ResponseBodyDTO;
import com.brisage.authentication.entity.User;
import com.brisage.authentication.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.DeferredSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "https://127.0.0.1:4200", allowCredentials = "true")
@RequiredArgsConstructor
public class LoginController {
    @Autowired
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @PostMapping("/login")
    public ResponseEntity<ResponseBodyDTO> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()));
            SecurityContext context = securityContextHolderStrategy.createEmptyContext();
            context.setAuthentication(authentication);
            securityContextHolderStrategy.setContext(context);
            securityContextRepository.saveContext(context, request, response);
        }
        catch (Exception e){
            return ResponseEntity.status(401).body(new ResponseBodyDTO(e.getMessage()));
        }
        final UserDetails user = userDetailsService.loadUserByUsername(loginDTO.getUsername());

        if(user != null){
            return ResponseEntity.ok(new ResponseBodyDTO(user.getUsername()+this.passwordEncoder.encode(user.getPassword())+user.getAuthorities()));
        }
        return ResponseEntity.status(401).body(new ResponseBodyDTO("Error occured"));
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }
        return "ok";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDto) {
        try {
            String response = userService.register(registerDto);
            return ResponseEntity.ok(new ResponseBodyDTO(response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseBodyDTO("Une erreur est survenue lors de l'inscription de l'utilisateur." + e.getMessage()));
        }
    }

    @GetMapping("/anon")
    public boolean anonSession(HttpServletResponse response, HttpServletRequest request){
        HttpSession session = request.getSession(true);
        if(session.isNew()){
            return true;
        }
        else{
            return false;
        }
    }

    @PostMapping("/correct-credentials")
    public boolean CorrectCredentials(@RequestBody LoginDTO login){
        try {
            User user = userService.findByEmail(login.getUsername());
            return passwordEncoder.matches(login.getPassword(), user.getPassword());
        }
        catch (Exception e){
            return false;
        }
    }

    @PostMapping("/encode-password")
    public String EncodePassword(@RequestBody String password){
        return passwordEncoder.encode(password);
    }
}
