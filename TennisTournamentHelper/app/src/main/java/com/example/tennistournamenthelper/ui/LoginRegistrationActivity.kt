package com.example.tennistournamenthelper.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tennistournamenthelper.MainActivity
import com.example.tennistournamenthelper.data.FirebaseAuthManager
import com.example.tennistournamenthelper.databinding.ActivityLoginRegistationBinding
import org.koin.android.ext.android.inject

class LoginRegistrationActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLoginRegistationBinding
    private val auth: FirebaseAuthManager by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginRegistationBinding.inflate(layoutInflater)
        if (auth.isCurrentUserLogged()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            setContentView(binding.root)
        }
    }
}