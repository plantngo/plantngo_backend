package me.plantngo.backend.security;

import me.plantngo.backend.jwt.JwtProvider;
import me.plantngo.backend.jwt.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenFilterConfg extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {


  private JwtRequestFilter jwtRequestFilter;

  @Autowired
  public JwtTokenFilterConfg(JwtRequestFilter jwtRequestFilter) {
    this.jwtRequestFilter = jwtRequestFilter;
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
  }

}
