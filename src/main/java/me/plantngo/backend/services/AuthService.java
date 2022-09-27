package me.plantngo.backend.services;

import me.plantngo.backend.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Service;

import me.plantngo.backend.models.Customer;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.models.RegistrationDTO;
import me.plantngo.backend.models.LoginDTO;
import me.plantngo.backend.repositories.CustomerRepository;
import me.plantngo.backend.repositories.MerchantRepository;

@Service
public class AuthService {

    private CustomerRepository customerRepository;
    private MerchantRepository merchantRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;

    @Autowired
    public AuthService(CustomerRepository customerRepository, MerchantRepository merchantRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.customerRepository = customerRepository;
        this.merchantRepository = merchantRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    public ResponseEntity<String> authenticateCustomer(LoginDTO loginDTO) {
        if (!customerRepository.existsByUsername(loginDTO.getUsername())) {
            return new ResponseEntity<>("Not a customer!", HttpStatus.BAD_REQUEST); 
        }
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),
                        loginDTO.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwt = "";
        if (userDetails != null){
            jwt = jwtProvider.generateToken(userDetails);
        }

        return ResponseEntity.ok().header("jwt", jwt).body("Customer Login Success!");
    }

    public ResponseEntity<String> authenticateMerchant(LoginDTO loginDTO) {

        if (!merchantRepository.existsByUsername(loginDTO.getUsername())) {
            return new ResponseEntity<>("Not a merchant!", HttpStatus.BAD_REQUEST); 
        }

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),
                        loginDTO.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwt = "";
        if (userDetails != null){
            jwt = jwtProvider.generateToken(userDetails);
        }

        return ResponseEntity.ok().header("jwt", jwt).body("Merchant Login Success!");
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
