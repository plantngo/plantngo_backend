package me.plantngo.backend.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import me.plantngo.backend.DTO.CategoryDTO;
import me.plantngo.backend.DTO.ProductDTO;
import me.plantngo.backend.DTO.RegistrationDTO;
import me.plantngo.backend.DTO.VoucherDTO;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.models.Promotion;
import me.plantngo.backend.models.Quest;
import me.plantngo.backend.repositories.PromotionRepository;
import me.plantngo.backend.repositories.QuestRepository;
import me.plantngo.backend.services.AuthService;
import me.plantngo.backend.services.MerchantService;
import me.plantngo.backend.services.MinioService;
import me.plantngo.backend.services.ShopService;

@Configuration
public class SampleDataConfig {

        @Autowired
        private AuthService authService;
        @Autowired
        private ShopService shopService;
        @Autowired
        private MerchantService merchantService;
        @Autowired
        private PromotionRepository promotionRepository;
        @Autowired
        private QuestRepository questRepository;

        @Autowired
        private MinioService minioService;

        @Bean
        CommandLineRunner commandLineRunner() {
                return args -> {

                        createCustomers();
                        createMerchants();
                        createMerchantCategories();
                        createMerchantProducts();
                        createVouchers();
                        createPromotions();
                        createQuests();
                        minioService.initBuckets();
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
                                "10:00AM - 20:00PM",
                                1905.13);
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
                                "7:00AM - 21:00PM",
                                728.98);
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
                                "10:00AM - 20:00PM",
                                2374.35);
                RegistrationDTO merchantGreenDot = new RegistrationDTO(
                                "greendot",
                                "greendot@example.com",
                                "Password123!",
                                'M',
                                "Green Dot",
                                "https://www.greendot.sg/wp-content/uploads/2014/09/logo.png",
                                "https://www.greendot.sg/wp-content/themes/Greendot/images/banner-connect.jpg",
                                "1 Raffles Pl, #03 - 23 / 24 / 25, Singapore 048616",
                                "Contemporary indoor-outdoor rooftop eatery serving a set menu of adventurous vegetarian cuisine.",
                                1.2838519,
                                103.7810027,
                                "Vegetarian",
                                2,
                                "11:00AM - 20:30PM",
                                2293.6);

                authService.registerUser(merchantPizzaHut);
                authService.registerUser(merchantFairPrice);
                authService.registerUser(merchantJoieVege);
                authService.registerUser(merchantGreenDot);

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

                Merchant greendot = merchantService.getMerchantByUsername("greendot");
                CategoryDTO greenDotCat1 = new CategoryDTO("Vegetarian");
                shopService.addCategory(greendot, greenDotCat1);
        }

        public void createMerchantProducts() throws MalformedURLException {
                Merchant pizzaHut = merchantService.getMerchantByUsername("pizzahut");
                ProductDTO pizzaHutProduct1 = new ProductDTO("Cheesy 7 Beyond Supreme - Regular Pan",
                                34.90,
                                "Cheesy 7 meets Beyond Meat! Made with Beyond Meat's plant based Italian Sausage Crumbles which leaves a hint of herbs and spice aroma, with capsicums, onions and mushrooms, on a bed of our signature sweet sauce and seven wondrous cheeses!",
                                1089.2,
                                new URL("https://static.phdvasia.com/sg1/menu/single/desktop_thumbnail_0bd9658b-d8eb-438d-bc2c-fef8afa1b71e.jpg"),
                                "Savoury & Cheesy");
                ProductDTO pizzaHutProduct2 = new ProductDTO("Cheesy 7 Beyond Supreme - Regular Cheesy Stuffed Crust",
                                43.40,
                                "Cheesy 7 meets Beyond Meat! Made with Beyond Meat's plant based Italian Sausage Crumbles which leaves a hint of herbs and spice aroma, with capsicums, onions and mushrooms, on a bed of our signature sweet sauce and seven wondrous cheeses!",
                                2582.4,
                                new URL("https://static.phdvasia.com/sg1/menu/single/desktop_thumbnail_0bd9658b-d8eb-438d-bc2c-fef8afa1b71e.jpg"),
                                "Savoury & Cheesy");
                ProductDTO pizzaHutProduct3 = new ProductDTO("Cheesy 7 Beyond Supreme - Large Pan",
                                40.90,
                                "Cheesy 7 meets Beyond Meat! Made with Beyond Meat's plant based Italian Sausage Crumbles which leaves a hint of herbs and spice aroma, with capsicums, onions and mushrooms, on a bed of our signature sweet sauce and seven wondrous cheeses!",
                                2043.8,
                                new URL("https://static.phdvasia.com/sg1/menu/single/desktop_thumbnail_0bd9658b-d8eb-438d-bc2c-fef8afa1b71e.jpg"),
                                "Savoury & Cheesy");
                shopService.addProduct(pizzaHut, "Pizza", pizzaHutProduct1);
                shopService.addProduct(pizzaHut, "Pizza", pizzaHutProduct2);
                shopService.addProduct(pizzaHut, "Pizza", pizzaHutProduct3);

                Merchant fairPrice = merchantService.getMerchantByUsername("fairprice");
                ProductDTO fairPriceProduct1 = new ProductDTO(
                                "The Vegetarian Butcher Vegan Meal - Chickened Out Chunks", 7.95,
                                "The Vegetarian Butcher Vegan Meal - Chickened Out Chunks",
                                386.6,
                                new URL("https://media.nedigital.sg/fairprice/fpol/media/images/product/XL/13208081_XL1_20220722.jpg?w=1200&q=70"),
                                "Plain");
                ProductDTO fairPriceProduct2 = new ProductDTO("Heinz Vegan Seriously Good Garlic Aioli Mayonnaise",
                                8.95,
                                "Heinz Vegan Seriously Good Garlic Aioli Mayonnaise",
                                165.5,
                                new URL("https://media.nedigital.sg/fairprice/fpol/media/images/product/XL/90094302_XL1_20210816.jpg?w=1200&q=70"),
                                "Sour");
                ProductDTO fairPriceProduct3 = new ProductDTO(
                                "Hey! Chips - Broccoli (100% Natural, Gluten-Free, Vegan)",
                                4.90,
                                "Hey! Chips - Broccoli (100% Natural, Gluten-Free, Vegan)",
                                386.6,
                                new URL("https://media.nedigital.sg/fairprice/fpol/media/images/product/XL/90045450_XL1_20211018.jpg?w=1200&q=70"),
                                "Salty");
                ProductDTO fairPriceProduct4 = new ProductDTO("Hey! Chips - Banana (100% Natural, Gluten-Free, Vegan)",
                                4.90,
                                "Hey! Chips - Banana (100% Natural, Gluten-Free, Vegan)",
                                386.6,
                                new URL("https://media.nedigital.sg/fairprice/fpol/media/images/product/L/90045446_L1_20211018.jpg?q=60"),
                                "Salty");
                ProductDTO fairPriceProduct5 = new ProductDTO("Hey! Chips - Bulk (100% Natural, Gluten-Free, Vegan)",
                                33.90,
                                "Hey! Chips - Bulk (100% Natural, Gluten-Free, Vegan",
                                2319.6,
                                new URL("https://media.nedigital.sg/fairprice/fpol/media/images/product/L/90027875_L1_20211018.jpg?q=60"),
                                "Salty");

                shopService.addProduct(fairPrice, "Groceries", fairPriceProduct1);
                shopService.addProduct(fairPrice, "Groceries", fairPriceProduct2);
                shopService.addProduct(fairPrice, "Snacks", fairPriceProduct3);
                shopService.addProduct(fairPrice, "Snacks", fairPriceProduct4);
                shopService.addProduct(fairPrice, "Snacks", fairPriceProduct5);

                Merchant joieVege = merchantService.getMerchantByUsername("joievege");
                ProductDTO joieVegeProduct1 = new ProductDTO("4 Course Set", 38.00, "Including 1 Drink of Choice",
                                2221.6,
                                new URL("https://static.wixstatic.com/media/591a97_20e12d8438d84db48839fb893ed60072~mv2.jpg/v1/fit/w_467,h_571,q_90/591a97_20e12d8438d84db48839fb893ed60072~mv2.jpg"),
                                "Savoury");
                ProductDTO joieVegeProduct2 = new ProductDTO("5 Course Set", 58.00, "Including 1 Drink of Choice",
                                2527.1,
                                new URL("https://static.wixstatic.com/media/591a97_49f940ea3868444b9867202f9002610c~mv2.jpg/v1/fit/w_467,h_693,q_90/591a97_49f940ea3868444b9867202f9002610c~mv2.jpg"),
                                "Savoury");
                shopService.addProduct(joieVege, "Fine Dining", joieVegeProduct1, null);
                shopService.addProduct(joieVege, "Fine Dining", joieVegeProduct2, null);

                Merchant greenDot = merchantService.getMerchantByUsername("greendot");
                ProductDTO greenDotProduct1 = new ProductDTO(
                                "Signature Lion Mane Mushroom Rendang with Turmeric Rice Bento Set", 10.90,
                                "Rendang Lion Mane Mushroom, Beijing Cabbage with Carrot Strips, Nyonya Achar, Turmeric Basmati Rice with Cranberry and Papadum. Complimentary Soup Included.",
                                2331.6,
                                new URL("https://www.greendot.sg/wp-content/uploads/2018/10/Screenshot-2022-04-24-at-11.14.29-PM.png"),
                                "Savoury");
                ProductDTO greenDotProduct2 = new ProductDTO(
                                "Crispy Beancurd Skin w Sesame Rice Bento", 9.9,
                                "Crispy Beancurd Skin, Braised Tau Kwa & Mushroom, Achar, Sesame Rice with Parsley & Crispy Beancurd Skin (Chili Provided). Complimentary Soup Included.",
                                2117.1,
                                new URL("https://www.greendot.sg/wp-content/uploads/2018/10/Screenshot-2022-04-24-at-11.15.46-PM-150x150.png"),
                                "Savoury");
                ProductDTO greenDotProduct3 = new ProductDTO(
                                "Angelica Herbal Trio Mushroom with Brown Rice Bento Set", 5.9,
                                "Angelica Herbal Fresh Trio Mushroom, Broccoli with Carrot Strips, Braised Pumpkin, Brown Rice. Complimentary Soup Included.",
                                2421.3,
                                new URL("https://www.greendot.sg/wp-content/uploads/2018/10/Screenshot-2022-04-24-at-11.17.30-PM-150x150.png"),
                                "Savoury");

                shopService.addProduct(greenDot, "Vegetarian", greenDotProduct1, null);
                shopService.addProduct(greenDot, "Vegetarian", greenDotProduct2, null);
                shopService.addProduct(greenDot, "Vegetarian", greenDotProduct3, null);
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

        private void createPromotions() throws MalformedURLException {
                Promotion promotion = new Promotion(1, "30% Off Storewide Ala carte item takeaways", null, "20/12/2022",
                                "06/11/2022",
                                new URL("https://www.whynotdeals.com/wp-content/uploads/2020/05/lotus-vegetarian-singapore-30-off-storewide-ala-carte-takeaways-promotion_why-not-deals.jpg"),
                                30);
                Promotion promotion2 = new Promotion(2,
                                "Meatless Monday Promotion! S$1 Claypot Rice with Chicken in GongBao Sauce", null,
                                "20/12/2022",
                                "06/11/2022",
                                new URL("https://media.womensweekly.com.sg/public/2019/11/Where-To-Go-For-Delicious-Vegetarian-Food-In-Singapore_3.jpg?compress=true&quality=80&w=480&dpr=2.6"),
                                20);
                Promotion promotion3 = new Promotion(3,
                                "Students and Seniors Specials, S$5.90 Signature Bento Set and Noodles", null,
                                "20/12/2022",
                                "06/11/2022",
                                new URL("https://www.greendot.sg/wp-content/uploads/2019/09/WEB_Students-Senior-Promotion-01.jpg"),
                                1);
                Promotion promotion4 = new Promotion(4,
                                "Greendot free delivery!", null,
                                "20/12/2022",
                                "06/11/2022",
                                new URL("https://www.greendot.sg/wp-content/uploads/2020/11/homebanner_getz-1048x576.jpeg"),
                                1);
                Promotion promotion5 = new Promotion(5,
                                "Lotus Kitchen Mother's Day, 9 Course Special", null,
                                "20/12/2022",
                                "06/11/2022",
                                new URL("https://www.whynotdeals.com/wp-content/uploads/2018/05/lotus-kitchen-singapore-mothers-day-vegetarian-9-course-meal-promotion-1-31-may-2018_why-not-deals.jpg"),
                                1);

                this.promotionRepository.saveAll(List.of(promotion, promotion2, promotion3, promotion4, promotion5));
        }

        private void createQuests() {
                Quest quest1 = new Quest(1, LocalDateTime.now(), "order", 1, 1000, LocalDateTime.now().plusDays(3),
                                null);
                // LocalDateTime.now().plusDays(7) );
                Quest quest2 = new Quest(2, LocalDateTime.now(), "login", 1, 100, LocalDateTime.now().plusDays(3),
                                null);

                Quest quest3 = new Quest(3, LocalDateTime.now(), "purchase-voucher", 1, 1000,
                                LocalDateTime.now().plusDays(3),
                                null);
                this.questRepository.saveAll(List.of(quest1, quest2, quest3));
        }

}
