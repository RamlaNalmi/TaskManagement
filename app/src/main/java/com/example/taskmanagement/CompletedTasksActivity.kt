package com.example.taskmanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagement.database.AppDatabase
import com.example.taskmanagement.database.repositories.TaskRepository

class CompletedTasksActivity : AppCompatActivity() {
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var completedAdapter: CompletedTasksAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_tasks)


        val taskDao = AppDatabase.getInstance(applicationContext).getTaskDao()
        val taskRepository = TaskRepository(taskDao)
        taskViewModel = ViewModelProvider(this, TaskViewModelFactory(taskRepository)).get(TaskViewModel::class.java)

        val userId = intent.getIntExtra("userId", -1)

        val completedRecyclerView = findViewById<RecyclerView>(R.id.completedTasksRecyclerView)
        completedRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize TaskAdapter
        completedAdapter = CompletedTasksAdapter(taskViewModel)

        // Set adapter to RecyclerView
        completedRecyclerView.adapter =  completedAdapter
        taskViewModel.getCompletedTaskForUser(userId)
        taskViewModel.completeTaskList.observe(this, { tasks ->
            completedAdapter.submitList(tasks)
        })
    }


}