package com.example.taskmanagement.database.dao

import androidx.room.Insert
import com.example.taskmanagement.database.entities.Task

interface TaskDao {

    @Insert
    suspend fun insertTask(task: Task)
}