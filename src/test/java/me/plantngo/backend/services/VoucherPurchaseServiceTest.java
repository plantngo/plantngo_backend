package me.plantngo.backend.services;

import me.plantngo.backend.exceptions.AlreadyExistsException;
import me.plantngo.backend.exceptions.NotExistException;
import me.plantngo.backend.models.Customer;
import me.plantngo.backend.models.Voucher;
import me.plantngo.backend.repositories.CustomerRepository;
import me.plantngo.backend.repositories.VoucherRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
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
    private List<Voucher> voucherInCart;

    private Voucher voucher;

    private List<Customer> customers;

    private Customer customer;

    @BeforeEach
    public void initEach() {

        allVouchers = new ArrayList<>();
        voucherInCart = new ArrayList<>();
        customers = new ArrayList<>();

        customer = new Customer();
        customer.setUsername("Daniel");
        customers.add(customer);

        customer = new Customer();
        customer.setUsername("Emil");
        customers.add(customer);

        // One voucher that only Emil owns
        // Emil has it in cart
        voucher = new Voucher();
        voucher.setCustomersThatOwn(List.of(customer));
        voucher.setCustomersInCart(List.of(customer));
        voucherInCart.add(voucher);
        allVouchers.add(voucher);

        // One voucher that both Daniel and Emil owns
        voucher = new Voucher();
        voucher.setCustomersThatOwn(customers);
        allVouchers.add(voucher);

        // One voucher that both Daniel and Emil owns
        // Both Daniel and Emil has it in cart
        voucher = new Voucher();
        voucher.setCustomersThatOwn(customers);
        voucher.setCustomersInCart(customers);
        voucherInCart.add(voucher);
        allVouchers.add(voucher);

    }

    @Test
    public void testGetAllVouchers_AllVouchers_ReturnAllVouchers() {
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
    public void testGetAllVouchers_NoVouchers_ReturnEmptyList() {
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
    public void testGetAllOwnedVouchers_ValidCustomer_ReturnAllVouchers() {
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
    public void testGetAllOwnedVouchers_InvalidCustomer_ReturnEmptyList() {
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
    public void testGetAllInCartVouchers_ValidCustomer_ReturnAllVouchersInCart() {
        // Arrange
        List<Voucher> expectedVouchers = voucherInCart;
        String customerName = "Emil";

        when(voucherRepository.findByCustomersInCart_Username(any(String.class))).thenReturn(voucherInCart);

        // Act
        List<Voucher> responseVouchers = voucherPurchaseService.getAllInCartVouchers(customerName);

        // Assert
        assertEquals(expectedVouchers, responseVouchers);
        verify(voucherRepository, times(1)).findByCustomersInCart_Username(customerName);

    }

    @Test
    public void testGetAllInCartVouchers_InvalidCustomer_ReturnEmptyList() {
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
    public void testGetAddToCart_InvalidVoucher_ThrowNotExistException() {
        // Arrange
        // Retrieve Customer Emil
        String exceptionMsg = "";
        Customer customer = customers.get(1);
        Voucher voucher = new Voucher();
        List<Voucher> expectedVouchers = new ArrayList<>();

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
    public void testGetAddToCart_VoucherAlrExists_ThrowAlreadyExistsException() {
        // Arrange
        // Retrieve Customer Emil
        String exceptionMsg = "";
        Customer customer = customers.get(1);
        // Retrieve voucher that is already in cart
        Voucher voucher = allVouchers.get(0);
        customer.setVouchersCart(Set.of(voucher));
        when(voucherRepository.findAll()).thenReturn(voucherInCart);

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
    // @Test
    // public void testGetAddToCart_NoVouchersInCart_AddToCart() {
    //     // Arrange

    //     // Retrieve Customer Emil
    //     Customer customer = customers.get(0);
    //     // Retrieve voucher that is not in cart
    //     Voucher voucher = allVouchers.get(0);

    //     when(voucherRepository.findAll()).thenReturn(allVouchers);

    //     // Act
    //     voucherPurchaseService.addToCart(customer, voucher);

    //     // Assert
    //     assertEquals(customer.getVouchersCart(), Set.of(voucher));
    //     verify(voucherRepository, times(1)).findAll();

    // }


    
    // deleteFromCart
    // addOwnedVoucher
    // purchaseVouchers
    // deleteOwnedVoucher
}
