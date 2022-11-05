package me.plantngo.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.plantngo.backend.models.Customer;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.models.Order;
import me.plantngo.backend.models.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findById(Integer id);

    List<Order> findAllByCustomerUsername(String username);

    List<Order> findAllByMerchantUsername(String username);

    List<Order> findAllByMerchantUsernameAndOrderStatus(String username, OrderStatus orderStatus);

    List<Order> findAllByCustomerUsernameAndMerchantUsername(String customerName, String merchantName);

    Order findFirstByCustomerUsernameAndMerchantUsernameAndOrderStatus(String customerName, String merchantName,
            OrderStatus orderStatus);
}
