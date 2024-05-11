package com.example.taskmanagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.database.entities.Task
import com.example.taskmanagement.database.repositories.TaskRepository
import com.example.taskmanagement.database.repositories.UserRepository
import kotlinx.coroutines.launch

class TaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    fun saveTask(task: Task) {
        // You can perform any background task here using coroutines
        viewModelScope.launch {
            // For example, you can save the task to a database
            // Or perform any other asynchronous operation
            taskRepository.insertTask(task)
        }
    }
}