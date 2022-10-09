package me.plantngo.backend.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.plantngo.backend.DTO.PlaceOrderDTO;
import me.plantngo.backend.exceptions.AlreadyExistsException;
import me.plantngo.backend.exceptions.NotExistException;
import me.plantngo.backend.models.Customer;
import me.plantngo.backend.models.Order;
import me.plantngo.backend.models.OrderItem;
import me.plantngo.backend.models.Product;
import me.plantngo.backend.repositories.OrderItemRepository;
import me.plantngo.backend.repositories.OrderRepository;
import me.plantngo.backend.repositories.ProductRepository;

@Service
public class OrderService {
    
    private OrderRepository orderRepository;

    private CustomerService customerService;

    private ProductRepository productRepository;

    private OrderItemRepository orderItemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, CustomerService customerService, ProductRepository productRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public List<Order> getOrdersByCustomerName(String name) {
        Customer customer = customerService.getCustomerByUsername(name);
        return orderRepository.findAllByCustomer(customer);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order addOrderItemToOrder(PlaceOrderDTO placeOrderDTO, Integer orderId) {
        // Check if customer exists
        Customer customer = customerService.getCustomerByUsername(placeOrderDTO.getCustomerName());

        // Check if order exists, create if doesn't
        Order order = this.getOrderFromDTO(placeOrderDTO, customer, orderId);

        List<OrderItem> orderItems = order.getOrderItems();
        OrderItem newItem = this.getOrderItemFromDTO(placeOrderDTO, order);

        // Check if order item exists in current order
        for (OrderItem o : orderItems) {
            if (o.getProduct().equals(newItem.getProduct())) {
                throw new AlreadyExistsException();
            }
        }

        orderItems.add(newItem);
        order.setOrderItems(orderItems);
        order.setTotalPrice(this.getTotalPrice(orderItems));

        orderRepository.save(order);

        return order;
    }

    public Order updateOrderItemInOrder(PlaceOrderDTO placeOrderDTO, Integer orderId) {
        // Get Customer
        Customer customer = customerService.getCustomerByUsername(placeOrderDTO.getCustomerName());

        // Check if order exists
        Order order = this.getOrderFromDTO(placeOrderDTO, customer, orderId);
        List<OrderItem> orderItems = order.getOrderItems();

        // Get Order Item
        OrderItem orderItem = this.getOrderItemFromDTO(placeOrderDTO, order);

        for (OrderItem o : orderItems) {
            if (o.getProductId().equals(orderItem.getProductId())) {
                o.setPrice(orderItem.getPrice());
                o.setQuantity(orderItem.getQuantity());
            }
        }

        // Save updated order
        order.setOrderItems(orderItems);
        order.setTotalPrice(this.getTotalPrice(orderItems));

        orderRepository.save(order);

        return order;
    }

    public void deleteOrder(Integer orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new NotExistException();
        }
        orderRepository.deleteById(orderId);
    }

    public void deleteOrderItem(Integer orderId, Integer productId) {
        if (!orderRepository.existsById(orderId)) {
            throw new NotExistException();
        }
        Order order = orderRepository.findById(orderId).get();

        List<OrderItem> orderItems = order.getOrderItems();
        Iterator<OrderItem> itr = orderItems.iterator();

        while (itr.hasNext()) {
            OrderItem orderItem = itr.next();
            if (orderItem.getProductId().equals(productId)) {
                itr.remove();
                order.setOrderItems(orderItems);
                orderRepository.save(order);
                return;
            }
        }

        throw new NotExistException();
    }

    private Double getTotalPrice(List<OrderItem> orderItems) {
        Double totalPrice = 0.0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getPrice();
        }
        return totalPrice;
    }

    private OrderItem getOrderItemFromDTO(PlaceOrderDTO placeOrderDTO, Order order) {

        // Check if product exists
        Optional<Product> tempProduct = productRepository.findById(placeOrderDTO.getProductId());
        if (tempProduct.isEmpty()) {
            throw new NotExistException();
        }
        Product product = tempProduct.get();

        OrderItem orderItem = new OrderItem();

        orderItem.setProduct(product);
        orderItem.setOrder(order);
        orderItem.setQuantity(placeOrderDTO.getQuantity());
        orderItem.setPrice(product.getPrice() * placeOrderDTO.getQuantity());
        orderItem.setProductId(product.getId());

        return orderItem;
    }

    private Order getOrderFromDTO(PlaceOrderDTO placeOrderDTO, Customer customer, Integer orderId) {
        Optional<Order> tempOrder = orderRepository.findById(orderId);
        Order order = null;
        if (tempOrder.isEmpty()) {
            order = new Order();
            order.setCustomer(customer);
            order.setCustomer_Id(customer.getId());
            order.setOrderItems(new ArrayList<OrderItem>());
            order.setTotalPrice(0.0);
        } else {
            order = tempOrder.get();
        }

        return order;
    }
}
