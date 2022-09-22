package me.plantngo.backend.repositories;

import me.plantngo.backend.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    public Optional<Account> findByUsername(String username);
    public Optional<Account> findById(Integer id);

    public Optional<Account> findByEmail(String email);
}