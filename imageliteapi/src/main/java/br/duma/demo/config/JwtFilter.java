package br.duma.demo.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import br.duma.demo.application.jwt.InvalidTokenException;
import br.duma.demo.application.jwt.JwtService;
import br.duma.demo.domain.entity.User;
import br.duma.demo.domain.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {       
        String token = getToken(request);
        if(token != null){
            try{
                jwtService.getEmailFromToken(token);
                String email = jwtService.getEmailFromToken(token);
                var user = userService.getByEmail(email);
                setUserAsAuthenticated(user);
            }catch(InvalidTokenException e){
                log.error("Invalid token: {}", e.getMessage());
            }catch(Exception e){
                log.error("Error while authenticating user: {}", e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }

    private void setUserAsAuthenticated( User user){
        UserDetails  userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles("USER")
                .build();
        UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(userDetails,
                             "", userDetails.getAuthorities()
                        );
        SecurityContextHolder.getContext().setAuthentication( authentication );
    }
        

    private String getToken(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")){
            String[] parts = header.split(" ");
            if(parts.length == 2 && !parts[1].isEmpty()){
                return parts[1];
            }
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.contains("/v1/users") ;
    }

}
