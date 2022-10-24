package me.plantngo.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.plantngo.backend.models.Merchant;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Integer>{

    Optional<Merchant> findByUsername(String username);
    Optional<Merchant> findById(Integer id);
    Optional<Merchant> findByEmail(String email);
    Optional<Merchant> findByCompany(String company);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    
}

