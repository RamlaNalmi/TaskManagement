package com.example.taskmanagement.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.database.entities.User
import com.example.taskmanagement.database.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun loginUser(email: String, password: String, onComplete: (Boolean, Pair<Int, String>?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userRepository.getUserByEmail(email)
            val result = if (user != null && user.password == password) {
                Pair(true, Pair(user.id, user.name)) // Pass the user ID and name
            } else {
                Pair(false, null)
            }
            // Perform UI-related operation on the main thread
            launch(Dispatchers.Main) {
                onComplete(result.first, result.second)
            }
        }
    }


    // You can add additional methods here as needed
}
