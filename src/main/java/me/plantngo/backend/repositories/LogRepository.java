package me.plantngo.backend.repositories;

import me.plantngo.backend.models.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LogRepository extends JpaRepository<Log, Integer> {
    List<Log> findAllByUsernameAndTypeAndDateTimeBetween (String username, String type, LocalDateTime postedDateTime, LocalDateTime endDateTime);
}
