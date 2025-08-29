package com.example.taskmanagement.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanagement.database.entities.User
import com.example.taskmanagement.database.repositories.UserRepository
import kotlinx.coroutines.launch

class SignUpViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun signUpUser(name: String, email: String, password: String, callback: (Boolean, String?, Int?) -> Unit) {


        // Create a new user object
        val user = User(name = name, email = email, password = password)

        // Insert the user into the database using a coroutine
        viewModelScope.launch {
            try {
                userRepository.insertUser(user)
                callback(true, user.name, user.id) // Callback indicating successful signup, include name and id
            } catch (e: Exception) {
                callback(false, null, null) // Callback indicating failed signup
            }
        }

    // You can add additional methods here as needed
}}

