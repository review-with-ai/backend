package com.aireview.review.login;


import com.aireview.review.domain.user.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;


public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {
    @Getter
    private User user;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetails(
            User user,
            Collection<? extends GrantedAuthority> authorities
    ) {
        Assert.isTrue(user != null && authorities != null
                , "user must be provided");
        this.user = user;
        this.authorities = authorities;
    }


    public UserDetails(User user) {
        this(user, List.of(new SimpleGrantedAuthority(Role.USER.value())));
    }

    public Long getUserId() {
        return user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail().getAddress();
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
