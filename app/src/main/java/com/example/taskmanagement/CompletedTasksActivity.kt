package com.example.taskmanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagement.database.AppDatabase
import com.example.taskmanagement.database.repositories.TaskRepository

class CompletedTasksActivity : AppCompatActivity() {
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var completedAdapter: CompletedTasksAdapter
    private lateinit var backButton: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_tasks)


        val taskDao = AppDatabase.getInstance(applicationContext).getTaskDao()
        val taskRepository = TaskRepository(taskDao)
        taskViewModel = ViewModelProvider(this, TaskViewModelFactory(taskRepository)).get(TaskViewModel::class.java)

        val userId = intent.getIntExtra("userId", -1)
        val name = intent.getStringExtra("name")
        backButton = findViewById(R.id.btnBack)

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

        backButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("name", name)
            startActivity(intent)
            finish()
        }
    }


}