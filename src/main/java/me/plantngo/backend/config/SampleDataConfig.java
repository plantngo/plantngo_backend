package me.plantngo.backend.config;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import me.plantngo.backend.DTO.CategoryDTO;
import me.plantngo.backend.DTO.ProductDTO;
import me.plantngo.backend.DTO.RegistrationDTO;
import me.plantngo.backend.DTO.VoucherDTO;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.services.AuthService;
import me.plantngo.backend.services.MerchantService;
import me.plantngo.backend.services.ShopService;

@Configuration
public class SampleDataConfig {

        @Autowired
        private AuthService authService;
        @Autowired
        private ShopService shopService;
        @Autowired
        private MerchantService merchantService;

        @Bean
        CommandLineRunner commandLineRunner() {
                return args -> {

                        createCustomers();
                        createMerchants();
                        createMerchantCategories();
                        createMerchantProducts();
                        createVouchers();

                };
        }

        public void createCustomers() {
                RegistrationDTO customer1 = new RegistrationDTO(
                                "soonann",
                                "soonann@example.com",
                                "Password123!",
                                'C',
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null);

                RegistrationDTO customer2 = new RegistrationDTO(
                                "gabriel",
                                "gabriel@example.com",
                                "Password123!",
                                'C',
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null);
                authService.registerUser(customer1);
                authService.registerUser(customer2);

        }

        public void createMerchants() {

                RegistrationDTO merchantPizzaHut = new RegistrationDTO(
                                "pizzahut",
                                "pizzahut@example.com",
                                "Password123!",
                                'M',
                                "Pizza Hut",
                                "https://www.pizzahut.co.uk/order/images/icons/logo-300x300.ed09f0955306cb0be42c35092733b5c2.png",
                                "https://imageio.forbes.com/specials-images/imageserve/6111c4616209edda9f419c4e/0x0.jpg?format=jpg&width=1200",
                                "Pizza Hut Plaza Singapura, 68 Orchard Road B2-01 Plaza, Singapore 238839",
                                "Pizza Hut is an American multinational restaurant chain and international franchise founded in 1958 in Wichita, Kansas by Dan and Frank Carney.",
                                1.3007165,
                                103.84271,
                                "Italian",
                                2,
                                "10:00AM - 20:00PM");
                RegistrationDTO merchantFairPrice = new RegistrationDTO(
                                "fairprice",
                                "fairprice@example.com",
                                "Password123!",
                                'M',
                                "FairPrice Xpress",
                                "https://lh6.googleusercontent.com/-_Lx4S-leRi8/AAAAAAAAAAI/AAAAAAAAAAA/WIQSo135oGk/s44-p-k-no-ns-nd/photo.jpg",
                                "https://www.fairprice.com.sg/wp-content/uploads/2019/01/fairprice-express-970x585.jpg",
                                "FairPrice Xpress, 1 Sophia Rd, #01-18, Peace Centre, Singapore 228149",
                                "NTUC FairPrice is the largest supermarket chain in Singapore. The company is a co-operative of the National Trades Union Congress. The group has 100 supermarkets across the island, with over 160 outlets of Cheers convenience stores island-wide.",
                                1.3012709,
                                103.8472445,
                                "Groceries",
                                1,
                                "7:00AM - 21:00PM");
                RegistrationDTO merchantJoieVege = new RegistrationDTO(
                                "joievege",
                                "Joie@example.com",
                                "Password123!",
                                'M',
                                "Joie - Vegetarian Fine Dining",
                                "https://static.wixstatic.com/media/0bc268_e584ce9e9e6b4bb5b4d3199e30089c8d~mv2.jpeg/v1/fill/w_237,h_120,al_c,q_80,usm_0.66_1.00_0.01,enc_auto/Joie%20Logo.jpeg",
                                "https://static.wixstatic.com/media/591a97_128872d8ff7a4f579355ac6bd342e83d~mv2.jpg/v1/fill/w_640,h_408,al_c,q_80,usm_0.66_1.00_0.01,enc_auto/591a97_128872d8ff7a4f579355ac6bd342e83d~mv2.jpg",
                                "Joie - Vegetarian Fine Dining, 181 Orchard Rd, #12 - 01, Singapore 238896",
                                "Contemporary indoor-outdoor rooftop eatery serving a set menu of adventurous vegetarian cuisine.",
                                1.3007905,
                                103.8372088,
                                "Fine Dining",
                                4,
                                "10:00AM - 20:00PM");

                authService.registerUser(merchantPizzaHut);
                authService.registerUser(merchantFairPrice);
                authService.registerUser(merchantJoieVege);

        }

        public void createMerchantCategories() {
                Merchant pizzaHut = merchantService.getMerchantByUsername("pizzahut");
                CategoryDTO pizzaHutCat1 = new CategoryDTO("Pizza");
                shopService.addCategory(pizzaHut, pizzaHutCat1);

                Merchant fairPrice = merchantService.getMerchantByUsername("fairprice");
                CategoryDTO fairPriceCat1 = new CategoryDTO("Groceries");
                CategoryDTO fairPriceCat2 = new CategoryDTO("Snacks");
                shopService.addCategory(fairPrice, fairPriceCat1);
                shopService.addCategory(fairPrice, fairPriceCat2);

                Merchant joieVege = merchantService.getMerchantByUsername("joievege");
                CategoryDTO joieVegeCat1 = new CategoryDTO("Fine Dining");
                shopService.addCategory(joieVege, joieVegeCat1);
        }

        public void createMerchantProducts() throws MalformedURLException {
                Merchant pizzaHut = merchantService.getMerchantByUsername("pizzahut");
                ProductDTO pizzaHutProduct1 = new ProductDTO("Cheesy 7 Beyond Supreme - Regular Pan",
                                34.90,
                                "Cheesy 7 meets Beyond Meat™! Made with Beyond Meat's plant based Italian Sausage Crumbles which leaves a hint of herbs and spice aroma, with capsicums, onions and mushrooms, on a bed of our signature sweet sauce and seven wondrous cheeses!",
                                10.0,
                                new URL("https://static.phdvasia.com/sg1/menu/single/desktop_thumbnail_0bd9658b-d8eb-438d-bc2c-fef8afa1b71e.jpg"),
                                "Savoury & Cheesy");
                ProductDTO pizzaHutProduct2 = new ProductDTO("Cheesy 7 Beyond Supreme - Regular Cheesy Stuffed Crust",
                                43.40,
                                "Cheesy 7 meets Beyond Meat™! Made with Beyond Meat's plant based Italian Sausage Crumbles which leaves a hint of herbs and spice aroma, with capsicums, onions and mushrooms, on a bed of our signature sweet sauce and seven wondrous cheeses!",
                                45.0,
                                new URL("https://static.phdvasia.com/sg1/menu/single/desktop_thumbnail_0bd9658b-d8eb-438d-bc2c-fef8afa1b71e.jpg"),
                                "Savoury & Cheesy");
                ProductDTO pizzaHutProduct3 = new ProductDTO("Cheesy 7 Beyond Supreme - Large Pan",
                                40.90,
                                "Cheesy 7 meets Beyond Meat™! Made with Beyond Meat's plant based Italian Sausage Crumbles which leaves a hint of herbs and spice aroma, with capsicums, onions and mushrooms, on a bed of our signature sweet sauce and seven wondrous cheeses!",
                                40.0,
                                new URL("https://static.phdvasia.com/sg1/menu/single/desktop_thumbnail_0bd9658b-d8eb-438d-bc2c-fef8afa1b71e.jpg"),
                                "Savoury & Cheesy");
                shopService.addProduct(pizzaHut, "Pizza", pizzaHutProduct1, null);
                shopService.addProduct(pizzaHut, "Pizza", pizzaHutProduct2, null);
                shopService.addProduct(pizzaHut, "Pizza", pizzaHutProduct3, null);

                Merchant fairPrice = merchantService.getMerchantByUsername("fairprice");
                ProductDTO fairPriceProduct1 = new ProductDTO(
                                "The Vegetarian Butcher Vegan Meal - Chickened Out Chunks", 7.95,
                                "The Vegetarian Butcher Vegan Meal - Chickened Out Chunks", 10.0,
                                new URL("https://media.nedigital.sg/fairprice/fpol/media/images/product/XL/13208081_XL1_20220722.jpg?w=1200&q=70"),
                                "Plain");
                ProductDTO fairPriceProduct2 = new ProductDTO("Heinz Vegan Seriously Good Garlic Aioli Mayonnaise",
                                8.95,
                                "Heinz Vegan Seriously Good Garlic Aioli Mayonnaise", 10.0,
                                new URL("https://media.nedigital.sg/fairprice/fpol/media/images/product/XL/90094302_XL1_20210816.jpg?w=1200&q=70"),
                                "Sour");
                ProductDTO fairPriceProduct3 = new ProductDTO(
                                "Hey! Chips - Broccoli (100% Natural, Gluten-Free, Vegan)",
                                4.90,
                                "Hey! Chips - Broccoli (100% Natural, Gluten-Free, Vegan)",
                                10.0,
                                new URL("https://media.nedigital.sg/fairprice/fpol/media/images/product/XL/90045450_XL1_20211018.jpg?w=1200&q=70"),
                                "Salty");
                ProductDTO fairPriceProduct4 = new ProductDTO("Hey! Chips - Banana (100% Natural, Gluten-Free, Vegan)",
                                4.90,
                                "Hey! Chips - Banana (100% Natural, Gluten-Free, Vegan)",
                                10.0,
                                new URL("https://media.nedigital.sg/fairprice/fpol/media/images/product/L/90045446_L1_20211018.jpg?q=60"),
                                "Salty");
                ProductDTO fairPriceProduct5 = new ProductDTO("Hey! Chips - Bulk (100% Natural, Gluten-Free, Vegan)",
                                33.90,
                                "Hey! Chips - Bulk (100% Natural, Gluten-Free, Vegan", 10.0,
                                new URL("https://media.nedigital.sg/fairprice/fpol/media/images/product/L/90027875_L1_20211018.jpg?q=60"),
                                "Salty");

                shopService.addProduct(fairPrice, "Groceries", fairPriceProduct1, null);
                shopService.addProduct(fairPrice, "Groceries", fairPriceProduct2, null);
                shopService.addProduct(fairPrice, "Snacks", fairPriceProduct3, null);
                shopService.addProduct(fairPrice, "Snacks", fairPriceProduct4, null);
                shopService.addProduct(fairPrice, "Snacks", fairPriceProduct5, null);

                Merchant joieVege = merchantService.getMerchantByUsername("joievege");
                ProductDTO joieVegeProduct1 = new ProductDTO("4 Course Set", 38.00, "Including 1 Drink of Choice", 10.0,
                                new URL("https://static.wixstatic.com/media/591a97_20e12d8438d84db48839fb893ed60072~mv2.jpg/v1/fit/w_467,h_571,q_90/591a97_20e12d8438d84db48839fb893ed60072~mv2.jpg"),
                                "Savoury");
                ProductDTO joieVegeProduct2 = new ProductDTO("5 Course Set", 58.00, "Including 1 Drink of Choice", 12.0,
                                new URL("https://static.wixstatic.com/media/591a97_49f940ea3868444b9867202f9002610c~mv2.jpg/v1/fit/w_467,h_693,q_90/591a97_49f940ea3868444b9867202f9002610c~mv2.jpg"),
                                "Savoury");
                shopService.addProduct(joieVege, "Fine Dining", joieVegeProduct1, null);
                shopService.addProduct(joieVege, "Fine Dining", joieVegeProduct2, null);
        }

        private void createVouchers() {
                Merchant pizzaHut = merchantService.getMerchantByUsername("pizzahut");
                VoucherDTO pizzaHutVoucher1 = new VoucherDTO(
                                100.0,
                                'F',
                                10.0,
                                "$10 off sides @ PizzaHut");
                VoucherDTO pizzaHutVoucher2 = new VoucherDTO(
                                150.0,
                                'P',
                                0.15,
                                "15% off total bill @ PizzaHut");
                VoucherDTO pizzaHutVoucher3 = new VoucherDTO(
                                200.0,
                                'P',
                                0.20,
                                "20% off all sides @ PizzaHut");
                shopService.addVoucher(pizzaHut, pizzaHutVoucher1);
                shopService.addVoucher(pizzaHut, pizzaHutVoucher2);
                shopService.addVoucher(pizzaHut, pizzaHutVoucher3);

                Merchant fairPrice = merchantService.getMerchantByUsername("fairprice");
                VoucherDTO fairPriceVoucher1 = new VoucherDTO(
                                50.0,
                                'F',
                                0.0,
                                "Free Hey Chips! - Banana Flavoured @ Fairprice");
                VoucherDTO fairPriceVoucher2 = new VoucherDTO(
                                300,
                                'F',
                                0.0,
                                "Free Hey Chips - Bulk (6) @ Fairprice ");
                VoucherDTO fairPriceVoucher3 = new VoucherDTO(
                                500.0,
                                'F',
                                0.0,
                                "1-1 all Fruits Purchased (max 5 fruits)");
                shopService.addVoucher(fairPrice, fairPriceVoucher1);
                shopService.addVoucher(fairPrice, fairPriceVoucher2);
                shopService.addVoucher(fairPrice, fairPriceVoucher3);

                Merchant joieVege = merchantService.getMerchantByUsername("joievege");
                VoucherDTO joieVegeVoucher1 = new VoucherDTO(
                                1500.0,
                                'P',
                                15.0,
                                "15% off 5 Course Set Meal");
                shopService.addVoucher(joieVege, joieVegeVoucher1);

        }

}
