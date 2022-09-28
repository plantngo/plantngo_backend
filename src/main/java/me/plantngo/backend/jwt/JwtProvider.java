package me.plantngo.backend.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import me.plantngo.backend.exceptions.UserNotFoundException;
import me.plantngo.backend.models.Customer;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.repositories.CustomerRepository;
import me.plantngo.backend.repositories.MerchantRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtProvider {

    private CustomerRepository customerRepo;
    private MerchantRepository merchantRepo;
    @Value("not-secret")
    private String SECRET_KEY;

    // 24 hours expiration date - change last digit to determine hours
    public static final long JWT_TOKEN_VALIDITY = 1000 * 5 * 60 * 60 * 24;

    @PostConstruct
    protected void init() {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    public JwtProvider(CustomerRepository customerRepo, MerchantRepository merchantRepo) {
        this.customerRepo = customerRepo;
        this.merchantRepo = merchantRepo;
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    // resolves claim from user's token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    // extracts the expiration date from the existing token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Check whether token has expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // generates token by calling createToken
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        Optional<Customer> search = customerRepo.findByUsername(userDetails.getUsername());
        Optional<Merchant> search2 = merchantRepo.findByUsername(userDetails.getUsername());
        String token = "";

        if (search.isPresent()){
            claims.put("Authority",search.get().getAUTHORITY());
            token = createToken(claims, search.get().getUsername());
        } else if (search2.isPresent()){
            claims.put("Authority",search2.get().getAUTHORITY());
            token = createToken(claims, search2.get().getUsername());
        } else {
            throw new UserNotFoundException();
        }

        return token;
    }

    // checks if user exists and token is not expired
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // extracts the jwt string from the header
    public String resolveToken(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");

        String jwt = null;

        if (Objects.nonNull(authorizationHeader)) {
            jwt = authorizationHeader.substring(7);
        }

        return jwt;
    }
}
