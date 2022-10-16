package me.plantngo.backend.controllers;

import java.util.List;

import javax.validation.Valid;

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
import me.plantngo.backend.DTO.PlaceOrderDTO;
import me.plantngo.backend.DTO.UpdateOrderItemDTO;
import me.plantngo.backend.exceptions.AlreadyExistsException;
import me.plantngo.backend.exceptions.UserNotFoundException;
import me.plantngo.backend.models.Order;
import me.plantngo.backend.services.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ApiOperation(value = "Get all placed Orders")
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @ApiOperation(value = "Get all Orders placed by a Customer given their Username")
    @GetMapping(path = "/{customerName}")
    public List<Order> getOrdersByCustomer(@PathVariable("customerName") String name) {
        return orderService.getOrdersByCustomerName(name);
    }
    
    @ApiOperation(value = "Add a new Order Item to an existing Order, create a new Order if none exists")
    @PostMapping(path = "/{orderId}")
    public ResponseEntity<Order> addToOrder(@RequestBody @Valid PlaceOrderDTO placeOrderDTO, @PathVariable("orderId") Integer orderId) {
        Order order = orderService.addOrderItemToOrder(placeOrderDTO, orderId);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
    
    @ApiOperation(value = "Update an existing Order Item in an Order")
    @PutMapping(path = "/{orderId}")
    public ResponseEntity<String> updateOrder(@RequestBody @Valid UpdateOrderItemDTO updateOrderItemDTO, @PathVariable("orderId") Integer orderId) {
        orderService.updateOrderItemInOrder(updateOrderItemDTO, orderId);
        return new ResponseEntity<>("Item updated", HttpStatus.CREATED);
    }

    @ApiOperation(value = "Delete an Order given its Id")
    @DeleteMapping(path = "/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable("orderId") Integer orderId) {
        orderService.deleteOrder(orderId);
        return new ResponseEntity<>("Order deleted", HttpStatus.OK);
    }

    @ApiOperation(value = "Delete an OrderItem in an existing Order given its Id")
    @DeleteMapping(path = "/{orderId}/{productId}")
    public ResponseEntity<String> deleteOrderItemInOrder(@PathVariable("orderId") Integer orderId, @PathVariable("productId") Integer productId) {
        orderService.deleteOrderItem(orderId, productId);
        return new ResponseEntity<>("Order Item deleted", HttpStatus.OK);
    }
}
