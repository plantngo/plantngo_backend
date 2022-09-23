package me.plantngo.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    
    private UserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
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
        // http
        // .httpBasic()
        //     .and() 
        // .authorizeRequests()
        //     .antMatchers(HttpMethod.GET).permitAll()
        //     .antMatchers(HttpMethod.POST).permitAll()
        //     .and()
        // .csrf().disable() // CSRF protection is needed only for browser based attacks
        // .formLogin().disable()
        // .headers().disable(); // Disable the security headers, as we do not return HTML in our service

        http
        .authorizeRequests()
            .antMatchers(HttpMethod.GET).permitAll()
            .antMatchers(HttpMethod.POST).permitAll()
            .and()
        .csrf().disable()
        .formLogin().disable()
        .headers().disable();

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        // auto-generate a random salt internally
        return new BCryptPasswordEncoder();
    }
}
