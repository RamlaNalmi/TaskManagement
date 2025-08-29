package com.example.taskmanagement.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.taskmanagement.R
import com.example.taskmanagement.database.AppDatabase
import com.example.taskmanagement.database.repositories.UserRepository
import com.example.taskmanagement.ui.viewmodels.SignUpViewModel
import com.example.taskmanagement.ui.viewmodels.SignUpViewModelFactory


class SignUpActivity : AppCompatActivity() {
    private lateinit var signupViewModel: SignUpViewModel
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnSignup: Button
    private lateinit var tvLoginLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnSignup = findViewById(R.id.btnSignup)
        tvLoginLink = findViewById(R.id.tvLoginLink)

        val userDao = AppDatabase.getInstance(applicationContext).getUserDao()
        val userRepository = UserRepository(userDao)
        signupViewModel = ViewModelProvider(this, SignUpViewModelFactory(userRepository)).get(
            SignUpViewModel::class.java)

        // Set up onClickListener for the signup button
        btnSignup.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            // Perform validation, e.g., check if passwords match
            if (password == confirmPassword) {
                signupViewModel.signUpUser(name, email, password) { success,name, userId  ->
                    if (success) {
                        // User signed up successfully, navigate to MainActivity
                        val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                        intent.putExtra("userId", userId)
                        intent.putExtra("name", name) // Pass the user's name to MainActivity
                        startActivity(intent)
                        finish() // Finish SignUpActivity so the user can't go back
                    } else {
                        // Show error message if signup failed
                        Toast.makeText(this@SignUpActivity, "Sign up failed", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                // Show error message that passwords don't match
                Toast.makeText(this@SignUpActivity, "Passwords don't match", Toast.LENGTH_SHORT)
                    .show()
            }
        }


            tvLoginLink.setOnClickListener {
            // Navigate to SignUpActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}