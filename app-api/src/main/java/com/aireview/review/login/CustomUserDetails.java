package com.aireview.review.login;


import com.aireview.review.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;


public class CustomUserDetails implements UserDetails {
    @Getter
    private User user;

    @Getter
    private Long userId;


    private String username;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(
            User user,
            Long userId,
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities
    ) {
        Assert.isTrue(user != null && userId != null && username != null && password != null && authorities != null
                , "user must be provided");
        this.user = user;
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }


    public CustomUserDetails(User user) {
        this(user, user.getId(), user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority(Role.USER.value())));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
