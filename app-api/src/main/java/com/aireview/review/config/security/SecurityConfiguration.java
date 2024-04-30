package com.aireview.review.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import java.util.List;

@Configuration
@EnableConfigurationProperties(value = {SecretEncoderConfig.class, JwtConfig.class})
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AccessDeniedHandler accessDeniedHandler,
            EntryPointUnauthorizedHandler entryPointUnauthorizedHandler,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) throws Exception {
        // TODO: 4/29/24 CSRF 설정 필요 
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/v1/account/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/account").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/coupon/fcfs").hasRole(Role.USER.name())
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAfter(jwtAuthenticationFilter, ExceptionTranslationFilter.class)
                .exceptionHandling(handler -> handler
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(entryPointUnauthorizedHandler));

        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtFilter(JwtConfig jwtConfig, Jwt jwt, AuthenticationManager authenticationManager) {
        return new JwtAuthenticationFilter(jwtConfig.getHeader(), jwt, authenticationManager);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            Jwt jwt
    ) {
        DaoAuthenticationProvider daoAuthenticationProvider = new CustomDaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(jwt);

        ProviderManager providerManager = new ProviderManager(List.of(daoAuthenticationProvider, jwtAuthenticationProvider));
        providerManager.setEraseCredentialsAfterAuthentication(true);

        return providerManager;
    }

    @Bean
    public PasswordEncoder pbk2PasswordEncoder(SecretEncoderConfig config) {
        return new Pbkdf2PasswordEncoder(
                config.getSecret(),
                config.getSaltLength(),
                config.getIteration(),
                Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
    }


}
