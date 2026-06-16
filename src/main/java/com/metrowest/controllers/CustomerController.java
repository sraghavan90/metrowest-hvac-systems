package com.metrowest.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
public class CustomerController
{
    @GetMapping("/dashboard")
    public String root()
    {
        return "customer/dashboard";
    }
}
