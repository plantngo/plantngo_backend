package me.plantngo.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.plantngo.backend.models.Order;
import me.plantngo.backend.models.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    
    void deleteByProductIdAndOrder(Integer productId, Order order);
    boolean existsByProductIdAndOrder(Integer productId, Order order);
}
