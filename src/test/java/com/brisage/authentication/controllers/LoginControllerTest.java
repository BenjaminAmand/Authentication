package com.brisage.authentication.controllers;


import com.brisage.authentication.config.AuthorizationServerConfiguration;
import com.brisage.authentication.config.MvcConfigurer;
import com.brisage.authentication.config.SpringSecurityConfiguration;
import com.brisage.authentication.dtos.LoginDTO;
import com.brisage.authentication.dtos.RegisterDTO;
import com.brisage.authentication.entity.Role;
import com.brisage.authentication.entity.User;
import com.brisage.authentication.entity.UserDetailsImpl;
import com.brisage.authentication.services.UserService;
import com.brisage.authentication.services.impls.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@WebAppConfiguration
public class LoginControllerTest {
    @InjectMocks
    private LoginController loginController;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private UserService userService;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Autowired
    private MockMvc mockMvc;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void login_Test() throws Exception {
        // GIVEN
        LoginDTO dto = new LoginDTO("username", "password");
        Authentication authentication = new UsernamePasswordAuthenticationToken("username", "password");
        UserDetails userDetails = new UserDetailsImpl(userDetailsService, new User(1, "username", "email", "password", new Role("ROLE_A", 1)));
        doReturn(authentication).when(authenticationManager).authenticate(any());
        doReturn(userDetails).when(userDetailsService).loadUserByUsername(any());
        // WHEN
        // THEN
        mockMvc.perform(post("/login")
                        .content(asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic("client1", "myClientSecretValue")))
                .andExpect(status().isOk());
        verify(authenticationManager).authenticate(any());
        verify(userDetailsService).loadUserByUsername(any());
    }

    @Test
    public void login_ErrorUser_Test() throws Exception {
        // GIVEN
        LoginDTO dto = new LoginDTO("username", "password");
        Authentication authentication = new UsernamePasswordAuthenticationToken("username", "password");
        doReturn(authentication).when(authenticationManager).authenticate(any());
        doReturn(null).when(userDetailsService).loadUserByUsername(any());
        // WHEN
        // THEN
        mockMvc.perform(post("/login")
                        .content(asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic("client1", "myClientSecretValue")))
                .andExpect(status().is(401));
        verify(authenticationManager).authenticate(any());
        verify(userDetailsService).loadUserByUsername(any());
    }

    @Test
    public void login_Throw_Test() throws Exception {
        // GIVEN
        LoginDTO dto = new LoginDTO("username", "password");
        doThrow(new AuthenticationException("") {}).when(authenticationManager).authenticate(any());
        // WHEN
        // THEN
        mockMvc.perform(post("/login")
                        .content(asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic("client1", "myClientSecretValue")))
                .andExpect(status().is(401));
        verify(authenticationManager).authenticate(any());
    }

    @Test
    public void logout_WithoutSession_Test() throws Exception {
        // GIVEN
        // WHEN
        // THEN
        mockMvc.perform(get("/logout")
                        .with(httpBasic("client1", "myClientSecretValue")))
                .andExpect(status().isOk());
    }

    @Test
    public void logout_WithSession_Test() throws Exception {
        // GIVEN
        HashMap<String, Object> sessionattr = new HashMap<String, Object>();
        sessionattr.put("userinfo", "XXXXXXXX");
        // WHEN
        // THEN
        mockMvc.perform(get("/logout")
                        .sessionAttrs(sessionattr)
                        .with(httpBasic("client1", "myClientSecretValue")))
                .andExpect(status().isOk());
    }

    @Test
    public void register_Test() throws Exception {
        // GIVEN
        RegisterDTO dto = new RegisterDTO("username", "email", "password");
        doReturn("ok").when(userService).register(any());
        // WHEN
        mockMvc.perform(post("/register")
                        .content(asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic("client1", "myClientSecretValue")))
                .andExpect(status().isOk());
        // THEN
        verify(userService).register(any());
    }

    @Test
    public void CorrectCredentials_Test() throws Exception {
        // GIVEN
        LoginDTO dto = new LoginDTO("username", "password");
        User user = new User(1, "username", "email", "password", new Role("ROLE_A", 1));
        doReturn(user).when(userService).findByEmail("username");
        doReturn(true).when(passwordEncoder).matches(any(), any());
        // WHEN
        mockMvc.perform(post("/correct-credentials")
                        .content(asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        // THEN
        verify(userService).findByEmail("username");
        verify(passwordEncoder).matches(any(), any());
    }
}
