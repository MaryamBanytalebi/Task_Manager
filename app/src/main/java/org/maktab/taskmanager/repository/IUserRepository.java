package org.maktab.taskmanager.repository;

import org.maktab.taskmanager.model.Task;
import org.maktab.taskmanager.model.User;

import java.util.List;

public interface IUserRepository {

    List<User> getUsers();
    User getUser(String username, String password);
    void insertUser(User user);
    void deleteUser(User user);
    void deleteUserTasks(long userId);
    List<Task> getUserTasks (long userId);
    int numberOfTask(long userId);
}
