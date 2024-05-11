package com.example.taskmanagement.database.repositories

import com.example.taskmanagement.database.AppDatabase
import com.example.taskmanagement.database.dao.UserDao
import com.example.taskmanagement.database.entities.User

class UserRepository(private val userDao: UserDao) {
    suspend fun insertUser(user: User) = userDao.insertUser(user)
    suspend fun getUserByEmail(email: String) = userDao.getUserByEmail(email)
    suspend fun doesUserExist(email: String): Boolean {
        val user = userDao.getUserByEmail(email)
        return user != null
    }
}

