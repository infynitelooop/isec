package com.infyniteloop.isec.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests ->
                requests.anyRequest().authenticated());
        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {

        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);

        if (!userDetailsManager.userExists("user1")) {
            userDetailsManager.createUser(
                    User.withUsername("user1")
                            .password("{noop}password1") // No password encoder for simplicity
                            .roles("USER")
                            .build()
            );
        }

        if (!userDetailsManager.userExists("admin")) {
            userDetailsManager.createUser(
                    User.withUsername("admin")
                            .password("{noop}adminpass") // No password encoder for simplicity
                            .roles("ADMIN")
                            .build()
            );
        }
        return userDetailsManager;
    }


}
