package com.example.attendance.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.attendance.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialize()
    }

    private fun initialize() {
        binding.btnLogin.setOnClickListener {
            if (binding.etPassword.text.toString().isEmpty()) {
                Toast.makeText(this, "Please Enter User Name", Toast.LENGTH_SHORT).show()
                binding.etUsername.requestFocus()
            } else if (binding.etPassword.text.toString().isEmpty()) {
                Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show()
                binding.etPassword.requestFocus()
            } else {
                startActivity(Intent(this,MainActivity::class.java))
            }
        }
    }

}