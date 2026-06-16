package com.metrowest.services;

import com.metrowest.repo.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService
{
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException
    {
        return userRepository.findByUsername(username)
            .map(u ->
            {
                var auth = new SimpleGrantedAuthority(u.getRole().role_string());
                return new User(u.getUsername(), u.getPassword_hash(), List.of(auth));
            })
            .orElseGet(()->new User(username, null, Collections.emptyList()));
    }
}
