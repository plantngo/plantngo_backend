package me.plantngo.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByNameAndMerchant(String name, Merchant merchant);
    Optional<Product> findById(Integer id);
    void deleteByNameAndMerchantAndCategory(String name, Merchant merchant, String category);
    Boolean existsByName(String name);
    Boolean existsByNameAndMerchantAndCategory(String name, Merchant merchant, String category);
    List<Product> findByMerchantUsernameOrderByCarbonEmission(String username);
    List<Product> findByMerchantUsername(String merchantName);
}
