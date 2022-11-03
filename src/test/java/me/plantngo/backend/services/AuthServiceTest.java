package me.plantngo.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import me.plantngo.backend.DTO.RegistrationDTO;
import me.plantngo.backend.exceptions.FailedRegistrationException;
import me.plantngo.backend.exceptions.InvalidUserTypeException;
import me.plantngo.backend.models.Customer;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.repositories.CustomerRepository;
import me.plantngo.backend.repositories.MerchantRepository;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private MerchantRepository merchantRepository;

    @InjectMocks
    private AuthService authService;

    private RegistrationDTO registrationDTO;

    private Merchant merchant;

    private Customer customer;

    @BeforeEach
    public void initEach() {
        registrationDTO = new RegistrationDTO();
        registrationDTO.setUsername("Daniel");
        registrationDTO.setEmail("daniel@gmail.com");
        registrationDTO.setPassword("securepassword");

        customer = new Customer();
        customer.setUsername("Daniel");
        customer.setEmail("daniel@gmail.com");

        merchant = new Merchant();
        merchant.setUsername("Daniel");
        merchant.setEmail("daniel@gmail.com");
    }

    @Test
    public void testRegisterUser_InvalidUserType_ThrowInvalidUserTypeException() {

        // Arrange
        registrationDTO.setUserType('T');
        String exceptionMsg = "";

        // Act
        try {
            Object responseObject = authService.registerUser(registrationDTO);
        } catch (InvalidUserTypeException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("User Type can only be 'M' or 'C'.", exceptionMsg);
    }

    @Test
    public void testRegisterUser_MerchantEmailAlreadyExists_ThrowFailedRegistrationException() {

        // Arrange
        registrationDTO.setUserType('M');
        String exceptionMsg = "";
        when(merchantRepository.existsByEmail(any(String.class)))
            .thenReturn(true);

        // Act
        try {
            Object responseObject = authService.registerUser(registrationDTO);
        } catch (FailedRegistrationException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Registration Failed: Email already taken!", exceptionMsg);
        verify(merchantRepository, times(1)).existsByEmail(registrationDTO.getEmail());
    }

    @Test
    public void testRegisterUser_ValidMerchant_ReturnMerchant() {

        // Arrange
        registrationDTO.setUserType('M');
        registrationDTO.setCompany("PizzaHut");
        merchant.setCompany("PizzaHut");
        when(merchantRepository.existsByEmail(any(String.class)))
            .thenReturn(false);
        when(customerRepository.existsByEmail(any(String.class)))
            .thenReturn(false);
        when(merchantRepository.existsByUsername(any(String.class)))
            .thenReturn(false);
        when(customerRepository.existsByUsername(any(String.class)))
            .thenReturn(false);

        // Act
        Object responseObject = authService.registerUser(registrationDTO);

        // Assert
        assertEquals(merchant, responseObject);
        verify(merchantRepository, times(1)).existsByEmail(registrationDTO.getEmail());
        verify(customerRepository, times(1)).existsByEmail(registrationDTO.getEmail());
        verify(merchantRepository, times(1)).existsByUsername(registrationDTO.getUsername());
        verify(customerRepository, times(1)).existsByUsername(registrationDTO.getUsername());
    }

    @Test
    public void testRegisterUser_CustomerUsernameAlreadyExists_ThrowFailedRegistrationException() {

        // Arrange
        registrationDTO.setUserType('C');
        String exceptionMsg = "";
        when(merchantRepository.existsByEmail(any(String.class)))
            .thenReturn(false);
        when(customerRepository.existsByEmail(any(String.class)))
            .thenReturn(false);
        when(customerRepository.existsByUsername(any(String.class)))
            .thenReturn(true);

        // Act
        try {
            Object responseObject = authService.registerUser(registrationDTO);
        } catch (FailedRegistrationException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Registration Failed: Username already taken!", exceptionMsg);
        verify(merchantRepository, times(1)).existsByEmail(registrationDTO.getEmail());
        verify(customerRepository, times(1)).existsByEmail(registrationDTO.getEmail());
        verify(customerRepository, times(1)).existsByUsername(registrationDTO.getUsername());
    }

    @Test
    public void testRegisterUser_ValidCustomer_ReturnCustomer() {

        // Arrange
        registrationDTO.setUserType('C');
        registrationDTO.setCompany("PizzaHut");
        when(merchantRepository.existsByEmail(any(String.class)))
            .thenReturn(false);
        when(customerRepository.existsByEmail(any(String.class)))
            .thenReturn(false);
        when(merchantRepository.existsByUsername(any(String.class)))
            .thenReturn(false);
        when(customerRepository.existsByUsername(any(String.class)))
            .thenReturn(false);

        // Act
        Object responseObject = authService.registerUser(registrationDTO);

        // Assert
        assertEquals(customer, responseObject);
        verify(merchantRepository, times(1)).existsByEmail(registrationDTO.getEmail());
        verify(customerRepository, times(1)).existsByEmail(registrationDTO.getEmail());
        verify(merchantRepository, times(1)).existsByUsername(registrationDTO.getUsername());
        verify(customerRepository, times(1)).existsByUsername(registrationDTO.getUsername());
    }
}
