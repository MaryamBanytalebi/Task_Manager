package org.maktab.taskmanager.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import org.maktab.taskmanager.model.Task;
import org.maktab.taskmanager.model.User;

import java.util.List;
import java.util.UUID;

@Dao
public interface TaskDatabaseDao {

    @Update
    void updateTask(Task task);

    @Insert
    void insertTask(Task task);

    @Insert
    void insertTasks(List<Task> tasks);

    @Delete
    void deleteTask(Task task);

    @Query("DELETE FROM task")
    void deleteAllTasks();

    @Query("SELECT * FROM task")
    List<Task> getTasks();

    @Query("SELECT * FROM task WHERE state ='Todo' AND user_id_fk=:userId")
    List<Task> getTodoTask(long userId);

    @Query("SELECT * FROM task WHERE state ='Doing' AND user_id_fk=:userId")
    List<Task> getDoingTask(long userId);

    @Query("SELECT * FROM task WHERE state ='Done' AND user_id_fk=:userId")
    List<Task> getDoneTask(long userId);

    @Query("SELECT * FROM task WHERE uuid =:inputUUID")
    Task getTask(UUID inputUUID);

    @Query("SELECT * FROM task WHERE user_id_fk=:userId AND title LIKE :searchValue OR description LIKE :searchValue OR date LIKE :searchValue")
    List<Task> searchTasks(String searchValue, long userId);

    @Insert
    void insertUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM user")
    List<User> getUsers();

    @Query("SELECT * FROM user WHERE  username=:name AND password=:pass")
    User getUser(String name, String pass);

    @Query("DELETE FROM task WHERE user_id_fk=:userId")
    void deleteUserTasks(long userId);

    @Query("SELECT * FROM task WHERE user_id_fk=:userId")
    List<Task> getUserTasks (long userId);

    @Query("SELECT COUNT(*) FROM task WHERE user_id_fk=:userId GROUP BY user_id_fk")
    int numberOfTask(long userId);
}
