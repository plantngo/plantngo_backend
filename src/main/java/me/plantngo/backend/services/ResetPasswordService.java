package me.plantngo.backend.services;

import me.plantngo.backend.exceptions.AlreadyExistsException;
import me.plantngo.backend.exceptions.InvalidUserTypeException;
import me.plantngo.backend.exceptions.NotExistException;
import me.plantngo.backend.exceptions.UserNotFoundException;
import me.plantngo.backend.models.Customer;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.repositories.CustomerRepository;
import me.plantngo.backend.repositories.MerchantRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ResetPasswordService {
    CustomerRepository customerRepository;
    MerchantRepository merchantRepository;

    @Autowired
    public ResetPasswordService(CustomerRepository customerRepository, MerchantRepository merchantRepository) {
        this.customerRepository = customerRepository;
        this.merchantRepository = merchantRepository;
    }

    public String getAndSetResetPassWordToken(String username, String email, Character userType){
        if(userType == 'C'){
            Customer customer = getCustomerByUsername(username);

            if(!customer.getEmail().equals(email)) throw new IllegalArgumentException("Email entered is incorrect");
            if(customer.getResetPasswordToken() != null) throw new AlreadyExistsException("Reset password token");

            customer.setResetPasswordToken(RandomString.make(16));
            customerRepository.saveAndFlush(customer);

            return customer.getResetPasswordToken();

        } else if(userType == 'M'){
            Merchant merchant = getMerchantByUsername(username);

            if(!merchant.getEmail().equals(email)) throw new IllegalArgumentException("Email entered is incorrect");
            if(merchant.getResetPasswordToken() != null) throw new AlreadyExistsException("Reset password token");

            merchant.setResetPasswordToken(RandomString.make(16));
            merchantRepository.saveAndFlush(merchant);

            return merchant.getResetPasswordToken();
        } else {
            throw new InvalidUserTypeException();
        }
    }

    public ResponseEntity<String> checkAndDeleteIfCorrectResetPasswordToken(String username, Character userType, String resetPasswordToken){
        if(userType == 'C') {
            Customer customer = getCustomerByUsername(username);

            if(customer.getResetPasswordToken() == null) throw new NotExistException("Reset password token");

            //wrong token
            if(!customer.getResetPasswordToken().equals(resetPasswordToken)) throw new IllegalArgumentException("Reset password token is incorrect. Please try again.");

            customer.setResetPasswordToken(null);
            customerRepository.saveAndFlush(customer);

            return new ResponseEntity<>("Token is correct.", HttpStatus.OK);
        } else if(userType == 'M') {
            Merchant merchant = getMerchantByUsername(username);

            if(merchant.getResetPasswordToken() == null) throw new NotExistException("Reset password token");

            //wrong token
            if(!merchant.getResetPasswordToken().equals(resetPasswordToken)) throw new IllegalArgumentException("Reset password token is incorrect. Please try again.");

            merchant.setResetPasswordToken(null);
            merchantRepository.saveAndFlush(merchant);

            return new ResponseEntity<>("Token is correct.", HttpStatus.OK);
        } else {
            throw new InvalidUserTypeException();
        }
    }

    private Customer getCustomerByUsername(String username){
        try{
            return customerRepository.findByUsername(username).get();
        } catch (NoSuchElementException e) {
            throw new UserNotFoundException("User does not exist");
        }
    }

    private Merchant getMerchantByUsername(String username){
        try{
            return merchantRepository.findByUsername(username).get();
        } catch (NoSuchElementException e) {
            throw new UserNotFoundException("User does not exist");
        }
    }
}
