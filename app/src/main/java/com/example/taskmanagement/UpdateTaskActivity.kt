package com.example.taskmanagement

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import com.example.taskmanagement.database.AppDatabase
import com.example.taskmanagement.database.entities.Task
import com.example.taskmanagement.database.repositories.TaskRepository
import java.util.Calendar

class UpdateTaskActivity : AppCompatActivity() {
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskNameEditText: EditText
    private lateinit var taskDescriptionEditText: EditText
    private lateinit var taskDateEditText: EditText
    private lateinit var taskTimeEditText: EditText
    private lateinit var prioritySpinner: Spinner
    private lateinit var categorySpinner: Spinner
    private lateinit var saveButton: Button




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_task)

        val taskId = intent.getIntExtra("taskId", -1)

        // Initialize views
        taskNameEditText = findViewById(R.id.etTaskName)
        taskDescriptionEditText = findViewById(R.id.etTaskDescription)
        taskDateEditText = findViewById(R.id.etTaskDate)
        taskTimeEditText = findViewById(R.id.etTaskTime)
        prioritySpinner = findViewById(R.id.spinnerPriority)
        categorySpinner = findViewById(R.id.spinnerCategory)
        saveButton = findViewById(R.id.btnSaveTask)
        val btnSelectDate = findViewById<ImageButton>(R.id.btnSelectDate)
        val btnSelectTime = findViewById<ImageButton>(R.id.btnSelectTime)

        val taskDao = AppDatabase.getInstance(applicationContext).getTaskDao()
        val taskRepository = TaskRepository(taskDao)
        taskViewModel = ViewModelProvider(this, TaskViewModelFactory(taskRepository)).get(TaskViewModel::class.java)

        taskViewModel.getTaskById(taskId)
        taskViewModel.task.observe(this, { task ->
            task?.let {
                populateUI(task)
            }
        })


        saveButton.setOnClickListener {
            // Update the task
            taskViewModel.task.value?.let { task ->
                updateTask(task)
            }
        }




        btnSelectDate.setOnClickListener {
            val currentDate = android.icu.util.Calendar.getInstance()
            val year = currentDate.get(android.icu.util.Calendar.YEAR)
            val month = currentDate.get(android.icu.util.Calendar.MONTH)
            val day = currentDate.get(android.icu.util.Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    taskDateEditText.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }



        btnSelectTime.setOnClickListener {
            val currentTime = android.icu.util.Calendar.getInstance()
            val hour = currentTime.get(android.icu.util.Calendar.HOUR_OF_DAY)
            val minute = currentTime.get(android.icu.util.Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                    val time = String.format("%02d:%02d", selectedHour, selectedMinute)
                    taskTimeEditText.setText(time)
                },
                hour,
                minute,
                true // 24-hour time format
            )
            timePickerDialog.show()
        }



    }

    private fun populateUI(task: Task) {
        taskNameEditText.setText(task.name)
        taskDescriptionEditText.setText(task.description)
        taskDateEditText.setText(task.date)
        taskTimeEditText.setText(task.time)

        // Populate priority spinner
        val priorityAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.priorities,
            android.R.layout.simple_spinner_item
        )
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        prioritySpinner.adapter = priorityAdapter
        prioritySpinner.setSelection(priorityAdapter.getPosition(task.priority))

        // Populate category spinner
        val categoryAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.categories,
            android.R.layout.simple_spinner_item
        )
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoryAdapter
        categorySpinner.setSelection(categoryAdapter.getPosition(task.category))
    }

    private fun updateTask(task: Task) {



        val updatedTask = Task(
            id = task.id,
            name = taskNameEditText.text.toString(),
            description = taskDescriptionEditText.text.toString(),
            date = taskDateEditText.text.toString(),
            time = taskTimeEditText.text.toString(),
            priority = prioritySpinner.selectedItem.toString(),
            category = categorySpinner.selectedItem.toString(),
            userId = task.userId
        )

        // Update the task in the database
        taskViewModel.updateTask(updatedTask)

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("userId", task.userId)
        startActivity(intent)
        finish()

        // Finish the current activity
        finish()
    }
}