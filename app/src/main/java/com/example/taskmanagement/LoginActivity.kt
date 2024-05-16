package com.example.taskmanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.taskmanagement.database.AppDatabase
import com.example.taskmanagement.database.repositories.UserRepository

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvSignUpLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvSignUpLink = findViewById(R.id.tvSignUpLink)

        val userDao = AppDatabase.getInstance(applicationContext).getUserDao()
        val userRepository = UserRepository(userDao)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(userRepository)).get(LoginViewModel::class.java)

        // Set up onClickListener for the login button
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            // Perform login
            loginViewModel.loginUser(email, password) { success, userInfo ->
                if (success) {
                    // Login successful, pass user ID and name to MainActivity
                    val (userId, name) = userInfo ?: return@loginUser
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("userId", userId)
                    intent.putExtra("name", name)
                    startActivity(intent)
                    finish() // Finish LoginActivity so the user can't go back
                } else {
                    // Show error message if login failed
                    Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }


        }

        // Set up onClickListener for the sign-up link
        tvSignUpLink.setOnClickListener {
            // Navigate to SignUpActivity
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
