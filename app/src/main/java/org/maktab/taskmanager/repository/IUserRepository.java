package org.maktab.taskmanager.repository;

import org.maktab.taskmanager.model.User;

import java.util.List;

public interface IUserRepository {

    List<User> getUsers();
    User getUser(String username);
    void insertUser(User user);

}
