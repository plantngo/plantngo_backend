package me.plantngo.backend.services;

import me.plantngo.backend.exceptions.AlreadyExistsException;
import me.plantngo.backend.exceptions.InsufficientBalanceException;
import me.plantngo.backend.exceptions.NotExistException;
import me.plantngo.backend.models.Customer;
import me.plantngo.backend.models.Voucher;
import me.plantngo.backend.repositories.CustomerRepository;
import me.plantngo.backend.repositories.VoucherRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VoucherPurchaseServiceTest {

    @Mock
    private VoucherRepository voucherRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private VoucherPurchaseService voucherPurchaseService;

    private List<Voucher> allVouchers;
    private Voucher voucherInCart;
    private Set<Voucher> vouchersOwned;

    private Voucher voucher;

    private List<Customer> customers;

    private Customer customer;


    @BeforeEach
    void initEach() {

        allVouchers = new ArrayList<>();
        customers = new ArrayList<>();
        vouchersOwned = new HashSet<>();

        customer = new Customer();
        customer.setUsername("Daniel");
        customers.add(customer);

        customer = new Customer();
        customer.setUsername("Emil");
        customers.add(customer);

        // Emil has it in cart
        voucherInCart = new Voucher();
        voucherInCart.setId(1);
        List<Customer> singleCustomer = new ArrayList<>();
        singleCustomer.add(customer);
        voucherInCart.setCustomersInCart(singleCustomer);

        Set<Voucher> singleVoucher = new HashSet<>();
        singleVoucher.add(voucherInCart);
        customer.setVouchersCart(singleVoucher);


        allVouchers.add(voucherInCart);

        // One voucher that both Daniel and Emil owns
        voucher = new Voucher();
        voucher.setCustomersThatOwn(customers);
        singleVoucher = new HashSet<>();
        singleVoucher.add(voucher);
        customer.setOwnedVouchers(singleVoucher);
        allVouchers.add(voucher);
        vouchersOwned.add(voucher);

        // One voucher that both Daniel and Emil owns
        voucher = new Voucher();
        voucher.setCustomersThatOwn(customers);
        allVouchers.add(voucher);
        

    }

    @Test
    void testGetAllVouchers_AllVouchers_ReturnAllVouchers() {
        // Arrange
        List<Voucher> expectedVouchers = allVouchers;

        when(voucherRepository.findAll()).thenReturn(allVouchers);

        // Act
        List<Voucher> responseVouchers = voucherPurchaseService.getAllVouchers();

        // Assert
        assertEquals(expectedVouchers, responseVouchers);
        verify(voucherRepository, times(1)).findAll();

    }

    @Test
    void testGetAllVouchers_NoVouchers_ReturnEmptyList() {
        // Arrange
        allVouchers = new ArrayList<>();
        List<Voucher> expectedVouchers = new ArrayList<>();

        when(voucherRepository.findAll()).thenReturn(allVouchers);

        // Act
        List<Voucher> responseVouchers = voucherPurchaseService.getAllVouchers();

        // Assert
        assertEquals(expectedVouchers, responseVouchers);
        verify(voucherRepository, times(1)).findAll();

    }

    @Test
    void testGetAllOwnedVouchers_ValidCustomer_ReturnAllVouchers() {
        // Arrange
        List<Voucher> expectedVouchers = allVouchers;
        String customerName = "Emil";

        when(voucherRepository.findByCustomersThatOwn_Username(any(String.class))).thenReturn(allVouchers);

        // Act
        List<Voucher> responseVouchers = voucherPurchaseService.getAllOwnedVouchers(customerName);

        // Assert
        assertEquals(expectedVouchers, responseVouchers);
        verify(voucherRepository, times(1)).findByCustomersThatOwn_Username(customerName);

    }

    @Test
    void testGetAllOwnedVouchers_InvalidCustomer_ReturnEmptyList() {
        // Arrange
        List<Voucher> expectedVouchers = new ArrayList<>();
        String customerName = "Kate";

        when(voucherRepository.findByCustomersThatOwn_Username(any(String.class))).thenReturn(expectedVouchers);

        // Act
        List<Voucher> responseVouchers = voucherPurchaseService.getAllOwnedVouchers(customerName);

        // Assert
        assertEquals(expectedVouchers, responseVouchers);
        verify(voucherRepository, times(1)).findByCustomersThatOwn_Username(customerName);

    }

    @Test
    void testGetAllInCartVouchers_ValidCustomer_ReturnAllVouchersInCart() {
        // Arrange
        List<Voucher> expectedVouchers = List.of(voucherInCart);
        String customerName = "Emil";

        when(voucherRepository.findByCustomersInCart_Username(any(String.class))).thenReturn(List.of(voucherInCart));

        // Act
        List<Voucher> responseVouchers = voucherPurchaseService.getAllInCartVouchers(customerName);

        // Assert
        assertEquals(expectedVouchers, responseVouchers);
        verify(voucherRepository, times(1)).findByCustomersInCart_Username(customerName);

    }

    @Test
    void testGetAllInCartVouchers_InvalidCustomer_ReturnEmptyList() {
        // Arrange
        List<Voucher> expectedVouchers = new ArrayList<>();
        String customerName = "Kate";

        when(voucherRepository.findByCustomersInCart_Username(any(String.class))).thenReturn(expectedVouchers);

        // Act
        List<Voucher> responseVouchers = voucherPurchaseService.getAllInCartVouchers(customerName);

        // Assert
        assertEquals(expectedVouchers, responseVouchers);
        verify(voucherRepository, times(1)).findByCustomersInCart_Username(customerName);

    }
    
    @Test
    void testGetAddToCart_InvalidVoucher_ThrowNotExistException() {
        // Arrange
        // Retrieve Customer Emil
        String exceptionMsg = "";
        Customer customer = customers.get(1);
        Voucher voucher = new Voucher();
        List<Voucher> expectedVouchers = allVouchers;

        when(voucherRepository.findAll()).thenReturn(expectedVouchers);

        try {
            voucherPurchaseService.addToCart(customer, voucher);
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }
        assertEquals("Voucher doesn't exist!", exceptionMsg);
        verify(voucherRepository, times(1)).findAll();

    }

    @Test
    void testGetAddToCart_VoucherAlrExists_ThrowAlreadyExistsException() {
        // Arrange
        // Retrieve Customer Emil
        String exceptionMsg = "";
        Customer customer = customers.get(1);
        // Retrieve voucher that is already in cart
        Voucher voucher = allVouchers.get(0);
        customer.setVouchersCart(Set.of(voucher));
        when(voucherRepository.findAll()).thenReturn(List.of(voucherInCart));

        // Act
        try {
            voucherPurchaseService.addToCart(customer, voucher);
        } catch (AlreadyExistsException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Voucher already exists!", exceptionMsg);
        verify(voucherRepository, times(1)).findAll();

    }

    // customer.getVouchersCart() == null
    // voucher.getCustomersInCart() == null
    @Test
    void testGetAddToCart_NoVouchersInCart_AddToCart() {
        // Arrange

        // Retrieve Customer Emil
        Customer customer = customers.get(0);
        // Retrieve voucher that is not in cart
        Voucher voucher = allVouchers.get(0);
        Set<Voucher> voucherSet = new HashSet<>();
        voucherSet.add(voucher);

        when(voucherRepository.findAll()).thenReturn(allVouchers);

        // Act
        voucherPurchaseService.addToCart(customer, voucher);

        // Assert

        assertEquals(customer.getVouchersCart().contains(voucher), voucherSet.contains(voucher));
        // assert
        // assertEquals(customer.getVouchersCart(), voucherSet);
        verify(voucherRepository, times(1)).findAll();

    }

    // deleteFromCart
    @Test
    void testDeleteFromCart_CustomerDoesNotHaveVoucherInCart_ThrowNotExistException() {
        // Arrange
        // Retrieve Customer Daniel (No Voucher in cart)
        String exceptionMsg = "";
        Customer customer = customers.get(0);
        // Retrieve voucher that is already in cart
        Voucher voucher = new Voucher();

        // Act
        try {
            voucherPurchaseService.deleteFromCart(customer, voucher);
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Voucher doesn't exist!", exceptionMsg);
    }

    @Test
    void testDeleteFromCart_DifferentVoucherFromCart_ThrowNotExistException() {
        // Arrange
        // Retrieve Customer Emil
        String exceptionMsg = "";
        Customer customer = customers.get(1);
        
        // Retrieve voucher that is already in cart
        Voucher voucher = new Voucher();

        // Act
        try {
            voucherPurchaseService.deleteFromCart(customer, voucher);
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Voucher doesn't exist!", exceptionMsg);

    }

    @Test
    void testDeleteFromCart_DeleteFromCart_DeleteFromCart(){

        // Arrange
        Voucher voucher = voucherInCart;
        Customer customer = customers.get(1);
        
        when(voucherRepository.saveAndFlush(any(Voucher.class))).thenReturn(voucher);
        
        // Act
        voucherPurchaseService.deleteFromCart(customer, voucher);


        // Assert
        
        assert(voucher.getCustomersInCart().isEmpty());
        assert(customer.getVouchersCart().isEmpty());
    }

    
    
    // addOwnedVoucher
    @Test
    void testAddOwnedVoucher_InvalidVoucher_ThrowNotExistException() {
        // Arrange
        // Retrieve Customer Emil
        String exceptionMsg = "";
        Customer customer = customers.get(1);
        Voucher voucher = new Voucher();
        List<Voucher> expectedVouchers = allVouchers;

        when(voucherRepository.findAll()).thenReturn(expectedVouchers);

        try {
            voucherPurchaseService.addOwnedVoucher(customer, voucher);
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }
        assertEquals("Voucher doesn't exist!", exceptionMsg);
        verify(voucherRepository, times(1)).findAll();

    }

    @Test
    void testAddOwnedVoucher_VoucherAlrExists_ThrowAlreadyExistsException() {
        // Arrange
        // Retrieve Customer Emil
        String exceptionMsg = "";
        Customer customer = customers.get(1);
        // Retrieve voucher that is already owned
        Voucher voucher = allVouchers.get(1);
        Set<Voucher> voucherSet = new HashSet<>();
        voucherSet.add(voucher);
        customer.setVouchersCart(voucherSet);
        when(voucherRepository.findAll()).thenReturn(allVouchers);

        // Act
        try {
            voucherPurchaseService.addOwnedVoucher(customer, voucher);
        } catch (AlreadyExistsException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Voucher already exists!", exceptionMsg);
        verify(voucherRepository, times(1)).findAll();

    }

    @Test
    void testAddOwnedVoucher(){
        // Arrange
        Voucher voucher = voucherInCart;
        Customer customer = customers.get(1);
        when(voucherRepository.findAll()).thenReturn(allVouchers);
        
        // Act
        voucherPurchaseService.addOwnedVoucher(customer, voucher);

        // Assert
        vouchersOwned.add(voucher);
        assertEquals(vouchersOwned, customer.getOwnedVouchers());
        assert(customer.getVouchersCart().isEmpty());
        assert(voucher.getCustomersThatOwn().contains(customer));
        // assertEquals(vouchersOwned, customer.getOwnedVouchers());
    }

    // purchaseVouchers
    @Test
    void testPurchaseVouchers_InsufficientBalance_ThrowInsufficientBalanceException() {
        // Arrange
        // Retrieve Customer Emil
        String exceptionMsg = "";
        Customer customer = customers.get(1);
        Voucher voucher = voucherInCart;
        voucher.setValue(1);
    
        // Act
        try {
            voucherPurchaseService.purchaseVouchers(customer);
        } catch (InsufficientBalanceException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Insufficient Green Points", exceptionMsg);

    }

    @Test
    void testPurchaseVouchers(){
        // Arrange
        Customer customer = customers.get(1);
        customer.setGreenPoints(2);
        Voucher voucher = voucherInCart;
        voucher.setValue(1);

        // Act
        voucherPurchaseService.purchaseVouchers(customer);

        // Assert
        assert(customer.getVouchersCart().isEmpty());
        assertEquals(1, customer.getGreenPoints());
        assert(customer.getVouchersCart().isEmpty());
        vouchersOwned.add(voucher);
        assertEquals(vouchersOwned, customer.getOwnedVouchers());
        assert(voucher.getCustomersThatOwn().contains(customer));
    }


    // deleteOwnedVoucher
    @Test
    void testDeleteOwnedVoucher_CustomerHasNoVouchers_ThrowNotExistException() {
        // Arrange
        String exceptionMsg = "";
        Customer customer = new Customer();
        Voucher voucher = new Voucher();

        // Act
        try {
            voucherPurchaseService.deleteOwnedVoucher(customer, voucher);
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Voucher doesn't exist!", exceptionMsg);

    }

    @Test
    void testDeleteOwnedVoucher_DoesNotContainThisVoucher_ThrowNotExistException() {
        // Arrange
        // Retrieve Customer Emil
        String exceptionMsg = "";
        Customer customer = customers.get(1);
        Voucher voucher = new Voucher();

        // Act
        try {
            voucherPurchaseService.deleteOwnedVoucher(customer, voucher);
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Voucher doesn't exist!", exceptionMsg);

    }


}
