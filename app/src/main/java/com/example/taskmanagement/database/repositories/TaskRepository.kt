package com.example.taskmanagement.database.repositories

import com.example.taskmanagement.database.dao.TaskDao

import com.example.taskmanagement.database.entities.Task

class TaskRepository (private val taskDao: TaskDao) {
    suspend fun insertTask(task: Task) = taskDao.insertTask(task)
}