package me.plantngo.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import me.plantngo.backend.DTO.RegistrationDTO;
import me.plantngo.backend.exceptions.InvalidUserTypeException;
import me.plantngo.backend.repositories.CustomerRepository;
import me.plantngo.backend.repositories.MerchantRepository;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private MerchantRepository merchantRepository;

    @InjectMocks
    private AuthService authService;

    private RegistrationDTO registrationDTO;

    @BeforeEach
    public void initEach() {
        registrationDTO = new RegistrationDTO();
        registrationDTO.setUsername("Daniel");
        registrationDTO.setEmail("daniel@gmail.com");
        registrationDTO.setPassword("securepassword");
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

    
}
