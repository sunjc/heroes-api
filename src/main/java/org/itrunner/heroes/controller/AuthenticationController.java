package org.itrunner.heroes.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.itrunner.heroes.dto.AuthenticationRequest;
import org.itrunner.heroes.dto.AuthenticationResponse;
import org.itrunner.heroes.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = {"Authentication Controller"})
@Slf4j
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping
    public AuthenticationResponse login(@RequestBody @Valid AuthenticationRequest request) {
        // Perform the security
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate token
        String token = jwtService.generate((UserDetails) authentication.getPrincipal());

        // Return the token
        return new AuthenticationResponse(token);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void handleAuthenticationException(AuthenticationException exception) {
        log.error(exception.getMessage(), exception);
    }
}