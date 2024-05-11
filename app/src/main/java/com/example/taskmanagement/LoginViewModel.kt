package com.example.taskmanagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.database.entities.User
import com.example.taskmanagement.database.repositories.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun loginUser(email: String, password: String, onComplete: (Boolean) -> Unit) {
        // You can add validation logic here if needed

        // Check if the user exists in the database
        viewModelScope.launch {
            val user = userRepository.getUserByEmail(email)
            if (user != null && user.password == password) {
                // User exists and password matches, login successful
                onComplete(true)
            } else {
                // User does not exist or password does not match, login failed
                onComplete(false)
            }
        }
    }

    // You can add additional methods here as needed
}
