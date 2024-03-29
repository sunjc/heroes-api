package org.itrunner.heroes.util;

import org.itrunner.heroes.domain.Authority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class AuthorityUtils {

    private AuthorityUtils() {
    }

    public static List<GrantedAuthority> toGrantedAuthorities(List<Authority> authorities) {
        return authorities.stream().map(authority -> new SimpleGrantedAuthority(authority.getName().name())).collect(Collectors.toList());
    }

    public static List<GrantedAuthority> toGrantedAuthorities(String... authorities) {
        return Stream.of(authorities).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public static String[] getAuthorities(UserDetails user) {
        return user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
    }
}