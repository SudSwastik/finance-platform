package com.finance.platform.security;

import com.finance.platform.common.domain.UserId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class PlatformUserPrincipal implements UserDetails {

    private final UserId userId;
    private final List<GrantedAuthority> authorities;

    public PlatformUserPrincipal(UserId userId, List<String> groups) {
        this.userId = userId;
        this.authorities = groups.stream()
                .map(g -> new SimpleGrantedAuthority("ROLE_" + g.toUpperCase()))
                .collect(Collectors.toList());
    }

    public UserId userId() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return userId.value();
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
