package org.itrunner.heroes.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
    private Set<String> authorities;
}
