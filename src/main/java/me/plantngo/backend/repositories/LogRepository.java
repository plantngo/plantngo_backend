package me.plantngo.backend.repositories;

import me.plantngo.backend.models.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface LogRepository extends JpaRepository<Log, Integer> {
    Optional<Log> findAllByUsernameAndTypeAndDateTimeAfter (String username, String type, LocalDateTime postedDateTime);
}
