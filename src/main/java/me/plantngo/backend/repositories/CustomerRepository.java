package me.plantngo.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.plantngo.backend.models.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>{
    
    Optional<Customer> findByUsername(String username);
    Optional<Customer> findById(Integer id);
    Optional<Customer> findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    void deleteByUsername(String username);
}
