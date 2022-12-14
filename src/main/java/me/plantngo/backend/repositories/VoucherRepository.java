package me.plantngo.backend.repositories;

import me.plantngo.backend.models.Voucher;
import me.plantngo.backend.models.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {

    Optional<Voucher> findByIdAndMerchant(Integer id, Merchant merchant);

    Boolean existsByIdAndMerchant(Integer id, Merchant merchant);

    List<Voucher> findAllByMerchant(Merchant merchant);

    List<Voucher> findByCustomersThatOwn_Username(String username);

    List<Voucher> findByCustomersInCart_Username(String username);
}
