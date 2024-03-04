package org.example.config;

import lombok.RequiredArgsConstructor;
import org.example.mediumModels.UserMedium;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserMedium userMedium = jdbcTemplate.queryForObject("select * from users where email=? limit 1",
                new BeanPropertyRowMapper<>(UserMedium.class), email);


        Long roleId = jdbcTemplate.queryForObject("select role_id from users_roles where usermedium_id=? limit 1", Long.class, userMedium.getId());

        String roleName = jdbcTemplate.queryForObject("select name from roles where id=? limit 1", String.class, roleId);

        return User.builder().username(userMedium.getEmail())
                .password(userMedium.getPassword())
                .roles(roleName.toUpperCase()).build();
    }
}