package me.plantngo.backend.services;

import me.plantngo.backend.DTO.VoucherDTO;
import me.plantngo.backend.exceptions.AlreadyExistsException;
import me.plantngo.backend.exceptions.InsufficientBalanceException;
import me.plantngo.backend.exceptions.NotExistException;
import me.plantngo.backend.models.Customer;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.models.Voucher;
import me.plantngo.backend.repositories.CustomerRepository;
import me.plantngo.backend.repositories.VoucherRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class VoucherPurchaseService {

    private VoucherRepository voucherRepository;
    private CustomerRepository customerRepository;

    @Autowired
    public VoucherPurchaseService(VoucherRepository voucherRepository, CustomerRepository customerRepository){
        this.voucherRepository = voucherRepository;
        this.customerRepository = customerRepository;
    }

    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }

    public List<Voucher> getAllOwnedVouchers(String customerUsername) {
        return voucherRepository.findByCustomersThatOwn_Username(customerUsername);
    }

    public List<Voucher> getAllInCartVouchers(String customerUsername) {
        return voucherRepository.findByCustomersInCart_Username(customerUsername);
    }

    public void addToCart(Customer customer, Voucher voucher){

        if(!voucherRepository.findAll().contains(voucher)) throw new NotExistException();

        if(customer.getVouchersCart().contains(voucher)) throw new AlreadyExistsException();

        if(customer.getVouchersCart() == null) customer.setVouchersCart(new HashSet<Voucher>());
        customer.getVouchersCart().add(voucher);

        if(voucher.getCustomersInCart() == null) voucher.setCustomersInCart(new ArrayList<Customer>());
        voucher.getCustomersInCart().add(customer);

        voucherRepository.saveAndFlush(voucher);

        customerRepository.saveAndFlush(customer);
    }

    public void deleteFromCart(Customer customer, Voucher voucher){

        if(customer.getVouchersCart() == null) throw new NotExistException();
        if(!customer.getVouchersCart().contains(voucher)) throw new NotExistException();
        customer.getVouchersCart().remove(voucher);

        voucher.getCustomersInCart().remove(customer);

        voucherRepository.saveAndFlush(voucher);

        customerRepository.saveAndFlush(customer);
    }

    public void addOwnedVoucher(Customer customer, Voucher voucher){

        if(!voucherRepository.findAll().contains(voucher)) throw new NotExistException();

        if(customer.getOwnedVouchers().contains(voucher)) throw new AlreadyExistsException();

        if(customer.getOwnedVouchers() == null) customer.setOwnedVouchers(new HashSet<Voucher>());
        customer.getOwnedVouchers().add(voucher);
        customer.getVouchersCart().remove(voucher);

        if(voucher.getCustomersThatOwn() == null) voucher.setCustomersThatOwn(new ArrayList<Customer>());
        voucher.getCustomersThatOwn().add(customer);

        voucherRepository.saveAndFlush(voucher);

        customerRepository.saveAndFlush(customer);
    }

    public void purchaseVouchers(Customer customer){
        Set<Voucher> vouchersInCart = customer.getVouchersCart();
        Integer balanceGreenPts = customer.getGreenPts() == null? 0 : customer.getGreenPts();
        Integer totalCost = 0;
        for (Voucher v: vouchersInCart){
            totalCost += v.getValue();
        }
        if (totalCost > balanceGreenPts){
            throw new InsufficientBalanceException();
        }
        for (Voucher v: vouchersInCart){
            addOwnedVoucher(customer, v);
        }
        customer.setGreenPts(customer.getGreenPts() - totalCost);

        customerRepository.save(customer);
    }

    public void deleteOwnedVoucher(Customer customer, Voucher voucher){

        if(customer.getOwnedVouchers() == null) throw new NotExistException();
        if(!customer.getOwnedVouchers().contains(voucher)) throw new NotExistException();
        customer.getOwnedVouchers().remove(voucher);

        voucher.getCustomersThatOwn().remove(customer);

        voucherRepository.saveAndFlush(voucher);

        customerRepository.saveAndFlush(customer);
    }
}
