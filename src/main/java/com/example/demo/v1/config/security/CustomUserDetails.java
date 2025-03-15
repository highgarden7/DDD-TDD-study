package com.example.demo.v1.config.security;

import com.example.demo.v1.domain.user.IdType;
import com.example.demo.v1.domain.user.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserId();
    }

    public String getName() {
        return user.getName();
    }

    public IdType getIdType() {
        return user.getIdType();
    }

    public String getIdValue() {
        return user.getIdValue();
    }
}
