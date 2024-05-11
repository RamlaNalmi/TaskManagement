package com.example.taskmanagement

import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import com.example.taskmanagement.database.AppDatabase
import com.example.taskmanagement.database.entities.Task
import com.example.taskmanagement.database.repositories.TaskRepository
import com.example.taskmanagement.database.repositories.UserRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    private lateinit var taskViewModel: TaskViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val taskDao = AppDatabase.getInstance(applicationContext).getTaskDao()
        val taskRepository = TaskRepository(taskDao)
        taskViewModel = ViewModelProvider(this, TaskViewModelFactory(taskRepository)).get(TaskViewModel::class.java)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_tasks -> {
                    // Show the tasks fragment or perform any related action
                    true
                }

                R.id.menu_calendar -> {
                    // Show the calendar fragment or perform any related action
                    true
                }

                R.id.menu_profile -> {
                    // Show the profile fragment or perform any related action
                    true
                }

                else -> false
            }
        }
        val AddTask = findViewById<FloatingActionButton>(R.id.AddTask)
        AddTask.setOnClickListener {
            showAddTaskDialog()
        }
    }
    private fun showAddTaskDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_task)

        val btnSaveTask = dialog.findViewById<Button>(R.id.btnSaveTask)
        val etTaskName = dialog.findViewById<EditText>(R.id.etTaskName)
        val etTaskDescription = dialog.findViewById<EditText>(R.id.etTaskDescription)
        val etTaskDate = dialog.findViewById<EditText>(R.id.etTaskDate)
        val etTaskTime = dialog.findViewById<EditText>(R.id.etTaskTime)
        val spinnerPriority = dialog.findViewById<Spinner>(R.id.spinnerPriority)
        val spinnerCategory = dialog.findViewById<Spinner>(R.id.spinnerCategory)
        val btnSelectDate = dialog.findViewById<Button>(R.id.btnSelectDate)

        btnSelectDate.setOnClickListener {
            val currentDate = Calendar.getInstance()
            val year = currentDate.get(Calendar.YEAR)
            val month = currentDate.get(Calendar.MONTH)
            val day = currentDate.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    etTaskDate.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        btnSaveTask.setOnClickListener {

            val task = Task(
                name = etTaskName.text.toString(),
                description = etTaskDescription.text.toString(),
                date = etTaskDate.text.toString(),
                time = etTaskTime.text.toString(),
                priority = spinnerPriority.selectedItem.toString(),
                category = spinnerCategory.selectedItem.toString()
            )

            taskViewModel.saveTask(task)
            dialog.dismiss()
        }

        dialog.show()
    }

}