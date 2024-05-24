package com.aireview.review.config.security;

import com.aireview.review.authentication.jwt.JwtAuthenticationFilter;
import com.aireview.review.authentication.jwt.JwtAuthenticationProvider;
import com.aireview.review.authentication.jwt.JwtConfig;
import com.aireview.review.authentication.jwt.JwtService;
import com.aireview.review.config.SecretEncoderConfig;
import com.aireview.review.login.LoginFailureHandler;
import com.aireview.review.login.Role;
import com.aireview.review.login.oauth.OAuth2AuthenticationSuccessHandler;
import com.aireview.review.login.usernamepassword.JsonUsernamePasswordAuthenticationFilter;
import com.aireview.review.login.usernamepassword.UsernamePasswordAuthenticationSuccessHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import java.util.List;

@Configuration
@EnableConfigurationProperties(value = {SecretEncoderConfig.class, JwtConfig.class})
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    @Value("${spring.security.debug:false}")
    private boolean debugEnabled;

    @Bean
    @Order(1)
    public SecurityFilterChain loginSecurityFilterChain(
            HttpSecurity http,
            OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
            LoginFailureHandler loginFailureHandler,
            JsonUsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter
    ) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .securityMatchers(matchers -> matchers
                        .requestMatchers(
                                "/api/v1/login",
                                "/oauth2/authorization/naver",
                                "/oauth2/authorization/kakao",
                                "/login/oauth2/code/naver",
                                "/login/oauth2/code/kakao"
                        )
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth -> oauth
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(loginFailureHandler))
                .addFilterAfter(usernamePasswordAuthenticationFilter, OAuth2LoginAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AccessDeniedHandler accessDeniedHandler,
            EntryPointUnauthenticatedHandler entryPointUnauthenticatedHandler,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) throws Exception {
        // TODO: 4/29/24 CSRF 설정 필요
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/manage/health").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/account").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/account/refresh-token").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/coupon/fcfs").hasRole(Role.USER.name())
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAfter(jwtAuthenticationFilter, ExceptionTranslationFilter.class)
                .exceptionHandling(handler -> handler
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(entryPointUnauthenticatedHandler));

        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtFilter(JwtConfig jwtConfig, JwtService jwtService, AuthenticationManager authenticationManager) {
        return new JwtAuthenticationFilter(jwtConfig.getHeader(), jwtService, authenticationManager);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(jwtService);

        ProviderManager providerManager = new ProviderManager(List.of(daoAuthenticationProvider, jwtAuthenticationProvider));
        providerManager.setEraseCredentialsAfterAuthentication(true);

        return providerManager;
    }


    @Bean
    public JsonUsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter(
            AuthenticationManager manager,
            UsernamePasswordAuthenticationSuccessHandler usernamePasswordAuthenticationSuccessHandler,
            LoginFailureHandler loginFailureHandler,
            ObjectMapper objectMapper
    ) {
        return new JsonUsernamePasswordAuthenticationFilter(
                manager,
                usernamePasswordAuthenticationSuccessHandler,
                loginFailureHandler,
                objectMapper
        );
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.debug(debugEnabled);
    }

}
