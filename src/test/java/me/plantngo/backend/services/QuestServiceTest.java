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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class QuestServiceTest {
    @Mock
    private QuestRepository questRepository;

    @Mock
    private LogRepository logRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private QuestService questService;

    @Test
    void testGetAllQuests_QuestsExist_ReturnAllQuests() {

        //arrange
        List<Quest> questList = List.of(new Quest(), new Quest(), new Quest());

        when(questRepository.findAll()).thenReturn(questList);

        //act
        List<Quest> returnValue = questService.getAllQuests();

        //assert
        assertEquals(questList, returnValue);
        verify(questRepository).findAll();
    }

    @Test
    void testGetQuestById_QuestExists_ReturnQuest() {
        //arrange
        Integer questId = 1;
        Quest quest = new Quest();
        quest.setId(questId);

        when(questRepository.findById(any(Integer.class)))
                .thenReturn(Optional.of(quest));

        //act
        Quest returnedQuest = questService.getQuest(questId);

        //assert
        assertEquals(quest, returnedQuest);
        verify(questRepository).findById(questId);
    }

    @Test
    void testGetQuestById_QuestDoesNotExist_ThrowNotExistException() {
        //arrange
        Integer questId = 1;
        String exceptionMessage = "foo";

        when(questRepository.findById(any(Integer.class)))
                .thenReturn(Optional.empty());

        //act
        try{
            questService.getQuest(questId);
        } catch (NotExistException e) {
            exceptionMessage = e.getMessage();
        }

        //assert
        assertEquals("Quest doesn't exist!", exceptionMessage);
        verify(questRepository).findById(questId);
    }

    @Test
    void testAddQuest_ValidQuestDTO_ReturnSuccess() {
        //arrange
        QuestDTO questDTO = new QuestDTO("login", 3, 200, 7);

        //act
        ResponseEntity<String> response = questService.addQuest(questDTO);

        //assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(questRepository).save(any(Quest.class));
    }

    @Test
    void testDeleteQuest_ValidQuestId_ReturnSuccess() {
        //arrange
        Integer questId = 1;
        Quest quest = new Quest();
        quest.setId(questId);

        //act
        ResponseEntity<String> response = questService.deleteQuest(questId);

        //assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(questRepository).deleteById(questId);
    }


    @Test
    void testRefreshQuestForCustomer_QuestNotFound_ReturnSuccess(){
        //arrange
        Integer questId = 1;
        Integer countToComplete = 1;
        Integer greenPoints = 100;
        String type = "order";
        LocalDateTime postedDateTime = LocalDateTime.of(2022,
                Month.JULY, 20, 23, 59);
        LocalDateTime endDateTime = LocalDateTime.of(2022,
                Month.JULY, 28, 23, 59);

        Quest quest = new Quest();
        quest.setId(questId);
        quest.setType(type);
        quest.setPostedDateTime(postedDateTime);
        quest.setEndDateTime(endDateTime);
        quest.setCountToComplete(countToComplete);
        quest.setPoints(greenPoints);

        String username = "john";
        Set<Quest> completedQuest = new HashSet<>();
        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setCompletedQuests(completedQuest);

        Integer logId = 2;
        LocalDateTime completedDateTime = LocalDateTime.of(2022,
                Month.JULY, 21, 23, 59);
        Log log = new Log();
        log.setType(type);
        log.setUsername(username);
        log.setId(logId);
        log.setDateTime(completedDateTime);

        List<Log> matches = new ArrayList<>();
        matches.add(log);

        when(questRepository.findById(any(Integer.class)))
                .thenReturn(Optional.of(quest));
        when(logRepository.findAllByUsernameAndTypeAndDateTimeBetween(any(String.class),any(String.class),
                any(LocalDateTime.class),any(LocalDateTime.class)))
                .thenReturn(matches);
        when(customerRepository.saveAndFlush(any(Customer.class)))
                .thenReturn(customer);

        //act
        ResponseEntity<String> response = questService.refreshQuestForCustomer(questId,customer);

        //assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(questRepository).findById(questId);
        verify(logRepository).findAllByUsernameAndTypeAndDateTimeBetween(username,type,postedDateTime,endDateTime);
        verify(customerRepository).saveAndFlush(customer);
    }

    @Test
    void testRefreshQuestForCustomer_QuestDoesNotExist_ThrowNotExistException(){
        //arrange
        String exceptionMsg = "";
        Customer customer = new Customer();
        //act
        try {
            questService.refreshQuestForCustomer(1,customer);
        } catch (NotExistException e){
            exceptionMsg = e.getMessage();
        }
        //assert
        assertEquals("Quest doesn't exist!",exceptionMsg);
    }

    @Test
    void testGetAllActiveQuestProgressByUsername_NoActiveQuest_ReturnEmptyList(){
        //arrange
        String username = "jacky";
        Set<Quest> completedQuests = new HashSet<>();
        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setCompletedQuests(completedQuests);

        List<Quest> quests = new ArrayList<>();

        when(questRepository.findAllByEndDateTimeAfter(any(LocalDateTime.class)))
                .thenReturn(quests);
        when(customerRepository.findByUsername(any(String.class)))
                .thenReturn(Optional.of(customer));
        //act
        List<QuestProgressDTO> output = questService.getAllActiveQuestProgressByUsername(username);

        //assert
        assertEquals(0, output.size());
        verify(questRepository).findAllByEndDateTimeAfter(any(LocalDateTime.class));
        verify(customerRepository).findByUsername(any(String.class));
    }

    @Test
    void testGetAllActiveQuestProgressByUsername_ActiveQuest_ReturnQuestProgressList(){
        //arrange
        Integer questId = 1;
        Integer countToComplete = 1;
        Integer greenPoints = 100;
        String type = "order";
        LocalDateTime postedDateTime = LocalDateTime.of(2022,
                Month.JULY, 20, 23, 59);
        LocalDateTime endDateTime = LocalDateTime.of(2022,
                Month.JULY, 28, 23, 59);

        Quest quest = new Quest();
        quest.setId(questId);
        quest.setType(type);
        quest.setPostedDateTime(postedDateTime);
        quest.setEndDateTime(endDateTime);
        quest.setCountToComplete(countToComplete);
        quest.setPoints(greenPoints);

        Integer quest2Id = 2;
        Quest quest2 = new Quest();
        quest.setId(quest2Id);
        quest.setType(type);
        quest.setPostedDateTime(postedDateTime);
        quest.setEndDateTime(endDateTime);
        quest.setCountToComplete(countToComplete);
        quest.setPoints(greenPoints);

        String username = "jacky";
        Set<Quest> completedQuests = new HashSet<>();
        completedQuests.add(quest2);
        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setCompletedQuests(completedQuests);

        List<Quest> quests = new ArrayList<>();
        quests.add(quest);

        Integer logId = 2;
        LocalDateTime completedDateTime = LocalDateTime.of(2022,
                Month.JULY, 21, 23, 59);
        Log log = new Log();
        log.setType(type);
        log.setUsername(username);
        log.setId(logId);
        log.setDateTime(completedDateTime);

        List<Log> matches = new ArrayList<>();
        matches.add(log);

        when(questRepository.findAllByEndDateTimeAfter(any(LocalDateTime.class)))
                .thenReturn(quests);
        when(customerRepository.findByUsername(any(String.class)))
                .thenReturn(Optional.of(customer));
        when(logRepository.findAllByUsernameAndTypeAndDateTimeBetween(any(String.class),any(String.class),
                any(LocalDateTime.class),any(LocalDateTime.class)))
                .thenReturn(matches);
        //act
        List<QuestProgressDTO> output = questService.getAllActiveQuestProgressByUsername(username);

        //assert
        assertEquals(1, output.size());
        verify(questRepository).findAllByEndDateTimeAfter(any(LocalDateTime.class));
        verify(customerRepository).findByUsername(any(String.class));
        verify(logRepository).findAllByUsernameAndTypeAndDateTimeBetween(any(String.class),any(String.class),
                any(LocalDateTime.class),any(LocalDateTime.class));
    }

    @Test
    void testRefreshQuest_ValidInput_returnSuccess(){
        //arrange
        Integer questId = 1;
        List<Customer> allCustomer = new ArrayList<>();
        when(customerRepository.findAll())
                .thenReturn(allCustomer);
        //act
        ResponseEntity<String> output = questService.refreshQuest(questId);

        //assert
        assertEquals("Refreshed quest 1 for all customers", output.getBody());
        verify(customerRepository).findAll();
    }

}
