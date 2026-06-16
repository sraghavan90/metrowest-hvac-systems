package com.metrowest.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
public class LoginController
{
    @GetMapping("/login")
    public String login()
    {
        return "login";
    }

    @PostMapping(value = "/role_check", produces = MediaType.TEXT_HTML_VALUE)
    public void hello(HttpServletResponse response, Authentication authentication) throws IOException
    {
        var roles = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();

        var redirect = "/";

        if      (roles.contains("ROLE_ADMIN"))      { redirect = "/admin/dashboard"; }
        else if (roles.contains("ROLE_MANAGER"))    { redirect = "/manager/dashboard"; }
        else if (roles.contains("ROLE_TECHNICIAN")) { redirect = "/technician/dashboard"; }
        else if (roles.contains("ROLE_CUSTOMER"))   { redirect = "/customer/dashboard"; }

        response.sendRedirect(redirect);
    }
}
