package me.plantngo.backend.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.plantngo.backend.DTO.PlaceOrderDTO;
import me.plantngo.backend.DTO.UpdateOrderItemDTO;
import me.plantngo.backend.exceptions.AlreadyExistsException;
import me.plantngo.backend.exceptions.NotExistException;
import me.plantngo.backend.models.Customer;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.models.Order;
import me.plantngo.backend.models.OrderItem;
import me.plantngo.backend.models.Product;
import me.plantngo.backend.repositories.OrderRepository;
import me.plantngo.backend.repositories.ProductRepository;

@Service
public class OrderService {
    
    private OrderRepository orderRepository;

    private CustomerService customerService;

    private ProductRepository productRepository;

    private MerchantService merchantService;

    @Autowired
    public OrderService(OrderRepository orderRepository, CustomerService customerService, ProductRepository productRepository, MerchantService merchantService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.productRepository = productRepository;
        this.merchantService = merchantService;
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

        // Check if merchant exists
        Merchant merchant = merchantService.getMerchantByUsername(placeOrderDTO.getMerchantName());

        // Check if order exists, create if doesn't
        Order order = this.getOrderFromDTO(placeOrderDTO, customer, merchant, orderId);

        List<OrderItem> orderItems = order.getOrderItems();
        OrderItem newItem = this.getOrderItemFromDTO(placeOrderDTO, order);

        // Check if order item exists in current order
        if (orderItems.contains(newItem)) {
            throw new AlreadyExistsException("Order Item");
        }

        orderItems.add(newItem);
        order.setOrderItems(orderItems);
        order.setTotalPrice(this.getTotalPrice(orderItems));

        orderRepository.save(order);

        return order;
    }

    public Order updateOrderItemInOrder(@Valid UpdateOrderItemDTO updateOrderItemDTO, Integer orderId) {
        // Check if order exists
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotExistException("Order"));
        List<OrderItem> orderItems = order.getOrderItems();

        for (OrderItem o : orderItems) {
            if (o.getProductId().equals(updateOrderItemDTO.getProductId())) {

                if (updateOrderItemDTO.getQuantity() != null) {
                    o.setPrice(o.getPrice() / o.getQuantity() * updateOrderItemDTO.getQuantity());
                    o.setQuantity(updateOrderItemDTO.getQuantity());
                }

                break;
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
            throw new NotExistException("Order");
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

        throw new NotExistException("Order Item");
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
            throw new NotExistException("Product");
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

    private Order getOrderFromDTO(PlaceOrderDTO placeOrderDTO, Customer customer, Merchant merchant, Integer orderId) {
        Optional<Order> tempOrder = orderRepository.findById(orderId);
        Order order = null;
        if (tempOrder.isEmpty()) {
            order = createNewOrderFromDTO(placeOrderDTO, customer, merchant);
        } else {
            order = tempOrder.get();
        }

        return order;
    }

    private Order createNewOrderFromDTO(PlaceOrderDTO placeOrderDTO, Customer customer, Merchant merchant) {
        Order order = new Order();
        order.setIsDineIn(placeOrderDTO.getIsDineIn());
        order.setCustomer(customer);
        order.setMerchant(merchant);
        order.setCustomer_Id(customer.getId());
        order.setOrderItems(new ArrayList<OrderItem>());
        order.setTotalPrice(0.0);

        return order;
    }
}
