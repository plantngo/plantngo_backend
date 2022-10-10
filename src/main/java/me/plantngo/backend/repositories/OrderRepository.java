package me.plantngo.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.plantngo.backend.models.Customer;
import me.plantngo.backend.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findById(Integer id);
    List<Order> findAllByCustomer(Customer customer);
}
