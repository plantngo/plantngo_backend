package me.plantngo.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import me.plantngo.backend.BackendApplication;
import me.plantngo.backend.DTO.LoginDTO;
import me.plantngo.backend.DTO.UpdateCustomerDetailsDTO;
import me.plantngo.backend.models.Customer;
import me.plantngo.backend.models.Order;
import me.plantngo.backend.models.Preference;
import me.plantngo.backend.models.Voucher;
import me.plantngo.backend.repositories.CustomerRepository;
import me.plantngo.backend.services.MailService;
import me.plantngo.backend.services.MinioService;

@SpringBootTest(classes = BackendApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CustomerControllerTest {

    private final String rootUrl = "http://localhost:";

    @LocalServerPort
    private int port;

    private final String apiUrl = "/api/v1/customer/";

    private final String loginUrl = "/api/v1/login/";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @MockBean
    private MailService mailService;

    @MockBean
    private JavaMailSender javaMailSender;

    @MockBean
    private MinioService minioService;

    private Customer customer;

    private String jwtToken;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();

        customer = new Customer();
        String encodedPassword = new BCryptPasswordEncoder().encode("password");
        customer.setUsername("Gabriel");
        customer.setPassword(encodedPassword);
        customer.setEmail("gabriel@yahoo.com.sg");
        customerRepository.save(customer);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", "Gabriel");
            jsonObject.put("password", "password");
            jsonObject.put("userType", "C");
        } catch (JSONException e) {
            
        }
        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), headers);

        LoginDTO loginDTO = new LoginDTO("Gabriel", "password");
        ResponseEntity<String> response = restTemplate.exchange(rootUrl + port + loginUrl, HttpMethod.POST, request, String.class);
        jwtToken = response.getHeaders().get("jwt").get(0);

    }

    @AfterEach
    void tearDown() {
        // clear the database after each test
        customerRepository.deleteAll();
    }


    @Test
    void testGetAllUsers_CustomersExist_CustomerList() throws Exception {

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
        assertEquals(2, customerList.length);

    }

    @Test
    void testGetAllUsers_NoCustomers_EmptyList() throws URISyntaxException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);
        
        URI uri = new URI(rootUrl + port + apiUrl);

        ResponseEntity<Customer[]> result = restTemplate.exchange(uri, HttpMethod.GET, request, Customer[].class);
        Customer[] customerList = result.getBody();

        // Should have 1, the default created Customer
        assertEquals(200, result.getStatusCode().value());
        assertEquals(1, customerList.length);
    }

    @Test
    void testGetUserByUsername_Exist_Customer() throws URISyntaxException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);

        URI uri = new URI(rootUrl + port + apiUrl + "Gabriel/");

        ResponseEntity<Customer> result = restTemplate.exchange(uri, HttpMethod.GET, request, Customer.class);
        Customer responseCustomer = result.getBody();

        assertEquals(200, result.getStatusCode().value());
        assertEquals(customer, responseCustomer);
    }

    @Test
    void testGetUserByUsername_NotFound_Failure() throws URISyntaxException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);

        URI uri = new URI(rootUrl + port + apiUrl + "Random/");

        ResponseEntity<Customer> result = restTemplate.exchange(uri, HttpMethod.GET, request, Customer.class);
        Customer responseCustomer = result.getBody();

        assertEquals(404, result.getStatusCode().value());
    }

    @Test
    void testUpdateCustomer_CustomerExist_ReturnCustomer() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        UpdateCustomerDetailsDTO updateCustomerDetailsDTO = new UpdateCustomerDetailsDTO(null, null, null, 100);
        HttpEntity<UpdateCustomerDetailsDTO> request = new HttpEntity<>(updateCustomerDetailsDTO, headers);

        Customer expectedCustomer = new Customer();
        String encodedPassword = new BCryptPasswordEncoder().encode("password");
        expectedCustomer.setUsername("Gabriel");
        expectedCustomer.setPassword(encodedPassword);
        expectedCustomer.setEmail("gabriel@yahoo.com.sg");
        expectedCustomer.setGreenPoints(100);
        expectedCustomer.setOrders(new ArrayList<>());
        expectedCustomer.setPreferences(new ArrayList<>());
        expectedCustomer.setPassword(encodedPassword);

        URI uri = new URI(rootUrl + port + apiUrl + "Gabriel/");

        ResponseEntity<Customer> result = restTemplate.exchange(uri, HttpMethod.PUT, request, Customer.class);
        Customer responseCustomer = result.getBody();
        responseCustomer.setId(null);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(expectedCustomer, responseCustomer);
    }

    @Test
    void testUpdateCustomer_CustomerNotExist_Failure() throws URISyntaxException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        UpdateCustomerDetailsDTO updateCustomerDetailsDTO = new UpdateCustomerDetailsDTO(null, null, null, 100);
        HttpEntity<UpdateCustomerDetailsDTO> request = new HttpEntity<>(updateCustomerDetailsDTO, headers);

        URI uri = new URI(rootUrl + port + apiUrl + "Random/");

        ResponseEntity<Customer> result = restTemplate.exchange(uri, HttpMethod.PUT, request, Customer.class);
        Customer responseCustomer = result.getBody();

        assertEquals(404, result.getStatusCode().value());
    }

    @Test
    void testDeleteCustomer_CustomerExists_ReturnSuccess() throws URISyntaxException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);

        URI uri = new URI(rootUrl + port + apiUrl + "Gabriel/");

        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.DELETE, request, String.class);
        String response = result.getBody();

        assertEquals(200, result.getStatusCode().value());
        assertEquals("Customer deleted!", response);
    }

    @Test
    void testDeleteCustomer_CustomerNotExist_ReturnFailure() throws URISyntaxException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);

        URI uri = new URI(rootUrl + port + apiUrl + "Random/");

        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.DELETE, request, String.class);
        String response = result.getBody();

        assertEquals(404, result.getStatusCode().value());

    }
}
