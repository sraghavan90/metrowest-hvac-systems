package com.metrowest.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicController
{
    @GetMapping("/")
    public String root() { return "home"; }

    @GetMapping("/about")
    public String about() { return "about"; }

    @GetMapping("/login")
    public String login()
    {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() { return "signup"; }
}
