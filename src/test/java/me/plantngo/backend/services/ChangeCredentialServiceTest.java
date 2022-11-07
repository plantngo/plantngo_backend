package me.plantngo.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import me.plantngo.backend.exceptions.AlreadyExistsException;
import me.plantngo.backend.exceptions.UserNotFoundException;
import me.plantngo.backend.models.Customer;
import me.plantngo.backend.repositories.CustomerRepository;
import me.plantngo.backend.repositories.MerchantRepository;

@ExtendWith(MockitoExtension.class)
public class ChangeCredentialServiceTest {
    
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private ChangeCredentialService changeCredentialService;

    @Test
    void testValidateNewUsername_UsernameExists_ReturnSuccess() {

        // Arrange
        String newUsername = "Jacky";
        Character userType = 'C';

        when(customerRepository.existsByUsername(any(String.class)))
            .thenReturn(false);

        // Act
        changeCredentialService.validateNewUsername(newUsername, userType);

        // Assert
        verify(customerRepository, times(1)).existsByUsername(newUsername);
    }

    @Test
    void testValidateNewUsername_InvalidUserType_ThrowIllegalArgumentException() {

        // Arrange
        String newUsername = "Jacky";
        Character userType = 'T';
        String exceptionMsg = "";

        // Act
        try {
            changeCredentialService.validateNewUsername(newUsername, userType);
        } catch (IllegalArgumentException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Invalid user type", exceptionMsg);
    }

    @Test
    void testValidateNewUsername_UsernameAlreadyExists_ThrowAlreadyExistsException() {

        // Arrange
        String newUsername = "Jacky";
        Character userType = 'M';
        String exceptionMsg = "";

        when(merchantRepository.existsByUsername(any(String.class)))
            .thenReturn(true);
        
        // Act
        try {
            changeCredentialService.validateNewUsername(newUsername, userType);
        } catch (AlreadyExistsException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Username already exists!", exceptionMsg);
        verify(merchantRepository, times(1)).existsByUsername(newUsername);
    }

    @Test
    void testReplaceUsername_ValidCustomerUsername_ReturnResponseEntity() {

        // Arrange
        String oldUsername = "Daniel";
        String newUsername = "Jacky";
        Character userType = 'C';
        Customer customer = new Customer();
        customer.setUsername(oldUsername);
        Customer expectedCustomer = new Customer();
        expectedCustomer.setUsername(newUsername);
        ResponseEntity<String> expectedResponseEntity = new ResponseEntity<>("Successfully changed username to Jacky", HttpStatus.OK);

        when(customerRepository.findByUsername(any(String.class)))
            .thenReturn(Optional.of(customer));
        when(customerRepository.saveAndFlush(any(Customer.class)))
            .thenReturn(expectedCustomer);

        // Act
        ResponseEntity<String> responseEntity = changeCredentialService.replaceUsername(oldUsername, newUsername, userType);

        // Assert
        assertEquals(expectedResponseEntity, responseEntity);
        verify(customerRepository, times(1)).findByUsername(oldUsername);
        verify(customerRepository, times(1)).saveAndFlush(expectedCustomer);
    }

    @Test
    void testReplaceUsername_InvalidCustomerUsername_ThrowUserNotFoundException() {

        // Arrange
        String oldUsername = "Daniella";
        String newUsername = "Jacky";
        Character userType = 'C';
        String exceptionMsg = "";

        when(customerRepository.findByUsername(any(String.class)))
            .thenReturn(Optional.empty());

        // Act
        try {
            ResponseEntity<String> responseEntity = changeCredentialService.replaceUsername(oldUsername, newUsername, userType);
        } catch (UserNotFoundException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("User Not Found:User does not exist", exceptionMsg);
        verify(customerRepository, times(1)).findByUsername(oldUsername);
    }
}
