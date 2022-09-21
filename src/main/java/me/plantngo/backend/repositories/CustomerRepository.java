package me.plantngo.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

import me.plantngo.backend.models.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>{
    
    public Optional<Customer> findByUsername(String username);
    public Optional<Customer> findByEmail(String email);

}
