package me.plantngo.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import me.plantngo.backend.models.Customer;
import me.plantngo.backend.models.CustomerDetails;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.models.MerchantDetails;
import me.plantngo.backend.models.RegistrationDTO;
import me.plantngo.backend.models.LoginDTO;
import me.plantngo.backend.repositories.CustomerRepository;
import me.plantngo.backend.repositories.MerchantRepository;

@Service
public class AuthManager {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public AuthManager(CustomerRepository customerRepository, MerchantRepository merchantRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.customerRepository = customerRepository;
        this.merchantRepository = merchantRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public ResponseEntity<String> authenticateCustomer(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),
                        loginDTO.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE).body(customerDetails.toString());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE).body(userDetails.toString());

    }

    public ResponseEntity<String> authenticateMerchant(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),
                        loginDTO.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE).body(userDetails.toString());
    }

    public ResponseEntity<String> registerCustomer(RegistrationDTO registrationDTO) {

        // Check if email is already in use
        if (customerRepository.existsByEmail(registrationDTO.getEmail())
                || merchantRepository.existsByEmail(registrationDTO.getEmail())) {
            return new ResponseEntity<>("Email already taken!", HttpStatus.BAD_REQUEST);
        }

        // Check if username is already in use
        if (customerRepository.existsByUsername(registrationDTO.getUsername()) || merchantRepository.existsByUsername(registrationDTO.getUsername())) {
            return new ResponseEntity<>("Username already taken!", HttpStatus.BAD_REQUEST);
        }

        Customer customer = new Customer();
        customer.setEmail(registrationDTO.getEmail());
        customer.setUsername(registrationDTO.getUsername());
        customer.setPassword(bCryptPasswordEncoder.encode(registrationDTO.getPassword()));
        customer.setGreenPts(0);

        customerRepository.save(customer);

        return new ResponseEntity<>("Customer registered!", HttpStatus.CREATED);
    }

    public ResponseEntity<String> registerMerchant(RegistrationDTO registrationDTO) {

        // Check if email is already in use
        if (merchantRepository.existsByEmail(registrationDTO.getEmail())
                || customerRepository.existsByEmail(registrationDTO.getEmail())) {
            return new ResponseEntity<>("Email already taken!", HttpStatus.BAD_REQUEST);
        }

        // Check if username is already in use
        if (merchantRepository.existsByUsername(registrationDTO.getUsername()) || customerRepository.existsByUsername(registrationDTO.getUsername())) {
            return new ResponseEntity<>("Username already taken!", HttpStatus.BAD_REQUEST);
        }

        Merchant merchant = new Merchant();
        merchant.setEmail(registrationDTO.getEmail());
        merchant.setUsername(registrationDTO.getUsername());
        merchant.setPassword(bCryptPasswordEncoder.encode(registrationDTO.getPassword()));
        merchant.setCompany(registrationDTO.getCompany());

        merchantRepository.save(merchant);

        return new ResponseEntity<>("Merchant registered!", HttpStatus.CREATED);

    }
}
