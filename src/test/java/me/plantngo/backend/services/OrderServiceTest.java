package me.plantngo.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import me.plantngo.backend.DTO.OrderDTO;
import me.plantngo.backend.DTO.OrderItemDTO;
import me.plantngo.backend.DTO.UpdateOrderDTO;
import me.plantngo.backend.DTO.UpdateOrderItemDTO;
import me.plantngo.backend.exceptions.NotExistException;
import me.plantngo.backend.models.Customer;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.models.Order;
import me.plantngo.backend.models.OrderItem;
import me.plantngo.backend.models.OrderStatus;
import me.plantngo.backend.models.Product;
import me.plantngo.backend.repositories.OrderRepository;
import me.plantngo.backend.repositories.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private MerchantService merchantService;

    @InjectMocks
    private OrderService orderService;

    private List<Order> orders;

    private Merchant merchant;

    private Customer customer;

    private Order order;

    private Product product;

    private OrderItem orderItem;

    @BeforeEach
    public void initEach() {

        orders = new ArrayList<>();

        customer = new Customer();
        customer.setUsername("Daniel");

        merchant = new Merchant();
        merchant.setUsername("Annabelle");

        order = new Order();
        order.setId(1);
        order.setOrderStatus(OrderStatus.FULFILLED);
        order.setCustomer(customer);
        order.setMerchant(merchant);
        orders.add(order);

        order = new Order();
        order.setId(2);
        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setCustomer(customer);
        order.setMerchant(merchant);
        orders.add(order);

        product = new Product(1, "Laksa", 6.9, null, null, null, null, null, null, null);

        orderItem = new OrderItem(null, 1, 2, 13.8, order, product);

        Set<OrderItem> orderItems = new HashSet<>();
        orderItems.add(orderItem);
        
        order = new Order();
        order.setId(3);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setCustomer(customer);
        order.setMerchant(merchant);
        order.setOrderItems(orderItems);
        order.setIsDineIn(false);
        order.setTotalPrice(13.8);
        orders.add(order);

    }

    @Test
    public void testGetAllOrders_AllOrders_ReturnAllOrders() {

        // Arrange
        List<Order> expectedOrders = orders;

        when(orderRepository.findAll())
            .thenReturn(orders);
        
        // Act
        List<Order> responseOrders = orderService.getAllOrders();

        // Assert
        assertEquals(expectedOrders, responseOrders);
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllOrders_NoOrders_ReturnEmptyList() {

        // Arrange
        orders = new ArrayList<>();
        List<Order> expectedOrders = new ArrayList<>();

        when(orderRepository.findAll())
            .thenReturn(orders);

        // Act
        List<Order> responseOrders = orderService.getAllOrders();

        // Assert
        assertEquals(expectedOrders, responseOrders);
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    public void testGetOrdersByCustomerName_ValidCustomerName_ReturnAllOrdersByCustomer() {

        // Arrange
        List<Order> expectedOrders = orders;
        String customerName = "Daniel";

        when(orderRepository.findAllByCustomerUsername(any(String.class)))
            .thenReturn(orders);
        
        // Act
        List<Order> responseOrders = orderService.getOrdersByCustomerName(customerName);

        // Assert
        assertEquals(expectedOrders, responseOrders);
        verify(orderRepository, times(1)).findAllByCustomerUsername(customerName);
    }

    @Test
    public void testGetOrdersByCustomerName_InvalidCustomerName_ReturnEmptyList() {

        // Arrange
        List<Order> expectedOrders = new ArrayList<>();
        String customerName = "Jacky";

        when(orderRepository.findAllByCustomerUsername(any(String.class)))
            .thenReturn(expectedOrders);
        
        // Act
        List<Order> responseOrders = orderService.getOrdersByCustomerName(customerName);

        // Assert
        assertEquals(expectedOrders, responseOrders);
        verify(orderRepository, times(1)).findAllByCustomerUsername(customerName);
    }

    @Test
    public void testGetOrdersByMerchantName_ValidMerchantName_ReturnAllOrdersByMerchant() {

        // Arrange
        List<Order> expectedOrders = orders;
        String merchantName = "Annabelle";

        when(orderRepository.findAllByMerchantUsername(any(String.class)))
            .thenReturn(orders);
        
        // Act
        List<Order> responseOrders = orderService.getOrdersByMerchantName(merchantName);

        // Assert
        assertEquals(expectedOrders, responseOrders);
        verify(orderRepository, times(1)).findAllByMerchantUsername(merchantName);
    }

    @Test
    public void testGetOrdersByMerchantName_InvalidMerchantName_ReturnEmptyList() {

        // Arrange
        List<Order> expectedOrders = new ArrayList<>();
        String merchantName = "Jacky";

        when(orderRepository.findAllByMerchantUsername(any(String.class)))
            .thenReturn(expectedOrders);
        
        // Act
        List<Order> responseOrders = orderService.getOrdersByMerchantName(merchantName);

        // Assert
        assertEquals(expectedOrders, responseOrders);
        verify(orderRepository, times(1)).findAllByMerchantUsername(merchantName);
    }

    @Test
    public void testGetPendingOrdersByMerchantName_ValidMerchantName_ReturnAllPendingOrders() {

        // Arrange
        List<Order> expectedOrders = orders;
        Iterator<Order> iter = expectedOrders.iterator();
        while(iter.hasNext()) {
            Order o = iter.next();
            if (o.getOrderStatus().equals(OrderStatus.CANCELLED) || o.getOrderStatus().equals(OrderStatus.FULFILLED)) {
                iter.remove();
            }
        }
        String merchantName = "Annabelle";

        when(orderRepository.findAllByMerchantUsernameAndOrderStatus(any(String.class), any(OrderStatus.class)))
            .thenReturn(expectedOrders);
        
        // Act
        List<Order> responseOrders = orderService.getPendingOrdersByMerchantName(merchantName);

        // Assert
        assertEquals(expectedOrders, responseOrders);
        verify(orderRepository, times(1)).findAllByMerchantUsernameAndOrderStatus(merchantName, OrderStatus.PENDING);
    }

    @Test
    public void testGetPendingOrdersByMerchantName_InvalidMerchantName_ReturnEmptyList() {

        // Arrange
        List<Order> expectedOrders = new ArrayList<>();
        String merchantName = "Jacky";

        when(orderRepository.findAllByMerchantUsernameAndOrderStatus(any(String.class), any(OrderStatus.class)))
            .thenReturn(expectedOrders);
        
        // Act
        List<Order> responseOrders = orderService.getPendingOrdersByMerchantName(merchantName);

        // Assert
        assertEquals(expectedOrders, responseOrders);
        verify(orderRepository, times(1)).findAllByMerchantUsernameAndOrderStatus(merchantName, OrderStatus.PENDING);
    }

    @Test
    public void testGetFulfilledOrdersByMerchantName_ValidMerchantName_ReturnAllFulfilledOrders() {

        // Arrange
        List<Order> expectedOrders = orders;
        Iterator<Order> iter = expectedOrders.iterator();
        while(iter.hasNext()) {
            Order o = iter.next();
            if (o.getOrderStatus().equals(OrderStatus.CANCELLED) || o.getOrderStatus().equals(OrderStatus.PENDING)) {
                iter.remove();
            }
        }
        String merchantName = "Annabelle";

        when(orderRepository.findAllByMerchantUsernameAndOrderStatus(any(String.class), any(OrderStatus.class)))
            .thenReturn(expectedOrders);
        
        // Act
        List<Order> responseOrders = orderService.getFulfilledOrdersByMerchantName(merchantName);

        // Assert
        assertEquals(expectedOrders, responseOrders);
        verify(orderRepository, times(1)).findAllByMerchantUsernameAndOrderStatus(merchantName, OrderStatus.FULFILLED);
    }

    @Test
    public void testGetCancelledOrdersByMerchantName_ValidMerchantName_ReturnAllCancelledOrders() {

        // Arrange
        List<Order> expectedOrders = orders;
        Iterator<Order> iter = expectedOrders.iterator();
        while(iter.hasNext()) {
            Order o = iter.next();
            if (o.getOrderStatus().equals(OrderStatus.FULFILLED) || o.getOrderStatus().equals(OrderStatus.PENDING)) {
                iter.remove();
            }
        }
        String merchantName = "Annabelle";

        when(orderRepository.findAllByMerchantUsernameAndOrderStatus(any(String.class), any(OrderStatus.class)))
            .thenReturn(expectedOrders);
        
        // Act
        List<Order> responseOrders = orderService.getCancelledOrdersByMerchantName(merchantName);

        // Assert
        assertEquals(expectedOrders, responseOrders);
        verify(orderRepository, times(1)).findAllByMerchantUsernameAndOrderStatus(merchantName, OrderStatus.CANCELLED);
    }

    @Test
    public void testAddOrder_ValidOrder_ReturnOrder() {

        // Arrange
        order.setId(null);
        OrderItemDTO orderItemDTO = new OrderItemDTO(1, 2);
        OrderDTO orderDTO = new OrderDTO(false, merchant.getUsername(), OrderStatus.PENDING, List.of(orderItemDTO), null);
        Order expectedOrder = order;
        String customerName = "Daniel";
        String merchantName = "Annabelle";

        when(customerService.getCustomerByUsername(any(String.class)))
            .thenReturn(customer);
        when(merchantService.getMerchantByUsername(any(String.class)))
            .thenReturn(merchant);
        when(productRepository.findById(any(Integer.class)))
            .thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class)))
            .thenReturn(expectedOrder);

        // Act
        Order responseOrder = orderService.addOrder(orderDTO, customerName);

        // Assert
        assertEquals(expectedOrder, responseOrder);
        verify(customerService, times(1)).getCustomerByUsername(customerName);
        verify(merchantService, times(1)).getMerchantByUsername(merchantName);
        verify(productRepository, times(1)).findById(orderItemDTO.getProductId());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testAddOrder_InvalidProductId_ThrowNotExistException() {

        // Arrange
        order.setId(null);
        OrderItemDTO orderItemDTO = new OrderItemDTO(5, 2);
        OrderDTO orderDTO = new OrderDTO(false, merchant.getUsername(), OrderStatus.PENDING, List.of(orderItemDTO), null);
        String customerName = "Daniel";
        String merchantName = "Annabelle";
        String exceptionMsg = "";

        when(customerService.getCustomerByUsername(any(String.class)))
            .thenReturn(customer);
        when(merchantService.getMerchantByUsername(any(String.class)))
            .thenReturn(merchant);
        when(productRepository.findById(any(Integer.class)))
            .thenReturn(Optional.empty());

        // Act
        try {
            Order responseOrder = orderService.addOrder(orderDTO, customerName);
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Product doesn't exist!", exceptionMsg);
        verify(customerService, times(1)).getCustomerByUsername(customerName);
        verify(merchantService, times(1)).getMerchantByUsername(merchantName);
        verify(productRepository, times(1)).findById(orderItemDTO.getProductId());
    }

    @Test
    public void testUpdateOrder_ValidUpdateOrderDTO_ReturnUpdatedOrder() {

        // Arrange
        Integer orderId = order.getId();
        UpdateOrderItemDTO updateOrderItemDTO = new UpdateOrderItemDTO(1, 10);
        Set<UpdateOrderItemDTO> updateOrderItemDTOs = new HashSet<>();
        updateOrderItemDTOs.add(updateOrderItemDTO);
        UpdateOrderDTO updateOrderDTO = new UpdateOrderDTO(true, OrderStatus.CANCELLED, updateOrderItemDTOs, null);
        
        Order expectedOrder = order;
        OrderItem updatedOrderItem = orderItem;
        updatedOrderItem.setQuantity(10);
        updatedOrderItem.setPrice(69.0);
        Set<OrderItem> updatedOrderItems = new HashSet<>();
        updatedOrderItems.add(updatedOrderItem);
        expectedOrder.setIsDineIn(true);
        expectedOrder.setOrderStatus(OrderStatus.CANCELLED);
        expectedOrder.setTotalPrice(69.0);
        expectedOrder.setOrderItems(updatedOrderItems);

        when(orderRepository.findById(any(Integer.class)))
            .thenReturn(Optional.of(order));
        when(productRepository.findById(any(Integer.class)))
            .thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class)))
            .thenReturn(expectedOrder);
        
        // Act
        Order responseOrder = orderService.updateOrder(updateOrderDTO, orderId);

        // Assert
        assertEquals(expectedOrder, responseOrder);
        verify(orderRepository, times(1)).findById(orderId);
        verify(productRepository, times(1)).findById(updateOrderItemDTO.getProductId());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testUpdateOrder_InvalidProductId_ThrowNotExistException() {

        // Arrange
        String exceptionMsg = "";
        Integer orderId = order.getId();
        UpdateOrderItemDTO updateOrderItemDTO = new UpdateOrderItemDTO(5, 10);
        Set<UpdateOrderItemDTO> updateOrderItemDTOs = new HashSet<>();
        updateOrderItemDTOs.add(updateOrderItemDTO);
        UpdateOrderDTO updateOrderDTO = new UpdateOrderDTO(true, OrderStatus.CANCELLED, updateOrderItemDTOs, null);
        
        Order expectedOrder = order;
        OrderItem updatedOrderItem = orderItem;
        updatedOrderItem.setQuantity(10);
        updatedOrderItem.setPrice(69.0);
        Set<OrderItem> updatedOrderItems = new HashSet<>();
        updatedOrderItems.add(updatedOrderItem);
        expectedOrder.setIsDineIn(true);
        expectedOrder.setOrderStatus(OrderStatus.CANCELLED);
        expectedOrder.setTotalPrice(69.0);
        expectedOrder.setOrderItems(updatedOrderItems);

        when(orderRepository.findById(any(Integer.class)))
            .thenReturn(Optional.of(order));
        when(productRepository.findById(any(Integer.class)))
            .thenReturn(Optional.empty());
        
        // Act
        try {
            Order responseOrder = orderService.updateOrder(updateOrderDTO, orderId);
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Product doesn't exist!", exceptionMsg);
        verify(orderRepository, times(1)).findById(orderId);
        verify(productRepository, times(1)).findById(updateOrderItemDTO.getProductId());
    }

    @Test
    public void testDeleteOrder_OrderExists_ReturnSuccess() {

        // Arrange
        Integer orderId = order.getId();
        when(orderRepository.existsById(any(Integer.class)))
            .thenReturn(true);

        // Act
        orderService.deleteOrder(orderId);

        // Assert
        verify(orderRepository, times(1)).existsById(orderId);
        verify(orderRepository, times(1)).deleteById(orderId);
    }

    @Test
    public void testDeleteOrder_OrderNotExist_ThrowNotExistException() {

        // Arrange
        String exceptionMsg = "";
        Integer orderId = order.getId();

        when(orderRepository.existsById(any(Integer.class)))
            .thenReturn(false);

        // Act
        try {
            orderService.deleteOrder(orderId);
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Order doesn't exist!", exceptionMsg);
        verify(orderRepository, times(1)).existsById(orderId);
    }

    @Test
    public void testDeleteOrderItem_OrderItemExists_ReturnSuccess() {

        // Arrange
        Integer orderId = order.getId();
        Integer productId = product.getId();
        
        when(orderRepository.existsById(any(Integer.class)))
            .thenReturn(true);
        when(orderRepository.findById(any(Integer.class)))
            .thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class)))
            .thenReturn(order);
        
        // Act
        orderService.deleteOrderItem(orderId, productId);

        // Assert
        verify(orderRepository, times(1)).existsById(orderId);
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void testDeleteOrderItem_OrderItemNotExist_ThrowNotExistException() {

        // Arrange
        Integer orderId = order.getId();
        Integer productId = 1000;
        String exceptionMsg = "";
        
        when(orderRepository.existsById(any(Integer.class)))
            .thenReturn(true);
        when(orderRepository.findById(any(Integer.class)))
            .thenReturn(Optional.of(order));
        
        // Act
        try {
            orderService.deleteOrderItem(orderId, productId);
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Order Item doesn't exist!", exceptionMsg);
        verify(orderRepository, times(1)).existsById(orderId);
        verify(orderRepository, times(1)).findById(orderId);
    }
}
