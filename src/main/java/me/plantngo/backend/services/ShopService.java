package me.plantngo.backend.services;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import me.plantngo.backend.DTO.CategoryDTO;
import me.plantngo.backend.DTO.ProductDTO;
import me.plantngo.backend.DTO.UpdateCategoryDTO;
import me.plantngo.backend.DTO.UpdateProductDTO;
import me.plantngo.backend.DTO.UpdateVoucherDTO;
import me.plantngo.backend.DTO.VoucherDTO;
import me.plantngo.backend.exceptions.AlreadyExistsException;
import me.plantngo.backend.exceptions.NotExistException;
import me.plantngo.backend.models.Category;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.models.Product;
import me.plantngo.backend.models.Voucher;
import me.plantngo.backend.repositories.CategoryRepository;
import me.plantngo.backend.repositories.MerchantRepository;
import me.plantngo.backend.repositories.ProductRepository;
import me.plantngo.backend.repositories.VoucherRepository;

@Service
public class ShopService {

    private ProductRepository productRepository;
    private MerchantRepository merchantRepository;
    private CategoryRepository categoryRepository;
    private VoucherRepository voucherRepository;
    private MinioService minioService;

    @Autowired
    public ShopService(ProductRepository productRepository, MerchantRepository merchantRepository,
            CategoryRepository categoryRepository, VoucherRepository voucherRepository, MinioService minioService) {
        this.productRepository = productRepository;
        this.merchantRepository = merchantRepository;
        this.categoryRepository = categoryRepository;
        this.voucherRepository = voucherRepository;
        this.minioService = minioService;

    }

    /*
     * 
     * Voucher Related Methods
     * 
     */

    /**
     * Adds a Voucher for a Merchant
     * 
     * @param merchant
     * @param voucherDTO
     * @return
     */
    public Voucher addVoucher(Merchant merchant, VoucherDTO voucherDTO) {

        Voucher voucher = this.voucherMapToEntity(voucherDTO, merchant);
        voucher.setMerchantId(merchant.getId());
        voucherRepository.save(voucher);

        return voucher;
    }

    /**
     * Gets a Voucher by a Merchant given its Id
     * 
     * @param merchant
     * @param voucherId
     * @return
     */
    public Voucher getVoucher(Merchant merchant, Integer voucherId) {
        Optional<Voucher> tempVoucher = voucherRepository.findByIdAndMerchant(voucherId, merchant);
        if (tempVoucher.isEmpty()) {
            throw new NotExistException("Voucher");
        }
        return tempVoucher.get();
    }

    /**
     * Get all Vouchers by a Merchant
     * 
     * @param merchant
     * @return
     */
    public List<Voucher> getAllVouchersFromMerchant(Merchant merchant) {
        return voucherRepository.findAllByMerchant(merchant);
    }

    /**
     * Updates a Voucher for a given Merchant and VoucherId using updateVoucherDTO
     * 
     * @param merchant
     * @param voucherId
     * @param updateVoucherDTO
     * @return
     */
    public Voucher updateVoucher(Merchant merchant, Integer voucherId, UpdateVoucherDTO updateVoucherDTO) {

        // Check to see if voucher exists
        Optional<Voucher> tempVoucher = voucherRepository.findByIdAndMerchant(voucherId, merchant);
        if (tempVoucher.isEmpty()) {
            throw new NotExistException("Voucher");
        }

        // update the voucher's value
        Voucher voucher = tempVoucher.get();
        ModelMapper mapper = new ModelMapper();

        mapper.map(updateVoucherDTO, voucher);

        // In case we need to call it before method ends
        voucherRepository.saveAndFlush(voucher);

        return voucher;
    }

    /**
     * Deletes a Voucher given Merchant and VoucherId
     * 
     * @param merchant
     * @param voucherId
     */
    public void deleteVoucher(Merchant merchant, Integer voucherId) {
        Voucher voucher = voucherRepository.findByIdAndMerchant(voucherId, merchant)
            .orElseThrow(() -> new NotExistException("Voucher"));
        voucherRepository.delete(voucher);
    }

    /*
     * 
     * Category Related Methods
     * 
     */

    /**
     * Adds a category for a Merchant
     * 
     * @param merchant
     * @param categoryDTO
     * @return
     */
    public Category addCategory(Merchant merchant, CategoryDTO categoryDTO) {

        Category category = this.categoryMapToEntity(categoryDTO, merchant);

        // Check to see if same category under merchant already exists
        if (categoryRepository.existsByNameAndMerchant(category.getName(), merchant)) {
            throw new AlreadyExistsException("Category");
        }

        return categoryRepository.save(category);
    }

    /**
     * Gets a Category for a Merchant given its name
     * 
     * @param merchant
     * @param categoryName
     * @return
     */
    public Category getCategory(Merchant merchant, String categoryName) {
        Optional<Category> tempCategory = categoryRepository.findByNameAndMerchant(categoryName, merchant);
        if (tempCategory.isEmpty()) {
            throw new NotExistException("Category");
        }
        return tempCategory.get();
    }

    /**
     * Updates a Category for a Merchant given its name using updateCategoryDTO
     * 
     * @param merchant
     * @param categoryName
     * @param updateCategoryDTO
     * @return
     */
    public Category updateCategory(Merchant merchant, String categoryName, UpdateCategoryDTO updateCategoryDTO) {

        // Check to see if category exists under merchant
        Optional<Category> tempCategory = categoryRepository.findByNameAndMerchant(categoryName, merchant);
        if (tempCategory.isEmpty()) {
            throw new NotExistException();
        }

        // If changing category name, check to see if another category with that name
        // already exists
        if (!updateCategoryDTO.getName().equals(categoryName)
                && categoryRepository.existsByName(updateCategoryDTO.getName())) {
            throw new AlreadyExistsException("Category");
        }

        // Updating category
        Category category = tempCategory.get();
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setSkipNullEnabled(true);
        mapper.map(updateCategoryDTO, category);

        // In case we need to call it before method ends
        categoryRepository.saveAndFlush(category);

        return category;
    }

    /**
     * Deletes a Category from a Merchant given its name
     * 
     * @param merchant
     * @param categoryName
     */
    public void deleteCategory(Merchant merchant, String categoryName) {
        Category category = categoryRepository.findByNameAndMerchant(categoryName, merchant)
            .orElseThrow(() -> new NotExistException("Category"));
        categoryRepository.delete(category);
    }

    /*
     * 
     * Product Related Methods
     * 
     */

    /**
     * Gets a product by a Merchant
     * 
     * @param merchant
     * @param categoryName
     * @param productName
     * @return
     */
    public Product getProduct(Merchant merchant, String categoryName, String productName) {
        Category category = this.getCategory(merchant, categoryName);
        Optional<Product> tempProduct = productRepository.findByNameAndCategory(productName, category);
        if (tempProduct.isEmpty()) {
            throw new NotExistException("Product");
        }
        return tempProduct.get();
    }

    /**
     * Adds a new Product for a Merchant and upload the image file to AWSS3 bucket
     * 
     * @param merchant
     * @param categoryName
     * @param productDTO
     * @param file
     * @return
     * @throws MalformedURLException
     */
    public Product addProduct(Merchant merchant, String categoryName, ProductDTO productDTO, MultipartFile file)
            throws MalformedURLException {

        // Check to see if category exists
        Category category = categoryRepository.findByNameAndMerchant(categoryName, merchant)
            .orElseThrow(() -> new NotExistException("Category"));

        List<Product> productList = category.getProducts();

        // Check to see if product with same name already exists in category
        for (Product p : productList) {
            if (p.getName().equals(productDTO.getName())) {
                throw new AlreadyExistsException("Product");
            }
        }

        // Upload photo to AWSS3 and set the imageUrl to productDTO
        if (file != null && !file.isEmpty()) {
            try {
                String imageUrl = minioService.uploadFile(file, "product", merchant.getUsername());
                productDTO.setImageUrl(new URL(imageUrl));
            } catch (Exception e) {

            }

        }

        // Creating & Saving Product object
        Product product = this.productMapToEntity(productDTO, category);

        productRepository.save(product);

        return product;
    }

    /**
     * Adds a new product for a merchant without uploading any images
     * 
     * @param merchant
     * @param categoryName
     * @param productDTO
     * @return
     */
    public Product addProduct(Merchant merchant, String categoryName, ProductDTO productDTO) {

        // Check to see if category exists
        Category category = categoryRepository.findByNameAndMerchant(categoryName, merchant)
            .orElseThrow(() -> new NotExistException("Category"));
        
        List<Product> productList = category.getProducts();

        // Check to see if product with same name already exists in category
        for (Product p : productList) {
            if (p.getName().equals(productDTO.getName())) {
                throw new AlreadyExistsException("Product");
            }
        }

        // Creating & Saving Product object
        Product product = this.productMapToEntity(productDTO, category);

        productRepository.save(product);

        return product;
    }

    /**
     * Updates a product for a Merchant with updateProductDTO
     * 
     * @param category
     * @param productName
     * @param updateProductDTO
     * @return
     */
    public Product updateProduct(Category category, String productName, UpdateProductDTO updateProductDTO) {

        // Check to see if product exists under category
        Optional<Product> tempProduct = productRepository.findByNameAndCategory(productName, category);
        if (tempProduct.isEmpty()) {
            throw new NotExistException("Product");
        }

        // If changing product name, check to see if another product with that name
        // already exists in the category
        List<Product> productList = category.getProducts();
        Product product = tempProduct.get();

        for (Product p : productList) {
            if (p.getName().equals(updateProductDTO.getName()) && p != product) {
                throw new AlreadyExistsException("Product Name");
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

    // update product with image
    public Product updateProduct(Category category, String productName, UpdateProductDTO updateProductDTO,
            MultipartFile file) throws MalformedURLException {

        // Check to see if product exists under category
        Optional<Product> tempProduct = productRepository.findByNameAndCategory(productName, category);
        if (tempProduct.isEmpty()) {
            throw new NotExistException("Product");
        }

        // If changing product name, check to see if another product with that name
        // already exists in the category
        List<Product> productList = category.getProducts();
        Product product = tempProduct.get();

        for (Product p : productList) {
            if (p.getName().equals(updateProductDTO.getName()) && p != product) {
                throw new AlreadyExistsException("Product Name");
            }
        }

        if (file != null && !file.isEmpty()) {
            try {
                String imageUrl = minioService.uploadFile(file, "product", category.getMerchant().getUsername());
                updateProductDTO.setImageUrl(new URL(imageUrl));
            } catch (Exception e) {

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
        Category category = product.getCategory();
        category.getProducts().remove(product);
        productRepository.deleteById(product.getId());
    }

    // public List<Product> getAllProductsByMerchant(Merchant merchant) {
    // return productRepository.findByMerchant(merchant);
    // }

    /*
     * 
     * Helper Methods
     * 
     */

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
        if (product.getCarbonEmission() == null) {
            product.setCarbonEmission(0.0);
        }
        product.setCategory(category);

        return product;
    }

}
