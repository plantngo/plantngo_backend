package me.plantngo.backend.config.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import me.plantngo.backend.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private UserDetailsService userDetailsService;

    private JwtProvider jwtProvider;

    @Autowired
    public JwtRequestFilter(UserDetailsService userDetailsService, JwtProvider jwtProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String username = null;
        String jwtToken = jwtProvider.resolveToken(request);

            try {
                // If Token is not null, extract username and get UserDetails
                if(jwtToken != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    username = jwtProvider.extractUsername(jwtToken);
                    System.out.println(username);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // Check the validity of token
                    if (jwtProvider.validateToken(jwtToken,userDetails)){
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken
                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        // After setting the Authentication in the context, we specify that the current user is authenticated. So it passes the Spring Security Configurations successfully.
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            } catch (IllegalArgumentException e) {
                SecurityContextHolder.clearContext();
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                SecurityContextHolder.clearContext();
                System.out.println("JWT Token has expired");
            } catch (UsernameNotFoundException e){
                SecurityContextHolder.clearContext();;
                System.out.println("No such user");
            } catch (MalformedJwtException e){
                SecurityContextHolder.clearContext();
                System.out.println("Header expecting: Authentication: Bearer <JWTtoken>");
            }
        chain.doFilter(request, response);
    }

}
