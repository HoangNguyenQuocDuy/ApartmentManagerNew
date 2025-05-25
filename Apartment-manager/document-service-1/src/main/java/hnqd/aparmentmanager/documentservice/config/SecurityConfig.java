package hnqd.aparmentmanager.documentservice.config;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(
                            "/actuator/**", "/login", "/swagger-ui/**", "/v3/api-docs/**",
                            "/api/contracts/contract-for-resident", "/api/contracts/contract-for-resident**",
                            "/api/contracts/contract-for-resident/**", "/api/contracts/by-user/**",
                            "/by-user/**", "/by-room/**", "/api/contract-terms/user/**",
                            "/api/contracts/room/user/**"
                    ).permitAll();
                    authorize.requestMatchers(HttpMethod.PATCH, "/api/contract-terms/**").permitAll();
                    authorize.anyRequest().authenticated();
                })
//                .authenticationProvider()
                .addFilterBefore(authRequestFilter(), UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }

    @Bean
    public AuthRequestFilter authRequestFilter() {
        return new AuthRequestFilter(new JwtProvider());
    }
}
