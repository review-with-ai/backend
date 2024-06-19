package com.aireview.review.config.security;

import com.aireview.review.authentication.jwt.JwtAuthenticationFilter;
import com.aireview.review.authentication.jwt.JwtAuthenticationProvider;
import com.aireview.review.authentication.jwt.JwtConfig;
import com.aireview.review.authentication.jwt.JwtService;
import com.aireview.review.config.SecretEncoderConfig;
import com.aireview.review.login.LoginFailureHandler;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
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
            JsonUsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter,
            CorsConfigurationSource corsConfigurationSource
    ) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .securityMatchers(matchers -> matchers
                        .requestMatchers(
                                "/api/v1/login",
                                "/api/v1/oauth2/authorization/naver",
                                "/api/v1/oauth2/authorization/kakao",
                                "/login/oauth2/code/naver",
                                "/login/oauth2/code/kakao"
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth -> oauth
                        .authorizationEndpoint(endpoint -> endpoint.baseUri("/api/v1/oauth2/authorization"))
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(loginFailureHandler))
                .addFilterAfter(usernamePasswordAuthenticationFilter, OAuth2LoginAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://dev.api.ai-review.site",
                "http://localhost"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return corsConfigurationSource;
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AccessDeniedHandler accessDeniedHandler,
            EntryPointUnauthenticatedHandler entryPointUnauthenticatedHandler,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CorsConfigurationSource corsConfigurationSource,
            List<RequestMatcher> excludeAuthenticationRequestMatchers
    ) throws Exception {
        // TODO: 4/29/24 CSRF,CORS 설정 필요
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(excludeAuthenticationRequestMatchers.toArray(new RequestMatcher[]{})).permitAll()
//                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
//                        .requestMatchers(HttpMethod.GET, "/manage/health").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/manage/beans").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/manage/metrics/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/manage/env").permitAll()
//
//                        .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/v1/refresh-token").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/v1/subscriptions/payment/approve").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/v1/subscriptions/payment/fail").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/v1/subscriptions/payment/cancel").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(handler -> handler
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(entryPointUnauthenticatedHandler));

        return http.build();
    }

    @Bean
    List<RequestMatcher> excludeAuthenticationRequestMatchers() {
        return Arrays.asList(
                CorsUtils::isPreFlightRequest,
                new AntPathRequestMatcher("/manage/health", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/manage/beans", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/manage/metrics/**", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/manage/env", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/swagger-ui/**", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/v3/api-docs/**", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/api/v1/refresh-token", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/api/v1/users", HttpMethod.POST.name()),
                new AntPathRequestMatcher("/api/v1/subscriptions/payment/approve", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/api/v1/subscriptions/payment/fail", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/api/v1/subscriptions/payment/cancel", HttpMethod.GET.name())
        );
    }

    @Bean
    public JwtAuthenticationFilter jwtFilter(JwtConfig jwtConfig,
                                             JwtService jwtService,
                                             AuthenticationManager authenticationManager,
                                             List<RequestMatcher> excludeAuthenticationRequestMatchers
    ) {
        return new JwtAuthenticationFilter(jwtConfig.getAccessTokenName(), jwtService, authenticationManager, excludeAuthenticationRequestMatchers);
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
