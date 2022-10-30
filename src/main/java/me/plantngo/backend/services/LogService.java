package me.plantngo.backend.services;

import me.plantngo.backend.models.Log;
import me.plantngo.backend.repositories.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LogService {
    private LogRepository logRepository;

    @Autowired
    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public Log addLog(String username, String type) {
        Log log = new Log();
        log.setUsername(username);
        log.setType(type);
        log.setDateTime(LocalDateTime.now());
        logRepository.save(log);
        //TODO: remove
        System.out.println("09090909_______---___---__-__-___---_-__----)LOGGED_______---___---__-__-___---_-__----)09090909");
        return log;
    }

    //TODO: check for any change
}
