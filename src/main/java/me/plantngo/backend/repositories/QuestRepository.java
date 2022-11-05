package me.plantngo.backend.repositories;

import me.plantngo.backend.models.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuestRepository extends JpaRepository<Quest, Integer> {
    public List<Quest> findAllByEndDateTimeAfter(LocalDateTime localDateTime);
    public List<Quest> findAllByEndDateTimeBefore(LocalDateTime localDateTime);
}
