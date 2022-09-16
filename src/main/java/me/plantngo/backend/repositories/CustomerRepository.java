package me.plantngo.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.plantngo.backend.models.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>{
    
}
