package br.com.devdojo.config;

import br.com.devdojo.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

import static br.com.devdojo.config.SecurityConstants.SIGN_UP_URL;

/**
 * Created by Thiago Rodrigues on 29/11/2019
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // Usando basic Auth
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/*/protected/**").hasRole("USER")
//                .antMatchers("/*/admin/**").hasRole("ADMIN")
//                .and()
//                .httpBasic().
//                .and()
//                .csrf().disable();
//    }

    // usando JWTToken Authentication
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())// liberar acesso de aplicação frontend
                .and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, SIGN_UP_URL).permitAll();
        http.authorizeRequests()
                .antMatchers("/*/protected/**").hasRole("USER")
                .antMatchers("/*/admin/**").hasRole("ADMIN")
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), customUserDetailsService));

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    //    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("thiago").password("{noop}123456").roles("USER")
//                .and()
//                .withUser("admin").password("{noop}123456").roles("USER","ADMIN");
//    }
}
