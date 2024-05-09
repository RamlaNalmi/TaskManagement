package com.example.taskmanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.taskmanagement.database.AppDatabase
import com.example.taskmanagement.database.repositories.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private lateinit var viewModel: SignUpViewModel
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonSignup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val userDao = AppDatabase.getInstance(applicationContext)
        val userRepository = UserRepository(userDao)

        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)

        // Initialize views
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonSignup = findViewById(R.id.buttonSignup)

        buttonSignup.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            signup(email, password)
        }
    }

    private fun signup(email: String, password: String) {
        launch {
            val success = viewModel.signup(email, password)
            if (success) {
                // Signup successful, navigate to main activity
                startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                finish() // Finish the current activity to prevent going back to the sign-up page using the back button
            } else {
                // Signup failed, show error message and make login text clickable
                val errorTextView = findViewById<TextView>(R.id.errorTextView)
                errorTextView.text = "Failed to sign up. An account exists for this email. Try login."
                val loginTextView = findViewById<TextView>(R.id.textLogin)
                loginTextView.setOnClickListener {
                    startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                    finish() // Finish the current activity to prevent going back to the sign-up page using the back button
                }
            }
        }
    }

}