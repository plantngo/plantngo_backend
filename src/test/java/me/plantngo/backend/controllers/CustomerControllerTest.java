package me.plantngo.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import me.plantngo.backend.BackendApplication;
import me.plantngo.backend.models.Customer;
import me.plantngo.backend.models.Order;
import me.plantngo.backend.models.Preference;
import me.plantngo.backend.models.Voucher;
import me.plantngo.backend.repositories.CustomerRepository;

@SpringBootTest(classes = BackendApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerControllerTest {

    private final String rootUrl = "http://localhost:";

    @LocalServerPort
    private int port;

    private final String apiUrl = "/api/v1/customer/";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    void tearDown() {
        // clear the database after each test
        customerRepository.deleteAll();
    }

    @Test
    void testGetAllUsers_CustomersExist_CustomerList() throws Exception {

        URI uri = new URI(rootUrl + port + apiUrl);
        Customer customer = new Customer(1, "John Doe", "john.doe@example.com", "Password12345!",
                new ArrayList<Preference>(), 1000, new HashSet<Voucher>(), new HashSet<Voucher>(),
                new ArrayList<Order>());
        customerRepository.save(customer);

        ResponseEntity<Customer[]> result = restTemplate.getForEntity(uri, Customer[].class);
        Customer[] customerList = result.getBody();

        assertEquals(200, result.getStatusCode().value());
        assertEquals(1, customerList.length);

    }

    // @Test
    // void testGetAllUsers_NoCustomers_EmptyList() {

    // }

    // @Test
    // void testGetUserByUsername_Exist_Customer() {

    // }

    // @Test
    // void testGetUserByUsername_NotFound_Failure() {

    // }
}
