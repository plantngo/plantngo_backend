package me.plantngo.backend.services;

import java.util.List;
import java.util.Optional;

import me.plantngo.backend.DTO.*;
import me.plantngo.backend.models.Voucher;
import me.plantngo.backend.repositories.VoucherRepository;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.plantngo.backend.exceptions.AlreadyExistsException;
import me.plantngo.backend.exceptions.NotExistException;
import me.plantngo.backend.models.Category;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.models.Product;
import me.plantngo.backend.repositories.CategoryRepository;
import me.plantngo.backend.repositories.MerchantRepository;
import me.plantngo.backend.repositories.ProductRepository;

@Service
public class ShopService {
    
    private ProductRepository productRepository;
    private MerchantRepository merchantRepository;
    private CategoryRepository categoryRepository;
    private VoucherRepository voucherRepository;

    @Autowired
    public ShopService(ProductRepository productRepository, MerchantRepository merchantRepository, CategoryRepository categoryRepository, VoucherRepository voucherRepository) {
        this.productRepository = productRepository;
        this.merchantRepository = merchantRepository;
        this.categoryRepository = categoryRepository;
        this.voucherRepository = voucherRepository;
    }

    public Voucher addVoucher(Merchant merchant, VoucherDTO voucherDTO) {

        Voucher voucher = this.voucherMapToEntity(voucherDTO, merchant);
        voucher.setMerchantId(merchant.getId());
        voucherRepository.save(voucher);

        return voucher;
    }

    public Voucher getVoucher(Merchant merchant, Integer voucherId) {
        Optional<Voucher> tempVoucher = voucherRepository.findByIdAndMerchant(voucherId, merchant);
        if (tempVoucher.isEmpty()) {
            throw new NotExistException();
        }
        return tempVoucher.get();
    }

    public List<Voucher> getAllVouchersFromMerchant(Merchant merchant) {
        return voucherRepository.findAllByMerchant(merchant);
    }

    public Voucher updateVoucher(Merchant merchant, Integer voucherId, UpdateVoucherDTO updateVoucherDTO) {

        // Check to see if voucher exists
        Optional<Voucher> tempVoucher = voucherRepository.findByIdAndMerchant(voucherId, merchant);
        if (tempVoucher.isEmpty()) {
            throw new NotExistException();
        }

        //update the voucher's value
        Voucher voucher = tempVoucher.get();
        ModelMapper mapper = new ModelMapper();

        mapper.map(updateVoucherDTO, voucher);;

        // In case we need to call it before method ends
        voucherRepository.saveAndFlush(voucher);

        return voucher;
    }

    public void deleteVoucher(Merchant merchant, Integer voucherId) {
        // Check to see if same category under merchant already exists
        if (voucherRepository.findByIdAndMerchant(voucherId, merchant).isEmpty()) {
            throw new NotExistException();
        }

        Voucher voucher = voucherRepository.findByIdAndMerchant(voucherId, merchant).get();
        voucherRepository.delete(voucher);
    }
    public Category addCategory(Merchant merchant, CategoryDTO categoryDTO) {

        Category category = this.categoryMapToEntity(categoryDTO, merchant);

        // Check to see if same category under merchant already exists
        if (categoryRepository.existsByNameAndMerchant(category.getName(), merchant)) {
            throw new AlreadyExistsException("Category");
        }

        categoryRepository.save(category);

        return category;
    }

    public Category getCategory(Merchant merchant, String categoryName) {
        Optional<Category> tempCategory = categoryRepository.findByNameAndMerchant(categoryName, merchant);
        if (tempCategory.isEmpty()) {
            throw new NotExistException();
        }
        return tempCategory.get();
    }
    
    public Category updateCategory(Merchant merchant, String categoryName, UpdateCategoryDTO updateCategoryDTO) {

        // Check to see if category exists under merchant
        Optional<Category> tempCategory = categoryRepository.findByNameAndMerchant(categoryName, merchant);
        if (tempCategory.isEmpty()) {
            throw new NotExistException();
        }

        // If changing category name, check to see if another category with that name already exists
        if (!updateCategoryDTO.getName().equals(categoryName) && categoryRepository.existsByName(updateCategoryDTO.getName())) {
            throw new AlreadyExistsException();
        }

        // Updating category
        Category category = tempCategory.get();
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setSkipNullEnabled(true);
        mapper.map(updateCategoryDTO, category);;

        // In case we need to call it before method ends
        categoryRepository.saveAndFlush(category);

        return category;
    }

    public void deleteCategory(Merchant merchant, String categoryName) {
        // Check to see if same category under merchant already exists
        if (categoryRepository.findByNameAndMerchant(categoryName, merchant).isEmpty()) {
            throw new NotExistException();
        }

        Category category = categoryRepository.findByNameAndMerchant(categoryName, merchant).get();
        categoryRepository.delete(category);
    }

    public Product getProduct(Merchant merchant, String categoryName, String productName) {
        Category category = this.getCategory(merchant, categoryName);
        Optional<Product> tempProduct = productRepository.findByNameAndCategory(productName, category);
        if (tempProduct.isEmpty()) {
            throw new NotExistException();
        }
        return tempProduct.get();
    }

    public Product addProduct(Merchant merchant, String categoryName, ProductDTO productDTO) {

        // Check to see if category exists
        if (!categoryRepository.existsByNameAndMerchant(categoryName, merchant)) {
            throw new NotExistException();
        }

        Category category = categoryRepository.findByNameAndMerchant(categoryName, merchant).get();
        List<Product> productList = category.getProducts();

        // Check to see if product with same name already exists in category
        for (Product p : productList) {
            if (p.getName().equals(productDTO.getName())) {
                throw new AlreadyExistsException();
            }
        }

        // Creating  & Saving Product object
        Product product = this.productMapToEntity(productDTO, category);

        productRepository.save(product);

        return product;
    }

    public Product updateProduct(Category category, String productName, UpdateProductDTO updateProductDTO) {
        
        // Check to see if product exists under category
        Optional<Product> tempProduct = productRepository.findByNameAndCategory(productName, category);
        if (tempProduct.isEmpty()) {
            throw new NotExistException();
        }

        // If changing product name, check to see if another product with that name already exists in the category
        List<Product> productList = category.getProducts();
        Product product = tempProduct.get();

        for (Product p : productList) {
            if (p.getName().equals(updateProductDTO.getName()) && p != product) {
                throw new AlreadyExistsException();
            }
        }

        // Updating product
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setSkipNullEnabled(true);
        mapper.map(updateProductDTO, product);

        // In case we need to call it before method ends
        productRepository.saveAndFlush(product);

        return product;
    }

    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }

    // public List<Product> getAllProductsByMerchant(Merchant merchant) {
    //     return productRepository.findByMerchant(merchant);
    // }


    private Voucher voucherMapToEntity(VoucherDTO voucherDTO, Merchant merchant) {
        ModelMapper mapper = new ModelMapper();

        Voucher voucher = mapper.map(voucherDTO, Voucher.class);
        voucher.setMerchant(merchant);

        return voucher;
    }
    private Category categoryMapToEntity(CategoryDTO categoryDTO, Merchant merchant) {
        ModelMapper mapper = new ModelMapper();

        Category category = mapper.map(categoryDTO, Category.class);
        category.setMerchant(merchant);

        return category;
    }

    private Product productMapToEntity(ProductDTO productDTO, Category category) {
        ModelMapper mapper = new ModelMapper();

        Product product = mapper.map(productDTO, Product.class);
        product.setCarbonEmission(0.0);
        product.setCategory(category);

        return product;
    }
}
