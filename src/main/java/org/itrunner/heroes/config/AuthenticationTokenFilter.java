package org.itrunner.heroes.config;

import lombok.extern.slf4j.Slf4j;
import org.itrunner.heroes.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authToken = request.getHeader(securityProperties.getJwt().getHeader());

        if (authToken != null && authToken.startsWith("Bearer ")) {
            authToken = authToken.substring(7);

            try {
                UserDetails user = jwtService.verify(authToken);

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    logger.info("checking authentication for user " + user.getUsername());

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), "N/A", user.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                logger.error(e);
                request.setAttribute("exception", e);
            }
        }

        chain.doFilter(request, response);
    }
}