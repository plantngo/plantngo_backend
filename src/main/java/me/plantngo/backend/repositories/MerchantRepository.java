package me.plantngo.backend.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.plantngo.backend.models.Merchant;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Integer>{
    
}

