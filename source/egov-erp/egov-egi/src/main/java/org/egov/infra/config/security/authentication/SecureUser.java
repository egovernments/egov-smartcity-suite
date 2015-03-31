package org.egov.infra.config.security.authentication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.egov.infra.admin.master.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class SecureUser implements UserDetails {
    private static final long serialVersionUID = -8756608845278722035L;
    private final User user;
    private final List<SimpleGrantedAuthority> authorities = new ArrayList<>();

    public SecureUser(User user) {
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        } else {
            this.user = user;
            user.getRoles().forEach((role) -> {
                this.authorities.add(new SimpleGrantedAuthority(role.getName()));
            });
        }
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public boolean isAccountNonExpired() {
        return this.user.getPwdExpiryDate().isAfterNow();
    }

    public boolean isAccountNonLocked() {
        return this.user.isActive();
    }

    public boolean isCredentialsNonExpired() {
        return this.user.getPwdExpiryDate().isAfterNow();
    }

    public boolean isEnabled() {
        return this.user.isActive();
    }

    public String getPassword() {
        return this.user.getPassword();
    }

    public String getUsername() {
        return this.user.getUsername();
    }
    
    public Long getUserId() {
        return this.user.getId();
    }
}
