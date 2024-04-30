package com.aireview.review.controller;

import com.aireview.review.config.security.AuthenticatedPrincipal;
import com.aireview.review.config.security.Jwt;
import com.aireview.review.domain.user.User;
import com.aireview.review.model.CustomUserDetails;
import com.aireview.review.model.JoinRequest;
import com.aireview.review.model.LoginRequest;
import com.aireview.review.model.LoginResponse;
import com.aireview.review.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AuthenticationManager authenticationManager;

    private final Jwt jwt;
    private final UserService userService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.unauthenticated(
                request.getUsername(),
                request.getPassword());

        Authentication result = authenticationManager.authenticate(authentication);

        AuthenticatedPrincipal principal = (AuthenticatedPrincipal) result.getPrincipal();

        String token = jwt.create(Jwt.Claims.of(
                principal.getId(),
                principal.getEmail(),
                result.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new)));

        return new LoginResponse(token, principal.getId());
    }

    @PostMapping
    public ResponseEntity<Void> join(@Valid @RequestBody JoinRequest request) {
        User user = userService.join(
                request.getEmail(),
                request.getPassword(),
                request.getName(),
                request.getNickname());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
