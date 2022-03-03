package com.csys.pharmacie.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.web.context.request.RequestContextHolder;

@Configuration
@EnableWebSecurity
@Profile("prod")
@EnableRedisHttpSession
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfigProd extends WebSecurityConfigurerAdapter {

    private final Environment env;
    @Value("${http.auth-token-header-name}")
    private String principalRequestHeader;

    @Value("${http.auth-token}")
    private String principalRequestValue;

    public SecurityConfigProd(Environment env) {
        this.env = env;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
         APIKeyAuthFilter filter = new APIKeyAuthFilter(principalRequestHeader);
        
        filter.setAuthenticationManager((Authentication authentication) -> {
            String principal = (String) authentication.getPrincipal();
            if (!principalRequestValue.equals(principal)) {
                throw new BadCredentialsException("The API key was not found or not the expected value.");
            }
            authentication.setAuthenticated(true);
            RequestContextHolder.getRequestAttributes().setAttribute(principalRequestHeader, principalRequestValue, 0);
            return authentication;
        }//
        );
        http.addFilter(filter);
        http.csrf().disable();
        if (!env.acceptsProfiles("unsecure")) {
            http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/reference-price**").permitAll();
            http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/**").authenticated();
            http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/**").authenticated();
            http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/**").authenticated();
            http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/**").permitAll();
        }
    }

    @Bean
    HeaderHttpSessionStrategy sessionStrategy() {
        return new HeaderHttpSessionStrategy();
    }

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        DefaultHttpFirewall firewall = new DefaultHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        return firewall;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.httpFirewall(allowUrlEncodedSlashHttpFirewall());

    }

}
