package com.nick.webserviceproject.config.security;

import com.nick.webserviceproject.model.CustomUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public AppSecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .cors(Customizer.withDefaults())
                .authorizeRequests(auth -> auth
                        .requestMatchers("/", "/api/**", "/weather/**").permitAll()
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        //.requestMatchers(HttpMethod.GET, "/weather/*").hasRole("USER")
                       // .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults());

        return http.build();


    }
}
