package com.rodolfocf.taskscheduler.business.mapper;

import com.rodolfocf.taskscheduler.business.dto.TaskDTO;
import com.rodolfocf.taskscheduler.infrastructure.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UpdateTaskConverter {

    void taskUpdate(TaskDTO taskDTO, @MappingTarget TaskEntity taskEntity);

}
