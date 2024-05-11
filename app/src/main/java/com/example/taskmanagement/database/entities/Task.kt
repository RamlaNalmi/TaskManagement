package com.example.taskmanagement.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val date: String,
    val time: String,
    val priority: String,
    val category:String
)