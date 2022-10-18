package me.plantngo.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.plantngo.backend.models.Ingredient;
import me.plantngo.backend.models.Product;
import me.plantngo.backend.models.ProductIngredient;

@Repository
public interface ProductIngredientRepository extends JpaRepository<ProductIngredient, Integer> {
    
    Optional<ProductIngredient> findByIngredientAndProduct(Ingredient ingredient, Product product);
    boolean existsByIngredientAndProduct(Ingredient ingredient, Product product);
}
