package me.plantngo.backend.services;

import me.plantngo.backend.DTO.UpdateCustomerDetailsDTO;
import me.plantngo.backend.DTO.UpdateMerchantDetailsDTO;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ResetPasswordService {
    
    private CustomerRepository customerRepository;

    private MerchantRepository merchantRepository;

    private ChangeCredentialService changeCredentialService;

    private MailService mailService;

    @Autowired
    public ResetPasswordService(CustomerRepository customerRepository, MerchantRepository merchantRepository,
                                ChangeCredentialService changeCredentialService, MailService mailService) {
        this.customerRepository = customerRepository;
        this.merchantRepository = merchantRepository;
        this.changeCredentialService = changeCredentialService;
        this.mailService = mailService;
    }

    public ResponseEntity<String> setResetPasswordTokenAndSendEmail(String email){
        Customer customer = null;
        Merchant merchant = null;
        boolean isCustomer;

        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
        try{
            if(optionalCustomer.isPresent()){
                customer = optionalCustomer.get();
                isCustomer = true;
            } else {
                merchant = merchantRepository.findByEmail(email).get();
                isCustomer = false;
            }
        } catch (NoSuchElementException e) {
            throw new NotExistException("Account");
        }

        if(isCustomer) {
            if(!customer.getEmail().equals(email)) throw new IllegalArgumentException("Email entered is incorrect");
            if(customer.getResetPasswordToken() != null) throw new AlreadyExistsException("Reset password token");

            customer.setResetPasswordToken(RandomString.make(16));
            customerRepository.saveAndFlush(customer);

            mailService.sendSimpleMessage(email, "PlantNGo Password Reset", customer.getResetPasswordToken());
        } else {
            if(!merchant.getEmail().equals(email)) throw new IllegalArgumentException("Email entered is incorrect");
            if(merchant.getResetPasswordToken() != null) throw new AlreadyExistsException("Reset password token");

            merchant.setResetPasswordToken(RandomString.make(16));
            merchantRepository.saveAndFlush(merchant);

            mailService.sendSimpleMessage(email, "PlantNGo Password Reset", merchant.getResetPasswordToken());
        }

        return new ResponseEntity<>("An email has been sent to you. Please check it for a code to reset your password!", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> checkAndDeleteTokenAndChangePasswordIfCorrectResetPasswordToken(String email, String resetPasswordToken, String newPassword){

        Customer customer = null;
        Merchant merchant = null;
        boolean isCustomer;

        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
        try{
            if(optionalCustomer.isPresent()){
                customer = optionalCustomer.get();
                isCustomer = true;
            } else {
                merchant = merchantRepository.findByEmail(email).get();
                isCustomer = false;
            }
        } catch (NoSuchElementException e) {
            throw new NotExistException("Account");
        }

        /*
        we delete the reset password token here if it exists
         */
        if(isCustomer) {
            if(customer.getResetPasswordToken() == null) throw new NotExistException("Reset password token");

            //wrong token
            if(!customer.getResetPasswordToken().equals(resetPasswordToken)) throw new IllegalArgumentException("Reset password token is incorrect. Please try again.");

            customer.setResetPasswordToken(null);
            customerRepository.saveAndFlush(customer);
        } else {
            if(merchant.getResetPasswordToken() == null) throw new NotExistException("Reset password token");

            //wrong token
            if(!merchant.getResetPasswordToken().equals(resetPasswordToken)) throw new IllegalArgumentException("Reset password token is incorrect. Please try again.");

            merchant.setResetPasswordToken(null);
            merchantRepository.saveAndFlush(merchant);
        }

        /*
        we reset the user's password here
         */

        resetUserPassword(isCustomer, isCustomer ? customer : merchant, newPassword);


        return new ResponseEntity<>("Token is correct. Password reset.", HttpStatus.OK);
    }

    private void resetUserPassword(boolean isCustomer, Object user, String newPassword) {
        Customer customer = null;
        Merchant merchant = null;

        if(isCustomer) {
            customer = (Customer) user;
            changeCredentialService.replacePassword(customer.getUsername(), newPassword, 'C');
        }
        else {
            merchant = (Merchant) user;
            changeCredentialService.replacePassword(merchant.getUsername(), newPassword, 'M');
        }
    }
}
