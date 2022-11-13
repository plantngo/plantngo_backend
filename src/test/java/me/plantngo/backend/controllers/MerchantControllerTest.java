package me.plantngo.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

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
import me.plantngo.backend.DTO.UpdateMerchantDetailsDTO;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.repositories.MerchantRepository;
import me.plantngo.backend.services.MailService;
import me.plantngo.backend.services.MinioService;

@SpringBootTest(classes = BackendApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class MerchantControllerTest {

    private final String rootUrl = "http://localhost:";

    @LocalServerPort
    private int port;

    private final String apiUrl = "/api/v1/merchant/";

    private final String loginUrl = "/api/v1/login/";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MerchantRepository merchantRepository;

    @MockBean
    private MailService mailService;

    @MockBean
    private JavaMailSender javaMailSender;

    @MockBean
    private MinioService minioService;

    private Merchant merchant;

    private String jwtToken;

    @BeforeEach
    void setUp() {
        merchantRepository.deleteAll();

        merchant = new Merchant();
        String encodedPassword = new BCryptPasswordEncoder().encode("password");
        merchant.setUsername("Gabriel");
        merchant.setPassword(encodedPassword);
        merchant.setEmail("gabriel@yahoo.com.sg");
        merchant.setCompany("PizzaHut");
        merchantRepository.save(merchant);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", "Gabriel");
            jsonObject.put("password", "password");
            jsonObject.put("userType", "M");
        } catch (JSONException e) {
            
        }
        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(rootUrl + port + loginUrl, HttpMethod.POST, request, String.class);
        jwtToken = response.getHeaders().get("jwt").get(0);
    }

    @AfterEach
    void tearDown() {
        // clear the database after each test
        merchantRepository.deleteAll();
    }

    @Test
    void testGetAllUsers_MerchantsExist_ReturnMerchantsList() throws URISyntaxException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);

        URI uri = new URI(rootUrl + port + apiUrl);
        Merchant newMerchant = new Merchant();
        newMerchant.setUsername("Jacky");
        newMerchant.setPassword("securepassword");
        newMerchant.setEmail("jacky@yahoo.com.sg");
        newMerchant.setCompany("McDonalds");
        merchantRepository.save(newMerchant);

        ResponseEntity<Merchant[]> result = restTemplate.exchange(uri, HttpMethod.GET, request, Merchant[].class);
        Merchant[] merchantList = result.getBody();

        assertEquals(200, result.getStatusCode().value());
        assertEquals(2, merchantList.length);
    }

    @Test
    void testGetAllUsers_NoMerchants_ReturnEmptyList() throws URISyntaxException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);

        URI uri = new URI(rootUrl + port + apiUrl);

        ResponseEntity<Merchant[]> result = restTemplate.exchange(uri, HttpMethod.GET, request, Merchant[].class);
        Merchant[] merchantList = result.getBody();

        assertEquals(200, result.getStatusCode().value());
        assertEquals(1, merchantList.length);
    }

    @Test
    void testGetMerchantByUsername_MerchantExists_ReturnMerchant() throws URISyntaxException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);
        merchant.setPassword(null);

        URI uri = new URI(rootUrl + port + apiUrl + "Gabriel/");

        ResponseEntity<Merchant> result = restTemplate.exchange(uri, HttpMethod.GET, request, Merchant.class);
        Merchant responseMerchant = result.getBody();
        responseMerchant.setCategories(null);
        responseMerchant.setPromotions(null);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(merchant, responseMerchant);
    }

    @Test
    void testGetMerchantByUsername_MerchantNotExists_ReturnFailure() throws URISyntaxException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);

        URI uri = new URI(rootUrl + port + apiUrl + "Random/");

        ResponseEntity<Merchant> result = restTemplate.exchange(uri, HttpMethod.GET, request, Merchant.class);

        assertEquals(404, result.getStatusCode().value());
    }

    @Test
    void testUpdateMerchant_MerchantExists_ReturnMerchant() throws URISyntaxException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        UpdateMerchantDetailsDTO updateMerchantDetailsDTO = new UpdateMerchantDetailsDTO();
        updateMerchantDetailsDTO.setUsername("Soon Ann");
        HttpEntity<UpdateMerchantDetailsDTO> request = new HttpEntity<>(updateMerchantDetailsDTO, headers);

        Merchant expectedMerchant = new Merchant();
        expectedMerchant.setUsername("Soon Ann");
        expectedMerchant.setPassword(null);
        expectedMerchant.setEmail("gabriel@yahoo.com.sg");
        expectedMerchant.setCompany("PizzaHut");

        URI uri = new URI(rootUrl + port + apiUrl + "Gabriel/");

        ResponseEntity<Merchant> result = restTemplate.exchange(uri, HttpMethod.PUT, request, Merchant.class);
        Merchant responseMerchant = result.getBody();
        responseMerchant.setId(null);
        responseMerchant.setCategories(null);
        responseMerchant.setPromotions(null);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(expectedMerchant, responseMerchant);
    }

    @Test
    void testUpdateMerchant_MerchantNotExists_ReturnFailure() throws URISyntaxException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        UpdateMerchantDetailsDTO updateMerchantDetailsDTO = new UpdateMerchantDetailsDTO();
        updateMerchantDetailsDTO.setUsername("Soon Ann");
        HttpEntity<UpdateMerchantDetailsDTO> request = new HttpEntity<>(updateMerchantDetailsDTO, headers);

        URI uri = new URI(rootUrl + port + apiUrl + "Random/");

        ResponseEntity<Merchant> result = restTemplate.exchange(uri, HttpMethod.PUT, request, Merchant.class);

        assertEquals(404, result.getStatusCode().value());
    }
}
