package me.plantngo.backend.services;

import me.plantngo.backend.DTO.QuestDTO;
import me.plantngo.backend.exceptions.NotExistException;
import me.plantngo.backend.models.Quest;
import me.plantngo.backend.repositories.QuestRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class QuestService {

    private final QuestRepository questRepository;

    @Autowired
    public QuestService(QuestRepository questRepository) {
        this.questRepository = questRepository;
    }
    public List<Quest> getAllQuests() {return questRepository.findAll();}

    public Quest getQuest(Integer id) {
        try{
            return questRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new NotExistException("Quest");
        }
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
}
