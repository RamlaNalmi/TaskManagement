package com.example.taskmanagement


import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagement.database.AppDatabase
import com.example.taskmanagement.database.entities.Task
import com.example.taskmanagement.database.repositories.TaskRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskAdapter: TaskAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val taskDao = AppDatabase.getInstance(applicationContext).getTaskDao()
        val taskRepository = TaskRepository(taskDao)
        taskViewModel = ViewModelProvider(
            this,
            TaskViewModelFactory(taskRepository)
        ).get(TaskViewModel::class.java)


        val userId = intent.getIntExtra("userId", -1)
        val name = intent.getStringExtra("name")
        val userNameTextView = findViewById<TextView>(R.id.userNameTextView)
        userNameTextView.text = "Welcome, $name"

        val taskRecyclerView = findViewById<RecyclerView>(R.id.taskRecyclerView)
        taskRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize TaskAdapter

        if (name != null) {
            taskAdapter = TaskAdapter(taskViewModel,name)
        } else {
            // Handle the case where name is null
            taskAdapter = TaskAdapter(taskViewModel,"")
        }

        // Set adapter to RecyclerView
        taskRecyclerView.adapter = taskAdapter







        taskViewModel.getAllTasksForUser(userId)
        taskViewModel.taskList.observe(this, { tasks ->
            Log.d("MainActivity", "Observer invoked, tasks: $tasks")
            taskAdapter.submitList(tasks)
        })


        val chipPriorityAll = findViewById<Chip>(R.id.chipPriorityAll)
        chipPriorityAll.isChecked = true

        // Highlight "All" chip for category
        val chipCategoryAll = findViewById<Chip>(R.id.chipCategoryAll)
        chipCategoryAll.isChecked = true

        // Inside onCreate() method of MainActivity
        val priorityChipGroup = findViewById<ChipGroup>(R.id.priorityChipGroup)
        val categoryChipGroup = findViewById<ChipGroup>(R.id.categoryChipGroup)

// Priority chip selection listener
        priorityChipGroup.setOnCheckedChangeListener { _, checkedId ->
            val chip = findViewById<Chip>(checkedId)
            val selectedPriority = if (chip != null && chip.id != R.id.chipPriorityAll) {
                chip.text.toString()
            } else {
                null
            }
            taskViewModel.setSelectedPriority(selectedPriority, userId)
            updateChipColors(priorityChipGroup, checkedId)
        }

// Category chip selection listener
        categoryChipGroup.setOnCheckedChangeListener { _, checkedId ->
            val chip = findViewById<Chip>(checkedId)
            val selectedCategory = if (chip != null && chip.id != R.id.chipCategoryAll) {
                chip.text.toString()
            } else {
                null
            }
            taskViewModel.setSelectedCategory(selectedCategory, userId)
            updateChipColors(categoryChipGroup, checkedId)
        }


        val checkCompletedTasksTextView = findViewById<TextView>(R.id.checkCompletedTasksTextView)
        checkCompletedTasksTextView.setOnClickListener {
            if (name != null) {
                openCompletedTasksActivity(userId, name)
            } else {
                // Handle the case where name is null
                openCompletedTasksActivity(userId, "")
            }

        }


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val iconColorSelector =
            ContextCompat.getColorStateList(this, R.drawable.bottom_nav_icon_color_selector)
        bottomNavigationView.itemIconTintList = iconColorSelector
        bottomNavigationView.itemTextColor = iconColorSelector
        bottomNavigationView.selectedItemId = R.id.menu_tasks // or R.id.menu_calendar, R.id.menu_profile, etc.



        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_tasks -> {
                    // Start MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("userId", userId)
                    intent.putExtra("name", name)
                    startActivity(intent)
                    true
                }

                R.id.menu_calendar -> {
                    // Start CalendarActivity
                    val intent = Intent(this, CalendarActivity::class.java)
                    intent.putExtra("userId", userId)
                    intent.putExtra("name", name)
                    startActivity(intent)
                    true
                }

                R.id.menu_profile -> {
                    // Start ProfileActivity or show profile fragment
                    true
                }

                else -> false
            }


        }


        val addTask = findViewById<FloatingActionButton>(R.id.AddTask)
        addTask.setOnClickListener {

            addTask.setOnClickListener {
                    showAddTaskDialog(userId)
            }

        }
    }

    private fun updateChipColors(chipGroup: ChipGroup, checkedId: Int) {
        for (index in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(index) as Chip
            if (chip.id == checkedId) {
                // Chip is selected
                chip.setChipBackgroundColorResource(R.color.secondary)
                chip.setTextColor(ContextCompat.getColor(this, R.color.black))
            } else {
                // Chip is unselected
                chip.setChipBackgroundColorResource(R.color.acc)
                chip.setTextColor(ContextCompat.getColor(this, R.color.gray))
            }
        }
    }
    private fun showAddTaskDialog(userId: Int) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_task)

        val btnSaveTask = dialog.findViewById<Button>(R.id.btnSaveTask)
        val etTaskName = dialog.findViewById<EditText>(R.id.etTaskName)
        val etTaskDescription = dialog.findViewById<EditText>(R.id.etTaskDescription)
        val etTaskDate = dialog.findViewById<EditText>(R.id.etTaskDate)
        val etTaskTime = dialog.findViewById<EditText>(R.id.etTaskTime)
        val spinnerPriority = dialog.findViewById<Spinner>(R.id.spinnerPriority)
        val spinnerCategory = dialog.findViewById<Spinner>(R.id.spinnerCategory)
        val btnSelectDate = dialog.findViewById<ImageButton>(R.id.btnSelectDate)
        val btnSelectTime = dialog.findViewById<ImageButton>(R.id.btnSelectTime)


        btnSelectDate.setOnClickListener {
            val currentDate = android.icu.util.Calendar.getInstance()
            val year = currentDate.get(android.icu.util.Calendar.YEAR)
            val month = currentDate.get(android.icu.util.Calendar.MONTH)
            val day = currentDate.get(android.icu.util.Calendar.DAY_OF_MONTH)

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



        btnSelectTime.setOnClickListener {
            val currentTime = android.icu.util.Calendar.getInstance()
            val hour = currentTime.get(android.icu.util.Calendar.HOUR_OF_DAY)
            val minute = currentTime.get(android.icu.util.Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                    val time = String.format("%02d:%02d", selectedHour, selectedMinute)
                    etTaskTime.setText(time)
                },
                hour,
                minute,
                true // 24-hour time format
            )
            timePickerDialog.show()
        }




        btnSaveTask.setOnClickListener {

            val task = Task(
                name = etTaskName.text.toString(),
                description = etTaskDescription.text.toString(),
                date = etTaskDate.text.toString(),
                time = etTaskTime.text.toString(),
                priority = spinnerPriority.selectedItem.toString(),
                category = spinnerCategory.selectedItem.toString(),
                userId = userId
            )

            taskViewModel.saveTask(task) {
                // This code will be executed after the task is successfully inserted
                // Display a success message
                showToast("Task inserted successfully!")
            }
            dialog.dismiss()
        }

        dialog.show()
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun openCompletedTasksActivity(userId: Int, name: String) {
        val intent = Intent(this, CompletedTasksActivity::class.java)
        intent.putExtra("userId", userId)
        intent.putExtra("name", name)
        startActivity(intent)

    }


}