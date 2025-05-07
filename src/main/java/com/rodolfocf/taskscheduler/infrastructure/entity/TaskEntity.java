package com.rodolfocf.taskscheduler.infrastructure.entity;


import com.rodolfocf.taskscheduler.infrastructure.enums.NotificationStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document("task")
public class TaskEntity {

    @Id
    private String id;
    private String taskName;
    private String description;
    private LocalDateTime dateOfCreation;
    private LocalDateTime taskDate;
    private String clientEmail;
    private LocalDateTime modificationDate;
    private NotificationStatusEnum notificationStatusEnum;


}
