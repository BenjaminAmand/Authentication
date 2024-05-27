package com.brisage.authentication.config;

import com.brisage.authentication.services.impls.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfiguration {
    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;

    /*@Bean
    // Gestion du CORS
    // A ABSOLUMENT MODIFIER POUR UNIQUEMENT AJOUTER LES ORIGINES DES MICRO SERVICE CAR ACTUELLEMENT AUTHORISE TOUTES LES ORIGINES AVEC TOUTES LES METHODES DE REQUETES
    FilterRegistrationBean<CorsFilter> simpleCorsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Collections.singletonList("*"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }*/

    /*
    @Bean
    // Creation des users
    // A MODIFIER POUR RECUPERER LES USERS DU SERVER
    public UserDetailsService users() {
        PasswordEncoder encoder = passwordEncoder();
        UserDetails everybody = User.withUsername("everybody")
                .password(encoder.encode("password"))
                .roles()
                .build();
        UserDetails user = User.withUsername("sergey")
                .password(encoder.encode("password"))
                .roles("USER")
                .build();
        UserDetails user2 = User.withUsername("sergey2")
                .password(encoder.encode("password"))
                .roles("USER", "USER2")
                .build();
        return new InMemoryUserDetailsManager(user, user2);
    }*/

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public AuthenticationProvider authenticationProvider(){
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(this.userDetailsServiceImpl);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

}
