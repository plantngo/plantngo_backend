package me.plantngo.backend.services;

import me.plantngo.backend.DTO.QuestDTO;
import me.plantngo.backend.exceptions.NotExistException;
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

import java.util.List;
import java.util.Optional;

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
}
