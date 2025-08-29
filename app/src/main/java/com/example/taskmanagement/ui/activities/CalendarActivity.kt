package com.example.taskmanagement.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagement.R
import com.example.taskmanagement.database.AppDatabase
import com.example.taskmanagement.database.repositories.TaskRepository
import com.example.taskmanagement.ui.adapters.CalendarTaskAdapter
import com.example.taskmanagement.ui.viewmodels.TaskViewModel
import com.example.taskmanagement.ui.viewmodels.TaskViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class CalendarActivity : AppCompatActivity() {
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var calendarAdapter: CalendarTaskAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        val userId = intent.getIntExtra("userId", -1)
        val name = intent.getStringExtra("name")

        val taskDao = AppDatabase.getInstance(applicationContext).getTaskDao()
        val taskRepository = TaskRepository(taskDao)
        taskViewModel = ViewModelProvider(this, TaskViewModelFactory(taskRepository)).get(
            TaskViewModel::class.java)

        val calRecyclerView = findViewById<RecyclerView>(R.id.tasksRecyclerView)
        calRecyclerView.layoutManager=  LinearLayoutManager(this)

        calendarAdapter = CalendarTaskAdapter(taskViewModel)

        calRecyclerView.adapter = calendarAdapter

        val calendarView = findViewById<CalendarView>(R.id.calendarView)

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${month + 1}/$year"

            Log.d("CalendarActivity", " $selectedDate")
            displayTasksForDate(selectedDate, userId)
        }

        taskViewModel.calList.observe(this, Observer { tasks ->
            calendarAdapter.submitList(tasks)
            Log.d("CalendarActivity", "Tasks for selected date: $tasks")
        })


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val iconColorSelector =
            ContextCompat.getColorStateList(this, R.drawable.bottom_nav_icon_color_selector)
        bottomNavigationView.itemIconTintList = iconColorSelector
        bottomNavigationView.itemTextColor = iconColorSelector
        bottomNavigationView.selectedItemId =
            R.id.menu_calendar // or R.id.menu_calendar, R.id.menu_profile, etc.



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

    }

    private fun displayTasksForDate(selectedDate: String,userId :Int) {
        taskViewModel.getTasksForDate(selectedDate,userId)
    }
}