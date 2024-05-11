package com.example.taskmanagement.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.taskmanagement.database.entities.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM User WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?
}
