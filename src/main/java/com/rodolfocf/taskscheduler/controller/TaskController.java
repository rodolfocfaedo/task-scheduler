package com.rodolfocf.taskscheduler.controller;

import com.rodolfocf.taskscheduler.business.TaskService;
import com.rodolfocf.taskscheduler.business.dto.TaskDTO;
import com.rodolfocf.taskscheduler.infrastructure.enums.NotificationStatusEnum;
import com.rodolfocf.taskscheduler.infrastructure.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDTO> addTask(@RequestBody TaskDTO taskDTO, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(taskService.addTask(token, taskDTO));
    }

    @GetMapping("/scheduler_tasks")
    public ResponseEntity<List<TaskDTO>> searchScheduledTasksByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime finalDate) {
        return ResponseEntity.ok(taskService.searchScheduledTasksByPeriod(startDate, finalDate));
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> findByClientByEmail(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(taskService.findByClientEmail(token));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteTaskById(@RequestParam("id") String id) {
        try {
            taskService.deleteTaskById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Error to delete task by id. Non-existent id: " + id, e.getCause());
        }
        return ResponseEntity.ok().body("Task deleted!");
    }

    @PatchMapping
    public ResponseEntity<TaskDTO> changeNotificationStatus(@RequestParam("status") NotificationStatusEnum statusEnum,
                                                            @RequestParam("id") String id) {
        return ResponseEntity.ok(taskService.changeStatus(statusEnum, id));
    }

    @PutMapping
    public ResponseEntity<TaskDTO> taskUpdate(@RequestBody TaskDTO taskDTO, @RequestParam("id") String id){
        return ResponseEntity.ok(taskService.taskUptade(taskDTO, id));
    }

}
