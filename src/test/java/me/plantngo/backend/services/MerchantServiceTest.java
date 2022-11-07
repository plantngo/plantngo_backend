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
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.repositories.MerchantRepository;

@ExtendWith(MockitoExtension.class)
public class MerchantServiceTest {

    @Mock
    private MerchantRepository merchantRepository;
    @InjectMocks
    private MerchantService merchantService;

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


  


}
