package me.plantngo.backend.services;

import java.util.List;
import java.util.Optional;

import me.plantngo.backend.DTO.UpdateMerchantDetailsDTO;
import me.plantngo.backend.exceptions.AlreadyExistsException;
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

    /**
     * Gets merchant with given username
     * 
     * @param username
     * @return
     */
    public Merchant getMerchantByUsername(String username) {
        Optional<Merchant> optionalMerchant = merchantRepository.findByUsername(username);
        if (optionalMerchant.isEmpty()) {
            throw new UserNotFoundException("Username not found");
        }
        return optionalMerchant.get();
    }

    /**
     * Gets merchant with given Id
     * 
     * @param id
     * @return
     */
    public Merchant getMerchantById(Integer id){
        if (merchantRepository.findById(id).isEmpty()) {
            throw new UserNotFoundException();
        }
        return merchantRepository.findById(id).get();
    }
    
    /**
     * Gets merchant with given email
     * 
     * @param email
     * @return
     */
    public Merchant getMerchantByEmail(String email) {
        Optional<Merchant> optionalMerchant = merchantRepository.findByEmail(email);
        if (optionalMerchant.isEmpty()) {
            throw new UserNotFoundException("Email not found");
        }
        return optionalMerchant.get();
    }

    /**
     * Gets merchant with given company
     * 
     * @param company
     * @return
     */
    public Merchant getMerchantByCompany(String company) {
        Optional<Merchant> optionalMerchant = merchantRepository.findByCompany(company);
        if (optionalMerchant.isEmpty()) {
            throw new UserNotFoundException("Company not found");
        }
        return optionalMerchant.get();
    }

    /**
     * Gets all merchants
     * 
     * @return
     */
    public List<Merchant> findAll() {
        return merchantRepository.findAll();
    }

    public List<Merchant> findMerchantsInRange(double latitude, double longitude) {
        // some logic to filter out by location
        return merchantRepository.findAll();
    }

    /**
     * Updates merchant with given username using updateMerchantDetailsDTO
     * 
     * @param username
     * @param updateMerchantDetailsDTO
     * @return
     */
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
