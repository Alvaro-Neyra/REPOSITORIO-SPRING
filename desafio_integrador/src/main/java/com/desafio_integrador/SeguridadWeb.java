package com.desafio_integrador;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SeguridadWeb {
        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.authorizeHttpRequests((authorize) -> authorize
                                .requestMatchers("/css/**", "/js/**", "/img/**", "/").permitAll()
                                .requestMatchers("/error").permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/inicio").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/registrar", "/registro").permitAll()
                                .anyRequest().permitAll()
                        )
                        .formLogin((form) -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/logincheck")
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .defaultSuccessUrl("/inicio", true)
                                .permitAll()
                        )
                        .logout((logout) -> logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login")
                                .permitAll()
                        )
                        .exceptionHandling((exceptions) -> exceptions
                                .accessDeniedPage("/error")
                        )
                        .csrf(csrf -> csrf.disable());
                
                return http.build();
        }
}
