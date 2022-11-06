package me.plantngo.backend.services;

import me.plantngo.backend.DTO.QuestDTO;
import me.plantngo.backend.DTO.QuestProgressDTO;
import me.plantngo.backend.exceptions.NotExistException;
import me.plantngo.backend.models.Customer;
import me.plantngo.backend.models.Log;
import me.plantngo.backend.models.Quest;
import me.plantngo.backend.repositories.CustomerRepository;
import me.plantngo.backend.repositories.LogRepository;
import me.plantngo.backend.repositories.QuestRepository;
import me.plantngo.backend.repositories.VoucherRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class QuestService {

    private final QuestRepository questRepository;

    private final LogRepository logRepository;

    private final CustomerRepository customerRepository;

    @Autowired
    public QuestService(QuestRepository questRepository, LogRepository logRepository,
            CustomerRepository customerRepository) {
        this.questRepository = questRepository;
        this.logRepository = logRepository;
        this.customerRepository = customerRepository;
    }

    public List<Quest> getAllQuests() {
        return questRepository.findAll();
    }

    public Quest getQuest(Integer id) {
        try {
            return questRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new NotExistException("Quest");
        }
    }

    public List<Quest> getActiveQuests() {
        return questRepository.findAllByEndDateTimeAfter(LocalDateTime.now());
    }

    public List<Quest> getInactiveQuests() {
        return questRepository.findAllByEndDateTimeBefore(LocalDateTime.now());
    }

    public ResponseEntity<String> addQuest(QuestDTO questDTO) {
        Quest quest = new Quest();

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setSkipNullEnabled(true);
        mapper.map(questDTO, quest);

        quest.setPostedDateTime(LocalDateTime.now());
        quest.setEndDateTime(LocalDateTime.now().plus(Duration.ofDays(questDTO.getPersistForHowManyDays())));

        questRepository.save(quest);

        return new ResponseEntity<>("Successfully added new quest", HttpStatus.OK);
    }

    public ResponseEntity<String> deleteQuest(Integer id) {
        questRepository.deleteById(id);
        return new ResponseEntity<>("Successfully deleted quest with id:" + id, HttpStatus.OK);
    }

    public ResponseEntity<String> refreshQuestByCustomerUsername(Integer id, String username) {
        Customer customer = customerRepository.findByUsername(username).get();
        return refreshQuestForCustomer(id, customer);

    }

    public ResponseEntity<String> refreshQuestForCustomer(Integer questId, Customer customer) {
        String username = customer.getUsername();

        Quest quest;
        try {
            quest = questRepository.findById(questId).get();
        } catch (NoSuchElementException e) {
            throw new NotExistException("Quest");
        }

        List<Log> matches = logRepository.findAllByUsernameAndTypeAndDateTimeBetween(
                username,
                quest.getType(),
                quest.getPostedDateTime(),
                quest.getEndDateTime());

        /*
         * update customer's completed quests if necessary
         */
        if (matches.size() >= quest.getCountToComplete()) {
            if (addCompletedQuestForCustomer(customer, quest)) {
                System.out.println("<QUEST>: Updated quest status for customer: " + username);
                customer.setGreenPoints(customer.getGreenPoints() + quest.getPoints());
                customerRepository.save(customer);
            }

        }

        return new ResponseEntity<>("Refreshed quest for customer: " + username, HttpStatus.OK);
    }

    public ResponseEntity<String> refreshQuest(Integer questId) {
        List<Customer> allCustomers = customerRepository.findAll();

        System.out.println("-------------------------------------");
        System.out.println("<QUEST>: Refreshing quest " + questId);
        for (Customer customer : allCustomers) {
            refreshQuestForCustomer(questId, customer);
        }
        System.out.println("<QUEST>: Refreshed quest " + questId);
        System.out.println("-------------------------------------");

        return new ResponseEntity<>("Refreshed quest " + questId + " for all customers", HttpStatus.OK);
    }

    public ResponseEntity<String> refreshAll() {

        List<Quest> allQuests = questRepository.findAll();

        List<Integer> allQuestIds = extractQuestIdsList(allQuests);

        for (Integer id : allQuestIds) {
            refreshQuest(id);
        }

        System.out.println("<QUEST>: Refreshed ALL quests ");
        return new ResponseEntity<>("Refreshed all quests for all customers", HttpStatus.OK);
    }

    private Boolean addCompletedQuestForCustomer(Customer customer, Quest newQuest) {

        Set<Quest> completed = customer.getCompletedQuests();

        if (completed == null) {
            completed = new HashSet<>();
        }

        Boolean isAdded = completed.add(newQuest);

        customer.setCompletedQuests(completed);

        customerRepository.saveAndFlush(customer);

        return isAdded;
    }

    private List<Integer> extractQuestIdsList(List<Quest> quests) {

        List<Integer> output = new ArrayList<>();

        for (Quest quest : quests) {
            output.add(quest.getId());
        }

        return output;
    }

    public List<QuestProgressDTO> getAllActiveQuestProgressByUsername(String username) {

        List<Quest> quests = this.questRepository.findAllByEndDateTimeAfter(LocalDateTime.now());
        // List<Quest> customerQuests = this.questRepository
        // .findAllBycustomersThatHaveCompletedUsernameAndEndDateTimeAfter(
        // username,
        // LocalDateTime.now());
        System.out.println(quests);

        List<QuestProgressDTO> questProgress = new ArrayList<>();
        for (Quest quest : quests) {
            List<Log> matches = logRepository.findAllByUsernameAndTypeAndDateTimeBetween(
                    username,
                    quest.getType(),
                    quest.getPostedDateTime(),
                    quest.getEndDateTime());

            QuestProgressDTO questProgressDTO = new QuestProgressDTO();
            ModelMapper mapper = new ModelMapper();
            mapper.getConfiguration().setSkipNullEnabled(true);
            mapper.map(quest, questProgressDTO);
            questProgressDTO.setCountCompleted(matches.size());
            questProgress.add(questProgressDTO);
        }

        return questProgress;
    }

}
