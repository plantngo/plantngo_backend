package me.plantngo.backend.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import me.plantngo.backend.DTO.RegistrationDTO;
import me.plantngo.backend.models.Preference;
import me.plantngo.backend.services.AuthService;

@Configuration
public class SampleDataConfig implements CommandLineRunner {

    @Autowired
    private AuthService authService;

    @Override
    public void run(String... args) throws Exception {

        // create sample customers
        List<RegistrationDTO> customerList = createCustomers();
        for (RegistrationDTO customer : customerList) {
            authService.registerUser(customer);
        }

        // create sample merchants
        List<RegistrationDTO> merchantList = createMerchants();
        for (RegistrationDTO merchant : merchantList) {
            authService.registerUser(merchant);
        }
    }

    public static List<RegistrationDTO> createCustomers() {
        ArrayList<RegistrationDTO> customerList = new ArrayList<>();
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
        customerList.addAll(List.of(customer1, customer2));

        return customerList;
    }

    public static List<RegistrationDTO> createMerchants() {
        ArrayList<RegistrationDTO> merchantList = new ArrayList<>();
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

        merchantList.addAll(List.of(merchantPizzaHut, merchantFairPrice, merchantJoieVege));

        return merchantList;

    }

}
