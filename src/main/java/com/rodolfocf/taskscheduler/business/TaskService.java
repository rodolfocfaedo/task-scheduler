package com.rodolfocf.taskscheduler.business;

import com.rodolfocf.taskscheduler.business.dto.TaskDTO;
import com.rodolfocf.taskscheduler.business.mapper.TaskConverter;
import com.rodolfocf.taskscheduler.business.mapper.UpdateTaskConverter;
import com.rodolfocf.taskscheduler.infrastructure.entity.TaskEntity;
import com.rodolfocf.taskscheduler.infrastructure.enums.NotificationStatusEnum;
import com.rodolfocf.taskscheduler.infrastructure.exceptions.ResourceNotFoundException;
import com.rodolfocf.taskscheduler.infrastructure.repository.TaskRepository;
import com.rodolfocf.taskscheduler.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskConverter taskConverter;
    private final JwtUtil jwtUtil;
    private final UpdateTaskConverter updateTaskConverter;

    public TaskDTO addTask(String token, TaskDTO taskDTO) {
        String email = jwtUtil.extractEmailFromToken(token.substring(7));
        taskDTO.setDateOfCreation(LocalDateTime.now());
        taskDTO.setNotificationStatusEnum(NotificationStatusEnum.PENDING);
        taskDTO.setClientEmail(email);
        TaskEntity taskEntity = taskConverter.toTaskEntity(taskDTO);
        return taskConverter.toTaskDTO(taskRepository.save(taskEntity));
    }

    public List<TaskDTO> searchScheduledTasksByPeriod(LocalDateTime startDate, LocalDateTime finalDate) {
        return taskConverter.toListTaskDTO(taskRepository.findByTaskDateBetween(startDate, finalDate));
    }

    public List<TaskDTO> findByClientEmail(String token) {
        String email = jwtUtil.extractEmailFromToken(token.substring(7));
        return taskConverter.toListTaskDTO(taskRepository.findByClientEmail(email));
    }

    public void deleteTaskById(String id) {
        taskRepository.deleteById(id);
    }

    public TaskDTO changeStatus(NotificationStatusEnum statusEnum, String id) {
        try {
            TaskEntity taskEntity = taskRepository.findById(id).
                    orElseThrow(() -> new ResourceNotFoundException("Task not found"));
            taskEntity.setNotificationStatusEnum(statusEnum);
            return taskConverter.toTaskDTO(taskRepository.save(taskEntity));
        } catch (ResourceNotFoundException e) {
            throw  new ResourceNotFoundException("Error changing task status", e.getCause());
        }
    }

    public TaskDTO taskUptade (TaskDTO taskDTO, String id) {
        try {
            TaskEntity taskEntity = taskRepository.findById(id).
                    orElseThrow(() -> new ResourceNotFoundException("Task not found"));
            updateTaskConverter.taskUpdate(taskDTO, taskEntity);
            return taskConverter.toTaskDTO(taskRepository.save(taskEntity));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Error to update task", e.getCause());
        }
    }




}
