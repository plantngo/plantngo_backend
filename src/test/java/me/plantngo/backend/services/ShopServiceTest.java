package me.plantngo.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import me.plantngo.backend.DTO.CategoryDTO;
import me.plantngo.backend.DTO.ProductDTO;
import me.plantngo.backend.DTO.UpdateCategoryDTO;
import me.plantngo.backend.DTO.UpdateProductDTO;
import me.plantngo.backend.DTO.UpdateVoucherDTO;
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

@ExtendWith(MockitoExtension.class)
public class ShopServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private MinioService minioService;

    @Mock
    private VoucherRepository voucherRepository;

    @InjectMocks
    private ShopService shopService;

    private Merchant merchant;

    private Category category;

    private Product product;

    private MultipartFile file;

    private Voucher voucher;

    @BeforeEach
    void initEach() {

        file = mock(MultipartFile.class);

        merchant = new Merchant();
        merchant.setUsername("Daniel");
        merchant.setAddress("42 Spring Crescent");

        category = new Category();
        category.setName("Food");

        Category category2 = new Category();
        category2.setName("Dessert");

        product = new Product();
        product.setName("Laksa");
        product.setDescription("It's Laksa");
        product.setPrice(6.1);
        product.setCarbonEmission(0.0);
        product.setCategory(category);

        category.setMerchant(merchant);
        category2.setMerchant(merchant);

        ArrayList<Product> products = new ArrayList<>();
        products.add(product);
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(category);
        categories.add(category2);

        category.setProducts(products);
        merchant.setCategories(categories);

        voucher = new Voucher();
        voucher.setId(1);
        voucher.setMerchant(merchant);
    }

    @Test
    void testGetVoucher_VoucherExists_ReturnVoucher() {

        // Arrange
        Integer voucherId = 1;

        when(voucherRepository.findByIdAndMerchant(any(Integer.class), any(Merchant.class)))
            .thenReturn(Optional.of(voucher));

        // Act
        Voucher responseVoucher = shopService.getVoucher(merchant, voucherId);

        // Assert
        assertEquals(voucher, responseVoucher);
        verify(voucherRepository, times(1)).findByIdAndMerchant(voucherId, merchant);
    }

    @Test
    void testGetVoucher_VoucherNotExist_ThrowNotExistException() {

        // Arrange
        Integer voucherId = 2;
        String exceptionMsg = "";

        when(voucherRepository.findByIdAndMerchant(any(Integer.class), any(Merchant.class)))
            .thenReturn(Optional.empty());

        // Act
        try {
            Voucher responseVoucher = shopService.getVoucher(merchant, voucherId);
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Voucher doesn't exist!", exceptionMsg);
        verify(voucherRepository, times(1)).findByIdAndMerchant(voucherId, merchant);
    }

    @Test
    void testGetAllVouchersFromMerchant_VouchersExist_ReturnVoucherList() {

        // Arrange
        Voucher voucher2 = new Voucher();
        voucher2.setId(2);
        voucher2.setMerchant(merchant);
        Voucher voucher3 = new Voucher();
        voucher3.setId(3);
        voucher3.setMerchant(merchant);
        Voucher voucher4 = new Voucher();
        voucher4.setId(4);
        voucher4.setMerchant(merchant);
        List<Voucher> vouchers = List.of(voucher, voucher2, voucher3, voucher4);

        when(voucherRepository.findAllByMerchant(any(Merchant.class)))
            .thenReturn(vouchers);

        // Act
        List<Voucher> responseVouchers = shopService.getAllVouchersFromMerchant(merchant);

        // Assert
        assertEquals(vouchers, responseVouchers);
        verify(voucherRepository, times(1)).findAllByMerchant(merchant);
    }

    @Test
    void testUpdateVoucher_VoucherExists_ReturnVoucher() {

        // Arrange
        UpdateVoucherDTO updateVoucherDTO = new UpdateVoucherDTO(10.0, null, null, "It's a Voucher");
        Integer voucherId = 1;
        Voucher expectedVoucher = new Voucher();
        expectedVoucher.setId(1);
        expectedVoucher.setMerchant(merchant);
        expectedVoucher.setValue(10);
        expectedVoucher.setDescription("It's a Voucher");

        when(voucherRepository.findByIdAndMerchant(any(Integer.class), any(Merchant.class)))
            .thenReturn(Optional.of(voucher));

        // Act
        Voucher responseVoucher = shopService.updateVoucher(merchant, voucherId, updateVoucherDTO);

        // Assert
        assertEquals(expectedVoucher, responseVoucher);
        verify(voucherRepository, times(1)).findByIdAndMerchant(voucherId, merchant);
    }

    @Test
    void testUpdateVoucher_VoucherNotExist_ThrowNotExistException() {

        // Arrange
        UpdateVoucherDTO updateVoucherDTO = new UpdateVoucherDTO(10.0, null, null, "It's a Voucher");
        Integer voucherId = 1;
        String exceptionMsg = "";

        when(voucherRepository.findByIdAndMerchant(any(Integer.class), any(Merchant.class)))
            .thenReturn(Optional.empty());

        // Act
        try {
            Voucher responseVoucher = shopService.updateVoucher(merchant, voucherId, updateVoucherDTO);
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Voucher doesn't exist!", exceptionMsg);
        verify(voucherRepository, times(1)).findByIdAndMerchant(voucherId, merchant);
    }

    @Test
    void testDeleteVoucher_VoucherExists_ReturnSuccess() {

        // Arrange
        Integer voucherId = 1;

        when(voucherRepository.findByIdAndMerchant(any(Integer.class), any(Merchant.class)))
            .thenReturn(Optional.of(voucher));
        
        // Act
        shopService.deleteVoucher(merchant, voucherId);

        // Assert
        verify(voucherRepository, times(2)).findByIdAndMerchant(voucherId, merchant);
        verify(voucherRepository, times(1)).delete(voucher);
    }

    @Test
    void testDeleteVoucher_VoucherNotExist_ThrowNotExistException() {

        // Arrange
        Integer voucherId = 1;
        String exceptionMsg = "";

        when(voucherRepository.findByIdAndMerchant(any(Integer.class), any(Merchant.class)))
            .thenReturn(Optional.empty());
        
        // Act
        try {
            shopService.deleteVoucher(merchant, voucherId);
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Voucher doesn't exist!", exceptionMsg);
        verify(voucherRepository, times(1)).findByIdAndMerchant(voucherId, merchant);
    }

    @Test
    void testAddCategory_NewCategory_ReturnCategory() {

        // Arrange
        CategoryDTO categoryDTO = new CategoryDTO("Food");
        category.setProducts(null);
        when(categoryRepository.existsByNameAndMerchant(any(String.class), any(Merchant.class)))
                .thenReturn(false);
        when(categoryRepository.save(any(Category.class)))
                .thenReturn(category);

        // Act
        Category responseCategory = shopService.addCategory(merchant, categoryDTO);

        // Assert
        assertEquals(category, responseCategory);
        verify(categoryRepository, times(1)).existsByNameAndMerchant(categoryDTO.getName(), merchant);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testAddCategory_ExistingCategory_ThrowAlreadyExistException() {

        // Arrange
        CategoryDTO categoryDTO = new CategoryDTO("Dessert");
        when(categoryRepository.existsByNameAndMerchant(any(String.class), any(Merchant.class)))
                .thenReturn(true);
        String exceptionMsg = "";

        // Act
        try {
            shopService.addCategory(merchant, categoryDTO);
        } catch (Exception e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Category already exists!", exceptionMsg);
        verify(categoryRepository, times(1)).existsByNameAndMerchant(categoryDTO.getName(), merchant);

    }

    @Test
    void testGetCategory_CategoryExists_ReturnCategory() {

        // Arrange
        Optional<Category> optionalCategory = Optional.of(category);
        when(categoryRepository.findByNameAndMerchant(any(String.class), any(Merchant.class)))
                .thenReturn(optionalCategory);

        // Act
        Category responseCategory = shopService.getCategory(merchant, category.getName());

        // Assert
        assertEquals(category, responseCategory);
        verify(categoryRepository, times(1)).findByNameAndMerchant(category.getName(), merchant);
    }

    @Test
     void testGetCategory_CategoryNotExist_ThrowNotExistException() {

        // Arrange
        when(categoryRepository.findByNameAndMerchant(any(String.class), any(Merchant.class)))
            .thenReturn(Optional.empty());
        String exceptionMsg = "";

        // Act
        try {
            Category responseCategory = shopService.getCategory(merchant, category.getName());
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Category doesn't exist!", exceptionMsg);
        verify(categoryRepository, times(1)).findByNameAndMerchant(category.getName(), merchant);
    }

    @Test
     void testDeleteCategory_CategoryExists_Success() {

        // Arrange
        when(categoryRepository.findByNameAndMerchant(any(String.class), any(Merchant.class)))
            .thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).delete(any(Category.class));
        String exceptionMsg = "";

        // Act
        try {
            shopService.deleteCategory(merchant, category.getName());
        } catch (Exception e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("", exceptionMsg);
        verify(categoryRepository, times(2)).findByNameAndMerchant(category.getName(), merchant);
        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
     void testDeleteCategory_CategoryNotExist_ThrowNotExistException() {

        // Arrange
        when(categoryRepository.findByNameAndMerchant(any(String.class), any(Merchant.class)))
            .thenReturn(Optional.empty());
        String exceptionMsg = "";

        // Act
        try {
            shopService.deleteCategory(merchant, category.getName());
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Category doesn't exist!", exceptionMsg);
        verify(categoryRepository, times(1)).findByNameAndMerchant(category.getName(), merchant);

    }

    @Test
    void testUpdateCategory_CategoryNameAlreadyExists_ThrowAlreadyExistException() {

        // Arrange
        String exceptionMsg = "";
        String categoryName = "Food";
        when(categoryRepository.existsByName(any(String.class)))
                .thenReturn(true);
        when(categoryRepository.findByNameAndMerchant(any(String.class), any(Merchant.class)))
                .thenReturn(Optional.of(category));
        UpdateCategoryDTO updateCategoryDTO = new UpdateCategoryDTO("Dessert");

        // Act
        try {
            shopService.updateCategory(merchant, categoryName, updateCategoryDTO);
        } catch (Exception e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Category already exists!", exceptionMsg);
        verify(categoryRepository, times(1)).existsByName(updateCategoryDTO.getName());
        verify(categoryRepository, times(1)).findByNameAndMerchant(categoryName, merchant);
    }

    @Test
    void testUpdateCategory_CategoryNameValid_Success() {

        // Arrange
        String categoryName = "Food";
        when(categoryRepository.existsByName(any(String.class)))
                .thenReturn(false);
        when(categoryRepository.findByNameAndMerchant(any(String.class), any(Merchant.class)))
                .thenReturn(Optional.of(category));
        UpdateCategoryDTO updateCategoryDTO = new UpdateCategoryDTO("Drinks");

        // Act
        Category responseCategory = shopService.updateCategory(merchant, categoryName, updateCategoryDTO);

        // Assert
        assertEquals(category, responseCategory);
        verify(categoryRepository, times(1)).existsByName(updateCategoryDTO.getName());
        verify(categoryRepository, times(1)).findByNameAndMerchant(categoryName, merchant);
        verify(categoryRepository, times(1)).saveAndFlush(category);
    }

    @Test
    void testGetProduct_ProductNotExist_ThrowNotExistException() {

        // Arrange
        String exceptionMsg = "";
        String categoryName = "Food";
        String productName = "Sprite";
        when(categoryRepository.findByNameAndMerchant(any(String.class), any(Merchant.class)))
                .thenReturn(Optional.of(category));
        when(productRepository.findByNameAndCategory(any(String.class), any(Category.class)))
                .thenReturn(Optional.empty());

        // Act
        try {
            shopService.getProduct(merchant, categoryName, productName);
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Product doesn't exist!", exceptionMsg);
        verify(productRepository, times(1)).findByNameAndCategory(productName, category);
        verify(categoryRepository, times(1)).findByNameAndMerchant(categoryName, merchant);
    }

    @Test
    void testGetProduct_ProductExists_ReturnProduct() {

        // Arrange
        String categoryName = "Food";
        String productName = "Laksa";
        when(categoryRepository.findByNameAndMerchant(any(String.class), any(Merchant.class)))
                .thenReturn(Optional.of(category));
        when(productRepository.findByNameAndCategory(any(String.class), any(Category.class)))
                .thenReturn(Optional.of(product));

        // Act
        Product responseProduct = shopService.getProduct(merchant, categoryName, productName);

        // Assert
        assertEquals(product, responseProduct);
        verify(productRepository, times(1)).findByNameAndCategory(productName, category);
        verify(categoryRepository, times(1)).findByNameAndMerchant(categoryName, merchant);
    }

    @Test
    void testAddProduct_ValidProduct_ReturnProduct() throws MalformedURLException {

        // Arrange
        String categoryName = "Food";
        String imageUrl = "https://google.com.sg";
        ProductDTO productDTO = new ProductDTO("Bee Hoon", 5.5, "Yellow Noodles", null, null, null);
        Product expectedProduct = new Product(null, "Bee Hoon", 5.5, "Yellow Noodles", 0.0,
                new URL("https://google.com.sg"), null, category, null, null);

        when(categoryRepository.existsByNameAndMerchant(any(String.class), any(Merchant.class)))
                .thenReturn(true);
        when(categoryRepository.findByNameAndMerchant(any(String.class), any(Merchant.class)))
                .thenReturn(Optional.of(category));
        try {
            when(minioService.uploadFile(any(MultipartFile.class), any(String.class), any(String.class)))
                    .thenReturn(imageUrl);
        } catch (Exception e) {

        }
        when(productRepository.save(any(Product.class)))
                .thenReturn(expectedProduct);

        // Act
        Product responseProduct = null;
        try {
            responseProduct = shopService.addProduct(merchant, categoryName, productDTO, file);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Assert
        assertEquals(expectedProduct, responseProduct);
        verify(categoryRepository, times(1)).existsByNameAndMerchant(categoryName, merchant);
        verify(categoryRepository, times(1)).findByNameAndMerchant(categoryName, merchant);
        try {
            verify(minioService, times(1)).uploadFile(file, "product", merchant.getUsername());
        } catch (Exception e) {

        }

        verify(productRepository, times(1)).save(expectedProduct);
    }

    @Test
    void testAddProduct_InvalidCategory_ThrowNotExistException() {

        // Arrange
        String categoryName = "Appetizer";
        String exceptionMsg = "";
        ProductDTO productDTO = new ProductDTO("Bee Hoon", 5.5, "Yellow Noodles", null, null, null);
        when(categoryRepository.existsByNameAndMerchant(any(String.class), any(Merchant.class)))
                .thenReturn(false);

        // Act
        try {
            Product responseProduct = shopService.addProduct(merchant, categoryName, productDTO, file);
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Assert
        assertEquals("Category doesn't exist!", exceptionMsg);
        verify(categoryRepository, times(1)).existsByNameAndMerchant(categoryName, merchant);
    }

    @Test
    void testAddProduct_ProductAlreadyExists_ThrowAlreadyExistsException() {

        // Arrange
        String categoryName = "Food";
        String exceptionMsg = "";
        ProductDTO productDTO = new ProductDTO("Laksa", 6.1, "It's Laksa", null, null, null);
        when(categoryRepository.existsByNameAndMerchant(any(String.class), any(Merchant.class)))
                .thenReturn(true);
        when(categoryRepository.findByNameAndMerchant(any(String.class), any(Merchant.class)))
                .thenReturn(Optional.of(category));

        // Act
        try {
            Product responseProduct = shopService.addProduct(merchant, categoryName, productDTO, file);
        } catch (AlreadyExistsException e) {
            exceptionMsg = e.getMessage();
        } catch (Exception e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Product already exists!", exceptionMsg);
        verify(categoryRepository, times(1)).existsByNameAndMerchant(categoryName, merchant);
        verify(categoryRepository, times(1)).findByNameAndMerchant(categoryName, merchant);
    }

    @Test
    void testUpdateProduct_ValidUpdateProduct_ReturnUpdatedProduct() {

        // Arrange
        Product expectedProduct = product;
        expectedProduct.setPrice(10.11);
        String productName = "Laksa";
        UpdateProductDTO updateProductDTO = new UpdateProductDTO(null, 10.11, null, null, null, null);
        when(productRepository.findByNameAndCategory(any(String.class), any(Category.class)))
                .thenReturn(Optional.of(product));
        when(productRepository.saveAndFlush(any(Product.class)))
                .thenReturn(expectedProduct);

        // Act
        Product responseProduct = shopService.updateProduct(category, productName, updateProductDTO);

        // Assert
        assertEquals(expectedProduct, responseProduct);
        verify(productRepository, times(1)).findByNameAndCategory(productName, category);
        verify(productRepository, times(1)).saveAndFlush(expectedProduct);

    }

    @Test
    void testUpdateProduct_ProductNotExist_ThrowNotExistException() {

        // Arrange
        String productName = "Bee Hiang";
        String exceptionMsg = "";
        UpdateProductDTO updateProductDTO = new UpdateProductDTO(null, 10.11, null, null, null, null);
        when(productRepository.findByNameAndCategory(any(String.class), any(Category.class)))
                .thenReturn(Optional.empty());

        // Act
        try {
            Product responseProduct = shopService.updateProduct(category, productName, updateProductDTO);
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Product doesn't exist!", exceptionMsg);
        verify(productRepository, times(1)).findByNameAndCategory(productName, category);

    }

    @Test
    void testUpdateProduct_ProductNameAlreadyExists_ThrowAlreadyExistsException() {

        // Arrange
        Product product2 = new Product();
        product2.setName("Bee Hoon");
        category.setProducts(List.of(product, product2));
        String productName = "Laksa";
        String exceptionMsg = "";
        UpdateProductDTO updateProductDTO = new UpdateProductDTO("Bee Hoon", null, null, null, null, null);
        when(productRepository.findByNameAndCategory(any(String.class), any(Category.class)))
                .thenReturn(Optional.of(product));

        // Act
        try {
            Product responseProduct = shopService.updateProduct(category, productName, updateProductDTO);
        } catch (AlreadyExistsException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Product Name already exists!", exceptionMsg);
        verify(productRepository, times(1)).findByNameAndCategory(productName, category);

    }

    @Test
    void testUpdateProductWithImage_ValidProduct_ReturnProduct() throws Exception {

        // Arrange
        String productName = "Laksa";
        String exceptionMsg = "";
        String imageUrl = "https://google.com.sg";
        UpdateProductDTO updateProductDTO = new UpdateProductDTO("Bee Hoon", null, null, null, null, null);
        Product expectedProduct = new Product(null, "Bee Hoon", 6.1, "It's Laksa", 0.0,
                new URL("https://google.com.sg"), null, category, null, null);

        when(productRepository.findByNameAndCategory(any(String.class), any(Category.class)))
            .thenReturn(Optional.of(product));
        when(minioService.uploadFile(any(MultipartFile.class), anyString(), anyString()))
            .thenReturn(imageUrl);

        // Act
        Product responseProduct = shopService.updateProduct(category, productName, updateProductDTO, file);

        // Assert
        assertEquals(expectedProduct, responseProduct);
        verify(productRepository, times(1)).findByNameAndCategory(productName, category);
        verify(minioService, times(1)).uploadFile(file, "product", merchant.getUsername());
    }

    @Test
    void testDeleteProduct_ProductExists_ReturnSuccess() {

        // Arrange
        String productName = "Laksa";

        // Act
        shopService.deleteProduct(product);

        // Assert
        verify(productRepository, times(1)).deleteById(product.getId());
    }
}
