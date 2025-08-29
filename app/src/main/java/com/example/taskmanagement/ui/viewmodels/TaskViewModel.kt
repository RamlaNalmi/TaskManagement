package com.example.taskmanagement.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.database.entities.Task
import com.example.taskmanagement.database.repositories.TaskRepository
import com.example.taskmanagement.database.repositories.UserRepository
import kotlinx.coroutines.launch

class TaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {


    private val _taskList = MutableLiveData<List<Task>>()
    val taskList: LiveData<List<Task>> = _taskList

    private val _task = MutableLiveData<Task>()
    val task: LiveData<Task> = _task

    private val _completeTaskList = MutableLiveData<List<Task>>()
    val completeTaskList: LiveData<List<Task>> = _completeTaskList

    private val _calList =MutableLiveData<List<Task>>()
    val calList: LiveData<List<Task>> = _calList


    private var selectedPriority: String? = null
    private var selectedCategory: String? = null

    // Method to fetch filtered tasks based on selected priority and category
    fun filterTasksByPriorityAndCategory(userId: Int) {
        viewModelScope.launch {
            if (selectedPriority == null && selectedCategory == null) {
                // If no chips are selected, fetch all tasks
                _taskList.value = taskRepository.getAllTasksForUser(userId)
            } else {
                // Fetch tasks based on selected priority and category
                _taskList.value = taskRepository.getTasksByPriorityAndCategory(userId, selectedPriority, selectedCategory)
            }
        }
    }

    // Method to update selected priority
    fun setSelectedPriority(priority: String?, userId: Int) {
        selectedPriority = priority
        // Trigger filter when priority is selected
        filterTasksByPriorityAndCategory( userId)
    }

    // Method to update selected category
    fun setSelectedCategory(category: String?, userId: Int) {
        selectedCategory = category
        // Trigger filter when category is selected
        filterTasksByPriorityAndCategory( userId)
    }

    fun getAllTasksForUser(userId: Int) {
        viewModelScope.launch {
            _taskList.value = taskRepository.getAllTasksForUser(userId)
        }
    }

    fun getCompletedTaskForUser(userId: Int) {
        viewModelScope.launch {
            _completeTaskList.value = taskRepository.getAllCompletedTasksForUser(userId)
        }
    }

    fun getTasksForDate(selectedDate : String, userId: Int){
        viewModelScope.launch {
            _calList.value = taskRepository.getTasksForDate(selectedDate, userId)
        }
    }

    fun getTaskById(taskId: Int) {
        viewModelScope.launch {
            _task.value = taskRepository.getTaskById(taskId)
        }
    }




    fun saveTask(task: Task, onSuccess: () -> Unit) {
        viewModelScope.launch {
            taskRepository.insertTask(task)
            getAllTasksForUser(task.userId)
            onSuccess() // Invoke the callback function indicating successful insertion
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
            getAllTasksForUser(task.userId)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(task)

        }
    }

    fun updateTaskCompletionAndDate(taskId: Int, userId:Int,isComplete: Boolean) {
        viewModelScope.launch {
            taskRepository.updateTaskCompletionAndDate(taskId, isComplete)
            getAllTasksForUser(userId)
            getCompletedTaskForUser(userId)

        }
    }


}