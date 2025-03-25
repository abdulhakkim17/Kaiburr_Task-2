package com.example.task_manager.service;

import com.example.task_manager.models.Task;
import com.example.task_manager.models.TaskExecution;
import com.example.task_manager.repository.TaskRepository;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;

    private static final String[] UNSAFE_COMMANDS = {
        "rm -rf", "shutdown", "reboot", "mkfs", "wget http", "curl http", 
        "dd if=", ">:*", ":(){ :|:& };:", "poweroff"
    };

    private boolean isSafeCommand(String command) {
        for (String unsafe : UNSAFE_COMMANDS) {
            if (command.toLowerCase().contains(unsafe)) {
                return false;
            }
        }
        return true;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(String id) {
        return taskRepository.findById(id);
    }

    public List<Task> getTasksByName(String name) {
        return taskRepository.findAll().stream()
                .filter(task -> task.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Task createTask(Task task) throws IllegalArgumentException {
        if (!isSafeCommand(task.getCommand())) {
            throw new IllegalArgumentException("Unsafe command detected!");
        }
        return taskRepository.save(task);
    }

    public Task updateTask(String id, Task task) throws IllegalArgumentException {
        if (!isSafeCommand(task.getCommand())) {
            throw new IllegalArgumentException("Unsafe command detected!");
        }
        if (taskRepository.existsById(id)) {
            task.setId(id);
            return taskRepository.save(task);
        }
        return null;
    }

    public void deleteTask(String id) {
        taskRepository.deleteById(id);
    }

    public TaskExecution executeTask(String taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isEmpty()) {
            throw new RuntimeException("Task not found");
        }

        Task task = optionalTask.get();
        if (!isSafeCommand(task.getCommand())) {
            throw new IllegalArgumentException("Unsafe command detected!");
        }

        TaskExecution execution = new TaskExecution();
        execution.setStartTime(Instant.now()); 

        try {
            ApiClient client = Config.defaultClient();
            CoreV1Api api = new CoreV1Api(client);

            V1Pod pod = new V1Pod()
                .apiVersion("v1")
                .kind("Pod")
                .metadata(new V1ObjectMeta().name("task-execution-" + taskId))
                .spec(new V1PodSpec()
                    .containers(List.of(new V1Container()
                        .name("task-container")
                        .image("busybox")
                        .command(List.of("sh", "-c", task.getCommand()))
                    ))
                    .restartPolicy("Never")
                );

            logger.info("Creating Kubernetes pod for task execution...");
            api.createNamespacedPod("default", pod, null, null, null, null);
            execution.setOutput("Pod created successfully");
            logger.info("Pod created successfully for task: {}", taskId);
        } catch (ApiException e) {
            logger.error("Kubernetes API exception: {}", e.getResponseBody(), e);
            execution.setOutput("Error: " + e.getResponseBody());
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
            execution.setOutput("Unexpected error: " + e.getMessage());
        }

        execution.setEndTime(Instant.now()); 
        task.getTaskExecutions().add(execution);
        taskRepository.save(task);

        return execution;
    }
}
