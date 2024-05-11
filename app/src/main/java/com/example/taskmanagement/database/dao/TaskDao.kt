package com.example.taskmanagement.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.taskmanagement.database.entities.Task

@Dao
interface TaskDao {

    @Insert
    suspend fun insertTask(task: Task)
}