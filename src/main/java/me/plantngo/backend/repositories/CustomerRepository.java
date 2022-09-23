package me.plantngo.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

import me.plantngo.backend.models.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>{
    
    public Optional<Customer> findByUsername(String username);
    public Optional<Customer> findById(Integer id);
    public Optional<Customer> findByEmail(String email);
    public boolean existsByEmail(String email);
    public boolean existsByUsername(String username);

}
