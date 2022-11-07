package me.plantngo.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

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

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    void testGetAllUsers_CustomersExist_CustomerList() throws Exception {

        // String username = "Jaddd";
        // String password = "password";
        // String auth = username + ":" + password;
        // byte[] encodedAuth = Base64.encodeBase64(auth.getBytes());
        // String authHeader = new String(encodedAuth);

        String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKYWRkZCIsIkF1dGhvcml0eSI6IkNVU1RPTUVSIiwiZXhwIjoxNjY4MDkwOTQ3LCJpYXQiOjE2Njc2NTg5NDd9.Khx79-HhoEEgSEiEHl1Ps5qLw9_GYZcxhhHa6oUcq0c";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);

        URI uri = new URI(rootUrl + port + apiUrl);
        Customer customer = new Customer(1, "Jaddd", "john.doe@example.com", "password",
                new ArrayList<Preference>(), 1000, new HashSet<Voucher>(), new HashSet<Voucher>(),
                new ArrayList<Order>(), null, null);
        customerRepository.save(customer);

        ResponseEntity<Customer[]> result = restTemplate.exchange(uri, HttpMethod.GET, request, Customer[].class);
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
