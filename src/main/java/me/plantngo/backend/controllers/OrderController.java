package me.plantngo.backend.controllers;

import java.util.List;

import javax.validation.Valid;

import me.plantngo.backend.services.LogService;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.plantngo.backend.DTO.OrderDTO;
import me.plantngo.backend.DTO.OrderItemDTO;
import me.plantngo.backend.DTO.UpdateOrderDTO;
import me.plantngo.backend.DTO.UpdateOrderItemDTO;
import me.plantngo.backend.exceptions.AlreadyExistsException;
import me.plantngo.backend.exceptions.UserNotFoundException;
import me.plantngo.backend.models.Order;
import me.plantngo.backend.models.OrderItem;
import me.plantngo.backend.models.OrderStatus;
import me.plantngo.backend.services.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController()
@RequestMapping(path = "api/v1/order")
@Api(value = "Order Controller", description = "Customer ordering API")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService, LogService logService) {
        this.orderService = orderService;
    }

    @ApiOperation(value = "Get all placed Orders")
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @ApiOperation(value = "Get all Orders placed by a Customer given their Username")
    @GetMapping(path = "/customer/{customerName}")
    public List<Order> getOrdersByCustomer(@PathVariable("customerName") String name) {
        return orderService.getOrdersByCustomerName(name);
    }

    @ApiOperation(value = "Get all Orders placed by a Customer at a Merchant")
    @GetMapping(path = "/customer/{customerName}/merchant/{merchantName}")
    public List<Order> getOrdersByCustomerAndMerchant(@PathVariable("customerName") String customerName,
            @PathVariable("merchantName") String merchantName) {
        return orderService.getOrdersByCustomerNameAndMerchantName(customerName, merchantName);
    }

    @ApiOperation(value = "Get all Orders placed by a Customer at a Merchant that has a Order Status")
    @GetMapping(path = "/customer/{customerName}/merchant/{merchantName}/orderStatus/{orderStatus}")
    public Order getOrderByCustomerAndMerchantAndOrderStatus(@PathVariable("customerName") String customerName,
            @PathVariable("merchantName") String merchantName, @PathVariable("orderStatus") OrderStatus orderStatus) {
        return orderService.getOrdersByCustomerNameAndMerchantNameAndOrderStatus(customerName, merchantName,
                orderStatus);
    }

    @ApiOperation(value = "Get all Orders placed by a Merchant given their Username")
    @GetMapping(path = "merchant/{merchantName}")
    public List<Order> getOrdersByMerchant(@PathVariable("merchantName") String name) {
        return orderService.getOrdersByMerchantName(name);
    }

    @ApiOperation(value = "Get all Pending Orders placed by a Merchant given their Username")
    @GetMapping(path = "merchant/{merchantName}/pending")
    public List<Order> getPendingOrdersByMerchant(@PathVariable("merchantName") String name) {
        return orderService.getPendingOrdersByMerchantName(name);
    }

    @ApiOperation(value = "Get all Fulfilled Orders placed by a Merchant given their Username")
    @GetMapping(path = "merchant/{merchantName}/fulfilled")
    public List<Order> getFulfilledOrdersByMerchant(@PathVariable("merchantName") String name) {
        return orderService.getFulfilledOrdersByMerchantName(name);
    }

    @ApiOperation(value = "Get all Cancelled Orders placed by a Merchant given their Username")
    @GetMapping(path = "merchant/{merchantName}/cancelled")
    public List<Order> getCancelledOrdersByMerchant(@PathVariable("merchantName") String name) {
        return orderService.getCancelledOrdersByMerchantName(name);
    }

    @ApiOperation(value = "Create a new Order with Order Items")
    @PostMapping(path = "/{customerName}")
    public ResponseEntity<Order> addToOrder(@RequestBody @Valid OrderDTO placeOrderDTO,
            @PathVariable("customerName") String customerName) {
        Order order = orderService.addOrder(placeOrderDTO, customerName);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Add a new Order Item to an existing Order")
    @PostMapping(path = "/{customerName}/{orderId}")
    public ResponseEntity<Order> addToExistingOrder(
            @PathVariable("customerName") String customerName,
            @PathVariable("orderId") Integer orderId,
            @RequestBody OrderItemDTO orderItemDTO) {
        Order order = orderService.addOrderItem(customerName, orderId, orderItemDTO);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update an existing Order's status given the Order Id")
    @PutMapping(path = "/{orderId}")
    public ResponseEntity<Order> updateOrder(@RequestBody @Valid UpdateOrderDTO updateOrderDTO,
            @PathVariable("orderId") Integer orderId) {
        System.out.println(orderId + "and " + updateOrderDTO.toString());
        Order order = orderService.updateOrder(updateOrderDTO, orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete an Order given its Id")
    @DeleteMapping(path = "/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable("orderId") Integer orderId) {
        orderService.deleteOrder(orderId);
        return new ResponseEntity<>("Order deleted", HttpStatus.OK);
    }

    @ApiOperation(value = "Delete an OrderItem in an existing Order given its Id")
    @DeleteMapping(path = "/{orderId}/{productId}")
    public ResponseEntity<String> deleteOrderItemInOrder(@PathVariable("orderId") Integer orderId,
            @PathVariable("productId") Integer productId) {
        orderService.deleteOrderItem(orderId, productId);
        return new ResponseEntity<>("Order Item deleted", HttpStatus.OK);
    }
}
