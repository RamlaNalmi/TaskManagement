package com.example.taskmanagement.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.taskmanagement.database.entities.Task

@Dao
interface TaskDao {

    @Insert
    suspend fun insertTask(task: Task)

    @Query("SELECT * FROM Task WHERE userId = :userId AND completed = 0")
    suspend fun getAllTasksForUser(userId: Int): List<Task>

    @Query("SELECT * FROM Task WHERE userId = :userId AND completed = 1")
    suspend fun getAllCompletedTasksForUser(userId: Int): List<Task>

    @Delete
    suspend fun delete(task: Task)

    @Update
    suspend fun updateTask(task: Task)




    @Query("UPDATE Task SET completed = :completed, completedDate = :completionDate WHERE id = :taskId")
    suspend fun updateTaskCompletionAndDate(taskId: Int, completed: Boolean, completionDate: String?)




    @Query("SELECT * FROM Task WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): Task?

    @Query("SELECT * FROM Task WHERE userId = :userId " +
            "AND (:priority IS NULL OR priority = :priority) " +
            "AND (:category IS NULL OR category = :category)"+
            "AND (completed = 0)")
    suspend fun getTasksByPriorityAndCategory(userId: Int, priority: String?, category: String?): List<Task>

    @Query("SELECT * FROM Task WHERE userId = :userId AND date = :selectedDate")
    suspend fun getTasksForDate(selectedDate: String, userId: Int): List<Task>

}