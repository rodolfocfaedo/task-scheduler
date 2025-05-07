package com.rodolfocf.taskscheduler.infrastructure.repository;

import com.rodolfocf.taskscheduler.infrastructure.entity.TaskEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends MongoRepository<TaskEntity, String> {

    List<TaskEntity> findByTaskDateBetween(LocalDateTime startDate, LocalDateTime finalDate);

    List<TaskEntity> findByClientEmail(String email);
    
}
