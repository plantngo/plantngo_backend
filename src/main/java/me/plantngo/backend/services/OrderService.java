package me.plantngo.backend.services;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import me.plantngo.backend.models.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.plantngo.backend.DTO.OrderItemDTO;
import me.plantngo.backend.DTO.UpdateOrderDTO;
import me.plantngo.backend.DTO.OrderDTO;
import me.plantngo.backend.DTO.UpdateOrderItemDTO;
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

    private static final String ORDER_STRING = "Order";

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

    /**
     * Gets all orders
     * 
     * @return
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Gets all orders with given Customer's name
     * 
     * @param name
     * @return
     */
    public List<Order> getOrdersByCustomerName(String name) {
        return orderRepository.findAllByCustomerUsername(name);
    }

    /**
     * Gets all orders with given Merchant's name
     * 
     * @param name
     * @return
     */
    public List<Order> getOrdersByMerchantName(String name) {
        return orderRepository.findAllByMerchantUsername(name);
    }

    /**
     * Gets all Pending orders with given Merchant's name
     * 
     * @param name
     * @return
     */
    public List<Order> getPendingOrdersByMerchantName(String name) {
        return orderRepository.findAllByMerchantUsernameAndOrderStatus(name, OrderStatus.PENDING);
    }

    /**
     * Gets all Fulfilled orders with given Merchant's name
     * 
     * @param name
     * @return
     */
    public List<Order> getFulfilledOrdersByMerchantName(String name) {

        return orderRepository.findAllByMerchantUsernameAndOrderStatus(name, OrderStatus.FULFILLED);
    }

    /**
     * Gets all Cancelled orders with given Merchant's name
     * 
     * @param name
     * @return
     */
    public List<Order> getCancelledOrdersByMerchantName(String name) {

        return orderRepository.findAllByMerchantUsernameAndOrderStatus(name, OrderStatus.CANCELLED);
    }

    /**
     * Gets all orders for a given Customer and Merchant
     * 
     * @param customerName
     * @param merchantName
     * @return
     */
    public List<Order> getOrdersByCustomerNameAndMerchantName(String customerName, String merchantName) {
        return orderRepository.findAllByCustomerUsernameAndMerchantUsername(customerName, merchantName);
    }

    /**
     * Gets all orders for a given Customer and Merchant with a specified Order Status
     * 
     * @param customerName
     * @param merchantName
     * @param orderStatus
     * @return
     */
    public Order getOrdersByCustomerNameAndMerchantNameAndOrderStatus(String customerName, String merchantName,
            OrderStatus orderStatus) {
        return orderRepository.findFirstByCustomerUsernameAndMerchantUsernameAndOrderStatus(customerName, merchantName,
                orderStatus);
    }

    /**
     * Gets all orders for a given Customer with a specified Order Status
     * 
     * @param customerName
     * @param orderStatus
     * @return
     */
    public List<Order> getOrdersByCustomerNameAndOrderStatus(String customerName, OrderStatus orderStatus) {

        return orderRepository.findAllByCustomerUsernameAndOrderStatus(customerName, orderStatus);
    }

    /**
     * Gets all Pending and Fulfilled orders for a given Customer
     * 
     * @param customerName
     * @return
     */
    public List<Order> getAllPendingAndFulfilledOrdersByCustomer(String customerName) {
        List<Order> tmpList = orderRepository.findAllByCustomerUsernameAndOrderStatus(customerName,
                OrderStatus.PENDING);
        tmpList.addAll(orderRepository.findAllByCustomerUsernameAndOrderStatus(customerName, OrderStatus.FULFILLED));
        return tmpList;
    }

    /**
     * Adds a new order for a Customer
     * 
     * @param placeOrderDTO
     * @param customerName
     * @return
     */
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

        Order response = orderRepository.save(order);

        return response;
    }

    /**
     * Adds a new Order Item to an existing Order for a Customer
     * 
     * @param customerName
     * @param orderId
     * @param orderItemDTO
     * @return
     */
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

    /**
     * Updates an existing Order given it's id
     * 
     * @param updateOrderDTO
     * @param orderId
     * @return
     */
    public Order updateOrder(UpdateOrderDTO updateOrderDTO, Integer orderId) {
        // Check if order exists
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotExistException(ORDER_STRING));

        // Update Order
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setSkipNullEnabled(true);
        mapper.map(updateOrderDTO, order);

        /*
         * to log a fulfilled order
         */
        if (order.getOrderStatus() == OrderStatus.FULFILLED) {
            logService.addLog(order.getCustomer().getUsername(), "order");
        }

        // Update OrderItems in order
        Set<UpdateOrderItemDTO> updateOrderItemDTOs = updateOrderDTO.getUpdateOrderItemDTOs();
        if (updateOrderItemDTOs == null) {
            updateOrderItemDTOs = new HashSet<>();
        }
        Set<OrderItem> orderItems = order.getOrderItems();

        for (UpdateOrderItemDTO updateOrderItemDTO : updateOrderItemDTOs) {
            OrderItemDTO orderItemDTO = mapper.map(updateOrderItemDTO, OrderItemDTO.class);
            OrderItem orderItem = this.orderItemMapToEntity(orderItemDTO, order);
            orderItems.removeIf(x -> x.getProductId() == orderItem.getProductId());
            if (orderItem.getQuantity() > 0) {
                orderItems.add(orderItem);
            }

        }
        if (orderItems.size() > 0) {
            order.setTotalPrice(this.getTotalPrice(orderItems));
            order.setOrderItems(orderItems);
            orderRepository.save(order);
        } else {
            orderRepository.delete(order);
        }

        return order;
    }

    public void deleteOrder(Integer orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new NotExistException(ORDER_STRING);
        }
        orderRepository.deleteById(orderId);
    }

    public void deleteOrderItem(Integer orderId, Integer productId) {
        if (!orderRepository.existsById(orderId)) {
            throw new NotExistException(ORDER_STRING);
        }
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotExistException(ORDER_STRING));

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

    /*
     * 
     * Helper Methods
     * 
     */

    private Double getTotalPrice(Set<OrderItem> orderItems) {
        Double totalPrice = 0.0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getPrice();
        }
        return totalPrice;
    }

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

}
