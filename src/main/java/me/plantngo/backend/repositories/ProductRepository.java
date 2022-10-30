package me.plantngo.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.plantngo.backend.models.Category;
import me.plantngo.backend.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByNameAndCategory(String name, Category category);
    Optional<Product> findByNameAndCategoryMerchantUsername(String name, String merchantName);
    Optional<Product> findById(Integer id);
    Optional<Product> findByName(String name);
    Boolean existsByName(String name);
    Boolean existsByNameAndCategoryNameAndCategoryMerchantUsername(String name, String categoryName, String merchantName);
    void deleteByNameAndCategoryNameAndCategoryMerchantUsername(String name, String categoryName, String merchantName);
    List<Product> findByCategoryMerchantUsernameOrderByCarbonEmission(String username);
}
