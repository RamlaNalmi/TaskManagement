package com.example.taskmanagement

import com.example.taskmanagement.database.repositories.UserRepository


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SignUpViewModel(private val repository: UserRepository) : ViewModel() {

    suspend fun signup(email: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            val existingUser = repository.getUserByEmail(email)
            if (existingUser == null) {
                repository.addUser(email, password)
                true // Signup successful
            } else {
                false // Username already exists
            }
        }
    }
}
