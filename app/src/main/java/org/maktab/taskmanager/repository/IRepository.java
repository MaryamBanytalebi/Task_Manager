package org.maktab.taskmanager.repository;

import org.maktab.taskmanager.model.Task;

import java.io.File;
import java.util.List;
import java.util.UUID;

public interface IRepository {

    List<Task> getTasks();
    List<Task> searchTasks(String searchValue);
    Task getTask(UUID taskId);
    void insertTask(Task task);
    void updateTask(Task task);
    void deleteTask(Task task);
    void deleteAllTasks();
    List<Task> getTodoTask();
    List<Task> getDoingTask();
    List<Task> getDoneTask();
    File getPhotoFile(Task task);

}
