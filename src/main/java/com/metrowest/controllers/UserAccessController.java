package com.metrowest.controllers;

import com.metrowest.entity.Role;
import com.metrowest.entity.UserEntry;
import com.metrowest.repo.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class UserAccessController
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAccessController(UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public String user_signup(Model model,
                              @RequestParam("email") String email,
                              @RequestParam("username") String username,
                              @RequestParam("password") String password)
    {
        if (userRepository.existsByUsername(username))
        {
            model.addAttribute("error", "user: " + username + " already exists");
            return "error";
        }
        if (userRepository.existsByEmail(email))
        {
            model.addAttribute("error", "email: " + email + " already in use by another user");
            return "error";
        }
        if (!EmailValidator.getInstance().isValid(email))
        {
            model.addAttribute("error", "email: " + email + " is invalid");
            return "error";
        }
        if (password == null || password.isEmpty())
        {
            model.addAttribute("error", "password is null or empty");
            return "error";
        }
        if (password.length() < 8)
        {
            model.addAttribute("error", "password is too short, must be at least 8 characters");
            return "error";
        }

        var user = new UserEntry();
        user.setEmail(email);
        user.setUsername(username);
        user.setRole(Role.CUSTOMER);
        user.setPassword_hash(passwordEncoder.encode(password));
        var saved = userRepository.save(user);
        userRepository.flush();

        model.addAttribute("new_user", saved.getUsername());
        return "login";
    }

    @PostMapping(value = "/role_check", produces = MediaType.TEXT_HTML_VALUE)
    public void hello(HttpServletResponse response, Authentication authentication) throws IOException
    {
        var roles = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();

        System.out.println("principal: " + authentication.getPrincipal());

        var redirect = "/";

        if      (roles.contains("ROLE_ADMIN"))      { redirect = "/admin/dashboard"; }
        else if (roles.contains("ROLE_MANAGER"))    { redirect = "/manager/dashboard"; }
        else if (roles.contains("ROLE_TECHNICIAN")) { redirect = "/technician/dashboard"; }
        else if (roles.contains("ROLE_CUSTOMER"))   { redirect = "/customer/dashboard"; }

        response.sendRedirect(redirect);
    }
}
