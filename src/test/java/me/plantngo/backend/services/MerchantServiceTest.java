package me.plantngo.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import me.plantngo.backend.DTO.UpdateMerchantDetailsDTO;
import me.plantngo.backend.exceptions.AlreadyExistsException;
import me.plantngo.backend.exceptions.UserNotFoundException;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.repositories.CustomerRepository;
import me.plantngo.backend.repositories.MerchantRepository;

@ExtendWith(MockitoExtension.class)
public class MerchantServiceTest {

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private MerchantService merchantService;

    @BeforeEach
    public void initEach() {
        
    }


    @Test
    void testFindAll_AllMerchants_ReturnAllMerchants(){

        // arrange
        Merchant merchant1 = new Merchant();
        merchant1.setUsername("Fairprice");
        Merchant merchant2 = new Merchant();
        merchant2.setUsername("Pizzahut");
        Merchant merchant3 = new Merchant();
        merchant3.setUsername("Greendot");
        Merchant merchant4 = new Merchant();
        merchant4.setUsername("Fairprice");
        List<Merchant> merchantList = List.of(merchant1, merchant2, merchant3, merchant4);

        when(merchantRepository.findAll()).thenReturn(merchantList);

        // act 
        List<Merchant> responseList = merchantService.findAll();
        
        //assert
        verify(merchantRepository, times(1)).findAll();
        assertEquals(merchantList, responseList);
        
    }

    @Test
    void testFindAll_NoMerchants_ReturnEmptyList(){

        // arrange
        when(merchantRepository.findAll()).thenReturn(new ArrayList<Merchant>());

        // act 
        List<Merchant> responseList = merchantService.findAll();
        
        //assert
        verify(merchantRepository, times(1)).findAll();
        assertEquals(responseList, new ArrayList<Merchant>());
        
    }
    
    @Test
    void testGetMerchantByUsername_Exist_ReturnMerchant(){

        // arrange
        Merchant merchant = new Merchant();
        merchant.setUsername("John Doe");

        Optional<Merchant> optionalMerchant = Optional.of(merchant);
        when(merchantRepository.findByUsername(any(String.class))).thenReturn(optionalMerchant);

        // act 
        Merchant responseMerchant = merchantService.getMerchantByUsername(merchant.getUsername());

        //assert
        assertEquals(responseMerchant, merchant);
        verify(merchantRepository, times(1)).findByUsername(merchant.getUsername());

    }

    @Test
    void testGetMerchantByUsername_NotFound_ThrowUserNotFound(){

        // arrange
        Merchant merchant = new Merchant();
        merchant.setUsername("John Doe");
        String merchantUsernameToSearch = "Mary Jane";

        when(merchantRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        // act and assert
        Exception exception = assertThrows(UserNotFoundException.class, () -> merchantService.getMerchantByUsername(merchantUsernameToSearch));

        //assert
        verify(merchantRepository, times(1)).findByUsername(merchantUsernameToSearch);
        assertEquals(exception.getMessage(), "User Not Found:Username not found");
        
    }
    
    @Test
    void testGetMerchantByEmail_Exist_ReturnMerchant(){

        // arrange
        Merchant merchant = new Merchant();
        merchant.setEmail("john.doe@example.com");;

        Optional<Merchant> optionalMerchant = Optional.of(merchant);
        when(merchantRepository.findByEmail(any(String.class))).thenReturn(optionalMerchant);

        // act 
        Merchant responseMerchant = merchantService.getMerchantByEmail(merchant.getEmail());

        //assert
        assertEquals(responseMerchant, merchant);
        verify(merchantRepository, times(1)).findByEmail(merchant.getEmail());

    }

    @Test
    void testGetMerchantByEmail_NotFound_ThrowUserNotFound(){

        // arrange
        Merchant merchant = new Merchant();
        merchant.setEmail("john.doe@example.com");
        String merchantEmailToSearch = "mary.jane@example.com";
        
        when(merchantRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        // act and assert
        Exception exception = assertThrows(UserNotFoundException.class, () -> merchantService.getMerchantByEmail(merchantEmailToSearch));

        //assert
        verify(merchantRepository, times(1)).findByEmail(merchantEmailToSearch);
        assertEquals(exception.getMessage(), "User Not Found:Email not found");
        
    }

    @Test
    void testFindMerchantInRange_AllMerchants_ReturnAllMerchants(){

        // arrange
        Merchant merchant1 = new Merchant();
        merchant1.setUsername("Fairprice");
        Merchant merchant2 = new Merchant();
        merchant2.setUsername("Pizzahut");
        Merchant merchant3 = new Merchant();
        merchant3.setUsername("Greendot");
        Merchant merchant4 = new Merchant();
        merchant4.setUsername("Fairprice");
        List<Merchant> merchantList = List.of(merchant1, merchant2, merchant3, merchant4);

        when(merchantRepository.findAll()).thenReturn(merchantList);

        // act 
        List<Merchant> responseList = merchantService.findMerchantsInRange(1.0,1.0);
        
        //assert
        verify(merchantRepository, times(1)).findAll();
        assertEquals(merchantList, responseList);
        
    }

    @Test
    void testGetMerchantByCompany_Exist_ReturnMerchant(){
        // Arrange
        Merchant expectedMerchant = new Merchant();
        expectedMerchant.setCompany("ExampleCompany");
        String merchantCompanyToSearch = "ExampleCompany";
        Optional<Merchant> optionalMerchant = Optional.of(expectedMerchant);
        when(merchantRepository.findByCompany(any(String.class))).thenReturn(optionalMerchant);

        // Act
        Merchant responseMerchant = merchantService.getMerchantByCompany(expectedMerchant.getCompany());
        // Assert
        assertEquals(expectedMerchant, responseMerchant);
        verify(merchantRepository, times(1)).findByCompany(any(String.class));
    }

    @Test
    void testGetMerchantByCompany_NotFound_ThrowUserNotFound(){
        // Arrange
        Merchant expectedMerchant = new Merchant();
        expectedMerchant.setCompany("ExampleCompany");
        String merchantCompanyToSearch = "WrongExampleCompany";
        
        when(merchantRepository.findByCompany(any(String.class))).thenReturn(Optional.empty());

        // Act and assert
        Exception exception = assertThrows(UserNotFoundException.class, () -> merchantService.getMerchantByCompany(merchantCompanyToSearch));

        // Assert
        verify(merchantRepository, times(1)).findByCompany(merchantCompanyToSearch);
        assertEquals(exception.getMessage(), "User Not Found:Company not found");
        
    }


    @Test
    void testUpdateMerchant_UsernameExist_ThrowAlreadyExists(){
        // Arrange
        when(merchantRepository.existsByUsername(any(String.class))).thenReturn(true);

        Merchant merchant = new Merchant();
        merchant.setUsername("Fairprice");

        UpdateMerchantDetailsDTO update = new UpdateMerchantDetailsDTO("USERNAMEUSED", null, null, null, null, null, null);

        // Act
        Exception exception = assertThrows(AlreadyExistsException.class, () -> merchantService.updateMerchant("Fairprice", update));
        verify(merchantRepository, times(1)).existsByUsername(anyString());
        assertEquals("Username already exists!",exception.getMessage());
    }

    @Test
    void testUpdateMerchant_MerchantExists_ReturnMerchant(){
        
        // Arrange
        String username = "Daniel";
        UpdateMerchantDetailsDTO updateMerchantDetailsDTO = new UpdateMerchantDetailsDTO("Jacky", "jacky@yahoo.com.sg", null, null, null, null, null);
        Merchant merchant = new Merchant();
        merchant.setUsername("Daniel");
        Merchant expectedMerchant = new Merchant();
        expectedMerchant.setUsername(updateMerchantDetailsDTO.getUsername());
        expectedMerchant.setEmail(updateMerchantDetailsDTO.getEmail());

        when(merchantRepository.existsByUsername(anyString()))
            .thenReturn(false);
        when(customerRepository.existsByUsername(anyString()))
            .thenReturn(false);
        when(merchantRepository.findByUsername(anyString()))
            .thenReturn(Optional.of(merchant));
        when(merchantRepository.saveAndFlush(any(Merchant.class)))
            .thenReturn(expectedMerchant);

        // Act
        Merchant responseMerchant = merchantService.updateMerchant(username, updateMerchantDetailsDTO);

        // Assert
        assertEquals(expectedMerchant, responseMerchant);
        verify(merchantRepository, times(1)).existsByUsername(updateMerchantDetailsDTO.getUsername());
        verify(customerRepository, times(1)).existsByUsername(updateMerchantDetailsDTO.getUsername());
        verify(merchantRepository, times(1)).findByUsername(username);
        verify(merchantRepository, times(1)).saveAndFlush(expectedMerchant);
    }

    @Test
    void testUpdateMerchant_UsernameAlreadyExistsInMerchant_ThrowAlreadyExistsException(){
        
        // Arrange
        String username = "Daniel";
        UpdateMerchantDetailsDTO updateMerchantDetailsDTO = new UpdateMerchantDetailsDTO("Jacky", "jacky@yahoo.com.sg", null, null, null, null, null);
        String exceptionMsg = "";

        when(merchantRepository.existsByUsername(anyString()))
            .thenReturn(true);

        // Act
        try {
            Merchant responseMerchant = merchantService.updateMerchant(username, updateMerchantDetailsDTO);
        } catch (AlreadyExistsException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Username already exists!", exceptionMsg);
        verify(merchantRepository, times(1)).existsByUsername(updateMerchantDetailsDTO.getUsername());
    }

}
