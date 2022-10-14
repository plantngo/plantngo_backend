package me.plantngo.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.plantngo.backend.exceptions.NotExistException;
import me.plantngo.backend.exceptions.UserNotFoundException;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.repositories.MerchantRepository;

@Service
public class MerchantService {
    
    private MerchantRepository merchantRepository;

    @Autowired
    public MerchantService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
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
    

}
