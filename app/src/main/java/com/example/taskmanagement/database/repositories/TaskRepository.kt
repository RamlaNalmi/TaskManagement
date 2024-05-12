package com.example.taskmanagement.database.repositories

import com.example.taskmanagement.database.dao.TaskDao

import com.example.taskmanagement.database.entities.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskRepository (private val taskDao: TaskDao) {
    suspend fun insertTask(task: Task) = taskDao.insertTask(task)

    suspend fun getAllTasksForUser(userId: Int):List<Task> = taskDao.getAllTasksForUser(userId)

    suspend fun getAllCompletedTasksForUser(userId: Int):List<Task> = taskDao.getAllCompletedTasksForUser(userId)
    suspend fun deleteTask(task: Task) = taskDao.delete(task)

    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    suspend fun getTaskById(taskId: Int): Task? = taskDao.getTaskById(taskId)

    suspend fun getTasksByPriorityAndCategory(userId: Int, priority: String?, category: String?): List<Task> = taskDao.getTasksByPriorityAndCategory(userId, priority, category)

    suspend fun updateTaskCompletionAndDate(taskId: Int, isComplete: Boolean) {
        withContext(Dispatchers.IO) {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val currentDate = sdf.format(Date())
            if (isComplete) {
                taskDao.updateTaskCompletionAndDate(taskId, true, currentDate)
            } else {
                taskDao.updateTaskCompletionAndDate(taskId, false, null)
            }
        }
    }


}