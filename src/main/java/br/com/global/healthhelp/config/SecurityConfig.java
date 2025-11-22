package br.com.global.healthhelp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        // Rotas públicas
                        .requestMatchers(
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()

                        // Protege Swagger — login obrigatório
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).authenticated()

                        // Todo o resto também protegido
                        .anyRequest().authenticated()
                )

                // Config login
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )

                // Config logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )

                // CSRF
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails admin = User.withUsername("admin")
                .password("{noop}admin1234")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }
}
