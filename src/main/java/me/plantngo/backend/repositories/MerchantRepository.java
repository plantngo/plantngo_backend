package me.plantngo.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.plantngo.backend.models.Merchant;
import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Integer>{

    Optional<Merchant> findByUsername(String username);
    Optional<Merchant> findById(Integer id);
    Optional<Merchant> findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    
}

