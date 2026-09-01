package com.generation.SportHub.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.generation.SportHub.security.JpaUserDetailsService;

import java.util.HashMap;
import java.util.Map;


/**
 * Configurazione centrale della sicurezza dell'applicazione.
 *
 * Questa classe dice a Spring Security:
 * - quali URL sono pubblici e quali richiedono autenticazione;
 * - quali ruoli possono usare quali pagine o operazioni;
 * - come effettuare il login e il logout;
 * - come leggere gli utenti dal database;
 * - come confrontare le password salvate nel sistema.
 */


@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationProvider authenticationProvider
    ) throws Exception {
        
        
        // La nostra logica recupera l'utente dal database e confronta la password nel modo giusto.
        http
                .authenticationProvider(authenticationProvider)

                
                .authorizeHttpRequests(authorize -> authorize
                        
                        .requestMatchers(
                                "/", "/login", "/accesso-negato", "/homecss/**","/error",
                                "/home/index", "/store/**", "/login/**","/error/**", "/product/**", 
                                "/register/**", "/resources/**", "/css/**", "/js/**", "/res/**",
                                "/favicon.ico"
                        )
                        .permitAll()
                        


                        
                        .requestMatchers(
                                "/post/delete"
                             
                                
                        ).hasRole("ADMIN")

                        
                        .requestMatchers(HttpMethod.POST,
                                "/qA/delete-question"
                        ).hasAnyRole("ADMIN", "STAFF")

                        
                        .anyRequest().authenticated()
                )

                
                .formLogin(form -> form
                        
                        .loginPage("/login")
                        .usernameParameter("email")
                        
                        .defaultSuccessUrl("/", true)
                       
                        .failureUrl("/login?error")
                        .permitAll()
                )

                
                .logout(logout -> logout
                        
                        .logoutUrl("/logout")
                        
                        .logoutSuccessUrl("/login?logout")
                        
                        .invalidateHttpSession(true)
                        
                        .clearAuthentication(true)
       
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                
                
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedPage("/access-denied")
                )

                
              
                .sessionManagement(session -> session
                        .sessionFixation(fixation -> fixation.changeSessionId())
                )

               
                .csrf(Customizer.withDefaults())

                .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp.policyDirectives(
                        "default-src 'self'; " +
                        "script-src 'self' 'unsafe-inline'; " + 
                        "style-src 'self' 'unsafe-inline' https://cdnjs.cloudflare.com https://fonts.googleapis.com; " + // <-- Aggiunto 'unsafe-inline' qui
                        "font-src 'self' https://fonts.gstatic.com https://cdnjs.cloudflare.com; " +
                        "img-src 'self' data:; " +
                        "object-src 'none'; " +
                        "base-uri 'self'; " +
                        "frame-ancestors 'none'; " +
                        "form-action 'self'"
                        ))
                );

        return http.build();
    }

        
   @Bean
    public AuthenticationProvider authenticationProvider(
            JpaUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
                
        DaoAuthenticationProvider provider = 
        new DaoAuthenticationProvider(userDetailsService);
       
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
               
        Map<String, PasswordEncoder> encoders = new HashMap<>();
                
             
        encoders.put("bcrypt", new BCryptPasswordEncoder(12));
                
        return new DelegatingPasswordEncoder("bcrypt", encoders);
    }
} 
