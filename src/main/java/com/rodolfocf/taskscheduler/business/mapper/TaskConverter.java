package com.rodolfocf.taskscheduler.business.mapper;

import com.rodolfocf.taskscheduler.business.dto.TaskDTO;
import com.rodolfocf.taskscheduler.infrastructure.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface TaskConverter {

    TaskEntity toTaskEntity(TaskDTO taskDTO);

    TaskDTO toTaskDTO (TaskEntity taskEntity);

    List<TaskEntity> toListTaskEntity (List<TaskDTO> taskDTO);

    List<TaskDTO> toListTaskDTO (List<TaskEntity> taskEntity);

}
