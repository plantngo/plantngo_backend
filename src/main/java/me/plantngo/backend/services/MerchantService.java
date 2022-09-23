package me.plantngo.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            System.err.println("User doesn't exist");
            return null;
        }
        return merchantRepository.findByUsername(username).get();
    }
    
    public Merchant getMerchantByEmail(String email) {
        if (merchantRepository.findByEmail(email).isEmpty()) {
            System.out.println("Email doesn't exist");
            return null;
        }
        return merchantRepository.findByEmail(email).get();
    }

    public List<Merchant> findAll() {
        return merchantRepository.findAll();
    }
    

}
