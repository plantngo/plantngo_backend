package me.plantngo.backend.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import me.plantngo.backend.config.jwt.JwtRequestFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    private JwtRequestFilter jwtRequestFilter;
    private UserDetailsService userDetailsService;

    private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**",
            // -- Ordinary endpoints
            "/**/login",
            "/**/register",
            "/**/mailer",
            "/**/forgot-password/**",
            "/"
            // other public endpoints for API may be appended to this array
    };

    @Autowired
    public WebSecurityConfig(JwtRequestFilter jwtRequestFilter, UserDetailsService userDetailsService) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());

        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .httpBasic().and()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()

                // .antMatchers("/**/edit-profile").hasAnyRole("CUSTOMER","MERCHANT")
                //
                // .antMatchers("/**/customer/**").hasAnyRole("ADMIN","CUSTOMER")
                // .antMatchers(HttpMethod.GET,"/**/customer/**").hasAnyRole("ADMIN","MERCHANT")
                // .antMatchers("/**/merchant/**").hasAnyRole("ADMIN","MERCHANT")
                // .antMatchers(HttpMethod.GET,"/**/merchant/**").hasAnyRole("CUSTOMER")
                .anyRequest().authenticated()
                .and()
                .csrf().disable() // CSRF protection is needed only for browser based attacks
                .formLogin().disable()
                .headers().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // Disable the security
                                                                                             // headers, as we do not
                                                                                             // return HTML in our
                                                                                             // service

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        // auto-generate a random salt internally
        return new BCryptPasswordEncoder();
    }
}
