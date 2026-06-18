package com.metrowest.services;

import com.metrowest.entity.UserEntry;
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

    private static User convert_user(UserEntry user_entry)
    {
        var authority = new SimpleGrantedAuthority(user_entry.getRole().role_string());
        return new User(user_entry.getUsername(), user_entry.getPassword_hash(), List.of(authority));
    }

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException
    {
        return userRepository.findByUsername(username)
            .map(UserService::convert_user)
            .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
