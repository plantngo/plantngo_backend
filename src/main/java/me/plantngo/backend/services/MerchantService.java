package me.plantngo.backend.services;

import java.util.List;

import me.plantngo.backend.DTO.UpdateCustomerDetailsDTO;
import me.plantngo.backend.DTO.UpdateMerchantDetailsDTO;
import me.plantngo.backend.exceptions.AlreadyExistsException;
import me.plantngo.backend.models.Customer;
import me.plantngo.backend.repositories.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.plantngo.backend.exceptions.UserNotFoundException;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.repositories.MerchantRepository;

@Service
public class MerchantService {
    
    private MerchantRepository merchantRepository;

    private CustomerRepository customerRepository;

    @Autowired
    public MerchantService(MerchantRepository merchantRepository, CustomerRepository customerRepository) {
        this.merchantRepository = merchantRepository;
        this.customerRepository = customerRepository;
    }

    public Merchant getMerchantByUsername(String username) {
        if (merchantRepository.findByUsername(username).isEmpty()) {
            throw new UserNotFoundException("Username not found");
        }
        return merchantRepository.findByUsername(username).get();
    }

    public Merchant getMerchantById(Integer id){
        if (merchantRepository.findById(id).isEmpty()) {
            throw new UserNotFoundException();
        }
        return merchantRepository.findById(id).get();
    }
    
    public Merchant getMerchantByEmail(String email) {
        if (merchantRepository.findByEmail(email).isEmpty()) {
            throw new UserNotFoundException("Email not found");
        }
        return merchantRepository.findByEmail(email).get();
    }

    public List<Merchant> findAll() {
        return merchantRepository.findAll();
    }

    public List<Merchant> findMerchantsInRange(double latitude, double longitude) {
        // some logic to filter out by location
        return merchantRepository.findAll();
    }

    public Merchant updateMerchant(String username, UpdateMerchantDetailsDTO updateMerchantDetailsDTO) {

        // Check if new username is already taken
        if (merchantRepository.existsByUsername(updateMerchantDetailsDTO.getUsername()) || customerRepository.existsByUsername(updateMerchantDetailsDTO.getUsername())) {
            throw new AlreadyExistsException("Username");
        }

        Merchant merchant = this.getMerchantByUsername(username);

        // Updating Merchant
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setSkipNullEnabled(true);
        mapper.map(updateMerchantDetailsDTO, merchant);

        merchantRepository.saveAndFlush(merchant);

        return merchant;
    }
}
