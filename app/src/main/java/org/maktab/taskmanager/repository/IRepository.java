package org.maktab.taskmanager.repository;

import org.maktab.taskmanager.model.Task;

import java.util.List;
import java.util.UUID;

public interface IRepository {

    List<Task> getTasks();
    Task getTask(UUID taskId);
    void insertTask(Task task);
    void updateTask(Task task);
    void deleteTask(Task task);
    List<Task> getTodoTask();
    List<Task> getDoingTask();
    List<Task> getDoneTask();
}
