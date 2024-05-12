package com.example.taskmanagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.database.entities.User
import com.example.taskmanagement.database.repositories.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun loginUser(email: String, password: String, onComplete: (Boolean, Int?) -> Unit) {
        viewModelScope.launch {
            val user = userRepository.getUserByEmail(email)
            if (user != null && user.password == password) {
                onComplete(true, user.id) // Pass the user ID
            } else {
                onComplete(false, null)
            }
        }
    }

    // You can add additional methods here as needed
}
