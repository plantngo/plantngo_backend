package me.plantngo.backend.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(path = "/")
@ApiIgnore
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HomeController {
    @GetMapping
    public String homePage() {
        return "Health Check Endpoint";
    }

}
