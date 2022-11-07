package me.plantngo.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import me.plantngo.backend.exceptions.AlreadyExistsException;
import me.plantngo.backend.exceptions.NotExistException;
import me.plantngo.backend.models.Customer;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.repositories.CustomerRepository;
import me.plantngo.backend.repositories.MerchantRepository;
import net.bytebuddy.utility.RandomString;

@ExtendWith(MockitoExtension.class)
public class ResetPasswordServiceTest {
    
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private ChangeCredentialService changeCredentialService;

    @Mock
    private MailService mailService;

    @InjectMocks
    private ResetPasswordService resetPasswordService;

    @Test
    void testSetResetPasswordTokenAndSendEmail_ValidCustomer_ReturnResponseEntity() {

        // Arrange
        String email = "daniel@yahoo.com.sg";
        Customer customer = new Customer();
        customer.setEmail(email);
        Customer expectedCustomer = new Customer();
        expectedCustomer.setEmail(email);
        expectedCustomer.setResetPasswordToken(RandomString.make(16));
        ResponseEntity<String> expectedResponseEntity = new ResponseEntity<>("An email has been sent to you. Please check it for a code to reset your password!", HttpStatus.OK);

        when(customerRepository.findByEmail(anyString()))
            .thenReturn(Optional.of(customer));
        when(customerRepository.saveAndFlush(any(Customer.class)))
            .thenReturn(expectedCustomer);

        // Act
        ResponseEntity<String> responseEntity = resetPasswordService.setResetPasswordTokenAndSendEmail(email);

        // Assert
        assertEquals(expectedResponseEntity, responseEntity);
        verify(customerRepository, times(1)).findByEmail(email);
    }

    @Test
    void testSetResetPasswordTokenAndSendEmail_ValidMerchant_ReturnResponseEntity() {

        // Arrange
        String email = "daniel@yahoo.com.sg";
        String randomString = "randomString";
        Merchant merchant = new Merchant();
        merchant.setEmail(email);
        Merchant expectedMerchant = new Merchant();
        expectedMerchant.setEmail(email);
        expectedMerchant.setResetPasswordToken(randomString);
        ResponseEntity<String> expectedResponseEntity = new ResponseEntity<>("An email has been sent to you. Please check it for a code to reset your password!", HttpStatus.OK);

        when(merchantRepository.findByEmail(anyString()))
            .thenReturn(Optional.of(merchant));
        when(merchantRepository.saveAndFlush(any(Merchant.class)))
            .thenReturn(expectedMerchant);

        // Act
        ResponseEntity<String> responseEntity = resetPasswordService.setResetPasswordTokenAndSendEmail(email);

        // Assert
        assertEquals(expectedResponseEntity, responseEntity);
        verify(merchantRepository, times(1)).findByEmail(email);
    }

    @Test
    void testSetResetPasswordTokenAndSendEmail_MerchantAndCustomerNotExist_ThrowNotExistException() {

        // Arrange
        String email = "daniel@yahoo.com.sg";
        String exceptionMsg = "";

        when(merchantRepository.findByEmail(anyString()))
            .thenReturn(Optional.empty());
        when(customerRepository.findByEmail(anyString()))
            .thenReturn(Optional.empty());
        
        // Act
        try {
            ResponseEntity<String> responseEntity = resetPasswordService.setResetPasswordTokenAndSendEmail(email);
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Account doesn't exist!", exceptionMsg);
        verify(merchantRepository, times(1)).findByEmail(email);
        verify(customerRepository, times(1)).findByEmail(email);
    }

    @Test
    void testSetResetPasswordTokenAndSendEmail_InvalidCustomerEmail_ThrowIllegalArgumentException() {

        // Arrange
        String email = "daniel@yahoo.com.sg";
        Customer customer = new Customer();
        customer.setEmail("Hello");
        String exceptionMsg = "";

        when(customerRepository.findByEmail(anyString()))
            .thenReturn(Optional.of(customer));

        // Act
        try {
            ResponseEntity<String> responseEntity = resetPasswordService.setResetPasswordTokenAndSendEmail(email);
        } catch (IllegalArgumentException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Email entered is incorrect", exceptionMsg);
        verify(customerRepository, times(1)).findByEmail(email);
    }

    @Test
    void testSetResetPasswordTokenAndSendEmail_CustomerAlreadyHasResetPasswordToken_ThrowAlreadyExistsException() {

        // Arrange
        String email = "daniel@yahoo.com.sg";
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setResetPasswordToken("Hello");
        String exceptionMsg = "";

        when(customerRepository.findByEmail(anyString()))
            .thenReturn(Optional.of(customer));

        // Act
        try {
            ResponseEntity<String> responseEntity = resetPasswordService.setResetPasswordTokenAndSendEmail(email);
        } catch (AlreadyExistsException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Reset password token already exists!", exceptionMsg);
        verify(customerRepository, times(1)).findByEmail(email);
    }
}
