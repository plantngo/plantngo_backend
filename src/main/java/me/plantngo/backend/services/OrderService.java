package me.plantngo.backend.services;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import me.plantngo.backend.models.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.plantngo.backend.DTO.OrderItemDTO;
import me.plantngo.backend.DTO.UpdateOrderDTO;
import me.plantngo.backend.DTO.OrderDTO;
import me.plantngo.backend.DTO.UpdateOrderItemDTO;
import me.plantngo.backend.exceptions.AlreadyExistsException;
import me.plantngo.backend.exceptions.NotExistException;
import me.plantngo.backend.repositories.OrderRepository;
import me.plantngo.backend.repositories.ProductRepository;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    private CustomerService customerService;

    private ProductRepository productRepository;

    private MerchantService merchantService;

    private LogService logService;

    @Autowired
    public OrderService(OrderRepository orderRepository, CustomerService customerService,
            ProductRepository productRepository, MerchantService merchantService,
            LogService logService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.productRepository = productRepository;
        this.merchantService = merchantService;
        this.logService = logService;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByCustomerName(String name) {
        return orderRepository.findAllByCustomerUsername(name);
    }

    public List<Order> getOrdersByMerchantName(String name) {
        return orderRepository.findAllByMerchantUsername(name);
    }

    public List<Order> getPendingOrdersByMerchantName(String name) {
        return orderRepository.findAllByMerchantUsernameAndOrderStatus(name, OrderStatus.PENDING);
    }

    public List<Order> getFulfilledOrdersByMerchantName(String name) {

        return orderRepository.findAllByMerchantUsernameAndOrderStatus(name, OrderStatus.FULFILLED);
    }

    public List<Order> getCancelledOrdersByMerchantName(String name) {

        return orderRepository.findAllByMerchantUsernameAndOrderStatus(name, OrderStatus.CANCELLED);
    }

    public Order addOrder(OrderDTO placeOrderDTO, String customerName) {
        // Check if customer exists
        Customer customer = customerService.getCustomerByUsername(customerName);

        // Check if merchant exists
        Merchant merchant = merchantService.getMerchantByUsername(placeOrderDTO.getMerchantName());

        // Create Order
        Order order = this.orderMapToEntity(placeOrderDTO, customer, merchant);

        Set<OrderItem> orderItems = new HashSet<>();

        for (OrderItemDTO orderItemDTO : placeOrderDTO.getOrderItems()) {
            OrderItem orderItem = this.orderItemMapToEntity(orderItemDTO, order);
            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(this.getTotalPrice(orderItems));

        orderRepository.save(order);

        return order;
    }

    public Order addOrderItem(String customerName, Integer orderId, OrderItemDTO orderItemDTO) {

        // find existing order
        Order order = orderRepository.findById(orderId).get();

        Set<OrderItem> orderItems = order.getOrderItems();
        OrderItem orderItem = this.orderItemMapToEntity(orderItemDTO, order);
        orderItems.add(orderItem);

        order.setOrderItems(orderItems);
        order.setTotalPrice(this.getTotalPrice(orderItems));

        orderRepository.save(order);

        return order;
    }

    public Order updateOrder(UpdateOrderDTO updateOrderDTO, Integer orderId) {
        // Check if order exists
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotExistException("Order"));

        // Update Order
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setSkipNullEnabled(true);
        mapper.map(updateOrderDTO, order);

        /*
         * to log a fulfilled order
         */
        if (order.getOrderStatus() == OrderStatus.FULFILLED)
            logService.addLog(order.getCustomer().getUsername(), "order");

        orderRepository.save(order);
        // Update OrderItems in order
        Set<UpdateOrderItemDTO> updateOrderItemDTOs = updateOrderDTO.getUpdateOrderItemDTOs();
        Set<OrderItem> orderItems = order.getOrderItems();

        for (UpdateOrderItemDTO updateOrderItemDTO : updateOrderItemDTOs) {
            OrderItemDTO orderItemDTO = mapper.map(updateOrderItemDTO, OrderItemDTO.class);
            OrderItem orderItem = this.orderItemMapToEntity(orderItemDTO, order);
            orderItems.removeIf(x -> x.getProductId() == orderItem.getProductId());
            if (orderItem.getQuantity() > 0) {
                orderItems.add(orderItem);
            }

        }
        order.setTotalPrice(this.getTotalPrice(orderItems));
        order.setOrderItems(orderItems);
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

        Set<OrderItem> orderItems = order.getOrderItems();
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

    private Double getTotalPrice(Set<OrderItem> orderItems) {
        Double totalPrice = 0.0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getPrice();
        }
        return totalPrice;
        // double result = Double.valueOf(totalPrice);
        // result = Math.round(result * 100) / 100;

        // DecimalFormat df = new DecimalFormat("#.##");
        // df.setRoundingMode(RoundingMode.CEILING);

        // return Double.valueOf(df.format(result));
    }

    // private OrderItem getOrderItemFromDTO(PlaceOrderDTO placeOrderDTO, Order
    // order) {

    // // Check if product exists
    // Optional<Product> tempProduct =
    // productRepository.findById(placeOrderDTO.getProductId());
    // if (tempProduct.isEmpty()) {
    // throw new NotExistException("Product");
    // }
    // Product product = tempProduct.get();

    // OrderItem orderItem = new OrderItem();

    // orderItem.setProduct(product);
    // orderItem.setOrder(order);
    // orderItem.setQuantity(placeOrderDTO.getQuantity());
    // orderItem.setPrice(product.getPrice() * placeOrderDTO.getQuantity());
    // orderItem.setProductId(product.getId());

    // return orderItem;
    // }

    // private Order getOrderFromDTO(PlaceOrderDTO placeOrderDTO, Customer customer,
    // Merchant merchant, Integer orderId) {
    // Optional<Order> tempOrder = orderRepository.findById(orderId);
    // Order order = null;
    // if (tempOrder.isEmpty()) {
    // order = createNewOrderFromDTO(placeOrderDTO, customer, merchant);
    // } else {
    // order = tempOrder.get();
    // }

    // return order;
    // }

    private Order orderMapToEntity(OrderDTO placeOrderDTO, Customer customer, Merchant merchant) {

        ModelMapper mapper = new ModelMapper();

        Order order = mapper.map(placeOrderDTO, Order.class);
        order.setCustomer(customer);
        order.setMerchant(merchant);

        return order;
    }

    private OrderItem orderItemMapToEntity(OrderItemDTO orderItemDTO, Order order) {

        // Check if product exists
        Product product = productRepository.findById(orderItemDTO.getProductId())
                .orElseThrow(() -> new NotExistException("Product"));

        OrderItem orderItem = new OrderItem();

        orderItem.setProduct(product);
        orderItem.setOrder(order);
        orderItem.setQuantity(orderItemDTO.getQuantity());
        orderItem.setPrice(product.getPrice() * orderItemDTO.getQuantity());
        orderItem.setProductId(product.getId());

        return orderItem;
    }

    public List<Order> getOrdersByCustomerNameAndMerchantName(String customerName, String merchantName) {
        return orderRepository.findAllByCustomerUsernameAndMerchantUsername(customerName, merchantName);
    }

    public Order getOrdersByCustomerNameAndMerchantNameAndOrderStatus(String customerName, String merchantName,
            OrderStatus orderStatus) {
        return orderRepository.findFirstByCustomerUsernameAndMerchantUsernameAndOrderStatus(customerName, merchantName,
                orderStatus);
    }

}
