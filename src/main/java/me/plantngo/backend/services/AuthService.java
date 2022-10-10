package me.plantngo.backend.services;

import me.plantngo.backend.exceptions.FailedRegistrationException;
import me.plantngo.backend.exceptions.InvalidUserTypeException;
import me.plantngo.backend.jwt.JwtProvider;
import org.modelmapper.ModelMapper;
import javax.security.auth.login.FailedLoginException;
import org.springframework.beans.factory.annotation.Autowired;
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
import me.plantngo.backend.DTO.RegistrationDTO;
import me.plantngo.backend.DTO.LoginDTO;
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
            BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager,
            JwtProvider jwtProvider) {
        this.customerRepository = customerRepository;
        this.merchantRepository = merchantRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    public ResponseEntity<String> authenticateUser(LoginDTO loginDTO) {

        Authentication authentication = null;

        // throws AuthenticationException
        authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),
                        loginDTO.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwt = jwtProvider.generateToken(userDetails);

        return ResponseEntity.ok().header("jwt", jwt).body(" Login Success!");
    }

    public ResponseEntity<String> registerUser(RegistrationDTO registrationDTO) {
        if (registrationDTO.getUserType() == 'C') {
            return registerCustomer(registrationDTO);
        } else if (registrationDTO.getUserType() == 'M') {
            return registerMerchant(registrationDTO);
        }
        throw new InvalidUserTypeException();
    }

    public ResponseEntity<String> registerCustomer(RegistrationDTO registrationDTO) {

        // Check if email is already in use
        if (customerRepository.existsByEmail(registrationDTO.getEmail())
                || merchantRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new FailedRegistrationException("Email already taken!");
        }

        // Check if username is already in use
        if (customerRepository.existsByUsername(registrationDTO.getUsername())
                || merchantRepository.existsByUsername(registrationDTO.getUsername())) {
            throw new FailedRegistrationException("Username already taken!");
        }

        Customer customer = this.customerMapToEntity(registrationDTO);

        customerRepository.save(customer);

        return new ResponseEntity<>("Customer registered!", HttpStatus.CREATED);
    }

    public ResponseEntity<String> registerMerchant(RegistrationDTO registrationDTO) {

        // Check if email is already in use
        if (merchantRepository.existsByEmail(registrationDTO.getEmail())
                || customerRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new FailedRegistrationException("Email already taken!");
        }

        // Check if username is already in use
        if (merchantRepository.existsByUsername(registrationDTO.getUsername())
                || customerRepository.existsByUsername(registrationDTO.getUsername())) {
            throw new FailedRegistrationException("Username already taken!");
        }

        if (registrationDTO.getCompany() == null) {
            throw new FailedRegistrationException("Missing company");
        }

        Merchant merchant = new Merchant();
        merchant.setEmail(registrationDTO.getEmail());
        merchant.setUsername(registrationDTO.getUsername());
        merchant.setPassword(bCryptPasswordEncoder.encode(registrationDTO.getPassword()));
        merchant.setCompany(registrationDTO.getCompany());
        merchant.setCategories(null);

        merchantRepository.save(merchant);

        return new ResponseEntity<>("Merchant registered!", HttpStatus.CREATED);

    }

    private Customer customerMapToEntity(RegistrationDTO registrationDTO) {
        registrationDTO.setPassword(bCryptPasswordEncoder.encode(registrationDTO.getPassword()));
        ModelMapper mapper = new ModelMapper();

        Customer customer = mapper.map(registrationDTO, Customer.class);
        return customer;
    }

    private Merchant merchantMapToEntity(RegistrationDTO registrationDTO) {
        registrationDTO.setPassword(bCryptPasswordEncoder.encode(registrationDTO.getPassword()));
        ModelMapper mapper = new ModelMapper();

        Merchant merchant = mapper.map(registrationDTO, Merchant.class);
        return merchant;
    }
}
