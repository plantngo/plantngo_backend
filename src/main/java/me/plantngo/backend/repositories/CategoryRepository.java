package me.plantngo.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.plantngo.backend.models.Category;
import me.plantngo.backend.models.Merchant;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    
    Boolean existsByNameAndMerchant(String name, Merchant merchant);
    Boolean existsByName(String name);
    Optional<Category> findByNameAndMerchant(String name, Merchant merchant);
}
