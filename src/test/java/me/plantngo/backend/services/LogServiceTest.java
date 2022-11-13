package me.plantngo.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import me.plantngo.backend.models.Log;
import me.plantngo.backend.repositories.LogRepository;

@ExtendWith(MockitoExtension.class)
class LogServiceTest {
    
    @Mock
    private LogRepository logRepository;

    @InjectMocks
    private LogService logService;

    @Test
    void testAddLog_ValidInput_ReturnLog() {

        // Arrange
        String username = "Daniel";
        String type = "Orders";
        Log expectedLog = new Log();
        expectedLog.setUsername(username);
        expectedLog.setType(type);
        expectedLog.setDateTime(LocalDateTime.now());

        when(logRepository.save(any(Log.class)))
            .thenReturn(expectedLog);

        // Act
        Log responseLog = logService.addLog(username, type);

        // Assert
        assertEquals(expectedLog, responseLog);
        verify(logRepository, times(1)).save(expectedLog);
    }
}
