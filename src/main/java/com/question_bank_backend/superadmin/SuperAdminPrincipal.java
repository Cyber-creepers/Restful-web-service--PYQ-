package com.question_bank_backend.superadmin;

import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@NoArgsConstructor
public class SuperAdminPrincipal implements UserDetails {

    SuperAdminEntity superAdminEntity;

    SuperAdminPrincipal(SuperAdminEntity superAdminEntity) {
        this.superAdminEntity = superAdminEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));
    }

    @Override
    public String getPassword() {
        return superAdminEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return superAdminEntity.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return
                UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return superAdminEntity.getOtpverification().getStatus().equals("Verified");
    }
}
