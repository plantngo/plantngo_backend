package me.plantngo.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import me.plantngo.backend.exceptions.UserNotFoundException;
import me.plantngo.backend.models.Customer;
import me.plantngo.backend.repositories.CustomerRepository;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerService customerService;

    @Test
    void testFindAll_AllCustomers_ReturnAllCustomers(){

        // arrange
        Customer cust1 = new Customer();
        cust1.setUsername("John Doe");
        Customer cust2 = new Customer();
        cust2.setUsername("Mary Jane");
        Customer cust3 = new Customer();
        cust3.setUsername("Tom Cruise");
        Customer cust4 = new Customer();
        cust4.setUsername("Arnold Schwarzenegger");;
        List<Customer> customerList = List.of(cust1, cust2, cust3, cust4);

        when(customerRepository.findAll()).thenReturn(customerList);

        // act 
        List<Customer> responseList = customerService.findAll();
        
        //assert
        verify(customerRepository, times(1)).findAll();
        assertEquals(customerList, responseList);
        
    }

    @Test
    void testFindAll_NoCustomers_ReturnEmptyList(){

        // arrange
        when(customerRepository.findAll()).thenReturn(new ArrayList<Customer>());

        // act 
        List<Customer> responseList = customerService.findAll();
        
        //assert
        verify(customerRepository, times(1)).findAll();
        assertEquals(responseList, new ArrayList<Customer>());
        
    }
    
    @Test
    void testGetCustomerByUsername_Exist_ReturnCustomer(){

        // arrange
        Customer customer = new Customer();
        customer.setUsername("John Doe");

        Optional<Customer> optionalCustomer = Optional.of(customer);
        when(customerRepository.findByUsername(any(String.class))).thenReturn(optionalCustomer);

        // act 
        Customer responseCustomer = customerService.getCustomerByUsername(customer.getUsername());

        //assert
        assertEquals(responseCustomer, customer);
        verify(customerRepository, times(1)).findByUsername(customer.getUsername());

    }

    @Test
    void testGetCustomerByUsername_NotFound_ThrowUserNotFound(){

        // arrange
        Customer customer = new Customer();
        customer.setUsername("John Doe");
        String customerUsernameToSearch = "Mary Jane";

        when(customerRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        // act and assert
        Exception exception = assertThrows(UserNotFoundException.class, () -> customerService.getCustomerByUsername(customerUsernameToSearch));

        //assert
        verify(customerRepository, times(1)).findByUsername(customerUsernameToSearch);
        assertEquals(exception.getMessage(), "User Not Found:Username not found");
        
    }
    
    @Test
    void testGetCustomerByEmail_Exist_ReturnCustomer(){

        // arrange
        Customer customer = new Customer();
        customer.setEmail("john.doe@example.com");;

        Optional<Customer> optionalCustomer = Optional.of(customer);
        when(customerRepository.findByEmail(any(String.class))).thenReturn(optionalCustomer);

        // act 
        Customer responseCustomer = customerService.getCustomerByEmail(customer.getEmail());

        //assert
        assertEquals(responseCustomer, customer);
        verify(customerRepository, times(1)).findByEmail(customer.getEmail());

    }

    @Test
    void testGetCustomerByEmail_NotFound_ThrowUserNotFound(){

        // arrange
        Customer customer = new Customer();
        customer.setEmail("john.doe@example.com");
        String customerEmailToSearch = "mary.jane@example.com";
        
        when(customerRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        // act and assert
        Exception exception = assertThrows(UserNotFoundException.class, () -> customerService.getCustomerByEmail(customerEmailToSearch));

        //assert
        verify(customerRepository, times(1)).findByEmail(customerEmailToSearch);
        assertEquals(exception.getMessage(), "User Not Found:Email not found");
        
    }


  


}
