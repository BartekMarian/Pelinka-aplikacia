package com.example

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser

class VitajteActivity : AppCompatActivity() {

    var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vitajte)

        val loginButton = findViewById<Button>(R.id.welcome_log_in_button)
        loginButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

            ForegroundService.stopService(this)
        }

        val regButton = findViewById<Button>(R.id.welcome_register_button)
        regButton.setOnClickListener {
            val intent = Intent(this, Registracia::class.java)
            startActivity(intent)
            finish()
        }
    }

}