package com.example.taskmanagement.database.repositories

import com.example.taskmanagement.database.AppDatabase
import com.example.taskmanagement.database.entities.User

class UserRepository(private val db:AppDatabase) {


    suspend fun addUser(email: String, password: String) {
        val user = User(email = email, password = password)
        db.getUserDao().insertUser(user)
    }

    suspend fun getUserByEmail(email: String) = db.getUserDao().getUserByEmail(email)
}