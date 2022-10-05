package me.plantngo.backend.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(path = "api/v1/customer")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrderController {
    
}
