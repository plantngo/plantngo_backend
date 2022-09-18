package me.plantngo.backend.models;

import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

public abstract class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String username;

    private String email;
    private String name;
    private String password;

}
